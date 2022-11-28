package Bayesian_net;

import java.util.ArrayList;
import java.util.Arrays;

public class Variable {
    private ArrayList<Variable> Var_Parents;
    private ArrayList<Variable> Var_Children;
    private String Var_name;
    private String [] Outcomes;
    private Factors CPT;



    public Variable(String Var_name, String [] Outcomes){
        this.Var_name = Var_name;
        this.Outcomes = Outcomes;
    }

    public String getVar_name() {
        return Var_name;
    }

    public ArrayList<Variable> getChildren() {
        return Var_Children;
    }

    public String[] getOutcomes() {
        return Outcomes;
    }

    public ArrayList<Variable> getParents() {
        return Var_Parents;
    }
    public void addParent(Variable parent){
        this.Var_Parents.add(parent);
    }
    public void addChild(Variable child){
        this.Var_Children.add(child);
    }
    @Override
    public String toString() {
        return "Variable{" +
                "Var_Parents=" + Var_Parents +
                ", Var_Children=" + Var_Children +
                ", Var_name='" + Var_name + '\'' +
                ", Outcomes=" + Arrays.toString(Outcomes) +
                ", CPT=" + CPT +
                '}';
    }
}
