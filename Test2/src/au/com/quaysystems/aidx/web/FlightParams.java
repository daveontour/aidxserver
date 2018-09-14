package au.com.quaysystems.aidx.web;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Random;

import au.com.quaysystems.ssim.SSIMFlight;

public class FlightParams {
	
	public String delSystem;
	public String originator;

	public String flightNoCompound;
	public String origin;
	public String dest;
	public String seats;
	public String acType;
	public String mvtType;
	public String serviceType;
	public String airline;
	public String flightNo;
	public String originDate;
	public String acRego;

	public String linkFlightNo;
	public String linkOrigin;
	public String linkDest;
	public String linkOriginDate;
	public String linkAirline;
	
	public String linkFlightNo2;
	public String linkOrigin2;
	public String linkDest2;
	public String linkOriginDate2;
	public String linkAirline2;

	public String depPosPlan, paxGateDepPlan, paxGateArrPlan, bagMakeUpPlan, checkFirstPlan, checkLastPlan, runwayArrPlan, runwayDepPlan, bagClaimPlan;
	public String depPos, paxGateDep, paxGateArr, bagMakeUp, checkFirst, checkLast, runwayArr, runwayDep, bagClaim;
	
	public String tarsrt;
	public String actofb;
	public String caltko;
	public String tartko;
	public String acttko;
	public String sctonb;
	public String actonb;
	public String acttdn;
	public String estonb;
	public String calonb;
	public String now;
	
	private ZonedDateTime optime;

	private Field[] fields;
	private ZonedDateTime TARSRT,SCTOFB,ACTOFB,CALTKO,TARTKO,ACTTKO,ACTTDN,SCTONB,ACTONB,ESTONB,CALONB;
	private DateTimeFormatter fmtOut = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
	private Random rand;
	public String sctofb;

	public FlightParams () {
		fields = FlightParams.class.getDeclaredFields();
		rand = new Random();		
	}

	public ZonedDateTime getOpTime() {
		return this.optime;
	}
	public FlightParams setFlight(SSIMFlight fl) {
		
		this.flightNoCompound = fl.flightNoCompound;
		this.origin = fl.origin;
		this.dest = fl.dest;
		this.seats = fl.seats;
		this.acType = fl.acType;
		this.mvtType = fl.mvtType;
		this.serviceType = fl.serviceType;
		this.airline = fl.airline;
		this.flightNo = fl.flightNo;
		this.originDate = fl.originDate;
		this.acRego = fl.acRego;

		this.linkFlightNo = fl.linkFlightNo;
		this.linkOrigin = fl.linkOrigin;
		this.linkDest = fl.linkDest;
		this.linkOriginDate = fl.linkOriginDate;
		this.linkAirline = fl.linkAirline;
		
		this.optime = fl.optime;
		this.SCTOFB = fl.optime;
		this.SCTONB = fl.optime;
		
		this.ACTOFB = SCTOFB.plusMinutes(getOffset());
		this.TARSRT = SCTOFB.minusMinutes(5);

		this.CALTKO = ACTOFB.plusMinutes(rand.nextInt(5));
		this.TARTKO = SCTOFB.plusMinutes(10);
		this.ACTTKO = ACTOFB.plusMinutes(5 + rand.nextInt(10));
		
		this.ACTONB = SCTONB.plusMinutes(getOffset());
		this.ACTTDN = ACTONB.minusMinutes(5 + rand.nextInt(10));
		this.ESTONB = ACTTDN.plusMinutes(5 + rand.nextInt(5));
		this.CALONB = this.ESTONB;
		
		return this;
		
	}
	
	private int getOffset() {
		return new Long(Math.round(rand.nextGaussian()*15+10)).intValue();
	}

	public HashMap<String, Object> getParameterMap(){

		sctofb = this.SCTOFB.format(fmtOut);
		tarsrt = this.TARSRT.format(fmtOut);
		actofb = this.ACTOFB.format(fmtOut);
		caltko = this.CALTKO.format(fmtOut);
		tartko = this.TARTKO.format(fmtOut);
		acttko = this.ACTTKO.format(fmtOut);
		sctonb = this.SCTONB.format(fmtOut);
		actonb = this.ACTONB.format(fmtOut);
		acttdn = this.ACTTDN.format(fmtOut);
		estonb = this.ESTONB.format(fmtOut);
		calonb = this.CALONB.format(fmtOut);
		
		this.now = ZonedDateTime.now().format(fmtOut);
		this.delSystem="AODB";
		this.originator = "AODB";

		/*
		 * Create a map of all the public fields that
		 * will be passed to the template for inclusion
		 */
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			for (Field field : fields) {
				if (field.getModifiers() == Modifier.PUBLIC) {
					String key = field.getName();
					Object value = field.get(this);
					map.put(key, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 	

		return map;

	}
}
