package au.com.quaysystems.aidx;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.UUID;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import au.com.quaysystems.aidx.web.FlightParams;
import au.com.quaysystems.ssim.SSIMFlight;
import au.com.quaysystems.ssim.SSIMManager;

public class MessageManager {

	private XMLOutputter xmlOutput;
	private Integer correlID = 1000;
	private AIDXValidator validator;
	private SSIMManager flightMgr;
	private DateTimeFormatter fmtOut = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

	private static TemplateManager templateManager;
	private static FlightParamVisitor flightVisitor;
	private static HashMap<String, MessageManager> managerMap = new HashMap<String, MessageManager>();
	private static HashMap<String, ZonedDateTime> usageMap = new HashMap<String, ZonedDateTime>();

	public  boolean bNumericID = true;

	public String schemaVersion="16.1";
	public String originator = null;
	public String delSystem = null;

	public String apt;
	public String year;

	private MessageManager(String apt, String year, String webAppPath) {

		this.apt = apt;
		this.year = year;

		this.flightMgr = SSIMManager.getFlightManager(apt, year);
		this.xmlOutput = new XMLOutputter();
		this.xmlOutput.setFormat(Format.getPrettyFormat());

		this.validator = new AIDXValidator(webAppPath);
		templateManager = new TemplateManager(webAppPath);
		flightVisitor = FlightParamVisitor.getMessageManager(0.8);

	}

	public static MessageManager getMessageManager(String apt, String year, String webAppPath) {

		String key = apt.concat(year);

		MessageManager mm = managerMap.get(key);

		// Keep the managerMap clean, by limiting to 5 entries
		int mapSize = managerMap.keySet().size();
		if (mapSize == 5 && (mm == null)) {
			ArrayList<String> keys = new ArrayList<String>();
			keys.addAll(managerMap.keySet());

			//Sort the keys according to the last time used
			Collections.sort(keys, new Comparator<String>() {
				@Override
				public int compare(String lhs, String rhs) {
					return usageMap.get(lhs).compareTo(usageMap.get(rhs));
				}
			});

			String leastUsed = keys.get(0);
			managerMap.remove(leastUsed);
		}

		if (mm == null) {
			mm = new MessageManager(apt,year,webAppPath);
			managerMap.put(key, mm);
		}

		//Record the last time the particular map was used, so least used can be removed if necessary. 
		usageMap.put(key,  ZonedDateTime.now());

		return mm;

	}


	private FlightParams getFlightLegMessage(SSIMFlight flt) {

		FlightParams leg = new FlightParams();
		leg.setFlight(flt);
		flightVisitor.visit(leg);

		return leg;

	}

	private ArrayList<FlightParams> getRandomFlightLegs(int num) {

		ArrayList<FlightParams> flightMessages = new ArrayList<FlightParams>();

		for (SSIMFlight flt : flightMgr.getRandomFlight(num)) {
			flightMessages.add(getFlightLegMessage(flt));
		}
		return flightMessages;
	}

	public ArrayList<Element> getRandomFlights(int num, String type) {

		ArrayList<Element> flightMessages = new ArrayList<Element>();

		for (FlightParams msg : getRandomFlightLegs(num)) {

			String schema = "16.1FlightLegNotifRQ";
			String xml = templateManager.getXML(msg.getParameterMap(), type);

			if (xml.contains("FlightLegRQ")) {
				schema = "16.1FlightLegRQ";
			} else if (xml.contains("FlightLegRS")) {
				schema = "16.1FlightLegRS";				
			} else {
				schema = "16.1FlightLegNotifRQ";
			}

			if (xml.startsWith("<Sequence>")) {
				Element sequence = this.validator.getValidatedSequenceElement(xml);
				for (Element el : sequence.getChildren()) {
					el = this.prepareElement(el, msg);
					flightMessages.add(el);					
				}
			} else {
				Element leg = this.validator.getValidatedElement(xml, schema);
				leg = prepareElement(leg,msg);
				flightMessages.add(leg);
			}
		}
		return flightMessages;
	}
	
	private Element prepareElement(Element el,FlightParams msg) {
		
		ZonedDateTime optime = msg.getOpTime();
		optime = optime.minusDays(2);
		el.setAttribute("TimeStamp", optime.format(fmtOut));

		if (bNumericID) {
			el.setAttribute("CorrelationID", correlID.toString());
		} else {
			String uuid = UUID.randomUUID().toString().replace("-", "");
			el.setAttribute("CorrelationID", uuid);				
		}

		correlID++;
		
		return el;
	}


	public String getRandomFlightsXML(int num, String type) {

		StringBuilder sb = new StringBuilder();
		for (Element el : getRandomFlights(num,type)) {
			sb.append(xmlOutput.outputString(el));
			sb.append("\n\n");
		}

		String xml = sb.toString();

		return xml;

	}
	public boolean validateXML(String xml) {
		return	validator.validateXML(xml,schemaVersion);
	}
}
