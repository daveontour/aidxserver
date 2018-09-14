package au.com.quaysystems.ssim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;


public class SSIMManager {

	public ArrayList<SSIMFlight> flights = new  ArrayList<SSIMFlight>();
	private static HashMap<String, SSIMManager> instanceMap = new HashMap<String, SSIMManager>();

	public String homeApt;
	public String year;

	private SSIMManager(String apt, String year) {
		this.homeApt = apt;
		this.year = year;
	}
	
	public static SSIMManager getFlightManager(String apt, String year, String filename) {

		File file = new File(filename);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		String key = apt.concat(year).concat(filename);

		return getFlightManager(apt, year, key, reader);

	}

	public static SSIMManager getFlightManager(String apt, String year) {

		String key = apt.concat(year);
		InputStream in = SSIMManager.class.getResourceAsStream("SSIMFile.txt"); 
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		return getFlightManager(apt, year, key, reader);

	}
	
	private static SSIMManager getFlightManager(String apt, String year, String key, BufferedReader reader) {
		
		SSIMManager ssimMgr = instanceMap.get(key);
		
		if (ssimMgr == null) {
			ssimMgr = new SSIMManager(apt, year);
			ssimMgr.homeApt = apt;
			ssimMgr.year = year;

			try {
				ssimMgr.processFile(reader);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			instanceMap.put(key, ssimMgr);
		}
		return ssimMgr;
	}
	

	private void processFile(BufferedReader reader) throws FileNotFoundException {

		try {
			Scanner input = new Scanner(reader);

			while (input.hasNextLine()) {
				String entry = input.nextLine();
				if ((!entry.startsWith("H") && !entry.startsWith("N")) || entry.contains("9999") ) {
					continue;
				}
				
				SSIMEntry fl = new SSIMEntry(entry,this.year);
				flights.addAll(fl.rolloutFlights(homeApt));
			}

			input.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		Set<SSIMFlight> hs = new HashSet<>();
		hs.addAll(flights);

		flights.clear();
		flights.addAll(hs);

		//Sort
		Collections.sort(flights);
	}
	public ArrayList<SSIMFlight> getAllFlights(ZonedDateTime start, ZonedDateTime end){

		ArrayList<SSIMFlight> fls = new ArrayList<SSIMFlight>();
		for (SSIMFlight f : flights) {
			if (start == null || end == null) {
				fls.add(f);
				continue;
			}

			if(start.isBefore(f.optime) && end.isAfter(f.optime)) {
				fls.add(f);
			}
		}
		return fls;
	}
	public ArrayList<SSIMFlight> getAllFlights(){
		return flights;	
	}
	public SSIMFlight getRandomFlight() {
		Random rand = new Random();
		int idx = rand.nextInt(flights.size());
		return flights.get(idx);
	}
	public List<SSIMFlight> getRandomFlight(int numFlights) {
		Random rand = new Random();
		int idx = rand.nextInt(flights.size()-numFlights);
		return flights.subList(idx, Math.min(idx+numFlights,flights.size()));
	}	
}
