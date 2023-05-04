package ut.delta.oopkulmkapp2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ut.delta.oopkulmkapp2.api.Ese;
import ut.delta.oopkulmkapp2.api.Külmkapp;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class KulmkappApp extends Application {
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(KulmkappApp.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Külmkapp külmkapp;
        System.out.println("Tere tulemast külmkapi haldamise programmi kasutama!");
        System.out.println("Palun sisesta fail, kust lugeda külmkapp.");
        Scanner tekstiScanner = new Scanner(System.in);
        String failiNimi = tekstiScanner.nextLine();
        try {
            külmkapp = loeKülmkapp(failiNimi);
            interactiKülmkapigaRekursiivne(külmkapp, failiNimi, tekstiScanner);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
            System.exit(1);
        }

        launch();
    }

    /**
     * Suhtleb kasutajaga ja käitub vastavalt sisendile
     * @param külmkapp      Külmkapp, millega tegeleme
     * @param failiNimi     Failinimi, kust külmkapp leiti
     * @param tekstiScanner Avatud scanner, millega me loeme konsooli
     */
    private static void interactiKülmkapigaRekursiivne(Külmkapp külmkapp, String failiNimi, Scanner tekstiScanner) {
        System.out.println("Mida soovid teha?");
        System.out.println("1 - Näita külmkapi esemeid");
        System.out.println("2 - Lisa külmkappi ese");
        System.out.println("3 - Eemalda ese külmkapist");
        System.out.println("4 - Eemalda kõik halvaks läinud esemed");
        System.out.println("5 - Võta suvaline ese külmkapist");
        System.out.println("6 - Salvesta külmkapp ja lõpeta töö");
        int sisend = Integer.parseInt(tekstiScanner.nextLine());

        if (sisend == 1) {
            külmkapp.näitaKülmkappi();
        }

        if (sisend == 2) {
            System.out.println("Mis on uue eseme nimi?");
            String nimi = tekstiScanner.nextLine();
            System.out.println("Mitu ühikut soovid lisada?");
            int kogus = Integer.parseInt(tekstiScanner.nextLine());
            System.out.println("Kuupäev pahaks (MM/dd/yyyy)");
            try {
                Date date = sdf.parse(tekstiScanner.nextLine());
                Ese ese = new Ese(nimi, date, kogus);
                külmkapp.lisaKülmkappi(ese);
            } catch (ParseException e) {
                System.out.println("Midagi läks kuupäevaga valesti! Proovi uuesti.");
                interactiKülmkapigaRekursiivne(külmkapp, failiNimi, tekstiScanner);
            }

        }

        if (sisend == 3) {
            if (külmkapp.kasOnTühi()) {
                System.out.println("Ei saa eemaldada, külmkapp on tühi.");
                interactiKülmkapigaRekursiivne(külmkapp, failiNimi, tekstiScanner);
            }
            System.out.println("Mis on eseme nimi?");
            String nimi = tekstiScanner.nextLine();
            Ese ese = külmkapp.leiaEseNimetusega(nimi);
            külmkapp.kustutaEse(ese);
        }

        if (sisend == 4) {
            külmkapp.eemaldaKülmkapistHalvaksLäinud();
        }


        if (sisend == 5) {
            Ese ese = külmkapp.võtaSuvalineEse();
            if (ese != null) {
                prindiEse(ese);
            }
        }


        if (sisend == 6) {
            try {
                külmkapp.salvestaKülmkapp(failiNimi);

                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        interactiKülmkapigaRekursiivne(külmkapp, failiNimi, tekstiScanner);
    }

    /**
     * Prindib eseme objekti info
     *
     * @param ese Ese mida printida
     */
    private static void prindiEse(Ese ese) {
        System.out.println("Nimi: " + ese.getEsemeNimetus());
        System.out.println("Kogus: " + ese.getKogus());
        System.out.println("Läheb halvaks: " + sdf.format(ese.getLähebHalvaks()));
    }

    /**
     * Loeb külmkapi ja tagastab selle objekti
     *
     * @param failiNimi Fail kust külmkapp lugeda
     * @return Loetud külmkapp
     * @throws ParseException Probleem faili formaadiga
     */

    private static Külmkapp loeKülmkapp(String failiNimi) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        Külmkapp loodudKülmik;
        List<Ese> esemed = new ArrayList<>();
        int külmKapiSuurus = 0;
        Date külmkapiMuudetud = null;
        try {
            File file = new File(failiNimi);
            Scanner failiScanner = new Scanner(file);
            while (failiScanner.hasNextLine()) {
                String[] elemendid = failiScanner.nextLine().split(" ");
                if (elemendid[0].equals("K")) {
                    külmKapiSuurus = Integer.parseInt(elemendid[1]);
                    külmkapiMuudetud = sdf.parse(elemendid[2]);

                } else {
                    String esemeNimetus = elemendid[0];
                    int kogus = Integer.parseInt(elemendid[1]);
                    Date date = sdf.parse(elemendid[2]);
                    Ese ese = new Ese(esemeNimetus, date, kogus);
                    esemed.add(ese);
                }
            }

            loodudKülmik = new Külmkapp(külmKapiSuurus, esemed, külmkapiMuudetud);
        } catch (IOException e) {
            System.out.println("Loon uue külmkapi.");
            System.out.println("Sisesta uue külmkapi suurus");
            int suurus = Integer.parseInt(scanner.nextLine());
            loodudKülmik = new Külmkapp(suurus);
        }

        return loodudKülmik;
    }
}