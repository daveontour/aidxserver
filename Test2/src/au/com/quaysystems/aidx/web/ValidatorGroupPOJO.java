package au.com.quaysystems.aidx.web;

public class ValidatorGroupPOJO {
	
	String groupTitle;
	ValidatorEntryPOJO validators[];
	
	public String getGroupTitle() {
		return groupTitle;
	}
	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}
	public ValidatorEntryPOJO[] getTemplates() {
		return validators;
	}
	public void setTemplates(ValidatorEntryPOJO[] templates) {
		this.validators = templates;
	}

}
