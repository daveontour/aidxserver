package dave;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamSource;

import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaCollection;
import org.apache.ws.commons.schema.XmlSchemaElement;
import org.apache.ws.commons.schema.walker.XmlSchemaWalker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public String webAppPath;

	public JSONServlet() {
		super();
	}

	public void init (ServletConfig config) throws ServletException{
		super.init(config);
		webAppPath = getServletContext().getRealPath("/");
	}

	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)   throws ServletException, IOException {
		System.out.println(String.format("====> %s  :  %s  %s", request.getRemoteAddr(), request.getRequestURI(), request.getQueryString()));
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
		getJSON(response);
	}

	private void getJSON(HttpServletResponse response) {
		
		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		XmlSchemaCollection collection = null;
		FileReader fileReader = null;
		try {
			File file = new File("C:\\Users\\dave_\\eclipse-workspace\\Generate\\IATA_AIDX_FlightLegNotifRQ.xsd");
			try {
				fileReader = new FileReader(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			collection = new XmlSchemaCollection();
			collection.read(new StreamSource(fileReader, file.getAbsolutePath()));

		} finally {
			if (fileReader != null) {
				try {
					fileReader.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}

		TypeExtractor extractor = new TypeExtractor(collection);
		extractor.extractTypes();

		Visitor visitor = new Visitor(extractor,collection);

		XmlSchemaWalker walker = new XmlSchemaWalker(collection, visitor);
		try {
			XmlSchemaElement elem = getElementOf(collection, "IATA_AIDX_FlightLegNotifRQ");
			walker.walk(elem);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Element el = visitor.rootOutput;

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try {
			response.getWriter().println(gson.toJson(el));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static XmlSchemaElement getElementOf(XmlSchemaCollection collection, String name) {

		XmlSchemaElement elem = null;
		for (XmlSchema schema : collection.getXmlSchemas()) {
			elem = schema.getElementByName(name);
			if (elem != null) {
				break;
			}
		}
		return elem;
	}

}
