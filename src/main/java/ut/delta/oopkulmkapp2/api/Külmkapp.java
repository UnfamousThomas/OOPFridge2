package ut.delta.oopkulmkapp2.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Külmkapp {
    /**
     * List asjadest mis on külmikus
     */
    private List<Ese> asjadKülmikus;
    /**
     * Millal viimati külmkapi sisu muudeti
     */
    private Date viimatiMuudetud;
    /**
     * Kui suur on külmkapp (max)
     */
    private final int külmkapiSuurus;
    private int hetkelAsjuKülmikus;
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Konstrueerib külmmkapi.
     * @param külmkapiSuurus Kui suur on külmkapp
     */
    public Külmkapp(int külmkapiSuurus) {
        this.viimatiMuudetud = Date.from(Instant.now());
        this.asjadKülmikus = new ArrayList<>();
        this.külmkapiSuurus = külmkapiSuurus;
        this.hetkelAsjuKülmikus = 0;
    }

    /**
     * Loob külmkapi vastavalt parameetritele
     * @param külmkapiSuurus
     * @param esemed külmkapis olevad esemed
     * @param viimatiMuudetud
     */
    public Külmkapp(int külmkapiSuurus, List<Ese> esemed, Date viimatiMuudetud) {
        this.külmkapiSuurus = külmkapiSuurus;
        this.viimatiMuudetud = viimatiMuudetud;
        int koguEsemeid = 0;
        for (Ese ese : esemed) {
            koguEsemeid = koguEsemeid + ese.getKogus();
        }
        if(koguEsemeid > külmkapiSuurus) {
            throw new RuntimeException("Esemeid on rohkem kui külmkapi suurus");
        }

        this.asjadKülmikus = esemed;

        this.hetkelAsjuKülmikus = koguEsemeid;

    }

    public int getKülmkapiSuurus() {
        return külmkapiSuurus;
    }

    /**
     * Tühjendab külmkapi.
     */
    public void tühjendaKülmkapp() {
        asjadKülmikus.clear();
        this.hetkelAsjuKülmikus = 0;
        setViimatiMuudetudNow();
    }

    /**
     * Lisab eseme külmkappi
     * @param ese Ese mis lisada
     */
    public void lisaKülmkappi(Ese ese) {
        if(külmkapiSuurus < asjadKülmikus.size() + ese.getKogus()) {
            System.out.println("Ei mahtunud külmikusse. Söö ära.");
            return;
        }

        this.hetkelAsjuKülmikus = this.hetkelAsjuKülmikus + ese.getKogus();
        asjadKülmikus.add(ese);
        setViimatiMuudetudNow();
    }

    /**
     * Eemaldab eseme külmkapist kasutades indeksit
     * @param indeks Listi indeks
     */
    private void eemaldaKülmkapist(int indeks) {
        Ese ese = asjadKülmikus.get(indeks);
        eemaldaKülmkapist(ese);

    }

    /**
     * Paneb viimatimuudetud aja praeguse peale.
     */
    private void setViimatiMuudetudNow() {
        this.viimatiMuudetud = Date.from(Instant.now());
    }

    /**
     * Eemaldab eseme külmkapist
     * @param ese Ese mis eemaldada
     */
    private void eemaldaKülmkapist(Ese ese) {
        asjadKülmikus.remove(ese);
        this.hetkelAsjuKülmikus = this.hetkelAsjuKülmikus - ese.getKogus();
        setViimatiMuudetudNow();
    }

    public Ese leiaEseNimetusega(String nimetus) {
        for (Ese ese : asjadKülmikus) {
            if(ese.getEsemeNimetus().equals(nimetus)) return ese;
        }

        return null;
    }

    /**
     * Uuendab eseme kogust ja külmkapis olevate esemete hulka.
     * @param esemeNimetus Mille kogust muuta
     * @param uusKogus Eseme uus kogus
     */
    public void uuendaEsemeKogust(String esemeNimetus, int uusKogus) {
        for (Ese eseKülmikus : asjadKülmikus) {
            if(esemeNimetus.equals(eseKülmikus.getEsemeNimetus())) {
                int ilmaKoguseta = this.hetkelAsjuKülmikus - eseKülmikus.getKogus();
                int uueKogusega = ilmaKoguseta + uusKogus;
                if(külmkapiSuurus < uueKogusega) {
                    System.out.println("Uus kogus ei mahtunud külmikusse. Söö ära. (Ei muutnud andmeid)");
                    return;
                }
                this.hetkelAsjuKülmikus = this.hetkelAsjuKülmikus - eseKülmikus.getKogus() + uusKogus;
                eseKülmikus.setKogus(uusKogus);
                if(uusKogus == 0) kustutaEse(eseKülmikus);
                setViimatiMuudetudNow();
                return;
            }
        }

        System.out.println("Sellist eset ei ole külmkapis");
    }

    /**
     * Kustutab eseme külmkapi listist
     * @param ese Mis kustutada
     */
    public void kustutaEse(Ese ese) {
        asjadKülmikus.remove(ese);
        setViimatiMuudetudNow();
    }

    /**
     * Tagastab suvalise eseme ning eemaldab selle.
     * @return Suvaline ese esemtest külmkapist.
     */
    public Ese võtaSuvalineEse() {
        if(asjadKülmikus.isEmpty()) {
            System.out.println("Külmkapp on tühi!");
            return null;
        }
        Random random = new Random();

        int suurus = random.nextInt(0, asjadKülmikus.size());
        Ese ese = asjadKülmikus.get(suurus);
        int esemeHulk = ese.getKogus();
        this.hetkelAsjuKülmikus = this.hetkelAsjuKülmikus - esemeHulk;
        setViimatiMuudetudNow();
        kustutaEse(ese);
        return ese;
    }

    /**
     * Salvestab külmkapi faili
     * @param failiNimi
     * @throws IOException
     */
    public void salvestaKülmkapp(String failiNimi) throws IOException {
        System.out.println("asju: " + asjadKülmikus.size());
        PrintWriter pw = new PrintWriter(failiNimi);
        pw.println("K " + külmkapiSuurus + " " + sdf.format(viimatiMuudetud));
        for (Ese ese : asjadKülmikus) {
            pw.println(ese.getEsemeNimetus() + " " + ese.getKogus() + " " + sdf.format(ese.getLähebHalvaks()));
        }

        pw.close();
    }

    /**
     * Näitab külmkapi sisu ja kui midagi on halvaks läinud, siis ka seda
     */
    public String näitaKülmkappi() {
        System.out.println("Näitan külmkappi (suurusega " + külmkapiSuurus + ")");
        ArrayList<String> tooted = new ArrayList<>();
        for (Ese ese : asjadKülmikus) {
            tooted.add(ese.toString());
            if(!ese.kasOnHalvaksLäinud()) {
                System.out.println(ese.getEsemeNimetus() + " - " + ese.getKogus() + " - " + sdf.format(ese.getLähebHalvaks()));
            } else {
                System.out.println(ese.getEsemeNimetus() + " - " + ese.getKogus() + " - Läks halvaks: " + sdf.format(ese.getLähebHalvaks()));

            }
        }
        return tooted.toString();
    }


    public boolean kasOnTühi() {
        return hetkelAsjuKülmikus == 0;
    }

    /**
     * Eemaldab halvaks läinud esemed
     */
    public void eemaldaKülmkapistHalvaksLäinud() {
        for (Ese ese : asjadKülmikus) {
            if(ese.kasOnHalvaksLäinud()) {
                System.out.println("Eemaldasin: " + ese.getEsemeNimetus() + " - " + ese.getKogus() + " - " + sdf.format(ese.getLähebHalvaks()));
                eemaldaKülmkapist(ese);
            }
        }
    }
}