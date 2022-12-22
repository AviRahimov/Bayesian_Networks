package Bayesian_net;

import java.util.*;

public class VariableElimination_algo {
    private BayesianNetwork factor_net;
    private final BayesianNetwork full_net;
    private BayesianNetwork copy_factor_net = new BayesianNetwork();
    private ArrayList<Variable> hidden_variables = new ArrayList<>();
    private final String question;

    private int mult_count = 0;

    private int add_count = 0;

    // Constructor
    public VariableElimination_algo(String question, BayesianNetwork factor_net){
        this.question = question;
        this.factor_net = factor_net;
        this.full_net = new BayesianNetwork();
        this.full_net.setNet((ArrayList<Variable>) this.factor_net.getNet().clone());
    }

    public ArrayList<String> getEvidence(){
        ArrayList<String> evidence= new ArrayList<>();
        String temp;
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
        return full_net.getVars(question.substring(2, question.indexOf("=")));
    }
    public ArrayList<Variable> getHidden(BayesianNetwork net){
        hidden_variables = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < net.getNet().size(); i++) {
            index = question.indexOf(net.getNet().get(i).getVar_name());
            if(!(question.contains(net.getNet().get(i).getVar_name()))){
                hidden_variables.add(net.getNet().get(i));
            }

            else if (question.substring(index-1).indexOf("=") == 0){
                hidden_variables.add(net.getNet().get(i));
            }
        }
        return hidden_variables;
    }
    public void Evidence_elimination(){
        hidden_variables = getHidden(factor_net);
        ArrayList<String> evidence = getEvidence();
        String [] variable_keys;
        for (Variable variables : factor_net.getNet()){
            variable_keys = HashMap_keySet_to_String_Array(variables.getCPT().keySet().toString());
            for (String evidence_var : evidence){
                for (String keys : variable_keys){
                    if (keys.contains(evidence_var.substring(0,evidence_var.indexOf("=")))){
                        if (!(keys.contains(evidence_var))){
                            factor_net.getVars(variables.getVar_name()).getCPT().remove(keys);
                        }
                    }
                    else {
                        break;
                    }

                }
            }
        }
        // reduce the factors by removing the evidence variables from the table
        String [] ev = evidence.toArray(new String[0]);
        double prob;
        String str_prob;
        HashMap<String,Double> temp = new HashMap<>();
        for (Variable variable : factor_net.getNet()){
            variable_keys = HashMap_keySet_to_String_Array(variable.getCPT().keySet().toString());
            for (String key : variable_keys){
                prob = variable.getCPT().get(key);
                str_prob = pure_factor_from_evidence(key, ev);
                temp.put(str_prob, prob);
            }
            factor_net.getVars(variable.getVar_name()).setCPT(temp);
            temp = new HashMap<>();
        }
        ArrayList<Variable> to_remove = new ArrayList<>();
        for (Variable variable : factor_net.getNet()){
            if (variable.getCPT().size() <= 1){
                to_remove.add(variable);
            }
        }
        for (Variable variable : to_remove){
            factor_net.getNet().remove(variable);
        }
    }
    public String pure_factor_from_evidence(String to_change, String [] evidences){
        StringBuffer to_delete = new StringBuffer(to_change);
        int start, end = 0;
        for (String evidence : evidences){
            if (to_change.contains(evidence)){
                if(to_delete.indexOf(evidence) == 2){
                    start = 2;
                    end = evidence.length()+3;
                    to_delete = to_delete.delete(start, end);
                }
                else {
                    start = to_delete.indexOf(evidence)-1;
                    end = evidence.length()+start+1;
                    to_delete = to_delete.delete(start, end);
                }
            }
        }
        to_change = to_delete.toString();
        return to_change;
    }

    public String [] HashMap_keySet_to_String_Array(String keyset){
        String[] key_set_arraylist = keyset.split("\\)");
        for (int i = 0; i < key_set_arraylist.length-1; i++) {
            key_set_arraylist[i] = key_set_arraylist[i].replace(" ", "");
            key_set_arraylist[i] = key_set_arraylist[i].replace("[", "");
            key_set_arraylist[i] += ")";
            if(i!= 0){
                key_set_arraylist[i] = key_set_arraylist[i].substring(1);
            }
        }
        String [] updated_array = new String[key_set_arraylist.length-1];
        for (int i = 0; i < updated_array.length; i++) {
            updated_array[i] = key_set_arraylist[i];
        }
        return updated_array;
    }
    // eliminate unneeded hidden variables i.e. hidden that not ancient parents of query or evidence
    // must become after running the evidence elimination because we need the factors and not the full CPT
    public void Hidden_elimination(){
        hidden_variables = getHidden(factor_net);
        ArrayList<Variable> copy_hidden = (ArrayList<Variable>) hidden_variables.clone();
        for(Variable hid : copy_hidden){
            if(!(is_contain_QueryOrEvidence(hid))){
               factor_net.getNet().remove(hid);
            }
        }
    }
    // The recursion function to find if a hidden variable is an ancient parent of query or evidence variable
    public boolean is_contain_QueryOrEvidence(Variable hid){
        if(hid == null){
            return false;
        }
        if(hid.getVar_name().equals(getQuery().getVar_name()) || evidenceWithNoOutcome().contains(hid.getVar_name())){
            return true;
        }
        for (Variable child : hid.getChildren()) {
            return(is_contain_QueryOrEvidence(child));
        }
        return false;
    }
    public ArrayList<String> evidenceWithNoOutcome(){
        ArrayList<String> evidence = new ArrayList<>();
        for (String evid : getEvidence()){
            evidence.add(String.valueOf(evid.charAt(0)));
        }
        return evidence;
    }


    // The join function...
    //need to join all the variables that contain the hidden variables remained and finally to eliminate the current hidden
    public HashMap<String,Double> join(ArrayList<HashMap<String,Double>> factors_containing_hidden, String hidden){
        // removing the factors that I'm joined
        ArrayList<Variable> factor_net_copy = (ArrayList<Variable>) factor_net.getNet().clone();
        for (HashMap<String,Double> cpt : factors_containing_hidden) {
            for (Variable var : factor_net_copy) {
                if (var.getCPT().equals(cpt)) {
                    factor_net.getNet().remove(var);
                }
            }
        }
        System.out.println("NEW******************************************************************************************************************************************");
        HashMap<String,Double> merged_factors = new HashMap<>();
        if(factors_containing_hidden.size()==1){
            System.out.println("The hidden variable I'm going to eliminate is: " + hidden);
            merged_factors = eliminate(factors_containing_hidden.get(0), hidden);
            factors_containing_hidden.remove(factors_containing_hidden.get(0));
            factors_containing_hidden.add(merged_factors);
            System.out.println("END NEW***************************************************************************************************************************************");
        }
        else {
            ArrayList<HashMap<String, Double>> factors_containing_hidden_copy = (ArrayList<HashMap<String, Double>>) factors_containing_hidden.clone();
            for (int i = 0; i < factors_containing_hidden_copy.size() - 1; i++) {
                // find two minimalistic factors
                ArrayList<HashMap<String, Double>> two_minimal_factors = find_two_minimal_factors((ArrayList<HashMap<String, Double>>) factors_containing_hidden.clone());
                // removing from the list of factors that containing the hidden variables the two minimalistic
                // factors, and then I will add the joined factor of them.
                for (HashMap<String, Double> remove_factor : two_minimal_factors) {
                    factors_containing_hidden.remove(remove_factor);
                }
        /*
         saving  2 minimum factor sizes to find the two minimalistic factors and join them
         in addition, saving their indexes to send them to the join_two_factors function,
         I'm not sending the factors in the current map of factors because I remove them from the map
        and can't access them after deletion.
        */
                merged_factors = two_factors_to_join(two_minimal_factors.get(0), two_minimal_factors.get(1));
                factors_containing_hidden.add(merged_factors);
            }

            factors_containing_hidden.add(eliminate(merged_factors, hidden));
            factors_containing_hidden.remove(merged_factors);
        }
        return factors_containing_hidden.get(0);
    }

    // running all over the factors that contains the hidden variables and finding the two minimum factor sizes
    // to send them to the join_two_factors function, because I join factors by their size
    public ArrayList<HashMap<String,Double>> find_two_minimal_factors(ArrayList<HashMap<String,Double>> list_of_factors) {
        ArrayList<HashMap<String,Double>> two_minimal_factors = new ArrayList<>();
        HashMap<String,Double> min_factor1 = list_of_factors.get(0);
        for (int i = 1; i < list_of_factors.size(); i++) {
            if(list_of_factors.get(i).size()<min_factor1.size()){
                min_factor1 = list_of_factors.get(i);
            }
        }
        list_of_factors.remove(min_factor1);
        HashMap<String,Double> min_factor2 = list_of_factors.get(0);
        for (int i = 1; i < list_of_factors.size(); i++) {
            if(list_of_factors.get(i).size()<min_factor2.size()){
                min_factor2 = list_of_factors.get(i);
            }
        }
        two_minimal_factors.add(min_factor1);
        two_minimal_factors.add(min_factor2);
        return two_minimal_factors;
    }

    public HashMap<String,Double> two_factors_to_join(HashMap<String,Double> factor1, HashMap<String,Double> factor2){
        HashMap<String,Double> merged_factor = new HashMap<>();
        ArrayList<String> merged_table_vars = new ArrayList<>();
        // run over factor1 and factor2 to extract the common variables and put them into array
        // that contain all the common variables
        String temp_vars = factor1.keySet().toArray()[0].toString().substring(2);
        temp_vars = temp_vars.replace(")", ",");
        temp_vars = temp_vars.replace("|", ",");
        int i = 0;
        while (i<temp_vars.length()){
            merged_table_vars.add(temp_vars.substring(0, temp_vars.indexOf("=")));
            if(temp_vars.indexOf(",")+1>=temp_vars.length()){
                break;
            }
            temp_vars = temp_vars.substring(temp_vars.indexOf(",")+1);
            i = temp_vars.indexOf(",");
        }
        temp_vars = factor2.keySet().toArray()[0].toString().substring(2);
        temp_vars = temp_vars.replace(")", ",");
        temp_vars = temp_vars.replace("|", ",");
        while (i<temp_vars.length()){
            if(!(merged_table_vars.contains(temp_vars.substring(0, temp_vars.indexOf("="))))){
                merged_table_vars.add(temp_vars.substring(0, temp_vars.indexOf("=")));
            }
            if(temp_vars.indexOf(",")+1>=temp_vars.length()){
                break;
            }
            temp_vars = temp_vars.substring(temp_vars.indexOf(",")+1);
            i = temp_vars.indexOf(",");
        }
        // Now I'm going to create the merged table for the factors with the numbers of outcomes
        // in the merged_table_vars array.
        ArrayList<Variable> variables_in_truth_table = new ArrayList<>();
        for (String merged_table_var : merged_table_vars) {
            Variable temp = full_net.getVars(merged_table_var);
            variables_in_truth_table.add(temp);
        }
        String [] truth_table = make_truth_table(variables_in_truth_table);
        /*
         Now I'm going to add the probability for each cell in the truth table I created and then add it to a new
         map that will hold the probability strings with numbers and this will be the merged(joined) factors- factor1
         and factor2.
        */
        ArrayList<String> needed_lines_factor1 = extract_lines(truth_table, factor1);
        ArrayList<String> needed_lines_factor2 = extract_lines(truth_table, factor2);
        ArrayList<Double> truth_table_probability = extract_prob_in_lines(factor1, factor2, needed_lines_factor1, needed_lines_factor2);
        for (int j = 0; j < truth_table.length; j++) {
            merged_factor.put(truth_table[j], truth_table_probability.get(j));
        }
        return merged_factor;
    }

    // Check if all the Strings in the str_array is in the str string
    public boolean is_contain(String str, String [] str_array){
        for (String s : str_array) {
            if (!(str.contains(s))) {
                return false;
            }
        }
        return true;
    }
    // function to extract the lines I need from two factors I have i.e. if I have some truth_table so, to fill the
    // table I need to take specific lines from factor1 and factor2, it's a part of join method and because
    // I do this for factor1 and factor2, so I'm creating a function for this.
    public ArrayList<String> extract_lines(String [] truth_table, HashMap<String,Double> factor) {
        ArrayList<String> factor_prob = new ArrayList<>();
        String prob_factor = "";
        String truth_table_string;
        String factor_keyset_string;
        String factor_keyset_one_string;
        String[] factor_keyset_one_string_array;
        // iterate over the truth table strings to add the probability
        for (String s : truth_table) {
            factor_keyset_string = factor.keySet().toString();
            factor_keyset_string = factor_keyset_string.replace("|", ",");
            truth_table_string = s;// P(E=T,A=T,B=T)
            // iterate over the factor1 strings
            for (int k = 0; k < factor.keySet().size(); k++) {// 8 times
                factor_keyset_one_string = factor_keyset_string.substring(3, factor_keyset_string.indexOf(")"));// A=F,E=T,B=T
                factor_keyset_one_string_array = factor_keyset_one_string.split(",");// [A=F, E=T, B=T]
                if (is_contain(truth_table_string, factor_keyset_one_string_array)) {
                    prob_factor = factor_keyset_string.substring(1, factor_keyset_string.indexOf(")") + 1);// P(A=T|E=T,B=T)
                    break;
                } else {
                    factor_keyset_string = factor_keyset_string.substring(factor_keyset_string.substring(2).indexOf("P") + 1);
                }
            }
            if (factor.keySet().toString().contains("|")) {
                prob_factor = prob_factor.replace(" ", "");
                prob_factor = prob_factor.replaceFirst(",", "|");
                factor_prob.add(prob_factor);
            } else {
                prob_factor = prob_factor.replace(" ", "");
                factor_prob.add(prob_factor);
            }
        }
        return factor_prob;
    }
    public ArrayList<Double> extract_prob_in_lines(HashMap<String,Double> factor1, HashMap<String,Double> factor2, ArrayList<String> factor1_needed_lines, ArrayList<String> factor2_needed_lines){
        ArrayList<Double> truth_table_probability = new ArrayList<>();
        for (int i = 0; i < factor1_needed_lines.size(); i++) {
            truth_table_probability.add(factor1.get(factor1_needed_lines.get(i))*factor2.get(factor2_needed_lines.get(i)));
            mult_count++;
        }
        return truth_table_probability;
    }
    //The eliminate function...
    public HashMap<String,Double> eliminate(HashMap<String,Double> factor_to_eliminate, String variable_to_eliminate){
        System.out.println("Variable to eliminate: " + variable_to_eliminate);
        System.out.println("The factor that holds the variable: " + factor_to_eliminate);
        if(factor_to_eliminate.size()>2) {
            HashMap<String, Double> factor_after_elimination = new HashMap<>();
            ArrayList<Variable> all_var_without_varElim = new ArrayList<>();
            // string_to_var contains the variables in all_var_without_varElim in String
            ArrayList<String> string_to_var = new ArrayList<>();
            // Creating StringBuffer because later I will delete char in the string and, it's easy to do
            // with StringBuffer.
            StringBuffer constant = new StringBuffer("");
            // taking random key from the factor to extract the variables without the variable I eliminate
            // and because it doesn't matter what is the outcome so, I can choose random key.
            String search = factor_to_eliminate.keySet().toArray()[0].toString();
            String sub_string = search.substring(search.indexOf(variable_to_eliminate));
            int index_of_var_eliminate = search.indexOf(variable_to_eliminate);
            String temp;
            try {
                constant.append(search.replace(sub_string.substring(0,sub_string.indexOf(",")),""));
                constant.deleteCharAt(index_of_var_eliminate);
                temp = constant.toString().substring(2);
            }
            catch (Exception e){
                return factor_to_eliminate;
            }

            int i = -1;
            while (i < constant.length() && i != 0) {
                string_to_var.add(temp.substring(0, temp.indexOf("=")));
                if (temp.contains("|")) {
                    i = temp.indexOf("|") + 1;
                    temp = temp.substring(temp.indexOf("|"));
                } else {
                    i = temp.indexOf(",") + 1;
                    temp = temp.substring(temp.indexOf(",") + 1);
                }
            }
            for (String s : string_to_var) {
                all_var_without_varElim.add(copy_factor_net.getVars(s));
            }
            System.out.println("The variables without the variable that need to be eliminated are: " + all_var_without_varElim);
            String[] elimination_truth_table = make_truth_table(all_var_without_varElim);
            Double[] elimination_prob_truth_table = new Double[elimination_truth_table.length];
            Arrays.fill(elimination_prob_truth_table, 0.0);
            int count = 0;
            // create an array that will hold the Strings in the factor
            String[] factor_strings = factor_to_eliminate.keySet().toString().substring(1, factor_to_eliminate.keySet().toString().length() - 1).split("\\)");
            for (int j = 0; j < elimination_truth_table.length; j++) {
                for (String keys : factor_strings) {
                    keys = keys + ")";
                    keys = keys.replace(", ", "");

                    if (is_contain_NotInOrder(keys, elimination_truth_table[j].substring(2, elimination_truth_table[j].length() - 1))) {
                        elimination_prob_truth_table[j] += factor_to_eliminate.get(keys);
                        count++;
                    }
                }
            }
            count/=2;
            add_count+=count;
            for (int j = 0; j < elimination_prob_truth_table.length; j++) {
                factor_after_elimination.put(elimination_truth_table[j], elimination_prob_truth_table[j]);
            }
            return factor_after_elimination;
        }
        else{
            return factor_to_eliminate;
        }
    }
    public boolean is_contain_NotInOrder(String full, String sub_str){
        String [] sub_str_arr = sub_str.split(",");
        for (String sub_string : sub_str_arr){
            if(!(full.contains(sub_string))){
                return false;
            }
        }
        return true;
    }
    public String[] make_truth_table(ArrayList<Variable> variables){
        int table_size = 1;
        for (Variable variable : variables) {
            table_size *= full_net.getVars(variable.getVar_name()).getOutcomes().length;
        }
        String [] truth_table = new String[table_size];
        Arrays.fill(truth_table, "");
        int divide = table_size;
        for (Variable temp : variables) {
            divide /= temp.getOutcomes().length;
            int module_outcome = 0;
            int count = 0;

            for (int j = 0; j < truth_table.length; j++) {
                if (count < divide) {
                    truth_table[j] += temp.getVar_name() + "=" + temp.getOutcomes()[module_outcome] + ",";
                    count++;
                } else {
                    module_outcome++;
                    module_outcome %= temp.getOutcomes().length;
                    count = 0;
                    j--;
                }
            }
        }
        for (int j = 0; j < truth_table.length; j++) {
            truth_table[j] = "P(" + truth_table[j].substring(0, truth_table[j].length()-1) + ")";
        }
        return truth_table;
    }
    public HashMap<String,Double> join_query(ArrayList<HashMap<String,Double>> query_and_last_factor) {
        Variable query = getQuery();
        HashMap<String, Double> last_factor;
        for (HashMap<String,Double> factors : query_and_last_factor){
            if(!factors.keySet().toString().contains(query.getVar_name())){
                query_and_last_factor.remove(factors);
                return query_and_last_factor.get(0);
            }
        }
        last_factor = join(query_and_last_factor, query.getVar_name());
        return last_factor;
    }
    public double normalize(HashMap<String,Double> last_factor, String query){
        double to_divide = 0;
        double mone = last_factor.get(query);
        for (double sum : last_factor.values()){
            to_divide+=sum;
            add_count++;
        }
        add_count--;
        return (mone/to_divide);
    }
    public double question_is_exist_in_net(String question){
        String temp_exist = "";

        for (String exist_cpt: full_net.getVars(question.substring(2, question.indexOf("="))).getCPT().keySet()){
            temp_exist = exist_cpt.replace("|", ",");
            temp_exist = temp_exist.replace(")", ",");
            int temp_count = 2;
            boolean is_exist = true;
            for (int j = 0; j < question.chars().filter(ch -> ch == '=').count();j++){
                try {
                    if(!(question.contains(temp_exist.substring(temp_count, temp_exist.substring(temp_count, temp_exist.length()).indexOf(",") + temp_count)))){
                        is_exist = false;
                        break;
                    }
                } catch (StringIndexOutOfBoundsException e) {
                    return -1;
                }

                temp_count+=temp_exist.substring(temp_count, temp_exist.length()).indexOf(",")+1;
            }
            if (is_exist == true){
                String cpt = question.substring(2, question.indexOf("="));
                return factor_net.getVars(cpt).getCPT().get(exist_cpt);
            }
        }
        return -1;
    }
    public double Variable_elimination(){
        System.out.println("the factor net before changes factor_net: " + factor_net);
        if (question_is_exist_in_net(question) == -1) {
            Evidence_elimination();
            System.out.println("the net after evidence elimination " + factor_net);
            System.out.println("*****************************************************************************************");
            Hidden_elimination();
            System.out.println("the net after hidden elimination " + factor_net);
            System.out.println("*****************************************************************************************");
            // Sorting the hidden variables
            hidden_variables = getHidden(factor_net);
            ArrayList<String> hidden_sorted = new ArrayList<>();
            for (Variable hidden : hidden_variables) {
                hidden_sorted.add(hidden.getVar_name());
            }
            Collections.sort(hidden_sorted);
            System.out.println("the sorted arraylist of hidden variables " + hidden_sorted);
            System.out.println("*****************************************************************************************");
            // copy the factor_net to work on it and to make changes on it because if I want to delete some
            // variable from the net, so I can't do this on the original net.
            copy_factor_net = new BayesianNetwork(factor_net);
            // find the factors that contains the hidden variables
            for (String hidden : hidden_sorted) {
                System.out.println("the factor net before eliminate the hidden " + hidden + " is" + factor_net);
                System.out.println("***********************************************************************************");
                ArrayList<HashMap<String, Double>> factors_contains_hidden = new ArrayList<>();
                for (Variable is_contain : factor_net.getNet()) {
                    if (is_contain.getCPT().keySet().toString().contains(hidden)) {
                        factors_contains_hidden.add(is_contain.getCPT());
                    }
                }
                System.out.println("the factors that contains the hidden variable " + hidden + " are: " + factors_contains_hidden);
                System.out.println("the size of the factors is: " + factors_contains_hidden.size());
                System.out.println("***********************************************************************************");
                Variable eliminated_var = new Variable("eliminated_var", null);
                eliminated_var.setCPT(join(factors_contains_hidden, hidden));
                factor_net.getNet().add(eliminated_var);
                System.out.println("the factor net after eliminate the " + hidden + " variable " + factor_net);
            }
            System.out.println("***********************************************************************************");
            System.out.println("the factor net after joining all the hidden variables: " + factor_net);

            ArrayList<HashMap<String, Double>> query_and_last_factor = new ArrayList<>();
            for (Variable variable : factor_net.getNet()) {
                query_and_last_factor.add(variable.getCPT());
            }
            System.out.println("***********************************************************************************");
            System.out.println("The query factor and the last factor are: " + query_and_last_factor);
            HashMap<String, Double> result_factor;
            result_factor = join_query(query_and_last_factor);
            System.out.println("***********************************************************************************");
            System.out.println("The result factor is: " + result_factor);
            String final_question = question.substring(0, question.indexOf("|")) + ")";
            return normalize(result_factor, final_question);
        }
        return question_is_exist_in_net(question);
    }
    public BayesianNetwork getFactor_net() {
        return factor_net;
    }

    public int getMult_count() {
        return mult_count;
    }

    public int getAdd_count() {
        return add_count;
    }
    public ArrayList<String> different_heuristic(){
        ArrayList<String> sorted_min_edges = new ArrayList<>(full_net.getNet().size());
        Integer [] number_of_edges = new Integer[full_net.getNet().size()];
        Variable temp;
        for (int i = 0; i < full_net.getNet().size(); i++) {
            temp = full_net.getNet().get(i);
//            number_of_edges[i] = temp.getParents().size()+temp.getChildren().size();
            number_of_edges[i] = temp.getOutcomes().length;
        }
        Arrays.sort(number_of_edges);
        boolean flag = true;
        for (Variable variable : full_net.getNet()){
            flag = true;
            for (String evid : getEvidence()){
                if (evid.contains(variable.getVar_name())){
                    flag = false;
                    break;
                }
            }
            if (flag == true){
                if(!(getQuery().getVar_name().equals(variable.getVar_name()))){
                    for (int number_of_edge : number_of_edges){
                        if (variable.getChildren().size()+variable.getParents().size() == number_of_edge){
                            sorted_min_edges.add(variable.getVar_name());
                            break;
                        }
                    }
                }
            }
            }
        return sorted_min_edges;
    }
    public double Different_heuristic_Variable_elimination(){
        this.factor_net.setNet((ArrayList<Variable>) full_net.getNet().clone());
        System.out.println("the full net is: " + full_net);
        System.out.println("the factor net before changes factor_net: " + factor_net);
//        question_is_exist_in_net();
        Evidence_elimination();
        System.out.println("the net after evidence elimination " + factor_net);
        System.out.println("*****************************************************************************************");
        Hidden_elimination();
        System.out.println("the net after hidden elimination " + factor_net);
        System.out.println("*****************************************************************************************");
        // Sorting the hidden variables
        hidden_variables = getHidden(factor_net);
        ArrayList<String> hidden_sorted = different_heuristic();
        System.out.println("the sorted arraylist of hidden variables " + hidden_sorted);
        System.out.println("*****************************************************************************************");
        // copy the factor_net to work on it and to make changes on it because if I want to delete some
        // variable from the net, so I can't do this on the original net.
        copy_factor_net = new BayesianNetwork(factor_net);
        // find the factors that contains the hidden variables
        for (String hidden : hidden_sorted){
            System.out.println("the factor net before eliminate the hidden " + hidden + " is" + factor_net);
            System.out.println("***********************************************************************************");
            ArrayList<HashMap<String,Double>> factors_contains_hidden = new ArrayList<>();
            for (Variable is_contain : factor_net.getNet()){
                if(is_contain.getCPT().keySet().toString().contains(hidden)){
                    factors_contains_hidden.add(is_contain.getCPT());
                }
            }
            if(factors_contains_hidden.size()!=0) {
                System.out.println("the factors that contains the hidden variable " + hidden + " are: " + factors_contains_hidden);
                System.out.println("the size of the factors is: " + factors_contains_hidden.size());
                System.out.println("***********************************************************************************");
                Variable eliminated_var = new Variable("eliminated_var", null);
                eliminated_var.setCPT(join(factors_contains_hidden, hidden));
                factor_net.getNet().add(eliminated_var);
                System.out.println("the factor net after eliminate the " + hidden + " variable " + factor_net);
            }
        }
        System.out.println("***********************************************************************************");
        System.out.println("the factor net after joining all the hidden variables: " + factor_net);

        ArrayList<HashMap<String,Double>> query_and_last_factor = new ArrayList<>();
        for (Variable variable : factor_net.getNet()){
            query_and_last_factor.add(variable.getCPT());
        }
        System.out.println("***********************************************************************************");
        System.out.println("The query factor and the last factor are: " + query_and_last_factor);
        HashMap<String,Double> result_factor;
        result_factor = join_query(query_and_last_factor);
        System.out.println("***********************************************************************************");
        System.out.println("The result factor is: " + result_factor);
        String final_question = question.substring(0, question.indexOf("|")) + ")";
        return normalize(result_factor, final_question);
    }
}
