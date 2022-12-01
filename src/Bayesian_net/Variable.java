package Bayesian_net;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Variable {
    private ArrayList<Variable> Var_Parents;
    private ArrayList<Variable> Var_Children;
    private String Var_name;
    private String [] Outcomes;
    private HashMap<String, Double> CPT;



    public Variable(String Var_name, String [] Outcomes){
        this.Var_name = Var_name;
        this.Outcomes = Outcomes;
        this.Var_Children = new ArrayList<>();
        this.Var_Parents = new ArrayList<>();
        this.CPT = new HashMap<>();
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

    public HashMap<String, Double> getCPT() {
        return this.CPT;
    }

    public void addParent(Variable parent){
        this.Var_Parents.add(parent);
    }
    public void addChild(Variable child){
        this.Var_Children.add(child);
    }
    public boolean isParent(){
        return this.Var_Parents.size()>0;
    }
    @Override
    public String toString() {
        return "Variable{" +
                "Var_Parents=" + Var_Parents +
                ", Var_Children=" + Var_Children +
                ", Var_name='" + Var_name + '\'' +
                ", Outcomes=" + Arrays.toString(Outcomes) +
                ", CPT=" + CPT +
                '}' + "\n";
    }
}
