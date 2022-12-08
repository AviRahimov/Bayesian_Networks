package Bayesian_net;

import java.util.ArrayList;

public class Algorithms {
    // the current bayesian network that I'm working with
    private BayesianNetwork net = new BayesianNetwork();
    // the probability in string that I'm going to calculate
    private String question;
    private int mult_count = 0;
    private int add_count = 0;
    private ArrayList<String> hidden= new ArrayList<String>();
    public Algorithms(String question, BayesianNetwork net){
        this.net = net;
        this.question = question;
    }
    public double cpt_is_exist(String question){
        for (int i = 0; i < net.getNet().size(); i++) {
            for (int j = 0; j < net.getNet().get(i).getCPT().size(); j++) {
                if(net.getNet().get(i).getCPT().keySet().contains(question)){
                    return net.getNet().get(i).getCPT().get(question);
                }
            }
        }
        return -1;
    }

    public double Simple_dist(){

        if(cpt_is_exist(question) != -1){
            return cpt_is_exist(question);
        }

        String mone = question.replace('|', ',');
        String mechane = "P(" + question.substring(6, question.length());

        int mult_outcomes_hidden = 0;

        for (int i = 0; i < net.getNet().size(); i++) {
//            if(!question)

        }
        for (int i = 0; i < hidden.size(); i++) {
//            hidden.get(i)
        }

        return 0;
    }

    public double Variable_Elimination(){
        if(cpt_is_exist(question) != -1){
            cpt_is_exist(question);
        }
        else{

        }
        return 0;
    }
    public double My_Variable_Elimination(){
        return 0;}
    public int getMult_count(){
        return mult_count;
    }
    public int getAdd_count(){
        return add_count;
    }
}
