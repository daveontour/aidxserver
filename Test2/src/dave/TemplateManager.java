package dave;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class TemplateManager {
	
//	private String webAppPath;
	private Configuration cfg;
	private static HashMap<String, Template> templateMap = new HashMap<String,Template>();


	public TemplateManager() {
		
        try {
			cfg = new Configuration(Configuration.VERSION_2_3_27);
			cfg.setDirectoryForTemplateLoading(new File("C:\\Users\\dave_\\eclipse-workspace\\Generate\\templates"));
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
			    temp = cfg.getTemplate(template);
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
}
