package Part3;

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

    public static Country countryWithHighestCH4InYear(List<Country> countries, int year){
        double highestCH4InYear = 0.0;
        Country highestCH4Country = countries.get(0);

        for(Country country : countries){
            double currentCH4 = country.emissions.get(year).getCH4();
            if(currentCH4 > highestCH4InYear){
                highestCH4InYear = currentCH4;
                highestCH4Country = country;
            }
        }

        System.out.printf("Country with highest methane output: %s | %.1f\n", highestCH4Country.name,highestCH4InYear);
        return highestCH4Country;
    }

    public static Country countryWithHighestChangeInEmissions(List<Country> countries, int startYear, int endYear){
        Country countryWithHighestChangeInEmissions = countries.get(0);
        double greatestChangeInEmissions = 0;

        for(Country country : countries){
                Emission startYearEmissions = country.emissions.get(startYear);
                Emission endYearEmissions = country.emissions.get(endYear);

                double currentCountriesChangeInEmissions = (endYearEmissions.getCH4() + endYearEmissions.getCO2() + endYearEmissions.getN2O())
                        - (startYearEmissions.getCH4() + startYearEmissions.getCO2() +startYearEmissions.getN2O());

                if(currentCountriesChangeInEmissions>greatestChangeInEmissions){
                    greatestChangeInEmissions = currentCountriesChangeInEmissions;
                    countryWithHighestChangeInEmissions=country;
                }
            }

        System.out.printf("Country with highest emission increase: %s | %.1f kilo-ton increase\n",countryWithHighestChangeInEmissions.name, greatestChangeInEmissions);

        return countryWithHighestChangeInEmissions;
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
