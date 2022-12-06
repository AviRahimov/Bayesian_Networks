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
import java.util.Arrays;

public class XMLParser {
    private static BayesianNetwork net = new BayesianNetwork();

    public static void main(String[] args) {
        Insert_Variables("alarm_net.xml");
//        System.out.println(net.getNet());
    }
    /*
    The Insert_Variables function get an XML file and extract the Variables from the file into bayesian network
    by searching the elements that contain the names of the variables and the elements that contain the outcomes
    for each variable
     */
    public static void Insert_Variables(String xml) {
        File XML_file = new File(xml);
        // The variable name
        String name = null;
        // the number of outcomes in the variable
        int count = 0;

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
                //each Node is a Node that contain a Variable
                Node Variable_num = VariablesList.item(i);
                if (Variable_num.getNodeType() == Node.ELEMENT_NODE) {
                    Element e1 = (Element) Variable_num;
                    count = e1.getElementsByTagName("OUTCOME").getLength();
                    name = VariablesList.item(i).getFirstChild().getNextSibling().getFirstChild().getTextContent();
                }
                String [] s = new String[count];
                for (int j = 0; j < count; j++) { // this is for the outcomes values
                    s[j] = ((Element) Variable_num).getElementsByTagName("OUTCOME").item(j).getTextContent();
                }
                net.Add_Var(new Variable(name, s));
            }
            /*
            The Definition part - in this part I'm going to extract the parents, children and the cpt for all the
            variables.
             */
            NodeList DefinitionList = doc.getElementsByTagName("DEFINITION");
            Variable temp = null;

            for (int i = 0; i < DefinitionList.getLength(); i++) {
                Node Def = DefinitionList.item(i);
                if (Def.getNodeType() == Node.ELEMENT_NODE) {
                    Element e1 = (Element) Def;
                    /*
                     collect the names of the variables that I want to add their parent and children and
                    to build the cpt.
                    */
                    temp = net.getVars(e1.getElementsByTagName("FOR").item(0).getTextContent());

                    NodeList Parents = e1.getElementsByTagName("GIVEN");
                    /*
                     If condition to check if a variable has parents or not
                     then if the Variable has parents then add them to Variable Parents
                     and do the same for the parent, the same means that also the parent add the
                     current variable as a child.
                    */
                    if (Parents.getLength()!= 0){
                        for (int j = 0; j < Parents.getLength(); j++) {
                            Variable parent = net.getVars(Parents.item(j).getTextContent());
                            temp.addParent(parent);
                            parent.addChild(temp);
                        }
                    }
                    /*
                    Creating two string arrays that the first contains the variables that I"m going to
                    create the cpt for them and the second array contains the probabilities for each
                    variation of some variables
                     */
                    String [] cpt_prob = e1.getElementsByTagName("TABLE").item(0).getTextContent().split(" ");
                    String [] cpt_vars = new String[e1.getElementsByTagName("TABLE").item(0).getTextContent().split(" ").length];
                    Arrays.fill(cpt_vars,"");
                    //checking if the variable has parents or not
                    if(temp.isParent()) {
                        /*
                        each parent has different appearance in the cpt table, so I arrange the table such that
                         each parent will be in the right spot with the right probability
                        */
                        int perform_num = (cpt_prob.length);
                        for (int j = 0; j < temp.getParents().size(); j++) {
                            perform_num = (cpt_prob.length)/(temp.getParents().get(j).getOutcomes().length);

                            //counter will count the number of time I used the parent variable in the same outcome
                            //and f switch when needed the outcome for the variable
                            int counter = 0;
                            int f = 0;
                            for (int k = 0; k < cpt_prob.length; k++) {
                                if(counter < perform_num){
                                    cpt_prob[k] += temp.getParents().get(j).getVar_name() + "=" + temp.getParents().get(j).getOutcomes()[f] + ",";
                                }
                                else{
                                    f++;
                                    f %= temp.getParents().get(j).getOutcomes().length;
                                    counter = 0;
                                }
                            }
                        }

                    }
                    // I finished the cpt only for the parents, now I need to add the variable itself and
                    // after that I need to add the probabilities, and I'm done.

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
