package ut.delta.oopkulmkapp2.api;

public class FailLugemiseTõrgeErind extends RuntimeException {
    public FailLugemiseTõrgeErind(String failinimi) {
        super("Faili: " + failinimi + " lugemisel esines tõrge.");
    }
}
