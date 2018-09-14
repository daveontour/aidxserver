package dave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaAnnotation;
import org.apache.ws.commons.schema.XmlSchemaAnnotationItem;
import org.apache.ws.commons.schema.XmlSchemaCollection;
import org.apache.ws.commons.schema.XmlSchemaComplexType;
import org.apache.ws.commons.schema.XmlSchemaDocumentation;
import org.apache.ws.commons.schema.XmlSchemaEnumerationFacet;
import org.apache.ws.commons.schema.XmlSchemaFacet;
import org.apache.ws.commons.schema.XmlSchemaFractionDigitsFacet;
import org.apache.ws.commons.schema.XmlSchemaMaxInclusiveFacet;
import org.apache.ws.commons.schema.XmlSchemaMaxLengthFacet;
import org.apache.ws.commons.schema.XmlSchemaMinInclusiveFacet;
import org.apache.ws.commons.schema.XmlSchemaMinLengthFacet;
import org.apache.ws.commons.schema.XmlSchemaPatternFacet;
import org.apache.ws.commons.schema.XmlSchemaSimpleType;
import org.apache.ws.commons.schema.XmlSchemaSimpleTypeList;
import org.apache.ws.commons.schema.XmlSchemaSimpleTypeRestriction;
import org.apache.ws.commons.schema.XmlSchemaSimpleTypeUnion;
import org.apache.ws.commons.schema.XmlSchemaType;
import org.apache.ws.commons.schema.XmlSchemaWhiteSpaceFacet;

public class TypeExtractor {

	HashMap<QName, HashMap<String, Object>> typeMap = new HashMap<>();
	XmlSchemaCollection collection;

	public TypeExtractor(XmlSchemaCollection collection) {
		super();
		this.collection = collection;
	}

	public TypeExtractor() {}

	public void SOP (Object s) {
		System.out.println(s);
	}

	public void extractTypes() {

		for (XmlSchema s : collection.getXmlSchemas()) {
			this.interpretType(s);
		}
	}

	public void interpretType(XmlSchema xs) {


		Map<QName, XmlSchemaType> map = xs.getSchemaTypes();
		for ( QName qname : map.keySet()) {

			if (qname.getLocalPart().startsWith("FlightLegScope")) {
				SOP("Mrker");
			}
			try {
				HashMap<String, Object> restrictionMap = new HashMap<String, Object>();

				XmlSchemaType st = map.get(qname);
				SOP(st.getName());
				HashMap<String, Object> m = null;
				if (st instanceof XmlSchemaComplexType) {
					m = interpretComplexType((XmlSchemaComplexType)st,restrictionMap);
				} else if (st instanceof XmlSchemaSimpleType) {
					m = interpretSimpleType((XmlSchemaSimpleType)st,restrictionMap);
				} else {
					SOP(">>> interpretType:  Unknown XmlSchemaType");
				}	



				if (m != null) {
					m.put("descritpion", this.getAnnotationText(st.getAnnotation()));
					typeMap.put(qname, m);
				} else {
					SOP("Not processed yet");
				}


			} catch (Exception e) {
				e.printStackTrace();
			}
		}


	}

	public HashMap<String, Object> interpretComplexType(XmlSchemaComplexType st, HashMap<String, Object> restrictionMap) {

		System.out.println("Complex Type - "+st.getName());

		return  null;
	}


