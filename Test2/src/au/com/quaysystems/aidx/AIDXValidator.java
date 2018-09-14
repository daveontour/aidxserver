package au.com.quaysystems.aidx;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderXSDFactory;
import org.jdom2.output.XMLOutputter;

import com.google.gson.Gson;

import au.com.quaysystems.aidx.web.ValidatorConfigPOJO;
import au.com.quaysystems.aidx.web.ValidatorEntryPOJO;
import au.com.quaysystems.aidx.web.ValidatorGroupPOJO;

public class AIDXValidator {

	private static String webAppPath;
	private Gson gson = new Gson();
	private XMLOutputter xmlOutput;

	public AIDXValidator(String path) {
		webAppPath = path;
		this.xmlOutput = new XMLOutputter();

	}

	public Element getValidatedElement(String xml, String schemaSelector) {

		ValidatorEntryPOJO validator = this.getValidator(schemaSelector);
		if (validator == null) {
			System.out.println("===> Could not find validator for "+ schemaSelector);
			return null;
		}
		SAXBuilder builder = this.getBuilder(validator);
		if (builder == null) {
			System.out.println("===> Could not create validator builder for "+ schemaSelector);
			return null;
		}

		try {
			InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
			Document doc = builder.build(stream);
			return doc.getRootElement();

		} catch (Exception e) {
			System.out.println("XML Validated FAILED against "+validator.getSelector());
			System.out.println(xml);
			return null;
		} 
	}

	public boolean validateXML(String xml, String schemaSelector) {

		ValidatorEntryPOJO validator = this.getValidator(schemaSelector);
		if (validator == null) {
			System.out.println("===> Could not find validator for "+ schemaSelector);
			return false;
		}
		SAXBuilder builder = this.getBuilder(validator);
		if (builder == null) {
			System.out.println("===> Could not create validator builder for "+ schemaSelector);
			return false;
		}

		try {
			InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
			builder.build(stream);
			return true;

		} catch (Exception e) {
			System.out.println("XML Validated FAILED against "+validator.getSelector());
			System.out.println(xml);
			return false;
		} 
	}
	public String validateUserXML(String xml, String schemaSelector) {

		ValidatorEntryPOJO validator = this.getValidator(schemaSelector);
		if (validator == null) {
			System.out.println("===> Could not find validator for "+ schemaSelector);
			return "{\"status\":false, \"message\":\"Unable to validate XML\"}";
		}
		SAXBuilder builder = this.getBuilder(validator);
		if (builder == null) {
			System.out.println("===> Could not create validator builder for "+ schemaSelector);
			return "{\"status\":false, \"message\":\"Unable to validate XML\"}";
		}


		try {
			InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
			builder.build(stream);
			return "{\"status\":true, \"message\":\""+validator.getSuccessMessage()+"\"}";

		} catch (Exception e) {
			e.getMessage();
			System.out.println(e.getMessage());
			return this.validateUserXMLMega(xml);
		} 
	}

	public String validateUserXMLMega(String xml) {

		String schemaSelector = "AIDXMessage16";

		if (xml.contains("\"16.1\"")) {
			schemaSelector = "AIDXMessage16";
		} else if (xml.contains("\"17.1\"")) {
			schemaSelector = "AIDXMessage17";
		} else if (xml.contains("\"18.1\"")) {
			schemaSelector = "AIDXMessage18";
		}

		xml = String.format("<AIDXMessage xmlns=\"http://www.iata.org/IATA/2007/00\">%s</AIDXMessage>", xml);

		ValidatorEntryPOJO validator = this.getValidator(schemaSelector);
		if (validator == null) {
			System.out.println("===> Could not find validator for "+ schemaSelector);
			return "{\"status\":false, \"message\":\"Unable to validate XML\"}";
		}
		SAXBuilder builder = this.getBuilder(validator);
		if (builder == null) {
			System.out.println("===> Could not create validator builder for "+ schemaSelector);
			return "{\"status\":false, \"message\":\"Unable to validate XML\"}";
		}


		try {
			InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
			builder.build(stream);
			return "{\"status\":true, \"message\":\""+validator.getSuccessMessage()+"\"}";

		} catch (Exception e) {
			e.getMessage();
			System.out.println(e.getMessage());
			return "{\"status\":false, \"message\":\""+e.getMessage().replaceAll("\"","`")+"\"}";
		} 
	}

	private ValidatorEntryPOJO getValidator(String schemaSelector) {

		if  (schemaSelector.equals("AIDXMessage16")) {
			return new ValidatorEntryPOJO(schemaSelector, "/schemas/aidx161","Valid AIDX Message(s). AIDX Schema Version 16.1", new String[] {"AIDXMessage.xsd"});
		}
		if  (schemaSelector.equals("AIDXMessage17")) {
			return new ValidatorEntryPOJO(schemaSelector, "/schemas/aidx171","Valid AIDX Message(s). AIDX Schema Version 17.1", new String[] {"AIDXMessage.xsd"});
		}
		if  (schemaSelector.equals("AIDXMessage18")) {
			return new ValidatorEntryPOJO(schemaSelector, "/schemas/aidx171","Valid AIDX Message(s). AIDX Schema Version 18.1", new String[] {"AIDXMessage.xsd"});
		}



		try {
			ValidatorConfigPOJO templateConfig = gson.fromJson(new FileReader(webAppPath+"/schemas/schemas.json"),  ValidatorConfigPOJO.class);

			for (ValidatorEntryPOJO entry:templateConfig.getStandalone()) {
				if (entry.getSelector().equals(schemaSelector)) {
					return entry;
				}
			}

			for (ValidatorGroupPOJO group:templateConfig.getGroups()) {
				for (ValidatorEntryPOJO entry:group.getTemplates()) {
					if (entry.getSelector().equals(schemaSelector)) {
						return entry;
					}
				}

			}			 
		} catch (Exception e) {
			e.printStackTrace();
		} 

		return null;
	}

	private SAXBuilder getBuilder(ValidatorEntryPOJO validatorConfig) {

		ArrayList<URL> urls = new ArrayList<URL>();

		try {
			for (File file : new File(webAppPath+validatorConfig.getSchemaDirectory()).listFiles()) {
				if (file.isFile() && contains(validatorConfig.getFiles(),file)) {
					urls.add(new File(file.getAbsolutePath()).toURI().toURL());
				} 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			URL[] schemaURLS = urls.toArray(new URL[] {});
			XMLReaderJDOMFactory factory= new XMLReaderXSDFactory(schemaURLS) ;
			SAXBuilder builder =  new SAXBuilder(factory);
			return builder;

		} catch (JDOMException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static boolean contains(final String[] files, final File file) {
		for (String filename : files) {
			if (filename.endsWith(file.getName())) {
				return true;
			} 
		}
		return false;
	}

	public Element getValidatedSequenceElement(String xml) {
		
		//Parse the XML document and then extract the individual message elements

		try {
			SAXBuilder builder = new SAXBuilder();
			InputStream stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
			
			// root will be an XML document with <Sequence> as the root element with FLightLegNotfRQ as the children of the root
			Document root = builder.build(stream);
			for (Element el : root.getRootElement().getChildren()) {
				
				// Now check each of the elements
				String elStr = this.xmlOutput.outputString(el);
				if (!this.validateXML(elStr, "16.1FlightLegNotifRQ")){
					System.err.println("Couldn't validate an element of a multi message sequence");
					System.err.println(elStr);
					return null;
				};
			}
			return root.getRootElement();
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(xml);
		} 
		return null;
	}
}
