package au.com.quaysystems.aidx;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import au.com.quaysystems.aidx.web.TemplateConfigPOJO;
import au.com.quaysystems.aidx.web.TemplateEntryPOJO;
import au.com.quaysystems.aidx.web.TemplateGroupPOJO;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class TemplateManager {
	
	private String webAppPath;
	private Configuration cfg;
	private static Gson gson = new Gson();
	private static HashMap<String, Template> templateMap = new HashMap<String,Template>();


	public TemplateManager(String webAppPath) {
		this.webAppPath = webAppPath;
		
        try {
			cfg = new Configuration(Configuration.VERSION_2_3_27);
			cfg.setDirectoryForTemplateLoading(new File(this.webAppPath+"/templates"));
			cfg.setDefaultEncoding("UTF-8");
			cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
			cfg.setLogTemplateExceptions(false);
			cfg.setWrapUncheckedExceptions(true);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
	public String getXML(Map<String, Object> map, String template) {
		
		try {
			Template temp = templateMap.get(template);
			if (temp == null) {
				TemplateEntryPOJO entry = findEntry(template);
			    temp = cfg.getTemplate(entry.getTemplateFile());
			    templateMap.put(template, temp);
			}
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Writer out = new OutputStreamWriter(baos);
			temp.process(map, out);
			
			return new String(baos.toByteArray(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}

	private TemplateEntryPOJO findEntry(String template) {
		try {
			 TemplateConfigPOJO templateConfig = gson.fromJson(new FileReader(webAppPath+"/templates/templates.json"),  TemplateConfigPOJO.class);
			
			 for (TemplateEntryPOJO entry:templateConfig.getStandalone()) {
				 if (entry.getValue().equals(template)) {
					 return entry;
				 }
			 }
			 
			 for (TemplateGroupPOJO group:templateConfig.getGroups()) {
				 for (TemplateEntryPOJO entry:group.getTemplates()) {
					 if (entry.getValue().equals(template)) {
						 return entry;
					 }
				 }
				 
			 }			 
		} catch (Exception e) {
			e.printStackTrace();
		} 

		return null;
	}

}
