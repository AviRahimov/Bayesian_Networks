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
    public void setCPT(HashMap new_cpt){
        this.CPT = new_cpt;
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
        String Parents = "";
        String Children = "";
        if(isParent()) {
            Parents += "[";
            for (int i = 0; i < Var_Parents.size(); i++) {
                Parents += Var_Parents.get(i).Var_name + ",";
            }
            Parents = Parents.substring(0, Parents.length()-1) + "]";
        }
        else
            Parents = "[]";
        if(Var_Children.size()!=0) {
            Children += "[";
            for (int i = 0; i < Var_Children.size(); i++) {
                Children += Var_Children.get(i).Var_name + ",";
            }
            Children = Children.substring(0, Children.length()-1) + "]";
        }
        else
            Children = "[]";


        return  "Variable " + Var_name + ":\n" +
                "Var_Parents=" + Parents +
                "\nVar_Children=" + Children +
                "\nVar_name='" + Var_name + '\'' +
                "\nOutcomes=" + Arrays.toString(Outcomes) +
                "\nCPT=" + CPT +
                "\n\n";
    }
}