	@SuppressWarnings("rawtypes")
	public HashMap<String, Object> interpretSimpleType(XmlSchemaSimpleType stype, HashMap<String, Object> rm) {

		QName q = stype.getQName();

		if (q != null && q.getNamespaceURI().equals("http://www.w3.org/2001/XMLSchema")) {
			rm = processStandard(q,rm);
		} else if ( stype.getContent() instanceof XmlSchemaSimpleTypeRestriction) {
			rm =  processSimpleRestriction((XmlSchemaSimpleTypeRestriction)stype.getContent(), rm);
		} else 	if ( stype.getContent() instanceof XmlSchemaSimpleTypeUnion) {
			rm = processSimpleUnion((XmlSchemaSimpleTypeUnion)stype.getContent(), rm);
		} else 	if ( stype.getContent() instanceof XmlSchemaSimpleTypeList) {
			rm =  processSimpleTypeList((XmlSchemaSimpleTypeList)stype.getContent(),rm);
		} else {
			SOP(">>>>>>>>>>>>>>>>>>>UNHANDLED TYPE");
			SOP(stype.getParent());
		}

		// Set the model type


		if (rm.get("restrictionPattern") != null) {
			rm.put("model", "pattern");
			rm.put("modelDescription", "String of the pattern "+rm.get("restrictionPattern"));

		}

		if (rm.get("resctrictionMaxLength") != null) {
			if (rm.get("restrictionPattern") == null) {
				rm.put("model", "maxlength");
				rm.put("modelDescription", "String of maximum length "+rm.get("restrictionMaxLength"));
			} else {
				rm.put("model", "maxlengthpattern");
			}
		}


		if (rm.get("resctrictionMinLength") != null) {
			if (rm.get("restrictionPattern") == null) {
				rm.put("model", "minlength");
				rm.put("modelDescription", "String of minimum length "+rm.get("resctrictionMinLength"));
			} else {
				rm.put("model", "minlengthpattern");
			}
		}


		if (rm.get("resctrictionMaxLength") != null  && rm.get("resctrictionMinLength") != null) {
			if (rm.get("restrictionPattern") == null) {
				rm.put("model", "minmaxlength");
				rm.put("modelDescription", "String of minimum length "+rm.get("resctrictionMinLength")+" and maximum length "+ rm.get("resctrictionMaxLength"));
			} else {
				rm.put("model", "minmaxlengthpattern");
			}
		}

		if (rm.get("resctrictionMaxInclusive") != null) {
			if (rm.get("restrictionPattern") == null) {
				rm.put("model", "maxinclusive");
				rm.put("modelDescription", "Integer of maximum value "+rm.get("restrictionMaxInclusive"));
			} else {
				rm.put("model", "maxInclusivepattern");
			}
		}
		if (rm.get("resctrictionMinInclusive") != null) {
			if (rm.get("restrictionPattern") == null) {
				rm.put("model", "mininclusive");
				rm.put("modelDescription", "Integer of maximum value "+rm.get("restrictionMaxInclusive"));
			} else {
				rm.put("model", "minInclusivepattern");
			}
		}	

		if (rm.get("resctrictionMinInclusive") != null && rm.get("resctrictionMaxInclusive") != null ) {
			if (rm.get("restrictionPattern") == null) {
				rm.put("model", "minmaxinclusive");
				rm.put("modelDescription", "Integer of minimum value "+rm.get("restrictionMinInclusive")+" and maximum value "+ rm.get("resctrictionMaxInclusive"));
			} else {
				rm.put("model", "minmaxInclusivepattern");
			}
		}

		if (rm.get("restrictionEnumList") != null && ((List)rm.get("restrictionEnumList")).size() > 0) {
			rm.put("model", "enumlist");
			rm.put("modelDescription", "Select from the list of values");
			rm.put("restrictionEnumList", rm.get("restrictionEnumList"));
		}


		return rm;
	}

	private HashMap<String, Object> processStandard(QName q,HashMap<String, Object> rm){

		switch(q.getLocalPart()) {
		case "date":
			rm.put("model", "xs:date");
			rm.put("modelDescription", "Select Date");
			break;
		case "dateTime":
			rm.put("model", "xs:dateTime");
			rm.put("modelDescription", "Select Date");
			break;
		case "duration":
			rm.put("model", "xs:duration");
			rm.put("modelDescription", "Enter Duration");

			//			<xs:pattern value="\-?P(\d*D)?(T(\d*H)?(\d*M)?(\d*(\.\d*)?S)?)?"/>
			//			<xs:minInclusive value="-P10675199DT2H48M5.4775808S"/>
			//			<xs:maxInclusive value="P10675199DT2H48M5.4775807S"/>

			break;
		case "boolean":
			rm.put("model", "xs:boolean");
			rm.put("modelDescription", "Select ON/OFF");
			break;
		case "integer":
			rm.put("model", "xs:integer");
			rm.put("modelDescription", "Any Integer");
			rm.put("pattern", "[\\-+]?[0-9]+");
		case "nonNegativeInteger":
			rm.put("model", "xs:integer");
			rm.put("modelDescription", "Any Non Negative Integer");
			rm.put("pattern", "[\\+]?[0-9]+");
			break;
		case "decimal":
			rm.put("model", "xs:decimal");
			rm.put("modelDescription", "A decimal number");
			rm.put("pattern", "^\\d+\\.\\d{0,2}$");
			break;
		case "language":
			rm.put("model", "xs:language");
			rm.put("modelDescription", "RFC 1766 language codes");
			rm.put("pattern", "([a-zA-Z]{2}|[iI]-[a-zA-Z]+|[xX]-[a-zA-Z]{1,8})(-[a-zA-Z]{1,8})*");
			break;
		case "NMTOKEN":
		case "string":
			rm.put("model", "xs:string");
			rm.put("modelDescription", "A string of characters");
			rm.put("pattern", "$*");
			break;

		default:
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Standard Type not found " + q.getLocalPart());
		}

		return rm;
	}

