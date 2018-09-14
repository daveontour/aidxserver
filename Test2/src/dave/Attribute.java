package dave;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ws.commons.schema.XmlSchemaAnnotation;
import org.apache.ws.commons.schema.XmlSchemaAnnotationItem;
import org.apache.ws.commons.schema.XmlSchemaAttribute;
import org.apache.ws.commons.schema.XmlSchemaDocumentation;
import org.apache.ws.commons.schema.XmlSchemaElement;
import org.apache.ws.commons.schema.XmlSchemaSimpleType;
import org.apache.ws.commons.schema.XmlSchemaSimpleTypeContent;
import org.apache.ws.commons.schema.XmlSchemaSimpleTypeList;
import org.apache.ws.commons.schema.XmlSchemaSimpleTypeRestriction;
import org.apache.ws.commons.schema.walker.XmlSchemaAttrInfo;

public class Attribute {

	public String name;
	public boolean required;
	public String description;
	public boolean enabled;
	public String model;
	public String pattern;
	public String minLength;
	public String maxLength;
	public String minInclusive;
	public String maxInclusive;
	public String restrictionDescription;
	public ArrayList<String> restrictionEnumList = new ArrayList<>();
	public String modelDescription;





	@Override
	public String toString() {
		return "Attribute [name=" + name + ", required=" + required + ", description=" + description + ", enabled="
				+ enabled + ", model=" + model + ", pattern=" + pattern + ", minLength=" + minLength + ", maxLength="
				+ maxLength + ", minInclusive=" + minInclusive + ", maxInclusive=" + maxInclusive
				+ ", restrictionDescription=" + restrictionDescription + ", restrictionEnumList=" + restrictionEnumList
				+ ", modelDescription=" + modelDescription + "]";
	}

	private void SOP(Object arg0) {
		System.out.println(arg0);
	}

	@SuppressWarnings("unchecked")
	public Attribute(XmlSchemaElement element, XmlSchemaAttrInfo attrInfo, TypeExtractor extractor) {

		XmlSchemaAttribute att = attrInfo.getAttribute();

		this.name = att.getName();
		this.required = this.enabled = att.getUse().toString().equals("required")?true:false;
		if (att.getAnnotation() != null) {
			this.description = getAnnotationText(att.getAnnotation());			
		} else {
			this.description = att.getName();
		}
		
		if (att.getSchemaType() instanceof XmlSchemaSimpleType) {

			XmlSchemaSimpleType t = (XmlSchemaSimpleType) att.getSchemaType();
			XmlSchemaSimpleTypeContent c = t.getContent();

			if (att.getSchemaTypeName() != null) {
				HashMap<String, Object> map = extractor.typeMap.get(att.getSchemaTypeName());
				if (map != null) {
					this.pattern = (String) map.get("restrictionPattern");
					this.minLength = (String) map.get("resctrictionMinLength");
					this.maxLength = (String) map.get("resctrictionMaxLength");
					this.minInclusive = (String) map.get("resctrictionMinInclusive");
					this.maxInclusive = (String) map.get("resctrictionMaxInclusive");
					this.model = (String) map.get("model");
					this.modelDescription = (String) map.get("modelDescription");
					try {
						this.restrictionEnumList = (ArrayList<String>)map.get("restrictionEnumList");
					} catch (Exception e) {
						// TODO Auto-generated catch block
		
					}
				} else {
					SOP (att.getName()+" (att)----------Could not find type "+att.getSchemaTypeName());				
				}
			} else if (c instanceof XmlSchemaSimpleTypeRestriction) {
				this.processSimpleRestriction(att.getSchemaType());
			} else if (c instanceof XmlSchemaSimpleTypeList) {
				this.processSimpleTypeListRestriction((XmlSchemaSimpleTypeList)c);
			}  else {
				SOP("## UNKNOWN SIMPLE TYPE ###"+c);				
			}
		} else {
			SOP(att.getName()+" ???????????????????????Attribute Schema Type Not Known");
		}
	}

	private void processSimpleTypeListRestriction(XmlSchemaSimpleTypeList c) {
		// TODO Auto-generated method stub

	}
	private String getAnnotationText(XmlSchemaAnnotation a) {
		String s = "";
		for (XmlSchemaAnnotationItem item : a.getItems()) {
			XmlSchemaDocumentation doc = (XmlSchemaDocumentation)item;
			for( int i = 0; i < doc.getMarkup().getLength(); i++) {
				String text = doc.getMarkup().item(i).getTextContent();
				s = s.concat(text);
			}
		}

		return s;
	}

	@SuppressWarnings("unchecked")
	protected void processSimpleRestriction(XmlSchemaSimpleType xmlSchemaSimpleType) {

		TypeExtractor ex = new TypeExtractor();
		HashMap<String, Object> map = ex.interpretSimpleType((XmlSchemaSimpleType)xmlSchemaSimpleType,new HashMap<String, Object> () );

		this.pattern = (String) map.get("restrictionPattern");
		this.minLength = (String) map.get("resctrictionMinLength");
		this.maxLength = (String) map.get("resctrictionMaxLength");
		this.minInclusive = (String) map.get("resctrictionMinInclusice");
		this.maxInclusive = (String) map.get("resctrictionMaxInclusive");
		this.model = (String) map.get("model");
		this.modelDescription = (String) map.get("modelDescription");
		try {
			this.restrictionEnumList = (ArrayList<String>)map.get("restrictionEnumList");
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		
	}

}
