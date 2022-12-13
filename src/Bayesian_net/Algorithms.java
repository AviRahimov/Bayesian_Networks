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
    public Algorithms(String question, BayesianNetwork net){
        this.net = net;
        this.question = question;
    }

    /**
     *
     * @param question is the probability String that I want to compute. this function is only checks
     *                 if the question that given is in the cpt of some variable or not.
     * @return the probability if exist and -1 if not.
     */
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

    /**
     *
     * @return The probability of the question by the first method i.e. method number 1 - simple deduction
     */
    public double Simple_dist(){
        if(cpt_is_exist(question) != -1){
            return cpt_is_exist(question);
        }
        /*
         initialize a string array that holds the variables we need to compute to get the denominator
         of the fraction, for example: if i have the question:P(B=T|J=F) so, by bayes I can compute this:
         P(B=T|J=F) = P(B=T,J=F)/P(J=F) but P(J=F) by the law of total probability equal to P(B=T,J=F) + P(B=F,J=F).
        */
        ArrayList<String> complete_strings = new ArrayList<>();
        for (String outcomes: net.getVars(question.substring(2, question.indexOf("="))).getOutcomes()) {
            if(!outcomes.equals(question.substring(question.indexOf("=")+1, question.indexOf("|")))){
                complete_strings.add("P(" + question.substring(2, question.indexOf("=")) + "=" + outcomes + question.substring(question.indexOf("|"), question.length()));
            }
        }
        /*
         The number of times I Call the Simple_prob function is equal to the number of elements in
         the complete_string array
        */
        double complement = 0;
        for (int i = 0; i < complete_strings.size(); i++) {
            complement+=Simple_Prob(complete_strings.get(i));
        }
        // Computing the whole probability and name it total_probability_for_question.
        double numerator = Simple_Prob(this.question);
        double total_probability_for_question = numerator/(numerator+complement);
        add_count++;
        return total_probability_for_question;
    }

    /**
     *
     * @param question the probability string that I need to compute
     * @return specific probability to the probability string that the function gets,
     * important: this function not return the full solution in one call, we need to call it several times to
     * compute the whole probability as I mentioned above.
     */
    public double Simple_Prob(String question){
        // arraylist that contains all the hidden variables
        ArrayList<Variable> hidden = new ArrayList<>();

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
                if(net.getVars(String.valueOf(curr.substring(0, curr.indexOf("=")))).isParent()){
                    for (String exist_cpt: net.getVars(String.valueOf(curr.substring(0, curr.indexOf("=")))).getCPT().keySet()){
                        temp_exist = exist_cpt.replace("|", ",");
                        temp_exist = temp_exist.replace(")", ",");
                        int temp_count = 2;// 6
                        boolean is_exist = true;
                        for (int j = 0; j < net.getVars(String.valueOf(curr.substring(0, curr.indexOf("=")))).getParents().size()+1; j++){
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
//                    v = net.getVars(String.valueOf(temp.substring(2, temp.indexOf("="))));
//                    calculate_prob*=v.getCPT().get(temp);
//                    mult_count+=4;
//                    break;
                }
                else
                    temp = i.substring(index, i.substring(index+1, i.length()).indexOf("P")+1 + index);
                v = net.getVars(String.valueOf(temp.substring(2, temp.indexOf("="))));
                calculate_prob*=v.getCPT().get(temp);
                index = i.substring(index+1).indexOf("P")+1+index;
                mult_count++;
            }
            total_prob+=calculate_prob;
            add_count++;
            mult_count--;
        }
        add_count--;
        return total_prob;
    }
    public int getMult_count(){
        return mult_count;
    }
    public int getAdd_count(){
        return add_count;
    }
}
