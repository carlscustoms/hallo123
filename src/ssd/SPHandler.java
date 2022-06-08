package ssd;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import org.w3c.dom.Text;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * TODO: Implement this content handler.
 */
public class SPHandler extends DefaultHandler {
	/**
	 * Use this xPath variable to create xPath expression that can be
	 * evaluated over the XML document.
	 */
	private static XPath xPath = XPathFactory.newInstance().newXPath();
	
	/**
	 * Store and manipulate the  vaccination-plan XML document here.
	 */
	private Document spDoc;
	
	/**
	 * This variable stores the text content of XML Elements.
	 */
	private String eleText;

	class Patient {
		public String name;
		public String birth_year;
		public String residence;
		public String risk_group;
		public ArrayList<String> vaccination_dates = new ArrayList<>();
	}


	/**
	 * Insert local variables here
	 */

	private String vaccine_name = "";
	private String vaccine_type = "";

	private String description = "";
	private String date = "";
	private String size = "";
	private String order_date = "";
	private ArrayList<Patient> patients = new ArrayList<>();
	private Patient currPatient = new Patient();
	private String batch_id = "";

	private int running_batch_id = 0;
	private int running_patient_id = 0;


	public SPHandler(Document doc) {
    	spDoc = doc;
    }
    
    @Override
    /**
     * SAX calls this method to pass in character data
     */
  	public void characters(char[] text, int start, int length)
  			    throws SAXException {
  		eleText = new String(text, start, length);
  	}

    /**
     * 
     * Return the current stored vaccination-plan document
     * 
     * @return XML Document
     */
	public Document getDocument() {
		return spDoc;
	}
    
    //***TODO***
	//Specify additional methods to parse the exhibition document and modify the spDoc

	@Override
	public void startDocument() throws SAXException {

		// int randomNumber = 4; // chosen by fair dice roll.
		//                       // guaranteed to be random.

		// //create randomised catalog id
		// catalogId = "EX-" + String.format ("%03d", randomNumber) + "-a0";

	}

	private String getBatchID() {

		// TODO: generate new valid ID and compare it against the contents of XML file
		return "AstZ-0000-0A0";
	}

	private String getPID() {
		// TODO: generate new valid ID and compare it against the contents of XML file
		String out =  "P" + String.valueOf(running_patient_id);
		running_patient_id ++;
		return out;
	}


	@Override
	public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

		if ("patient".equals(qName)) {
			currPatient = new Patient();

			currPatient.name = atts.getValue("name");
			currPatient.birth_year = atts.getValue("birth_year");
			currPatient.residence = atts.getValue("residence");
			currPatient.risk_group = atts.getValue("risk-group");

		}

		if ("vaccine".equals(qName)) {
			vaccine_name = atts.getValue("name");
			vaccine_type = atts.getValue("type");
		}

