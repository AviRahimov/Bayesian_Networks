package Bayesian_net;

import java.util.ArrayList;

public class BayesianNetwork {
    private ArrayList<Variable> vars;
    public BayesianNetwork(){
        this.vars = new ArrayList<>();
    }
    public void Add_Var(Variable var){
        this.vars.add(var);
    }
    public ArrayList<Variable> getVars(){
        return this.vars;
    }

}
