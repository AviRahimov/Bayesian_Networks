package Bayesian_net;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Algorithms {

    // the current bayesian network that I'm working with
    private BayesianNetwork net = new BayesianNetwork();

    // the probability in string that I'm going to calculate
    private String question;
    private int mult_count = 0;
    private int add_count = 0;
    private ArrayList<Variable> hidden= new ArrayList<Variable>();
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

        String str = "";
        for (String outcomes: net.getVars(String.valueOf(question.charAt(2))).getOutcomes()) {
            if(!outcomes.equals(String.valueOf(question.charAt(2)))){
                str = "P(" +question.charAt(2) + "=" + outcomes + question.substring(question.indexOf("|"), question.length());
            }
        }

        double mone = simple_probability(this.question);
        double mashlim_mone = simple_probability(str);
//        double mechane = simple_probability("P(" + question.substring(question.indexOf("|")+1, question.length()));
        double total_probability_for_question = mone/(mone+mashlim_mone);

        return total_probability_for_question;
    }

    public double simple_probability(String question){
        int num_outcomes_hidden = 1;

        // add the hidden variables to arraylist
        for (int i = 0; i < net.getNet().size(); i++) {
            if(!(question.contains(net.getNet().get(i).getVar_name()))){
                hidden.add(net.getNet().get(i));
            }
        }

        for (int i = 0; i < hidden.size(); i++) {
            num_outcomes_hidden*=hidden.get(i).getOutcomes().length;
        }
        // truth table for all the hidden variables
        String [] hidden_var_truth_table = new String[num_outcomes_hidden];
        Arrays.fill(hidden_var_truth_table, "");

        int divide = num_outcomes_hidden;
        for (int i = 0; i < hidden.size(); i++) {
            Variable temp = hidden.get(i);
            divide /= temp.getOutcomes().length;
            int module_outcome = 0;
            int count = 0;

            for (int j = 0; j < hidden_var_truth_table.length; j++) {
                if (count<divide){
                    hidden_var_truth_table[j] += temp.getVar_name() + "="  + temp.getOutcomes()[module_outcome] + ",";
                    count++;
                }
                else {
                    module_outcome++;
                    module_outcome %= temp.getOutcomes().length;
                    count = 0;
                    j--;
                }
            }
        }

        // String array that will contain all the variables in one probability
        // for example: P(b,e,a,j,m)
        String [] all_var_to_count = new String[num_outcomes_hidden];
        String new_question = question.replace('|', ',');
        for (int i = 0; i < all_var_to_count.length; i++) {
            all_var_to_count[i] = new_question.substring(0, new_question.length()-1) + "," + hidden_var_truth_table[i];
        }

        // Now, I need to extract each variable from the all_var_to_count array and to calculate his
        // probability given his parents
        String [] final_prob = new String[all_var_to_count.length];
        Arrays.fill(final_prob, "");
        int counter = 0;
        String curr = "";
        String temp_exist = "";
        for(String s:all_var_to_count){
            curr = s.substring(2, s.length());
            for (int i = 0; i <net.getNet().size(); i++) {
                if(net.getVars(String.valueOf(curr.charAt(0))).isParent()){
                    for (String exist_cpt: net.getVars(String.valueOf(curr.charAt(0))).getCPT().keySet()){
                        temp_exist = exist_cpt.replace("|", ",");
                        temp_exist = temp_exist.replace(")", ",");
                        int temp_count = 2;// 6
                        boolean is_exist = true;
                        for (int j = 0; j < net.getVars(String.valueOf(curr.charAt(0))).getParents().size()+1; j++){
                            if(!(s.contains(temp_exist.substring(temp_count, temp_exist.substring(temp_count, temp_exist.length()).indexOf(",") + temp_count)))){
                                is_exist = false;
                                break;
                            }
                            temp_count+=temp_exist.substring(temp_count, temp_exist.length()).indexOf(",")+1;
                        }
                        if (is_exist == true){
                            final_prob[counter] += exist_cpt;
                            break;
                        }
                    }
                }
                else{
                    final_prob[counter] += "P(" + curr.substring(0, curr.indexOf(",")) + ")";
                }

                if (curr.indexOf(",") != curr.length()){
                    curr = curr.substring(curr.indexOf(",") + 1, curr.length());
                }
            }
            counter++;
        }
        double calculate_prob = 1;
        double total_prob = 0;
        String temp = "";
        int index = 0;
        Variable v;
        for (String i: final_prob) {
            index = 0;
            calculate_prob = 1;
            temp = i.substring(index, i.substring(index+1, i.length()).indexOf("P")+1 + index);
            for (int j = 0; j < net.getNet().size(); j++) {
                if(i.substring(index+1).indexOf("P") == -1){
                    temp = i.substring(index);
                }
                else
                    temp = i.substring(index, i.substring(index+1, i.length()).indexOf("P")+1 + index);
                v = net.getVars(String.valueOf(temp.charAt(2)));
                calculate_prob*=v.getCPT().get(temp);
                index = i.substring(index+1).indexOf("P")+1+index;
                mult_count++;
            }
            total_prob+=calculate_prob;
            add_count++;
            mult_count--;
        }
        add_count--;
        return calculate_prob;
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