		if ("batch".equals(qName)) {
			description = atts.getValue("description");
		}

	}

	@Override
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
		switch (qName) {
			case "batch":
				Element vaccinetypesElement = null;
				Element vaccinesElement = null ;
				Element patientsElement = null;

				try {
					vaccinetypesElement = (Element) xPath.compile("/vaccination-plan/vaccine-types").evaluate(getDocument(), XPathConstants.NODE);
					vaccinesElement = (Element) xPath.compile("/vaccination-plan/vaccines").evaluate(getDocument(), XPathConstants.NODE);
					patientsElement = (Element) xPath.compile("/vaccination-plan/patients").evaluate(getDocument(), XPathConstants.NODE);
				} catch (XPathExpressionException e) {
					e.printStackTrace();
					System.exit(1);
				}

				// Find or create a suitable vaccine (child of vaccines) parent element


				//
				Element vaccineElement;
				Boolean vaccinePresent = false;


				NodeList toplevelVaccines = vaccinetypesElement.getElementsByTagName("vaccine");

				for (int i = 0; i < toplevelVaccines.getLength(); i++) {
					Element vaccine = (Element) toplevelVaccines.item(i);
					NodeList nameList = vaccine.getElementsByTagName("name");
					Element name = (Element) nameList.item(0);
					String content = name.getTextContent();
					if ( content.equals(vaccine_name) ) {
						vaccinePresent = true;
					}

				}

				// add a suitable toplevel vaccine node if not present
				if (!vaccinePresent ){
					Element newVaccineElement = getDocument().createElement("vaccine");
					Element newNameElement = getDocument().createElement("name");
					newNameElement.setTextContent("vaccine_name");
					Element newTypeElement = getDocument().createElement("type");
					newTypeElement.setTextContent("vaccine_type");
					Element authorizedElement = getDocument().createElement("authorized");
					authorizedElement.setTextContent("true");

					newVaccineElement.appendChild(newNameElement);
					newVaccineElement.appendChild(newTypeElement);
					newVaccineElement.appendChild(authorizedElement);

					vaccinetypesElement.appendChild(newVaccineElement);
				}


				NodeList secondlevelVaccines = vaccinesElement.getElementsByTagName("vaccine");


				Boolean secondVaccinePresent = false;
				Element theOtherVaccineElement = null;

				for (int i = 0; i < secondlevelVaccines.getLength(); i++) {
					Element vaccine = (Element) secondlevelVaccines.item(i);

					String type_ref = vaccine.getAttribute("type_ref");
					if ( type_ref.equals(vaccine_name) ) {
						secondVaccinePresent = true;
						theOtherVaccineElement = vaccine;
					}

				}

				//add a suitable second level vaccine node if not present
				if (!secondVaccinePresent) {

					Element newVaccineElement = getDocument().createElement("vaccine");
					Attr type_refAttribute = getDocument().createAttribute("type_ref");
					type_refAttribute.setValue(vaccine_name);
					newVaccineElement.setAttributeNode(type_refAttribute);

					theOtherVaccineElement = newVaccineElement;
				}

				// Add the batch element to the vaccination-plan document

				Element batchElement = getDocument().createElement("batch");
				Element infoElement = getDocument().createElement("info");
				Attr dateAttribute = getDocument().createAttribute("date");
				dateAttribute.setValue(date);
				infoElement.setAttributeNode(dateAttribute);

				Text text1 = getDocument().createTextNode(" This batch has size ");
				Element sizeElement = getDocument().createElement("size");
				sizeElement.setTextContent(size);
				Text text2 = getDocument().createTextNode(". It was ordered on ");
				Element orderElement = getDocument().createElement("order-date");
				orderElement.setTextContent(order_date);
				Text text3 = getDocument().createTextNode(".");

				infoElement.appendChild(text1);
				infoElement.appendChild(sizeElement);
				infoElement.appendChild(text2);
				infoElement.appendChild(orderElement);
				infoElement.appendChild(text3);

				Attr batch_idAttribute = getDocument().createAttribute("id");

				String batch_idContent = getBatchID();
				batch_idAttribute.setValue(batch_idContent);
				batchElement.setAttributeNode(batch_idAttribute);
				batchElement.setTextContent(description);


				theOtherVaccineElement.appendChild(batchElement);
				theOtherVaccineElement.appendChild(infoElement);

				// add the final vaccine element to the vaccines top node
				if (!secondVaccinePresent) {
					vaccinesElement.appendChild(theOtherVaccineElement);
				}


				// Add the patient elements to the vaccination-plan document


				// Element tagsElement = getDocument().createElement("tags");

				for (Patient patient : patients) {
					Element patientElement = getDocument().createElement("patient");

					Attr nameAttr = getDocument().createAttribute("name");
					Attr birth_year = getDocument().createAttribute("birth_year");
					Attr pid = getDocument().createAttribute("pid");

					nameAttr.setValue(patient.name);
					birth_year.setValue(patient.birth_year);
					pid.setValue(getPID());

					patientElement.setAttributeNode(pid);
					patientElement.setAttributeNode(nameAttr);
					patientElement.setAttributeNode(birth_year);

					Element risk_group = getDocument().createElement("risk-group");
					risk_group.setTextContent(patient.risk_group);
					patientElement.appendChild(risk_group);

					for (String vaccination_date : patient.vaccination_dates) {
						Element vaccine = getDocument().createElement("vaccine");
						Attr ref_batch = getDocument().createAttribute("ref_batch");
						ref_batch.setValue(batch_idContent);
						vaccine.setAttributeNode(ref_batch);
						Element vaccination_dateElement = getDocument().createElement("vaccination-date");
						Attr date = getDocument().createAttribute("date");
						date.setTextContent(vaccination_date);
						vaccination_dateElement.setAttributeNode(date);

						patientElement.appendChild(vaccine);
						patientElement.appendChild(vaccination_dateElement);

					}

					Element residences = getDocument().createElement("residences");
					Element main = getDocument().createElement("main");
					main.setTextContent(patient.residence);
					residences.appendChild(main);
					patientElement.appendChild(residences);


					patientsElement.appendChild(patientElement);
				}


				// reset local files
				vaccine_name = "";
				vaccine_type = "";
				description = "";
				date = "";
				size = "";
				order_date = "";
				patients = new ArrayList<>();
				batch_id = "";

				break;
			case "batch-id":

				NodeList batchList = null;
				NodeList siblingList = null;
				NodeList patientList = null;
				try {
					batchList = (NodeList) xPath.compile("//batch[@id = '" + eleText + "']").evaluate(getDocument(), XPathConstants.NODESET);

					siblingList = (NodeList) xPath.compile("//info[preceding-sibling::batch[@id = '" + eleText + "']]").evaluate(getDocument(), XPathConstants.NODESET);


					patientList = (NodeList) xPath.compile("//patient[vaccine/@ref_batch= '" + eleText + "']").evaluate(getDocument(), XPathConstants.NODESET);

				} catch (XPathExpressionException e) {
					e.printStackTrace();
					System.exit(1);
				}


				for (int i = 0; i < batchList.getLength(); i++) {
					Element batch = (Element) batchList.item(i);
					batch.getParentNode().removeChild(batch);
				}

				for (int i = 0; i < siblingList.getLength(); i++) {
					Element sibling = (Element) siblingList.item(i);
					sibling.getParentNode().removeChild(sibling);
				}

				for (int i = 0; i < patientList.getLength(); i++) {
					Element patient = (Element) patientList.item(i);
					patient.getParentNode().removeChild(patient);
				}


				break;
			case "date":
				date =  eleText;
			case "size":
				size =  eleText;
				break;
			case "order-date":
				order_date = eleText;
				break;
			case "patient":
				patients.add(currPatient);

				// reset the currPatient field
				currPatient = new Patient();

				break;
			case "vaccination-date":
				currPatient.vaccination_dates.add(eleText);
				break;

			default:
				// ignore all other tags
		}


	}

	
}

