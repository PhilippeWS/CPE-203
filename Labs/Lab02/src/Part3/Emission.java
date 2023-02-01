package Part3;

public class Emission {
    private double CO2;
    private double N2O;
    private double CH4;

    public Emission(double CO2, double N2O, double CH4){
        this.CO2 = CO2;
        this.N2O = N2O;
        this.CH4 = CH4;
    }

    public double getCO2() {
        return CO2;
    }

    public double getN2O() {
        return N2O;
    }

    public double getCH4() {
        return CH4;
    }

    /*
    public void setCH4(double CH4) {
        this.CH4 = CH4;
    }

    public void setCO2(double CO2) {
        this.CO2 = CO2;
    }

    public void setN2O(double n2O) {
        N2O = n2O;
    }

    public String toString(){
        return String.format("%f.1,\n %f.1,\n %f.1", CO2, N2O, CH4);
    }

     */
}
