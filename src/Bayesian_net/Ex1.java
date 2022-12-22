package Bayesian_net;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class Ex1 {
    public static void main(String[]args) throws IOException {
//        XMLParser xml = new XMLParser("big_net.xml");
//        XMLParser xml = new XMLParser("alarm_net.xml");
//        XMLParser xml = new XMLParser("net3.xml");
//        XMLParser xml = new XMLParser("net4.xml");
//        XMLParser xml = new XMLParser("net5.xml");
//        XMLParser xml = new XMLParser("net6.xml");
        XMLParser xml = new XMLParser("net7.xml");
//        XMLParser xml = new XMLParser("net8.xml");
//        XMLParser xml = new XMLParser("net9.xml");
//        XMLParser xml = new XMLParser("net10.xml");
        /*
net3.xml
P(S=ok|M=N) = 0.37606 add=17 mul=54 // not working well with the first algorithm
P(S=ok|M=N) = 0.37606,17,27
P(M=N|S=ok,F=annoying) = 0.89250 add=3 mul=12
P(M=N|S=ok,F=annoying) = 0.89250,3,4
P(N=T|S=ok,F=annoying) = 0.05000 add=3 mul=12

net4.xml // working well
P(D=F|G=fine) = 0.21518 add=15 mul=64
P(D=F|G=fine) = 0.21518,3,6
P(S=T|L=F,G=high) = 0.55050 add=7 mul=32
P(S=T|L=F,G=high) = 0.55050,5,10
P(G=low|L=F,S=T) = 0.73694 add=11 mul=48
P(G=low|L=F,S=T) = 0.73694,11,23

net5.xml
P(G=low|B=F,H=F,F=always) = 0.41379 add=2 mul=9
P(G=low|B=F,H=F,F=always) = 0.41379,2,3
P(B=T|F=never,G=medium) = 0.44444 add=3 mul=12
P(B=T|F=never,G=medium) = 0.44444,1,2
P(G=low|B=F,H=F) = 0.21083 add=8 mul=27 // not working well on the first algorithm
P(G=low|B=F,H=F) = 0.21083,8,12

net6.xml // working well
P(B=noset|A=T) = 0.50000 add=8 mul=18
P(B=noset|A=T) = 0.50000,2,0
P(A=F|C=stay) = 0.82111 add=5 mul=12
P(A=F|C=stay) = 0.82111,5,8
P(C=stay|B=set,A=F) = 0.15000 add=2 mul=6
P(C=stay|B=set,A=F) = 0.15000,2,0

net7.xml
P(A=T|E=two,F=two),1 // not working on the second algorithm
P(A=F|B=T,C=F,D=T),1
P(A=F|B=T,C=F,D=T),2
P(G=one|B=T,I=ken),1
P(G=one|B=T,I=ken),2
P(D=T|E=one),1
P(D=T|E=one),2
         */
//        System.out.println("In the simple heuristic we getting: ");
//        VariableElimination_algo v = new VariableElimination_algo("P(A=F|B=T,C=F,D=T)", xml.getNet());
//        System.out.println(v.Variable_elimination());
//        System.out.println(v.getAdd_count());
//        System.out.println(v.getMult_count());
//        System.out.println();
        Algorithms a = new Algorithms("P(A=F|B=T,C=F,D=T)", xml.getNet());
        System.out.println(a.Simple_dist());
        System.out.println(a.getAdd_count());
        System.out.println(a.getMult_count());
//        VariableElimination_algo v1 = new VariableElimination_algo("P(S=ok|M=N)", xml.getNet());
//        v1.different_heuristic();
//        System.out.println("In the new heuristic we getting: ");
//        System.out.println(v1.Different_heuristic_Variable_elimination());
//        System.out.println(v1.getAdd_count());
//        System.out.println(v1.getMult_count());

    }
}