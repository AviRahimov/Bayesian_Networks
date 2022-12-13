package Bayesian_net;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Ex1 {
//    public static void main(String[]args) throws IOException {
//        XMLParser xml = new XMLParser("alarm_net.xml");
//        Algorithms a = new Algorithms("P(J=T|B=T)", xml.getNet());
//        System.out.println(a.Simple_dist());
//    }


//    public static void main(String[]args) throws IOException {
//        Scanner file = new Scanner(System.in);
//        try{
//            write_output_text(String.valueOf(file));
//        }
//        catch (Exception e){
//            write_output_text("input.txt");
//        }
//    }

    public static void main(String[]args) throws IOException {
        File output = new File("output.txt");
        BufferedWriter out_file = new BufferedWriter(new FileWriter(output));
        String ans = "";
        File input_txt = new File("src/input.txt");
        Scanner reader = new Scanner(input_txt);
        String xml_file = "";
        if(reader.hasNextLine()){
            xml_file = reader.nextLine();
        }
        XMLParser xml = new XMLParser(xml_file);

        while(reader.hasNextLine()){
            String line = reader.nextLine();
            if(line.charAt(-1) == '1'){
                Algorithms simple_probability = new Algorithms(line.substring(0, line.length()-1), xml.getNet());
                if(Double.toString(simple_probability.Simple_dist()).length()<7){
                    ans = Double.toString(simple_probability.Simple_dist()) + "," + Integer.toString(simple_probability.getAdd_count()) + "," + Integer.toString(simple_probability.getMult_count());
                }
                else{
                    ans = Double.toString(simple_probability.Simple_dist()).substring(0, 7) + "," + Integer.toString(simple_probability.getAdd_count()) + "," + Integer.toString(simple_probability.getMult_count());
                }
            } //else if (line.charAt(-1) == '2') {
//                Bayesian_net.VariableElimination_algo variable_elim = new Bayesian_net.VariableElimination_algo(line.substring(0, line.length()-1), xml.getNet());
//                if(Double.toString(variable_elim.Variable_Elimination()).length()<7){
//                    ans = Double.toString(variable_elim.Variable_Elimination()) + "," + Integer.toString(variable_elim.getAdd_count()) + "," + Integer.toString(variable_elim.getMult_count());
//                }
//                else{
//                    ans = Double.toString(variable_elim.Variable_Elimination()).substring(0, 7) + "," + Integer.toString(variable_elim.getAdd_count()) + "," + Integer.toString(variable_elim.getMult_count());
//                }
//            }
//            else{
//                Algorithms My_variable_elim = new Algorithms(line.substring(0, line.length()-1), xml.getNet());
//                if(Double.toString(My_variable_elim.My_Variable_Elimination()).length()<7){
//                    ans = Double.toString(My_variable_elim.My_Variable_Elimination()) + "," + Integer.toString(My_variable_elim.getAdd_count()) + "," + Integer.toString(My_variable_elim.getMult_count());
//                }
//                else{
//                    ans = Double.toString(My_variable_elim.My_Variable_Elimination()).substring(0, 7) + "," + Integer.toString(My_variable_elim.getAdd_count()) + "," + Integer.toString(My_variable_elim.getMult_count());
//
//                }
//            }
            out_file.write(ans);
            if(reader.hasNextLine()){
                out_file.newLine();
            }
        }
        reader.close();
        out_file.close();
    }

}
