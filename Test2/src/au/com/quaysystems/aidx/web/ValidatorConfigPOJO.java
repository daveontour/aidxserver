package au.com.quaysystems.aidx.web;

public class ValidatorConfigPOJO {
	
	ValidatorEntryPOJO standalone[];
	ValidatorGroupPOJO groups[];
	
	public ValidatorEntryPOJO[] getStandalone() {
		return standalone;
	}
	public void setStandalone(ValidatorEntryPOJO[] standalone) {
		this.standalone = standalone;
	}
	public ValidatorGroupPOJO[] getGroups() {
		return groups;
	}
	public void setGroups(ValidatorGroupPOJO[] groups) {
		this.groups = groups;
	}
}
