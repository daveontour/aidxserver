package dave;

import java.util.ArrayList;

import javax.xml.namespace.QName;

import org.apache.ws.commons.schema.XmlSchemaAnnotation;
import org.apache.ws.commons.schema.XmlSchemaAnnotationItem;
import org.apache.ws.commons.schema.XmlSchemaComplexType;
import org.apache.ws.commons.schema.XmlSchemaContentModel;
import org.apache.ws.commons.schema.XmlSchemaDocumentation;
import org.apache.ws.commons.schema.XmlSchemaElement;
import org.apache.ws.commons.schema.XmlSchemaSimpleContent;
import org.apache.ws.commons.schema.XmlSchemaSimpleContentExtension;

public abstract class SchemaBase {

	public String name;
	public boolean required;
	public String restriction ;
	public long minOccurs = 0;
	public long maxOccurs = 0;
	public String annotation;
	public String typeAnnotation;
	public QName qname;
	public boolean choice = false;
	public String restrictionTypeName;
	public String restrictionPattern;
	public String resctrictionMinInclusive;
	public String resctrictionMaxInclusive;
	public String resctrictionMinLength;
	public String resctrictionMaxLength;
	public String restrictionAnnotation;
	public ArrayList<String> restrictionEnumList = new ArrayList<String>();



	public void SOP (Object s) {
//		System.out.println(s);
	}
	
	
//	protected void processSimpleRestriction(XmlSchemaSimpleTypeRestriction tr) {
//		
////		SOP(tr.toString());
//
//		for (XmlSchemaFacet f :tr.getFacets()) {
//	//		SOP(f);
//			if (f instanceof XmlSchemaPatternFacet) {
//				this.restrictionPattern = ((XmlSchemaPatternFacet)f).getValue().toString();
//			} else if (f instanceof XmlSchemaMinLengthFacet) {
//				this.resctrictionMinLength = ((XmlSchemaMinLengthFacet)f).getValue().toString();
//			}  else if (f instanceof XmlSchemaMaxLengthFacet) {
//				this.resctrictionMaxLength = ((XmlSchemaMaxLengthFacet)f).getValue().toString();
//			} else if (f instanceof XmlSchemaMinInclusiveFacet) {
//				this.resctrictionMinInclusive = ((XmlSchemaMinInclusiveFacet)f).getValue().toString();
//			}  else if (f instanceof XmlSchemaMaxInclusiveFacet) {
//				this.resctrictionMaxInclusive = ((XmlSchemaMaxInclusiveFacet)f).getValue().toString();
//			}  else if (f instanceof XmlSchemaEnumerationFacet) {
//				restrictionEnumList.add(((XmlSchemaEnumerationFacet)f).getValue().toString());
//			} else if (f instanceof XmlSchemaFractionDigitsFacet) {
//				this.restrictionPattern = "\"[0-9]{1,3}(\\.[0-9]{1,4})?\"";
//			} else if (f instanceof XmlSchemaWhiteSpaceFacet) {
//				//Do Nothing
//			}  else {
//				SOP(f);
//			}
//		}
//	}
	
	protected QName getComplexTypeQName( XmlSchemaElement element) {
		
		if (element.getSchemaType() instanceof XmlSchemaComplexType) {
			XmlSchemaComplexType t = (XmlSchemaComplexType) element.getSchemaType();
			XmlSchemaContentModel c = t.getContentModel();
			if (c instanceof XmlSchemaSimpleContent) {
				XmlSchemaSimpleContent r = (XmlSchemaSimpleContent)c;
				if (r.getContent() instanceof XmlSchemaSimpleContentExtension) {
					XmlSchemaSimpleContentExtension x = (XmlSchemaSimpleContentExtension)r.getContent();
					return x.getBaseTypeName();
				}	
			}
		}
		
		return null;
	}
	
	protected void copyRestrictions(SchemaBase t1) {
		this.resctrictionMaxInclusive = t1.resctrictionMaxInclusive;
		this.resctrictionMinInclusive = t1.resctrictionMinInclusive;
		this.resctrictionMaxLength = t1.resctrictionMaxLength;
		this.resctrictionMinLength = t1.resctrictionMinLength;
		this.restrictionPattern = t1.restrictionPattern;
		this.restrictionTypeName = t1.name;
		this.restrictionAnnotation = t1.annotation;
	}
	
	protected void setAnnotationText(XmlSchemaAnnotation a) {
		annotation = "";
		try {
			for (XmlSchemaAnnotationItem item : a.getItems()) {
				XmlSchemaDocumentation doc = (XmlSchemaDocumentation)item;
				for( int i = 0; i < doc.getMarkup().getLength(); i++) {
					String text = doc.getMarkup().item(i).getTextContent();
					annotation = annotation.concat(text);
				}
			}
		} catch (Exception e) {
			annotation = null;
			return;
		}
	}
	
	protected void setAnnotationText(XmlSchemaElement el) {
		setAnnotationText(el.getAnnotation()); 
	}
}
