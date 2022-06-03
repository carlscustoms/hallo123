package ssd;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
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

	/**
	 * Insert local variables here
	 */
	
    
	
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
   
	
}

