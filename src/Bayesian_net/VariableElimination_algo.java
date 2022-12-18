package Bayesian_net;

import java.util.*;

public class VariableElimination_algo {
    private BayesianNetwork factor_net = new BayesianNetwork();
    private BayesianNetwork full_net = new BayesianNetwork();
    private String question;

    private int mult_count = 0;

    private int add_count = 0;

    // Constructor
    public VariableElimination_algo(String question, BayesianNetwork factor_net){
        this.question = question;
        this.factor_net = factor_net;
        this.full_net = new BayesianNetwork(factor_net);
    }

    public ArrayList<String> getEvidence(){
        ArrayList<String> evidence= new ArrayList<>();
        String temp = "";
        for (int i = question.indexOf("|")+1; i < question.length()-1; i++) {
            temp = question.substring(i);
            temp = temp.replace(")", ",");
            evidence.add(question.substring(i, temp.indexOf(",") + i));
            if(temp.indexOf(",") == temp.length()-1){
                return evidence;
            }
            else {
                i = temp.indexOf(",") + i;
            }
        }
        return evidence;
    }
    public Variable getQuery(){
        Variable query = factor_net.getVars(question.substring(2, question.indexOf("=")));;
        return query;
    }
    public ArrayList<Variable> getHidden(){
        ArrayList<Variable> hidden = new ArrayList<>();
        for (int i = 0; i < factor_net.getNet().size(); i++) {
            if(!(question.contains(factor_net.getNet().get(i).getVar_name()))){
                hidden.add(factor_net.getNet().get(i));
            }
        }
        return hidden;
    }
    public void Evidence_elimination(){
        ArrayList<String> evidence = getEvidence();
        HashMap<String, Double> curr_cpt;
        for (String evidence_var : evidence) {
            curr_cpt = new HashMap<>();
            for (String cpt : factor_net.getVars(evidence_var.substring(0, evidence_var.indexOf("="))).getCPT().keySet()){
                if(cpt.contains(evidence_var)){
                    curr_cpt.put(cpt, factor_net.getVars(evidence_var.substring(0, evidence_var.indexOf("="))).getCPT().get(cpt));
                }
            }
            factor_net.getVars(evidence_var.substring(0, evidence_var.indexOf("="))).setCPT(curr_cpt);
        }
        // reduce the factors by removing the evidence variables from the table
        double prob = 0;
        String [] cpt_keys;
        for (String evidence_var : evidence) {
            String evidence_test = evidence_var.substring(0, evidence_var.indexOf("="));
            if(factor_net.getVars(evidence_test).getCPT().keySet().size()!=1){
                cpt_keys = factor_net.getVars(evidence_test).getCPT().keySet().toArray(new String[0]);
                for (String evid_reduc : cpt_keys){
                    prob = factor_net.getVars(evidence_test).getCPT().get(evid_reduc);
                    factor_net.getVars(evidence_test).getCPT().remove(evid_reduc);
                    factor_net.getVars(evidence_test).getCPT().put(evid_reduc.replace(evid_reduc.substring(2, evid_reduc.indexOf("|")+1), ""), prob);
                }
            }
            else{
                factor_net.getNet().remove(factor_net.getVars(evidence_test));
            }
        }
    }
    // eliminate unneeded hidden variables i.e. hidden that not ancient parents of query or evidence
    // must become after running the evidence elimination because we need the factors and not the full CPT
    public void Hidden_elimination(){
       ArrayList<Variable> hidden = getHidden();
       ArrayList<Variable> copy_hidden = getHidden();
       // running over all the hidden variables
       for(Variable hid : copy_hidden){
           if(!(is_contain_QueryOrEvidence(hid))){
               factor_net.getNet().remove(hid);
               hidden.remove(hid);
           }
       }
    }
    // The recursion function to find if a hidden variable is a ancient parent of query or evidence variable
    public boolean is_contain_QueryOrEvidence(Variable hid){
        if(hid == null){
            return false;
        }
        if(hid.getVar_name().equals(getQuery().getVar_name()) || getEvidence().contains(hid.getVar_name())){
            return true;
        }
        for (Variable child : hid.getChildren()) {
            return(is_contain_QueryOrEvidence(child));
        }
        return false;
    }
    // The join function...
//    public
}
