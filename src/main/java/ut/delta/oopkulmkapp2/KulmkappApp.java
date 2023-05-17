package ut.delta.oopkulmkapp2;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ut.delta.oopkulmkapp2.api.Ese;
import ut.delta.oopkulmkapp2.api.FailLugemiseTõrgeErind;
import ut.delta.oopkulmkapp2.api.Külmkapp;
import ut.delta.oopkulmkapp2.api.SalvestamiseErind;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class KulmkappApp extends Application {
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private String failiNimi;
    private TextField valik;
    private String valikTekst;
    private Külmkapp külmkapp;
    private int suurus;

    /**
     * Käivitab külmkapp GUI
     * @param primaryStage Mis stageiga tegeleme
     */
    @Override
    public void start(Stage primaryStage)  {
        Label pealkiri = new Label("Tere tulemast külmkapi haldamise programmi kasutama!");
        Label failiKüsimine = new Label("Palun sisesta fail, kust lugeda külmkapp.");
        TextField sisend = new TextField();
        Button alusta = new Button("Alusta");
        alusta.setOnAction(event -> {
            failiNimi = sisend.getText();
            try {
                külmkapp = loeKülmkapp(sisend.getText());
                primaryStage.setScene(uusStseen(külmkapp, primaryStage));
            } catch (FailLugemiseTõrgeErind e) {
                primaryStage.setScene(küsiSuurust(primaryStage));
            }
        });
        VBox root = new VBox();
        root.getChildren().addAll(pealkiri, failiKüsimine, sisend, alusta);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Külmkapp");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Meetod et küsida külmkapi suurust
     * @return Stseen kus küsitakse suurust
     */
    private Scene küsiSuurust(Stage stage) {
        Label pealkiri = new Label("Loon uue külmkapi, palun sisesta suurus:");
        TextField sisend = new TextField();
        Button sisesta = new Button("Esita");
        sisesta.setOnAction(actionEvent -> {
            suurus = Integer.parseInt(sisend.getText());
            külmkapp = new Külmkapp(suurus);
            stage.setScene(uusStseen(külmkapp, stage));
        });
        VBox root = new VBox();
        root.getChildren().addAll(pealkiri, sisend, sisesta);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        return new Scene(root, 400, 300);
    }

    /**
     * Uus külmkappi menüü tseen
     * @param külmkapp Külmkappi objekt millega tegeleme
     * @param stage Stage kus toimub tegevus
     * @return uus stseen
     */
    private Scene uusStseen(Külmkapp külmkapp, Stage stage) {
        Label pealkiri = new Label("Mida soovid teha?");
        Label esimene = new Label("1 - Näita külmkapi esemeid");
        Label teine = new Label("2 - Lisa külmkappi ese");
        Label kolmas = new Label("3 - Eemalda ese külmkapist");
        Label neljas = new Label("4 - Eemalda kõik halvaks läinud esemed");
        Label viies = new Label("5 - Võta suvaline ese külmkapist");
        Label kuues = new Label("6 - Salvesta külmkapp ja lõpeta töö");
        valik = new TextField();
        Button sisesta = new Button("Esita");
        sisesta.setOnAction(actionEvent -> {
            valikTekst = valik.getText();
            if (Integer.parseInt(valikTekst) == 1)
                stage.setScene(looSisuStseen(stage));
            if (Integer.parseInt(valikTekst) == 2)
                stage.setScene(lisaEse(stage));
            if (Integer.parseInt(valikTekst) == 3)
                stage.setScene(looEemaldaEseStseen(stage));
            if (Integer.parseInt(valikTekst) == 4)
                stage.setScene(looHalvaksLäinudEemalduseStseen(stage));
            if (Integer.parseInt(valikTekst) == 5)
                stage.setScene(looSuvaliseEsemeStseen(stage));
            if (Integer.parseInt(valikTekst) == 6) {
                try {
                    külmkapp.salvestaKülmkapp(failiNimi);
                    System.exit(0);
                } catch (IOException e) {
                    throw new SalvestamiseErind(failiNimi);
                }
            }
        });
        VBox root = new VBox();
        root.getChildren().addAll(pealkiri, esimene, teine, kolmas, neljas, viies, kuues, valik, sisesta);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        return new Scene(root, 400, 300);
    }

    /**
     * Küsib eseme nimetust, kogust ja kuupäeva, millal läheb halvaks
     * @param stage Stage, kus toimub tegevus
     * @return Uus stseen   
     */
    private Scene lisaEse(Stage stage) {
        Label uusNimi = new Label("Mis on uue eseme nimi?");
        TextField nimi = new TextField();
        Label uusKogus = new Label("Mitu ühikut soovid lisada?");
        TextField kogus = new TextField();
        Label uusPahaks = new Label("Sisesta kuupäev, millal läheb halvaks (dd/MM/yyyy:");
        TextField pahaks = new TextField();
        Button sisend = new Button("Lisa");
        sisend.setOnAction(actionEvent -> {
            try {
                Date date = sdf.parse(pahaks.getText());
                String nimeSisend = nimi.getText();
                int koguseSisend = Integer.parseInt(kogus.getText());
                Ese ese = new Ese(nimeSisend, date, koguseSisend);
                külmkapp.lisaKülmkappi(ese);
                System.out.println("Lisasin külmkappi");
                stage.setScene(uusStseen(külmkapp, stage));
            } catch (ParseException e) {
                System.out.println("Midagi läks kuupäevaga valesti! Proovi uuesti.");
                stage.setScene(lisaEse(stage));
            }
        });
        VBox root = new VBox();
        root.getChildren().addAll(uusNimi, nimi, uusKogus, kogus, uusPahaks, pahaks, sisend);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        return new Scene(root, 400, 300);
    }

    /**
     * Meetod mis aktiveerib kui valitakse näidata sisu
     * @param stage stage kus asi toimub
     * @return Scene sisuga
     */
    private Scene looSisuStseen(Stage stage) {
        Label sisu = new Label("Näitan külmkapi sisu:");
        Label tooted = new Label(külmkapp.näitaKülmkappi());
        VBox root = new VBox();
        Button edasi = new Button("Edasi");
        edasi.setOnAction(actionEvent -> stage.setScene(uusStseen(külmkapp, stage)));
        root.getChildren().addAll(sisu, tooted, edasi);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        return new Scene(root, 400, 300);
    }

    /**
     * Metood mis aktiveerib kui soovitakse eemaldada üks ese külmkapist
     * @param stage Stage kus tegevus toimub
     * @return Scene eemaldamisega
     */
    private Scene looEemaldaEseStseen(Stage stage) {
        if (külmkapp.kasOnTühi()) {
            Label eiSaa = new Label("Ei saa eemaldada, külmkapp on tühi.");
            VBox root = new VBox();
            Button edasi = new Button("Edasi");
            edasi.setOnAction(actionEvent -> stage.setScene(uusStseen(külmkapp, stage)));
            root.getChildren().addAll(eiSaa, edasi);
            root.setAlignment(Pos.CENTER);
            root.setSpacing(10);
            return new Scene(root, 400, 300);
        }
        Label misNimi = new Label("Mis on eseme nimi?");
        TextField tf = new TextField();
        Button sisesta = new Button("Eemalda");
        sisesta.setOnAction(actionEvent -> {
            Ese ese = külmkapp.leiaEseNimetusega(tf.getText());
            külmkapp.kustutaEse(ese);
            stage.setScene(uusStseen(külmkapp, stage));
        });
        VBox root = new VBox();
        root.getChildren().addAll(misNimi, tf, sisesta);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        return new Scene(root, 400, 300);
    }

    /**
     * Meetod mis aktiveerub kui soovitakse eemaldada halvaks läinud asjad
     * @param stage Kus tegevus toimub
     * @return Scene peale eemaldamist
     */
    private Scene looHalvaksLäinudEemalduseStseen(Stage stage) {
        külmkapp.eemaldaKülmkapistHalvaksLäinud();
        Label eemaldatud = new Label("Halvaks läinud tooted eemaldatud!");
        VBox root = new VBox();
        Button edasi = new Button("Edasi");
        edasi.setOnAction(actionEvent -> stage.setScene(uusStseen(külmkapp, stage)));
        root.getChildren().addAll(eemaldatud, edasi);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        return new Scene(root, 400, 300);
    }

    /**
     * Aktiveerub kui soovitakse võtta suvaline ese
     * @param stage Stage millele loome uue stseeni
     * @return Stseen peale eseme võtmist
     */
    private Scene looSuvaliseEsemeStseen(Stage stage) {
        Label silt = new Label();
        Ese ese = külmkapp.võtaSuvalineEse();
        if (ese != null) {
            silt = new Label(ese.toString());
        } else {
            silt = new Label("Külmkapp on tühi!");
        }
        VBox root = new VBox();
        Button edasi = new Button("Edasi");
        edasi.setOnAction(actionEvent -> stage.setScene(uusStseen(külmkapp, stage)));
        root.getChildren().addAll(silt, edasi);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        return new Scene(root, 400, 300);
    }


    /**
     * Main meetod mida kasutatakse et launch kutsuda välja
     * @param args Command line argumendid
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Loeb külmkapi ja tagastab selle objekti
     *
     * @param failiNimi Fail kust külmkapp lugeda
     * @return Loetud külmkapp
     * @throws ParseException Probleem faili formaadiga
     */

    private static Külmkapp loeKülmkapp(String failiNimi) throws FailLugemiseTõrgeErind {
        Külmkapp loodudKülmik;
        List<Ese> esemed = new ArrayList<>();
        int külmKapiSuurus = 0;
        Date külmkapiMuudetud = null;
        File fail = new File(failiNimi);
        try (Scanner failiScanner = new Scanner(fail, StandardCharsets.UTF_8)) {
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
        } catch (IOException | ParseException e) {
            throw new FailLugemiseTõrgeErind(failiNimi);
        }
        return loodudKülmik;
    }
}