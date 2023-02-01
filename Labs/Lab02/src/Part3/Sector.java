package Part3;

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

    public int getYearWithHighestEmissions(){
        int yearOfHighestEmission = emissions.entrySet().iterator().next().getKey();
        double highestTotalEmission = emissions.entrySet().iterator().next().getValue();

        for(Map.Entry<Integer, Double> pairs: emissions.entrySet()){
            double currentTotalEmissions = pairs.getValue();
            if(currentTotalEmissions > highestTotalEmission){
                yearOfHighestEmission = pairs.getKey();
                highestTotalEmission = currentTotalEmissions;
            }
        }
        return yearOfHighestEmission;
    }

    public static Sector sectorWithBiggestChangeInEmissions(List<Sector> sectors, int startYear, int endYear){
        Sector sectorWithBiggestChangInEmissions = sectors.get(0);
        double highestAverageEmissions = 0;
        int yearDifference = endYear-startYear + 1;

        for(Sector sector : sectors){
            double currentEmissionTotal=0;
                for(int year = startYear; year <= endYear; year++){
                    currentEmissionTotal+=sector.emissions.get(year);
                }

            currentEmissionTotal = currentEmissionTotal/yearDifference;

            if(currentEmissionTotal > highestAverageEmissions){
                highestAverageEmissions = currentEmissionTotal;
                sectorWithBiggestChangInEmissions = sector;
            }
        }

        System.out.printf("Sector with highest average emissions: %s | %.1f kilo-ton average\n", sectorWithBiggestChangInEmissions.name, highestAverageEmissions);
        return  sectorWithBiggestChangInEmissions;
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
