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
    private static BayesianNetwork net = new BayesianNetwork();

    // I need to do a function that will add to the arraylist of variables the variables from the xml file
    // and to add the outcomes to the string array outcomes and the definition, prob  and so on...
    public static void main(String[] args) {
        Insert_Variables("alarm_net.xml");
        System.out.println(net.getVars().get(0));
    }

    public static void Insert_Variables(String xml) {
        File XML_file = new File(xml);
        // The var name
        String name = null;
        // the number of outcomes in the var
        int count = 0;
        // all outcomes
//        String [] s = new String[count];
        int counter = 0;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Getting the Document
            Document doc = builder.parse(new File(xml));

            // Normalize the xml file
            doc.getDocumentElement().normalize();
            // Getting all the elements by tag name
            NodeList VariablesList = doc.getElementsByTagName("VARIABLE");
            // 5 iterations because the name VARIABLE
            for (int i = 0; i < VariablesList.getLength(); i++) {
                //each Node is a Node that contain a Variable
                Node Variable_num = VariablesList.item(i);
                if (Variable_num.getNodeType() == Node.ELEMENT_NODE) {
                    Element e1 = (Element) Variable_num;
                    count = e1.getElementsByTagName("OUTCOME").getLength();
                    name = String.valueOf(VariablesList.item(i).getTextContent().charAt(2));
                }
                String [] s = new String[count];
                for (int j = 0; j < count; j++) { // this is for the outcomes values
//                    s[j] = ;
                    System.out.println(String.valueOf(VariablesList.item(i).getTextContent().charAt(5)));
                }
//                net.Add_Var(new Variable(name, s));
            }
            //here I need to do the same for the definition element




        } catch (ParserConfigurationException e) {
        throw new RuntimeException(e);
    } catch (IOException e) {
        throw new RuntimeException(e);
    } catch (SAXException e) {
        throw new RuntimeException(e);
    }
    }
}
