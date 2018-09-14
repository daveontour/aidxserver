package au.com.quaysystems.aidx.web;

public class TemplateConfigPOJO {
	TemplateEntryPOJO standalone[];
	TemplateGroupPOJO groups[];
	public TemplateEntryPOJO[] getStandalone() {
		return standalone;
	}
	public void setStandalone(TemplateEntryPOJO[] standalone) {
		this.standalone = standalone;
	}
	public TemplateGroupPOJO[] getGroups() {
		return groups;
	}
	public void setGroups(TemplateGroupPOJO[] groups) {
		this.groups = groups;
	}
}
