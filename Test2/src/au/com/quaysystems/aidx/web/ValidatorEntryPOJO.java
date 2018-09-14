package au.com.quaysystems.aidx.web;

import java.util.Arrays;

public class ValidatorEntryPOJO {
	
	public ValidatorEntryPOJO() {}
	public ValidatorEntryPOJO(String selector, String schemaDirectory, String successMessage, String[] files) {
		super();
		this.selector = selector;
		this.schemaDirectory = schemaDirectory;
		this.successMessage = successMessage;
		this.files = files;
	}
	private String selector;
	private String schemaDirectory;
	private String successMessage;
	private String buttonTitle;
	private String[] files;
	
	public String[] getFiles() {
		return files;
	}
	public void setFiles(String[] files) {
		this.files = files;
	}
	@Override
	public String toString() {
		return "ValidatorConfig [selector=" + selector + ", schemaDirectory=" + schemaDirectory + ", successMessage="
				+ successMessage + ", buttonTitle=" + buttonTitle + ", files=" + Arrays.toString(files) + "]";
	}
	public String getSelector() {
		return selector;
	}
	public void setSelector(String selector) {
		this.selector = selector;
	}
	public String getSchemaDirectory() {
		return schemaDirectory;
	}
	public void setSchemaDirectory(String schemaDirectory) {
		this.schemaDirectory = schemaDirectory;
	}
	public String getSuccessMessage() {
		return successMessage;
	}
	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}
	public String getButtonTitle() {
		return buttonTitle;
	}
	public void setButtonTitle(String buttonTitle) {
		this.buttonTitle = buttonTitle;
	}


}
