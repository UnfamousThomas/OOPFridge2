package ut.delta.oopkulmkapp2.api;

public class SalvestamiseErind extends RuntimeException{

    /**
     * Erind mis kutsutakse kui oli probleeme salvestamisega
     * @param failinimi Failinimi mida üritasime salvestada
     */
    public SalvestamiseErind(String failinimi) {
        super("Midagi läks külmkappi salvestamisel faili: " + failinimi + " valesti!");
    }
}
