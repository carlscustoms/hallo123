package ssd;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;


public class SSD {
    private static DocumentBuilderFactory documentBuilderFactory;
    private static DocumentBuilder documentBuilder;

	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
            System.err.println("Usage: java SSD <input.xml> <plan-updates.xml> <output.xml>");
            System.exit(1);
        }
	
		String inputPath = args[0];
		String updatePath = args[1];
		String outputPath = args[2];
		
    
       initialise();
       transform(inputPath, updatePath, outputPath);
    }
	
	private static void initialise() throws Exception {    
       documentBuilderFactory = DocumentBuilderFactory.newInstance();
       documentBuilderFactory.setNamespaceAware(true);
       documentBuilder = documentBuilderFactory.newDocumentBuilder();
    }
	
	/**
     * Use this method to encapsulate the main logic for this example. 
     * 
     * First read in the screening-plan document.
     * Second create a SPHandler and an XMLReader (SAX parser)
     * Third parse the plan-updates document
     * Last get the Document from the SPHandler and use a
     *    Transformer to print the document to the output path.
     * 
     * @param inputPath Path to the xml file to get read in.
     * @param updatePath Path to the plan-updates xml file
     * @param outputPath Path to the output xml file.
     */
    private static void transform(String inputPath, String updatePath, String outputPath) throws Exception {
        // Read in the data from the vaccination-plan xml document, created in Exercise Sheet 1
        Document spDoc = documentBuilder.parse(new File(inputPath));


        // Create an input source for the batch-delivery document
        InputSource batchdeliveryInputSource = new InputSource(new FileInputStream(batchdeliveryPath));

        XMLReader xr = XMLReaderFactory.createXMLReader();
        SPHandler bh = new SPHandler(spDoc);
        xr.setContentHandler(bh);


        // start the actual parsing
        xr.parse(batchdeliveryInputSource);


        // Validate file before storing        
        File schemaFile = new File("resources/vaccination-plan.xsd");
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(schemaFile);
        Validator validator = schema.newValidator();


        // get the document from the SPHandler
        spDoc = bh.getDocument();
        DOMSource source = new DOMSource(spDoc);


        //validate
        try {
            validator.validate(new DOMSource(spDoc));
        } catch (SAXException ex) {


            exit("Created document not valid!" + '\n' + ex.getMessage());
        }


        //store the document
        StreamResult result = new StreamResult(new File(outputPath));
        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(source, result);
    }

    /**
     * Prints an error message and exits with return code 1
     * 
     * @param message The error message to be printed
     */
    public static void exit(String message) {
    	System.err.println(message);
    	System.exit(1);
    }
    

}
