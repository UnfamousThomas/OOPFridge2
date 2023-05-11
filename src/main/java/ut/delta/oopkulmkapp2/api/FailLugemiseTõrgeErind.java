package ut.delta.oopkulmkapp2.api;

public class FailLugemiseTõrgeErind extends RuntimeException {

    /**
     * Erind mis visatakse kui tekkis probleem faili lugemisel
     * @param failinimi Failinimi mida üritasime lugeda
     */
    public FailLugemiseTõrgeErind(String failinimi) {
        super("Faili: " + failinimi + " lugemisel esines tõrge.");
    }
}
