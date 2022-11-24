package Bayesian_net;

import Bayesian_net.BayesianNetwork;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class XMLParser {
    private BayesianNetwork net = new BayesianNetwork();

    // I need to do a function that will add to the arraylist of variables the variables from the xml file
    // and to add the outcomes to the string array outcomes and the definition, prob  and so on...
    public static void main(String[] args) {
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();
//        try {
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            // Getting the Document
//            Document doc = builder.parse(new File("alarm_net.xml"));
//
//            // Normalize the xml file
//            doc.getDocumentElement().normalize();
//
//            // Getting all the elements by tag name
//            NodeList VariablesList = doc.getElementsByTagName("VARIABLE");
//            for (int i = 0; i < VariablesList.getLength(); i++) {
//                Node Variable_num = VariablesList.item(i);
//
//                if (Variable_num.getNodeType() == Node.ELEMENT_NODE) {
////                    Element Var_element = (Element) Variable_num;
//                    NodeList Var_details = Variable_num.getChildNodes();
//                    for (int j = 0; j < Var_details.getLength(); j++) {
//                        Node detail = Var_details.item(j);
//                        if (detail.getNodeType() == Node.ELEMENT_NODE) {
//                            Element detail_element = (Element) detail;
////                            System.out.println(" " + detail_element.getTagName() + ": " + detail_element.getTextContent());
//
//                        }
//                    }
//                }
//            }
//
//
//        } catch (ParserConfigurationException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (SAXException e) {
//            throw new RuntimeException(e);
//        }
        Insert_Variables("alarm_net.xml");
    }

    public static void Insert_Variables(String xml) {
        File XML_file = new File(xml);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Getting the Document
            Document doc = builder.parse(new File(xml));

            // Normalize the xml file
            doc.getDocumentElement().normalize();

            // Getting all the elements by tag name
            NodeList VariablesList = doc.getElementsByTagName("VARIABLE");
            for (int i = 0; i < VariablesList.getLength(); i++) {
                Node Variable_num = VariablesList.item(i);

                if (Variable_num.getNodeType() == Node.ELEMENT_NODE) {
//                    Element Var_element = (Element) Variable_num;
                    NodeList Var_details = Variable_num.getChildNodes();
                    for (int j = 0; j < Var_details.getLength(); j++) {
                        Node detail = Var_details.item(j);
                        if (detail.getNodeType() == Node.ELEMENT_NODE) {
                            Element detail_element = (Element) detail;
//                            this.net.setVars();
                            System.out.println(" " + detail_element.getTagName() + ": " + detail_element.getTextContent());

                        }
                    }
                }
            }
        } catch (ParserConfigurationException e) {
        throw new RuntimeException(e);
    } catch (IOException e) {
        throw new RuntimeException(e);
    } catch (SAXException e) {
        throw new RuntimeException(e);
    }
    }
}
