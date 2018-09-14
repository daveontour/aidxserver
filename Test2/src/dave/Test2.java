package dave;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.transform.stream.StreamSource;

import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaCollection;
import org.apache.ws.commons.schema.XmlSchemaElement;
import org.apache.ws.commons.schema.walker.XmlSchemaWalker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Test2 {

	public static void main(String[] args) throws FileNotFoundException {

		XmlSchemaCollection collection = null;
		FileReader fileReader = null;
		try {
			File file = new File("C:\\Users\\dave_\\eclipse-workspace\\Generate\\IATA_AIDX_FlightLegNotifRQ.xsd");
			fileReader = new FileReader(file);

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
		System.out.println(gson.toJson(el));
		
		try (PrintWriter out = new PrintWriter("filename.txt")) {
		    out.println(gson.toJson(el));
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
