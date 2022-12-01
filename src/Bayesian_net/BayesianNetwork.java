package Bayesian_net;

import java.util.ArrayList;

public class BayesianNetwork {
    private ArrayList<Variable> Net;
    public BayesianNetwork(){
        this.Net = new ArrayList<>();
    }
    public void Add_Var(Variable var){
        this.Net.add(var);
    }
    public boolean isVar(String var_name){
        for (int i = 0; i < Net.size(); i++) {
            if (var_name.equals(Net.get(i).getVar_name()))
                return true;
        }
        return false;
    }
    public Variable getVars(String var_name){
        if(isVar(var_name)){
            for (int i = 0; i < Net.size(); i++) {
                if (Net.get(i).getVar_name().equals(var_name))
                    return Net.get(i);
            }

        }
        return null;
    }
    public ArrayList<Variable> getNet() {
        return this.Net;
    }
    @Override
    public String toString() {
        return "BayesianNetwork{" +
                "Net=" + Net +
                '}';
    }
}
