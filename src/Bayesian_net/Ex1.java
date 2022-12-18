package Bayesian_net;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Ex1 {
    public static void main(String[]args) throws IOException {
//        XMLParser xml = new XMLParser("alarm_net.xml");
        XMLParser xml = new XMLParser("big_net.xml");
//        Variable R = new Variable("R", new String[]{"T", "F"});
//        Variable C = new Variable("C", new String[]{"T", "F"});
//        Variable H = new Variable("H", new String[]{"T", "F"});
//        Variable D = new Variable("D", new String[]{"T", "F"});
//        xml.getNet().Add_Var(R);
//        xml.getNet().Add_Var(C);
//        xml.getNet().Add_Var(H);
//        xml.getNet().Add_Var(D);
//        xml.getNet().getVars("R").addChild(xml.getNet().getVars("E"));
//        xml.getNet().getVars("R").addChild(C);
//        xml.getNet().getVars("C").addChild(H);
//        xml.getNet().getVars("C").addChild(D);
//        xml.getNet().getVars("E").addParent(R);
//        xml.getNet().getVars("C").addParent(R);
//        xml.getNet().getVars("H").addParent(C);
//        xml.getNet().getVars("D").addParent(C);

//        Algorithms a = new Algorithms("P(J=T|B=T,A=F)", xml.getNet());
//        VariableElimination_algo v = new VariableElimination_algo("P(J=T|B=T,A=F)", xml.getNet());
        VariableElimination_algo v = new VariableElimination_algo("P(D1=T|C2=v1,C3=F)", xml.getNet());
//        System.out.printf("%.5f %n", a.Simple_dist());
//        System.out.println(a.getAdd_count());
//        System.out.println(a.getMult_count());
//        System.out.println(xml.getNet());
        v.Evidence_elimination();
        v.Hidden_elimination();
        v.join();
//        System.out.println("**********************************************************************************");
//        System.out.println(xml.getNet());
        }


//        while(reader.hasNextLine()){
//            String line = reader.nextLine();
//            if(line.charAt(-1) == '1'){
//                Algorithms simple_probability = new Algorithms(line.substring(0, line.length()-1), xml.getNet());
//                if(Double.toString(simple_probability.Simple_dist()).length()<7){
//                    ans = Double.toString(simple_probability.Simple_dist()) + "," + Integer.toString(simple_probability.getAdd_count()) + "," + Integer.toString(simple_probability.getMult_count());
//                }
//                else{
//                    ans = Double.toString(simple_probability.Simple_dist()).substring(0, 7) + "," + Integer.toString(simple_probability.getAdd_count()) + "," + Integer.toString(simple_probability.getMult_count());
//                }
//            } //else if (line.charAt(-1) == '2') {
////                Bayesian_net.VariableElimination_algo variable_elim = new Bayesian_net.VariableElimination_algo(line.substring(0, line.length()-1), xml.getNet());
////                if(Double.toString(variable_elim.Variable_Elimination()).length()<7){
////                    ans = Double.toString(variable_elim.Variable_Elimination()) + "," + Integer.toString(variable_elim.getAdd_count()) + "," + Integer.toString(variable_elim.getMult_count());
////                }
////                else{
////                    ans = Double.toString(variable_elim.Variable_Elimination()).substring(0, 7) + "," + Integer.toString(variable_elim.getAdd_count()) + "," + Integer.toString(variable_elim.getMult_count());
////                }
////            }
////            else{
////                Algorithms My_variable_elim = new Algorithms(line.substring(0, line.length()-1), xml.getNet());
////                if(Double.toString(My_variable_elim.My_Variable_Elimination()).length()<7){
////                    ans = Double.toString(My_variable_elim.My_Variable_Elimination()) + "," + Integer.toString(My_variable_elim.getAdd_count()) + "," + Integer.toString(My_variable_elim.getMult_count());
////                }
////                else{
////                    ans = Double.toString(My_variable_elim.My_Variable_Elimination()).substring(0, 7) + "," + Integer.toString(My_variable_elim.getAdd_count()) + "," + Integer.toString(My_variable_elim.getMult_count());
////
////                }
////            }
//            out_file.write(ans);
//            if(reader.hasNextLine()){
//                out_file.newLine();
//            }
//        }
//        reader.close();
//        out_file.close();
//    }

}
