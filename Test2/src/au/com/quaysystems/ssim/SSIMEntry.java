package au.com.quaysystems.ssim;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;


public class SSIMEntry {
	
	public String rawEntry;
	public String[] parts;
	private String[] countryRego = {"A2-","A3-","A40-","A5-","A6-","A7-","A9C-","AP-","B-","B-","C-","C2-","C3-","C5-","C6-","C9-","CC-","CN-","CP-","CS-","CU-","CX","CX-","D-","D2-","D4-","D6-","DQ-","E3-","E5-","EC-","EI-","EK-","EL-","EP-","ER-","ES-","ET-","EW-","EX-","EY-","EZ-","F-","G-","GL-","H4-","HA-","HB-","HC-","HH-","HI-","HK-","HL","HP-","HR-","HS-","HV-","HZ-","I-","J2-","J3-","J5-","J6-","J7-","J8-","JA","JY-","LN-","LQ-","LV-","LX-","LY-","LZ-","M-","MT-","N","OB-","OD-","OE-","OH-","OK-","OM-","OO-","OY-","P-","P2-","P4-","PH-","PJ-","PK-","PP-","PR-","PT-","PZ-","RA-","RDPL-","RF-","RP-","S2-","S3-","S5-","S7-","S9-","SE-","SP-","ST-","SU-","SX-","T3-","T7-","T9-","TC-","TF-","TG-","TI-","TJ-","TL-","TN-","TR-","TS-","TT-","TU-","TY-","TZ-","UK-","UN-","UP-","UR-","V2-","V3-","V4-","V5-","V7-","V8-","VH-","VN-","VT-","XA-","XB-","XC-","XT-","XU-","XY-","XZ-","YA-","YI-","YJ-","YK-","YL-","YN-","YR-","YS-","YU-","YV-","Z-","Z3-","ZA-","ZJ-","ZK-","ZL-","ZP-","ZS-","ZT-","ZU-","2-","3A-","3B-","3C-","3D-","3X-","4K-","4L-","4O-","4R-","4X-","5A-","5B-","5H-","5N-","5R-","5T-","5U-","5V-","5W-","5X-","5Y-","6O-","6V-","6W-","6Y-","7O-","7P-","7Q-","7T-","8P-","8Q-","8R-","9A-","9G-","9H-","9J-","9K-","9L-","9M-","9N-","9Q-","9U-","9V-","9XR-","9Y-"};
	private String[] letters = {"A","B","C","D","E","F","G","H","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	private static HashMap<String, String> monthMap = new HashMap<String, String>();

	
	public String actionCode;
	public String arrFlightDesignator;
	public String depFlightDesignator;
	public String startOfPeriod;
	public String endOfPeriod;
	public String daysOfOperation;
	public String numOfSeats;
	public String iataAircraftType;
	public String prevStation;
	public String originStation;
	public String arrTimeUTC;
	public String depTimeUTC;
	public String destStation;
	public String nextStation;
	public String arrServiceType;
	public String depServiceType;
	public String overNightIndicator;
	
	public ZonedDateTime startTime;
	public ZonedDateTime endTime;
	public String acRego;
	
	byte mon = (byte)0b00000001;
	byte tue = (byte)0b00000010;
	byte wed = (byte)0b00000100;
	byte thu = (byte)0b00001000;
	byte fri = (byte)0b00010000;
	byte sat = (byte)0b00100000;
	byte sun = (byte)0b01000000;
	
	byte[] days = {mon,tue,wed,thu,fri,sat,sun};
	
	byte opMask = (byte)0b0;

	boolean isDeparture;
	boolean isDoubleEntry;
	
	static {
		monthMap.put("jan", "01");
		monthMap.put("feb", "02");
		monthMap.put("mar", "03");
		monthMap.put("apr", "04");
		monthMap.put("may", "05");
		monthMap.put("jun", "06");
		monthMap.put("jul", "07");
		monthMap.put("aug", "08");
		monthMap.put("sep", "09");
		monthMap.put("oct", "10");
		monthMap.put("nov", "11");
		monthMap.put("dec", "12");
	}

	public SSIMEntry(String entry,String subYear) {
		
		
		
		
		try {
			this.parts = entry.split(" ");			
			this.rawEntry = entry;
			this.isDeparture = rawEntry.substring(1, 2).equalsIgnoreCase(" "); 
			this.isDoubleEntry=(parts[parts.length-1].length() == 2)?true:false;
			
			this.actionCode = parts[0].substring(0, 1);
			
			if (isDeparture) {
				parts = Arrays.copyOfRange(parts, 1, parts.length);
			} else {
				parts[0] = parts[0].substring(1);
			}
			
			int idx = 0;
			if (isDoubleEntry) {
				
				idx = 1;
				
				arrTimeUTC = parts[5].substring(6);
				prevStation =  parts[5].substring(3,6);
				originStation =  parts[5].substring(0,3);
				
				//Overnight indicator
				if (parts[6].length() == 11) {
					depTimeUTC = parts[6].substring(0,4);
					overNightIndicator =  parts[6].substring(4,5);
					nextStation =  parts[6].substring(5,8);
					destStation =  parts[6].substring(8);					
				} else {
					depTimeUTC = parts[6].substring(0,4);
					nextStation =  parts[6].substring(4,7);
					destStation =  parts[6].substring(7);
				}
				
				arrServiceType = parts[7].substring(0,1);
				depServiceType = parts[7].substring(1);
				
				arrFlightDesignator = parts[0];
				depFlightDesignator = parts[1];
				
			} else {
				
				if (isDeparture) {
					depTimeUTC = parts[4].substring(0,4);
					nextStation =  parts[4].substring(4,7);
					destStation =  parts[4].substring(7);
					
					depServiceType = parts[5];
					depFlightDesignator = parts[0];

				} else {
					arrTimeUTC = parts[4].substring(6);
					prevStation =  parts[4].substring(3,6);
					originStation =  parts[4].substring(0,3);
					
					arrServiceType = parts[5];
					arrFlightDesignator = parts[0];

				}
			}
			
			startOfPeriod = parts[1+idx].substring(0, 5).toLowerCase();
			startOfPeriod = startOfPeriod.substring(0,2)+monthMap.get(startOfPeriod.substring(2));
			
			endOfPeriod = parts[1+idx].substring(5).toLowerCase();
			endOfPeriod = endOfPeriod.substring(0,2)+monthMap.get(endOfPeriod.substring(2));
			
			opMask = (byte) (opMask |(parts[2+idx].charAt(0)=='1'?mon:0b0));
			opMask = (byte) (opMask |(parts[2+idx].charAt(1)=='2'?tue:0b0));
			opMask = (byte) (opMask |(parts[2+idx].charAt(2)=='3'?tue:0b0));
			opMask = (byte) (opMask |(parts[2+idx].charAt(3)=='4'?tue:0b0));
			opMask = (byte) (opMask |(parts[2+idx].charAt(4)=='5'?tue:0b0));
			opMask = (byte) (opMask |(parts[2+idx].charAt(5)=='6'?tue:0b0));
			opMask = (byte) (opMask |(parts[2+idx].charAt(6)=='7'?tue:0b0));

			numOfSeats = parts[3+idx].substring(0,3);
			iataAircraftType = parts[3+idx].substring(3);
			acRego = createRego();
			
			
			String startStr = subYear+startOfPeriod+"0000+0000";
			String endStr = subYear+endOfPeriod+"0000+0000";
			
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyddMMHHmmZ");
			startTime = ZonedDateTime.parse(startStr, fmt).withHour(0).withMinute(0);
			endTime = ZonedDateTime.parse(endStr, fmt).withHour(0).withMinute(0);

			//Winter Schedule Crosses Year
			if (startTime.isAfter(endTime)) {
				startTime = startTime.minusYears(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<SSIMFlight> rolloutFlights(String homeApt) {
		
		SSIMFlight f1;
		SSIMFlight f2;

		ArrayList<SSIMFlight> flights = new  ArrayList<SSIMFlight>();

		//Iterate between the start and end date of the flight
		for(ZonedDateTime currentdate = startTime; currentdate.isBefore(endTime) || currentdate.isEqual(endTime); currentdate= currentdate.plusDays(1)){


			//Check if the flight operates on the current day of the week
			if ( ( days[currentdate.getDayOfWeek().getValue() -1] & this.opMask) == 0) {
				continue;
			}
			
			//Create the individual flight records
			if (isDoubleEntry) {
				
				//The Arrival Flight of a double
				f1 = new SSIMFlight(this, currentdate, true, homeApt);
				//The Departure Flight of a double
				f2 = new SSIMFlight(this, currentdate, false, homeApt);
				
				f1.linkAirline = f2.airline;
				f1.linkOrigin = f2.origin;
				f1.linkDest = f2.dest;
				f1.linkFlightNo = f2.flightNo;
				f1.linkOriginDate = f2.originDate;

				f2.linkAirline = f1.airline;
				f2.linkOrigin = f1.origin;
				f2.linkDest = f1.dest;
				f2.linkFlightNo = f1.flightNo;
				f2.linkOriginDate = f1.originDate;
				
				flights.add(f1);
				flights.add(f2);
			} else {
				f1 = new SSIMFlight(this, currentdate, !this.isDeparture, homeApt);
				flights.add(f1);
			}	
		}

		return flights;
	}
	
	private String createRego() {
		
		Random rand = new Random();
		
		String prefix = countryRego[rand.nextInt(countryRego.length)];
		int len = 2+rand.nextInt(3);
		
		for (int i = 0; i<len; i++) {
			prefix = prefix.concat(letters[rand.nextInt(letters.length)]);
		}
		
		return prefix;
	}
}