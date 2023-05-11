package ut.delta.oopkulmkapp2.api;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Ese {

    private String esemeNimetus;
    private Date lähebHalvaks;
    private int kogus;
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


    /**
     * Loob uue eseme objekti.
     * @param esemeNimetus Eseme nimi
     * @param lähebHalvaks Millal ese halvaks läheb
     * @param kogus Mitu eset on (näiteks mitu viinerit on pakis)
     */
    public Ese(String esemeNimetus, Date lähebHalvaks, int kogus) {
        this.esemeNimetus = esemeNimetus;
        this.lähebHalvaks = lähebHalvaks;
        this.kogus = kogus;
    }

    /**
     *
     * @return Eseme nimetus
     */
    public String getEsemeNimetus() {
        return esemeNimetus;
    }

    /**
     * Paneb esemele uue nimetuse.
     * @param esemeNimetus Uus esemenimetus.
     */
    public void setEsemeNimetus(String esemeNimetus) {
        this.esemeNimetus = esemeNimetus;
    }

    /**
     * Tagastab, millal ese halvaks läheb
     *
     * @return Date millal halvaks läheb
     */
    public Date getLähebHalvaks() {
        return lähebHalvaks;
    }

    /**
     *
     * @return Eseme kogus
     */
    public int getKogus() {
        return kogus;
    }

    /**
     *
     * @param lähebHalvaks Uus halvaks minemise aeg
     */
    public void setLähebHalvaks(Date lähebHalvaks) {
        this.lähebHalvaks = lähebHalvaks;
    }

    /**
     * Seab uue koguse
     * @param kogus Uus kogus
     */

    public void setKogus(int kogus) {
        this.kogus = kogus;
    }

    /**
     * Tagastab kas ese on halvaks läinud.
     * @return Tõeväärtus kas on halvaks läinud
     */
    public boolean kasOnHalvaksLäinud() {
        if(lähebHalvaks.getTime() < System.currentTimeMillis()) {
            return true;
        };

        return false;
    }

    /**
     * Tagastab stringina nimetuse ja koguse.
     * @return string of nimetus ja kogus.
     */
    @Override
    public String toString() {
        return esemeNimetus + ", kogus: " + kogus + ", läheb halvaks: " + sdf.format(lähebHalvaks);
    }

    /**
     * Kontrollib, kas kaks eset on võrdsed
     * @param obj Ese, millega this eset võrrelda.
     * @return Kas ese on võrdne
     */
    @Override
    public boolean equals(Object obj) {
        Ese eseObj = (Ese) obj;
        return this.toString().equals(eseObj.toString());
    }
}