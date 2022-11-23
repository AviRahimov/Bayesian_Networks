import java.util.ArrayList;

public class Variable {
    private ArrayList<Variable> Var_Parents;
    private ArrayList<Variable> Var_Children;
    private String Var_name;
    private String [] Outcomes;

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
}
