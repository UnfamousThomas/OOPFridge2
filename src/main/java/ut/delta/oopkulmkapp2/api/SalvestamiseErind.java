package ut.delta.oopkulmkapp2.api;

public class SalvestamiseErind extends RuntimeException{
    public SalvestamiseErind(String failinimi) {
        super("Midagi läks külmkappi salvestamisel faili: " + failinimi + " valesti!");
    }
}
