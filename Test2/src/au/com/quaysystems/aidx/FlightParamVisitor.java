package au.com.quaysystems.aidx;

import java.util.Random;

import au.com.quaysystems.aidx.web.FlightParams;

public class FlightParamVisitor {

	private static FlightParamVisitor instance;
	private Random rand = new Random();
	private double planningAccuracy = 0.8;


	private String[] gates = {"A20","A21","A22","A23","A24","A25","A26","A27","A28","A29","A30","B1","B2","B3","B4","B5","B6","B7","B8","B9","B10"};
	private String[] runways = {"29R","29L","11L","11R","09","27"};
	private String[] bagMakeup = {"1W","2W","3W","4W","5W","1E","2E","3E","4E","5E"};
	private String[] bagClaim = {"1","2","3","4","5","6","7","8","9"};
	private String[] paxGate = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
	private String[] checkInDesks = {"A1","A2","A3","A4","A5", "A6", "A7", "A8","B1","B2","B3","B4","B5", "B6", "B7", "B8","C1","C2","C3","C4","C5", "C6", "C7", "C8","D1","D2","D3","D4","D5", "D6", "D7", "D8"};;



	private FlightParamVisitor() {}

	public static FlightParamVisitor getMessageManager(double planningAccuracy) {

		if (instance == null) {
			instance = new FlightParamVisitor();
			instance.planningAccuracy = planningAccuracy;
		}

		return instance;
	}

	public String[] getGates() {
		return gates;
	}

	public void setGates(String[] gates) {
		this.gates = gates;
	}

	public String[] getRunways() {
		return runways;
	}

	public void setRunways(String[] runways) {
		this.runways = runways;
	}

	public String[] getBagMakeup() {
		return bagMakeup;
	}

	public void setBagMakeup(String[] bagMakeup) {
		this.bagMakeup = bagMakeup;
	}

	public String[] getBagClaim() {
		return bagClaim;
	}

	public void setBagClaim(String[] bagClaim) {
		this.bagClaim = bagClaim;
	}

	public String[] getCheckInDesks() {
		return checkInDesks;
	}

	public void setCheckInDesks(String[] checkInDesks) {
		this.checkInDesks = checkInDesks;
	}

	public FlightParams visit(FlightParams leg) {
		

		leg.depPosPlan = gates[rand.nextInt(gates.length)];
		leg.bagMakeUpPlan = bagMakeup[rand.nextInt(bagMakeup.length)];
		leg.runwayDepPlan = runways[rand.nextInt(runways.length)];
		leg.runwayArrPlan = runways[rand.nextInt(runways.length)];
		leg.bagClaimPlan = bagClaim[rand.nextInt(bagClaim.length)];
		leg.paxGateDepPlan = paxGate[rand.nextInt(paxGate.length)];
		leg.paxGateArrPlan = paxGate[rand.nextInt(paxGate.length)];

		int firstDeskIdx = rand.nextInt(checkInDesks.length-4);
		leg.checkFirstPlan = checkInDesks[firstDeskIdx];
		leg.checkLastPlan = checkInDesks[firstDeskIdx+4];


		if (rand.nextDouble() > planningAccuracy) {
			leg.depPos = gates[rand.nextInt(gates.length)];
		} else {
			leg.depPos = leg.depPosPlan;
		}
		
		if (rand.nextDouble() > planningAccuracy) {
			leg.paxGateDep = paxGate[rand.nextInt(paxGate.length)];
			leg.paxGateArr = paxGate[rand.nextInt(paxGate.length)];

		} else {
			leg.paxGateDep = leg.paxGateDepPlan;		
			leg.paxGateArr = leg.paxGateArrPlan;		
		}		
		
		if (rand.nextDouble() > planningAccuracy) {
			leg.runwayArr = runways[rand.nextInt(runways.length)];
			leg.runwayDep = runways[rand.nextInt(runways.length)];

		} else {
			leg.runwayArr = leg.runwayArrPlan;
			leg.runwayDep = leg.runwayDepPlan;
		}	
		
		if (rand.nextDouble() > planningAccuracy) {
			leg.bagMakeUp = bagMakeup[rand.nextInt(bagMakeup.length)];

		} else {
			leg.bagMakeUp = leg.bagMakeUpPlan;			
		}

		if (rand.nextDouble() > planningAccuracy) {
			leg.bagClaim = bagClaim[rand.nextInt(bagClaim.length)];
		} else {
			leg.bagClaim = leg.bagClaimPlan;
		}

		if (rand.nextDouble() > planningAccuracy) {
			firstDeskIdx = rand.nextInt(checkInDesks.length-4);
			leg.checkFirst = checkInDesks[firstDeskIdx];
			leg.checkLast = checkInDesks[firstDeskIdx+4];
		} else {
			leg.checkFirst = leg.checkFirstPlan;
			leg.checkLast = leg.checkLastPlan;
		}
		
		return leg;
		
	}
}
