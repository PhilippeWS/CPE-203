package part1;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Country {
    private String name;
    private Map<Integer, Emission> emissions = new LinkedHashMap<>();

    public Country(String name, Map<Integer, Emission> yearEmissionSummary){
        this.name = name;
        this.emissions = yearEmissionSummary;
    }

    public String getName() {
        return name;
    }

    public Map<Integer, Emission> getEmissions() {
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