	@SuppressWarnings("unchecked")
	private HashMap<String, Object> processSimpleUnion(XmlSchemaSimpleTypeUnion content,
			HashMap<String, Object> restrictionMap) {
		
		restrictionMap.put("model", "union");

		try {
			restrictionMap.put("restrictionUnionTypes", content.getMemberTypesQNames());		
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if(content.getBaseTypes() != null) {
				restrictionMap.put("restrictionUnionBaseTypes", content.getBaseTypes());
				for ( XmlSchemaSimpleType s : content.getBaseTypes()) {
					restrictionMap = this.interpretSimpleType(s, restrictionMap);
				}
			}

			if (restrictionMap.get("restrictionPattern") != null && restrictionMap.get("restrictionEnumList") != null) {
				String  p = (String)restrictionMap.get("restrictionPattern");
				for(String e : (ArrayList<String>)restrictionMap.get("restrictionEnumList")) {
					p = p.concat("|").concat(e);
				}
				restrictionMap.put("restrictionPattern",p);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return restrictionMap;
	}

	private HashMap<String, Object> processSimpleTypeList(XmlSchemaSimpleTypeList content, HashMap<String, Object> restrictionMap) {
		try {
			restrictionMap.put("restrictionListItemType", content.getItemType().getQName());
		} catch (Exception e) {
			restrictionMap.put("restrictionListItemType", content.getItemTypeName());
		}
		return restrictionMap;
	}

	protected HashMap<String, Object> processSimpleRestriction(XmlSchemaSimpleTypeRestriction tr, HashMap<String, Object> map) {

		ArrayList<String> restrictionEnumList = new ArrayList<>();


		for (XmlSchemaFacet f :tr.getFacets()) {

			if (f instanceof XmlSchemaPatternFacet) {
				map.put("restrictionPattern",  ((XmlSchemaPatternFacet)f).getValue().toString());
			} else if (f instanceof XmlSchemaMinLengthFacet) {
				map.put("resctrictionMinLength",  ((XmlSchemaMinLengthFacet)f).getValue().toString());
			}  else if (f instanceof XmlSchemaMaxLengthFacet) {
				map.put("resctrictionMaxLength", ((XmlSchemaMaxLengthFacet)f).getValue().toString());
			} else if (f instanceof XmlSchemaMinInclusiveFacet) {
				map.put("resctrictionMinInclusive", ((XmlSchemaMinInclusiveFacet)f).getValue().toString());
			}  else if (f instanceof XmlSchemaMaxInclusiveFacet) {
				map.put("resctrictionMaxInclusive", ((XmlSchemaMaxInclusiveFacet)f).getValue().toString());
			}  else if (f instanceof XmlSchemaEnumerationFacet) {
				restrictionEnumList.add(((XmlSchemaEnumerationFacet)f).getValue().toString());
			} else if (f instanceof XmlSchemaFractionDigitsFacet) {
				map.put("restrictionPattern", "\"[0-9]{1,3}(\\.[0-9]{1,4})?\"");
				map.put("modelDescription", "Decimal Number");

			} else if (f instanceof XmlSchemaWhiteSpaceFacet) {
				//Do Nothing
			}  else {
				SOP(f);
			}
		}

		if (restrictionEnumList.size() > 0) {
			map.put("restrictionEnumList", restrictionEnumList);
		}

		return map;
	}

	private String getAnnotationText(XmlSchemaAnnotation a) {

		String text = "";
		try {
			for (XmlSchemaAnnotationItem item : a.getItems()) {
				XmlSchemaDocumentation doc = (XmlSchemaDocumentation)item;
				for( int i = 0; i < doc.getMarkup().getLength(); i++) {
					text = text.concat(doc.getMarkup().item(i).getTextContent());
				}
			}
		} catch (Exception e) {
			text = null;
			return text;
		}

		return text;
	}
}
