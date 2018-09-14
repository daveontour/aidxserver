package au.com.quaysystems.ssim;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class SSIMFlight implements Comparable<SSIMFlight> {
	
	
	public String flightNoCompound;
	public String origin;
	public String dest;
	public ZonedDateTime optime;
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


	int hash = 1;
	

	boolean goodRecord = true;

	public SSIMFlight(SSIMEntry ssimEntry, ZonedDateTime opDate, boolean b, String homeApt) {
		
		if (ssimEntry == null) {
			this.goodRecord = false;
			return;
		}
		flightNoCompound = b?ssimEntry.arrFlightDesignator:ssimEntry.depFlightDesignator;
		origin = b?ssimEntry.originStation:homeApt;
		dest = b?homeApt:ssimEntry.destStation;
		seats = ssimEntry.numOfSeats;
		acType = ssimEntry.iataAircraftType;
		mvtType = b?"ARR":"DEP";
		serviceType = b?ssimEntry.arrServiceType:ssimEntry.depServiceType;
		acRego = ssimEntry.acRego;

		airline = flightNoCompound.substring(0,2);
		flightNo = flightNoCompound.substring(2);

		DateTimeFormatter dtfOut = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		try {
			

			if (b) {
				optime = opDate.withHour(Integer.parseInt(ssimEntry.arrTimeUTC.substring(0,2)))
					.withMinute(Integer.parseInt(ssimEntry.arrTimeUTC.substring(2)));
			} else {
				optime = opDate.withHour(Integer.parseInt(ssimEntry.depTimeUTC.substring(0,2)))
						.withMinute(Integer.parseInt(ssimEntry.depTimeUTC.substring(2)));
				
				//Adjust the departure date if the overnight indicator is set
				if (ssimEntry.overNightIndicator != null) {
					optime = optime.plusDays(Integer.parseInt(ssimEntry.overNightIndicator));
				}
				
			}

			if (mvtType.equals("DEP")){
				originDate = optime.format(dtfOut);
			} else {
				if (optime.getHour() > 3) {
					originDate =  optime.format(dtfOut);					
				} else {
					ZonedDateTime temp = optime.minusDays(1);
					originDate = temp.format(dtfOut);	
				}
			}


		} catch (Exception e) {
			goodRecord = false;
		}

		// Can happen coz of homeAirport Param
		if (origin.equals(dest)) {
			goodRecord = false;
		}

	}


	public String toString() {
		return String.format("%s %s %s %s %s %s %s %s %s %s", originDate, optime, airline, flightNo, serviceType, mvtType,seats, acType, origin, dest);
	}

	@Override
	public int hashCode(){

		return flightNoCompound.hashCode()
				+origin.hashCode()
				+dest.hashCode()
				+optime.hashCode()
				+acType.hashCode()
				+mvtType.hashCode()
				+serviceType.hashCode();

	}
	@Override
	public boolean equals(Object x) {

		if ( x == this) return true;
		if(!(x instanceof SSIMFlight)) return false;

		SSIMFlight fl = (SSIMFlight)x;

		return flightNoCompound == fl.flightNoCompound && optime.equals(fl.optime); 
	}

	@Override
	public int compareTo(SSIMFlight fl) {		
		try {
			ZonedDateTime x = fl.optime;
			return optime.compareTo(x);
		} catch (Exception e) {
			return 0;
		}
	}

}
