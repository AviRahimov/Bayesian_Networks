package Bayesian_net;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class Ex1 {
    public static void main(String[]args) throws IOException {
        XMLParser xml = new XMLParser("alarm_net.xml");
//        XMLParser xml = new XMLParser("big_net.xml");
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
//          Algorithms a = new Algorithms("P(J=T|B=T)", xml.getNet());
//        VariableElimination_algo v = new VariableElimination_algo("P(J=T|B=T,A=F)", xml.getNet());
//        VariableElimination_algo v = new VariableElimination_algo("P(B=T|J=T,M=T)", xml.getNet());
//        VariableElimination_algo v = new VariableElimination_algo("P(D1=T|C2=v1,C3=F)", xml.getNet());
          VariableElimination_algo v = new VariableElimination_algo("P(J=T|B=T)", xml.getNet());
//         System.out.printf("%.5f %n", a.Simple_dist());
//        System.out.println(a.getAdd_count());
//        System.out.println(a.getMult_count());
//        System.out.println(xml.getNet());
//        System.out.println("**********************************************************************************");
//        System.out.println(v.getFactor_net());
        System.out.println(v.Variable_elimination());
        System.out.println("Add: " + v.getAdd_count());
        System.out.println("Mult: " + v.getMult_count());
//        v.HashMap_keySet_to_String_Array(v.getFactor_net().getVars("C2").getCPT().keySet().toString());
//        System.out.println("P(A=F|E=T,B=T)".replace("B=T","" ));
//        String str = "P(A=F|E=T,B=T,D=v1)";
//        String [] st = {"A=F", "B=T"};
//        System.out.println(v.pure_factor_from_evidence(str, st));
//        HashMap<String,Double> temp = new HashMap<>();
//        temp.put("P(B=F)", 0.001491857649);
//        temp.put("P(B=T)", 5.922425899999999E-4);
//        System.out.println(Arrays.toString(new Collection[]{temp.values()}));
//        }
    }
}