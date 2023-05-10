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

    private String failiNimi;
    private TextField valik;
    private String valikTekst;
    private Külmkapp külmkapp;
    private int suurus;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Label pealkiri = new Label("Tere tulemast külmkapi haldamise programmi kasutama!");
        Label failiKüsimine = new Label("Palun sisesta fail, kust lugeda külmkapp.");
        TextField sisend = new TextField();
        Button alusta = new Button("Alusta");
        alusta.setOnAction(event -> {
            System.out.println("Alustan tööd");
            try {
                külmkapp = loeKülmkapp(sisend.getText());
            } catch (Exception e) {
                System.out.println("Ei leidnud, loon uue");
                primaryStage.setScene(küsiSuurust());
            }
            failiNimi = sisend.getText();
            primaryStage.setScene(uusStseen(külmkapp, primaryStage));
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

    private Scene küsiSuurust() {
        Label pealkiri = new Label("Loon uue külmkapi, palun sisesta suurus:");
        TextField sisend = new TextField();
        Button sisesta = new Button("Esita");
        sisesta.setOnAction(actionEvent -> suurus = Integer.parseInt(sisend.getText()));
        VBox root = new VBox();
        root.getChildren().addAll(pealkiri, sisend);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        return new Scene(root, 400, 300);
    }

    private Scene uusStseen(Külmkapp külmkapp, Stage stage) {
        Label pealkiri = new Label("Mida soovid teha?");
        Label esimene = new Label("1 - Näita külmkapi esemeid");
        Label teine = new Label("2 - Eemalda ese külmkapist");
        Label kolmas = new Label("3 - Eemalda kõik halvaks läinud esemed");
        Label neljas = new Label("4 - Võta suvaline ese külmkapist");
        Label viies = new Label("5 - Salvesta külmkapp ja lõpeta töö");
        valik = new TextField();
        Button sisesta = new Button("Esita");
        sisesta.setOnAction(actionEvent -> {
            valikTekst = valik.getText();
            if (Integer.parseInt(valikTekst) == 1)
                stage.setScene(valik1(stage));
            if (Integer.parseInt(valikTekst) == 2)
                stage.setScene(valik2(stage));
            if (Integer.parseInt(valikTekst) == 3)
                stage.setScene(valik3(stage));
            if (Integer.parseInt(valikTekst) == 4)
                stage.setScene(valik4(stage));
            if (Integer.parseInt(valikTekst) == 5) {
                try {
                    külmkapp.salvestaKülmkapp(failiNimi);

                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        VBox root = new VBox();
        root.getChildren().addAll(pealkiri, esimene, teine, kolmas, neljas, viies, valik, sisesta);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        return new Scene(root, 400, 300);
    }

    private Scene valik1(Stage stage) {
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

    private Scene valik2(Stage stage) {
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
        Button sisesta = new Button("Esita");
        sisesta.setOnAction(actionEvent -> {
            Ese ese = külmkapp.leiaEseNimetusega(tf.getText());
            külmkapp.kustutaEse(ese);
        });
        VBox root = new VBox();
        Button edasi = new Button("Edasi");
        edasi.setOnAction(actionEvent -> stage.setScene(uusStseen(külmkapp, stage)));
        root.getChildren().addAll(sisesta, misNimi, tf, edasi);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        return new Scene(root, 400, 300);
    }

    private Scene valik3(Stage stage) {
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

    private Scene valik4(Stage stage) {
        Label silt = new Label();
        Ese ese = külmkapp.võtaSuvalineEse();
        if (ese != null)
            silt = new Label(prindiEse(ese));
        VBox root = new VBox();
        Button edasi = new Button("Edasi");
        edasi.setOnAction(actionEvent -> stage.setScene(uusStseen(külmkapp, stage)));
        root.getChildren().addAll(silt, edasi);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        return new Scene(root, 400, 300);
    }


    public static void main(String[] args) {
        launch();
    }


    /**
     * Prindib eseme objekti info
     *
     * @param ese Ese mida printida
     */
    private static String prindiEse(Ese ese) {
        System.out.println("Nimi: " + ese.getEsemeNimetus());
        System.out.println("Kogus: " + ese.getKogus());
        System.out.println("Läheb halvaks: " + sdf.format(ese.getLähebHalvaks()));
        return "Nimi: " + ese.getEsemeNimetus() + ", kogus: " + ese.getKogus() + ", läheb halvaks: " + sdf.format(ese.getLähebHalvaks());
    }

    /**
     * Loeb külmkapi ja tagastab selle objekti
     *
     * @param failiNimi Fail kust külmkapp lugeda
     * @return Loetud külmkapp
     * @throws ParseException Probleem faili formaadiga
     */

    private static Külmkapp loeKülmkapp(String failiNimi) throws Exception {
        Külmkapp loodudKülmik;
        List<Ese> esemed = new ArrayList<>();
        int külmKapiSuurus = 0;
        Date külmkapiMuudetud = null;
        try (Scanner failiScanner = new Scanner(new File(failiNimi))) {
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
        }
        return loodudKülmik;
    }
}