package part1;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Sector {
    private String name;
    private Map<Integer, Double> emissions = new LinkedHashMap<>();

    public Sector(String name, Map<Integer, Double> emissions){
        this.name = name;
        this.emissions = emissions;
    }

    public String getName() {
        return name;
    }

    //DOes the return here imply total emissions? So return the sum of all emissions?
    public Map<Integer, Double> getEmissions() {
        return emissions;
    }

    /*
    public void setName(String name) {
        this.name = name;
    }

    public void setEmissions(Map<Integer, Emission> yearEmissionSummary) {
        this.emissions = yearEmissionSummary;
    }

    public void setNewEmission(int year, Emission emission){
        emissions.put(year, emission);
    }
     */


}
