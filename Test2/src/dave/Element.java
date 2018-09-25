package dave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.apache.ws.commons.schema.XmlSchemaChoice;
import org.apache.ws.commons.schema.XmlSchemaChoiceMember;
import org.apache.ws.commons.schema.XmlSchemaComplexType;
import org.apache.ws.commons.schema.XmlSchemaElement;
import org.apache.ws.commons.schema.XmlSchemaSequence;
import org.apache.ws.commons.schema.XmlSchemaSimpleType;
import org.apache.ws.commons.schema.XmlSchemaSimpleTypeContent;
import org.apache.ws.commons.schema.XmlSchemaSimpleTypeList;
import org.apache.ws.commons.schema.XmlSchemaSimpleTypeRestriction;
import org.apache.ws.commons.schema.utils.XmlSchemaRef;
import org.apache.ws.commons.schema.walker.XmlSchemaAttrInfo;

public class Element extends SchemaBase{

	public ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	public ArrayList<Element> childelements = new ArrayList<Element>();
	public ArrayList<String> choiceElementIdentifiers = new ArrayList<String>();
	public String pattern;
	public String minLength;
	public String maxLength;
	public String minInclusive;
	public String maxInclusive;
	public String model;
	public String modelDescription;
	public String description;
	public String complex = "NO";



	@Override
	public String toString() {
		return "Element [name="+this.qname.getLocalPart()+" complex="+this.complex+" description="+this.description+" pattern=" + pattern + ", minLength=" + minLength + ", maxLength=" + maxLength
				+ ", minInclusive=" + minInclusive + ", maxInclusive=" + maxInclusive + ", model=" + model
				+ ", modelDescription=" + modelDescription + "]   "+this.toString2()+"\n\n";
	}

	public String toString2() {
		String atts = "  ";
		for (Attribute a : attributes) {
			atts = atts.concat("\n").concat("  "+a.toString());
		}

		return atts;
	}

	public void addAttribute(XmlSchemaElement element, XmlSchemaAttrInfo attrInfo, TypeExtractor extractor) {
		this.attributes.add(new Attribute(element, attrInfo,extractor));
	}


	public Element(XmlSchemaElement el, TypeExtractor extractor) {
		this.qname = el.getQName();
		this.name = el.getName();
		this.required = el.getMinOccurs() > 0;
		this.minOccurs = el.getMinOccurs();
		this.maxOccurs = el.getMaxOccurs();

		setAnnotationText(el);
		this.description = this.annotation;

		if(el.getName().equals("OperationTime")) {
			SOP("OperationTime");

		}

		if (el.getSchemaType() instanceof XmlSchemaSimpleType) {
			this.processSimpleType(el, extractor);
		} else if (el.getSchemaType() instanceof XmlSchemaComplexType) {
			this.processComplexType(el, extractor);
		}
	}

	@SuppressWarnings("unchecked")
	private void processSimpleType(XmlSchemaElement el, TypeExtractor extractor) {
		HashMap<String, Object> map = extractor.typeMap.get(getComplexTypeQName(el));
		if (map == null) {
			SOP ( el.getName()+" (elem)----------Could not find type "+getComplexTypeQName(el));				
		}

		XmlSchemaSimpleType t = (XmlSchemaSimpleType) el.getSchemaType();
		XmlSchemaSimpleTypeContent c = t.getContent();

		if (c instanceof XmlSchemaSimpleTypeRestriction) {
			TypeExtractor ex = new TypeExtractor();
			HashMap<String, Object> m = ex.interpretSimpleType(t,new HashMap<String, Object> () );

			this.pattern = (String) m.get("restrictionPattern");
			this.minLength = (String) m.get("resctrictionMinLength");
			this.maxLength = (String) m.get("resctrictionMaxLength");
			this.minInclusive = (String) m.get("resctrictionMinInclusice");
			this.maxInclusive = (String) m.get("resctrictionMaxInclusive");
			this.model = (String) m.get("model");
			this.modelDescription = (String) m.get("modelDescription");
			try {
				this.restrictionEnumList = (ArrayList<String>)m.get("restrictionEnumList");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				
			}


		} else if (c instanceof XmlSchemaSimpleTypeList) {
			this.processSimpleTypeListRestriction((XmlSchemaSimpleTypeList)c);
		}  else {
			SOP("## UNKNOWN ###"+c);				
		}		
	}

	@SuppressWarnings("unchecked")
	private void processComplexType(XmlSchemaElement el, TypeExtractor extractor) {

		this.complex="YES";

		XmlSchemaComplexType st = (XmlSchemaComplexType)el.getSchemaType();

		//		if (st.getAttributes().size() >0 && st.getAttributes().get(0) instanceof XmlSchemaAttributeGroupRef) {
		//			XmlSchemaAttributeGroupRef rg = (XmlSchemaAttributeGroupRef)st.getAttributes().get(0);
		//			SOP("Attribute Reference Group "+rg.getTargetQName());
		//		} else {

		HashMap<String, Object> m = extractor.typeMap.get(getComplexTypeQName(el));
		
		QName d = getComplexTypeQName(el);
		
		if (m == null) {
			m = extractor.interpretComplexType(st, m);
			SOP (el.getName()+" (elem)----------Could not find type "+getComplexTypeQName(el));						
		} else {
			this.pattern = (String) m.get("restrictionPattern");
			this.minLength = (String) m.get("resctrictionMinLength");
			this.maxLength = (String) m.get("resctrictionMaxLength");
			this.minInclusive = (String) m.get("resctrictionMinInclusice");
			this.maxInclusive = (String) m.get("resctrictionMaxInclusive");
			this.model = (String) m.get("model");
			this.modelDescription = (String) m.get("modelDescription");	
			try {
				this.restrictionEnumList = (ArrayList<String>)m.get("restrictionEnumList");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				
			}
		}
		//		}
	}

	private void processSimpleTypeListRestriction(XmlSchemaSimpleTypeList c) {
		//		SOP("SimpleTypeList  ----"+c);

	}

	public Element(XmlSchemaChoice choice) {
		this.choice = true;
		this.required = choice.getMinOccurs() > 0;
		this.minOccurs = choice.getMinOccurs();
		this.maxOccurs = choice.getMaxOccurs();
		this.name = "{Choice}";

		Iterator<XmlSchemaChoiceMember> it = choice.getItems().iterator();

		int i = 0;
		while (it.hasNext()) {
			i++;
			Object member = it.next();
			
			if (member instanceof XmlSchemaElement) {
				XmlSchemaElement e = (XmlSchemaElement) member;
				String name = e.getName();
				if (name == null) {
					name = e.getRef().getTarget().getName();
				}
				choiceElementIdentifiers.add(name);				
			} else 	if (member instanceof XmlSchemaSequence ) {
				choiceElementIdentifiers.add("Sequence Type");				
			} else {
				choiceElementIdentifiers.add("Work It Out");				
			}
		}
	}
}
