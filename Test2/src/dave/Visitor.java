package dave;

import java.util.ArrayDeque;

import org.apache.ws.commons.schema.XmlSchemaAll;
import org.apache.ws.commons.schema.XmlSchemaAny;
import org.apache.ws.commons.schema.XmlSchemaAnyAttribute;
import org.apache.ws.commons.schema.XmlSchemaChoice;
import org.apache.ws.commons.schema.XmlSchemaCollection;
import org.apache.ws.commons.schema.XmlSchemaElement;
import org.apache.ws.commons.schema.XmlSchemaSequence;
import org.apache.ws.commons.schema.walker.XmlSchemaAttrInfo;
import org.apache.ws.commons.schema.walker.XmlSchemaTypeInfo;
import org.apache.ws.commons.schema.walker.XmlSchemaVisitor;

public class Visitor implements XmlSchemaVisitor {
	
	private enum StackState { INSEQUENCE, INCHOICE};

	private ArrayDeque<Element> outputElementStack = new ArrayDeque<Element>();
	private ArrayDeque<StackState> state = new ArrayDeque<StackState>();
	
	public Element rootOutput = null;
	private TypeExtractor extractor;
	public static XmlSchemaCollection collection;

	
	public Visitor(TypeExtractor extractor, XmlSchemaCollection collection) {
		this.extractor = extractor;
		Visitor.collection = collection;
	}

	public void SOP(Object s) {
		System.out.println(s);
	}

	@Override
	public void onEnterElement(XmlSchemaElement element, XmlSchemaTypeInfo typeInfo, boolean previouslyVisited) {
		
		Element el = new Element(element, this.extractor);	
//		
//		if (el.elementName.equals("Airline")) {
//			SOP ("AIRLINE ELEMENT");
//		}


		
		if (rootOutput == null) {
			rootOutput = el;
		}
		
		if (state.peek() == StackState.INSEQUENCE || state.peek() == StackState.INCHOICE) {
			// Add the element to the sequence of the element on the stack
			outputElementStack.peek().childelements.add(el);
		}
	
		outputElementStack.push(el);

	}

	@Override
	public void onExitElement(XmlSchemaElement element, XmlSchemaTypeInfo typeInfo, boolean previouslyVisited) {
		SOP(outputElementStack.pop());
	}

	@Override
	public void onVisitAttribute(XmlSchemaElement element, XmlSchemaAttrInfo attrInfo) {
		outputElementStack.peek().addAttribute(element, attrInfo, extractor);
	}

	@Override
	public void onEndAttributes(XmlSchemaElement element, XmlSchemaTypeInfo typeInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnterSubstitutionGroup(XmlSchemaElement base) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onExitSubstitutionGroup(XmlSchemaElement base) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnterAllGroup(XmlSchemaAll all) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onExitAllGroup(XmlSchemaAll all) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnterChoiceGroup(XmlSchemaChoice choice) {

		Element el = new Element(choice);
		
		if (state.peek() == StackState.INSEQUENCE || state.peek() == StackState.INCHOICE) {
			// Add the element to the sequence of the element on the stack
			outputElementStack.peek().childelements.add(el);
		}

		state.push(StackState.INCHOICE);
		outputElementStack.push(el);

	}

	@Override
	public void onExitChoiceGroup(XmlSchemaChoice choice) {
		state.pop();
		outputElementStack.pop();
	}

	@Override
	public void onEnterSequenceGroup(XmlSchemaSequence seq) {
		state.push(StackState.INSEQUENCE);
	}

	@Override
	public void onExitSequenceGroup(XmlSchemaSequence seq) {
	}

	@Override
	public void onVisitAny(XmlSchemaAny any) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVisitAnyAttribute(XmlSchemaElement element, XmlSchemaAnyAttribute anyAttr) {
		// TODO Auto-generated method stub

	}

}
