package part1;
import java.util.Map;

public class Util {
    public static int getYearWithHighestEmission(Sector sector){
        int yearOfHighestEmission = sector.getEmissions().entrySet().iterator().next().getKey();
        double highestTotalEmission = sector.getEmissions().entrySet().iterator().next().getValue();

        for(Map.Entry<Integer, Double> pairs: sector.getEmissions().entrySet()){
            double currentTotalEmissions = pairs.getValue();
            if(currentTotalEmissions > highestTotalEmission){
                yearOfHighestEmission = pairs.getKey();
                highestTotalEmission = currentTotalEmissions;
            }
        }
        return yearOfHighestEmission;
    }

    public static int getYearWithHighestEmission(Country country){
        int yearOfHighestEmission = country.getEmissions().entrySet().iterator().next().getKey();
        double totalHighestEmission = country.getEmissions().entrySet().iterator().next().getValue().getCH4()+country.getEmissions().entrySet().iterator().next().getValue().getN2O()+country.getEmissions().entrySet().iterator().next().getValue().getCO2();

        for(Map.Entry<Integer, Emission> pairs: country.getEmissions().entrySet()){
            double currentTotalEmissions = pairs.getValue().getN2O() + pairs.getValue().getCH4() + pairs.getValue().getCO2();
            if(currentTotalEmissions> totalHighestEmission){
                totalHighestEmission = currentTotalEmissions;
                yearOfHighestEmission = pairs.getKey();
            }
        }
        return yearOfHighestEmission;
    }
}
