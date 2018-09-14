package au.com.quaysystems.aidx.web;

public class TemplateGroupPOJO {
	
	String groupTitle;
	TemplateEntryPOJO templates[];
	
	public String getGroupTitle() {
		return groupTitle;
	}
	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}
	public TemplateEntryPOJO[] getTemplates() {
		return templates;
	}
	public void setTemplates(TemplateEntryPOJO[] templates) {
		this.templates = templates;
	}

}
