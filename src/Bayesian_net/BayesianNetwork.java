package Bayesian_net;

import java.util.ArrayList;
import java.util.HashMap;

public class BayesianNetwork {
    private ArrayList<Variable> Net;
    public BayesianNetwork(){
        this.Net = new ArrayList<>();
    }
    public BayesianNetwork(BayesianNetwork copy_net){
//        this.Net = new ArrayList<>();
        this.Net = (ArrayList<Variable>) copy_net.getNet().clone();
//        for (int i = 0; i < copy_net.getNet().size(); i++) {
//            this.Net.add(new Variable(copy_net.getNet().get(i).getVar_name(), copy_net.getNet().get(i).getOutcomes()));
//            this.Net.get(i).setCPT(new HashMap<>(copy_net.getNet().get(i).getCPT()));
//            for (int j = 0; j < copy_net.getNet().get(i).getParents().size(); j++) {
//                this.Net.get(i).addParent(copy_net.getNet().get(i).getParents().get(j));
//            }
//            for (int j = 0; j < copy_net.getNet().get(i).getChildren().size(); j++) {
//                this.Net.get(i).addChild(copy_net.getNet().get(i).getChildren().get(j));
//            }
//        }
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

    public void setNet(ArrayList<Variable> setting) {
        this.Net = setting;
    }
}
