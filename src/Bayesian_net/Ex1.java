package Bayesian_net;

public class Ex1 {
    public static void main(String[]args){
        /*
        we need Join function to join the tables
        Eliminate function to eliminate the calculated factor
        All proccess described in yael tirgul in the moodle
         */
        XMLParser file = new XMLParser("big_net.xml");
        System.out.println(file.getNet());
    }
}
