package Part2;

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

    public int getYearWithHighestEmissions(){
        int yearOfHighestEmission = emissions.entrySet().iterator().next().getKey();
        double totalHighestEmission = emissions.entrySet().iterator().next().getValue().getCH4()+emissions.entrySet().iterator().next().getValue().getN2O()+emissions.entrySet().iterator().next().getValue().getCO2();

        for(Map.Entry<Integer, Emission> pairs: emissions.entrySet()){
            double currentTotalEmissions = pairs.getValue().getN2O() + pairs.getValue().getCH4() + pairs.getValue().getCO2();
            if(currentTotalEmissions> totalHighestEmission){
                totalHighestEmission = currentTotalEmissions;
                yearOfHighestEmission = pairs.getKey();
            }
        }
        return yearOfHighestEmission;
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
