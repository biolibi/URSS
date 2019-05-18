import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;



/*
*              Projet par Olivier Girard et Vincent tremblay.
*
*
*              Les divers problèmes de notre programme sont les suivant:
*
*                   Le souffle du tireur peut ètre retenue à l'infini;
*                   La retenue de souffle empêche la visée (mais ça reste acceptable car lors d'un tir réel on est contraints dans nos mouvements lors de notre retenue de soufle)
*
*/
public class Main extends Application {

    private Timeline traj = new Timeline();

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage1) {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();              //Taille écran
        Controller controle = new Controller();

        // Variables

        double RatioDePoint[] = new double[2];
        RatioDePoint[0] = 0;
        RatioDePoint[1] = 0;
        double tir[] = new double[1];

        int[] points = new int[1];
        points[0] = 0;

        int[] compteurDePoint = new int[4];

        final Boolean[] respiration = new Boolean[1];
        respiration[0] = true;

        final int[] ajustement = new int[2];    // 0 = X   1 = Y
        ajustement[1] = 0;
        ajustement[0] = 0;

        Random randomVent = new Random();

        //Texture
        final PhongMaterial ciel = new PhongMaterial();
        final PhongMaterial couleurTable = new PhongMaterial();
        Image fondurl = new Image(getClass().getResourceAsStream("Package_texture/brick_2.png"));
        Image aide = new Image(getClass().getResourceAsStream("Package_texture/controle.png"));
        ImagePattern imagePattern = new ImagePattern(fondurl);
        Image cibletexture = new Image(getClass().getResourceAsStream("Package_texture/cible.jpg"));
        Image fondniveau1Image = new Image(getClass().getResourceAsStream("Package_texture/niveau1.PNG"));
        ImageView fondNiveau1 = new ImageView(fondniveau1Image);
        couleurTable.setDiffuseMap(new Image(getClass().getResourceAsStream("Package_texture/wood_light.png")));
        ciel.setDiffuseColor(Color.LIGHTBLUE);
        ciel.setSpecularColor(Color.GREEN);
        ciel.setDiffuseMap(new Image(getClass().getResourceAsStream("Package_texture/brick_2.png")));

        //Objet javafx
        Rectangle carreMenuFond = new Rectangle(screenSize.getWidth(), screenSize.getHeight());
        Rectangle carreCheatFond = new Rectangle(screenSize.getWidth(), screenSize.getHeight());


        Box piedsChaiseDroit = new Box(screenSize.getWidth() / 48, screenSize.getHeight() / 4.32, 1000);
        piedsChaiseDroit.setTranslateY(screenSize.getHeight() / 1.15);
        piedsChaiseDroit.setTranslateX(screenSize.getWidth() - 30);
        piedsChaiseDroit.setMaterial(couleurTable);

        Box table = new Box(screenSize.getWidth(), 40, 1000);
        table.setMaterial(couleurTable);
        table.setTranslateX(screenSize.getWidth() / 2);
        table.setTranslateY(screenSize.getHeight() / 1.3);

        Box piedsChaiseGauche = new Box(screenSize.getWidth() / 48, screenSize.getHeight() / 4.32, 1000);
        piedsChaiseGauche.setTranslateY(screenSize.getHeight() / 1.15);
        piedsChaiseGauche.setTranslateX(20);
        piedsChaiseGauche.setMaterial(couleurTable);

        Box toit = new Box(screenSize.getWidth(), 10, 5000);
        toit.setTranslateX(screenSize.getWidth() / 2);

        Box murGauche = new Box(screenSize.getWidth() / 96, screenSize.getHeight(), 5000);
        murGauche.setTranslateX(screenSize.getWidth() / 96);
        murGauche.setTranslateY(screenSize.getHeight() / 2);
        murGauche.setMaterial(ciel);

        Box murDroit = new Box(screenSize.getWidth() / 96, screenSize.getHeight(), 5000);
        murDroit.setMaterial(ciel);
        murDroit.setTranslateX(screenSize.getWidth());
        murDroit.setTranslateY(screenSize.getHeight() / 2);

        //Création avec javaFX
        Ellipse balle[] = new Ellipse[1];
        balle[0] = new Ellipse(5, 5);
        balle[0].setTranslateY(0);              //   pour faire disparaitre la balle si elle n'est pas en utilisation
        balle[0].setTranslateX(0);              //   pour faire disparaitre la balle si elle n'est pas en utilisation
        balle[0].setFill(Color.RED);

        TextField password = new TextField();
        password.setTranslateY(130);
        password.setTranslateX(screenSize.getWidth() / 2.1);
        password.setScaleX(screenSize.getWidth() / 400);
        password.setScaleY(screenSize.getWidth() / 400);

        Ellipse cible = new Ellipse(screenSize.getWidth() / 2, screenSize.getHeight() / 2, screenSize.getWidth() / 16, screenSize.getWidth() / 16);
        Ellipse cible2 = new Ellipse(screenSize.getWidth() / 2, screenSize.getHeight() / 2, screenSize.getWidth() / 16, screenSize.getWidth() / 16);

        Circle mire = new Circle(screenSize.getWidth() / 48, screenSize.getWidth() / 48, screenSize.getWidth() / 16);
        mire.setStroke(Color.BLACK);
        mire.setStrokeWidth(2);
        mire.setFill(Color.TRANSPARENT);
        mire.setCenterX(screenSize.getWidth() / 2);
        mire.setCenterY(screenSize.getHeight() / 2);

        Rectangle info = new Rectangle(screenSize.getWidth(), screenSize.getWidth());
        info.setOpacity(0.5);

        Circle viseur = new Circle(screenSize.getWidth() / 2, screenSize.getHeight() / 2, screenSize.getWidth() / 24);
        viseur.setStroke(Color.BLACK);
        viseur.setStrokeWidth(2);
        viseur.setFill(Color.TRANSPARENT);

        Line viseurLineVertical = new Line((screenSize.getWidth() / 2), (screenSize.getHeight() / 2) - (screenSize.getWidth() / 24), (screenSize.getWidth() / 2), (screenSize.getHeight() / 2) + (screenSize.getWidth() / 24));
        viseurLineVertical.setTranslateX(screenSize.getWidth() / 2);
        viseurLineVertical.setTranslateZ(500);
        viseurLineVertical.setStrokeWidth(2);
        viseurLineVertical.setFill(Color.RED);

        Line viseurLineHorizontal = new Line((screenSize.getWidth() / 2) - (screenSize.getWidth() / 24), (screenSize.getHeight() / 2), (screenSize.getWidth() / 2) + (screenSize.getWidth() / 24), (screenSize.getHeight() / 2));
        viseurLineHorizontal.setTranslateX(screenSize.getHeight() / 2);
        viseur.setTranslateZ(500);
        viseurLineHorizontal.setTranslateZ(500);
        viseurLineHorizontal.setStrokeWidth(2);
        viseurLineHorizontal.setFill(Color.RED);

        cible.setFill(new ImagePattern(cibletexture, 0, 0, 1, 1, true));
        cible.setTranslateZ(5000);
        toit.setMaterial(ciel);
        carreMenuFond.setFill(imagePattern);
        carreCheatFond.setFill(imagePattern);

        Camera camera1 = new PerspectiveCamera();
        camera1.setTranslateX(0);
        camera1.setTranslateY(0);

        Camera camera2 = new PerspectiveCamera();
        camera2.setTranslateZ(400);

        balle[0] = controle.Visée((camera1.getTranslateX() + (screenSize.getWidth() / 2)), camera1.getTranslateY() + (screenSize.getHeight() / 2) , 1);
        balle[0].setFill(Color.rgb(181, 166, 66));
        balle[0].setTranslateZ(-3000);

        //niveau 2   (pas utilisé, utilisation futur)
        Rectangle niveau2FondSable = new Rectangle(screenSize.getWidth(), screenSize.getHeight());
        Box cactus = new Box(screenSize.getWidth() / 48, screenSize.getHeight() / 4.32, 1000);
        Box cactus2 = new Box(screenSize.getWidth() / 48, screenSize.getHeight() / 4.32, 1000);
        Box desertMap2Toit = new Box(screenSize.getWidth(), 10, 5000);
        TriangleMesh pyramidDesert = new TriangleMesh();
        pyramidDesert.getTexCoords().addAll(
                0, 0, 0, 0, 200, -100 / 2, -100 / 2, 200, 0, 100 / 2, 200, 0, 0, 200, 100 / 2
        );
        MeshView pyramidDesertVisuel = new MeshView(pyramidDesert);
        pyramidDesertVisuel.setDrawMode(DrawMode.FILL);
        pyramidDesertVisuel.setMaterial(ciel);
        pyramidDesertVisuel.setTranslateX(200);
        pyramidDesertVisuel.setTranslateY(100);
        pyramidDesertVisuel.setTranslateZ(200);

        //label et slider
        Slider quantitéPoudreNoir = new Slider(0.5, 50, 1);
        quantitéPoudreNoir.setShowTickLabels(true);
        quantitéPoudreNoir.setMajorTickUnit(0.2);
        quantitéPoudreNoir.setBlockIncrement(0.1);
        quantitéPoudreNoir.setStyle("-fx-background-color: linear-gradient(to right, #2D819D 0%, #969696 0%);");

        Slider ventTir = new Slider(-75, 75, 0);
        ventTir.setShowTickLabels(true);
        ventTir.setMajorTickUnit(5);
        ventTir.setBlockIncrement(5);
        ventTir.setStyle("-fx-background-color: linear-gradient(to right, #2D819D 0%, #969696 0%);");

        Slider masseBalle = new Slider(0.5, 50, 1);
        masseBalle.setShowTickLabels(true);
        masseBalle.setMajorTickUnit(5);
        masseBalle.setBlockIncrement(1);
        masseBalle.setStyle("-fx-background-color: linear-gradient(to right, #2D819D 0%, #969696 0%);");

        Slider gravitéTir = new Slider(0, 98, 9.8);
        gravitéTir.setShowTickLabels(true);
        gravitéTir.setMajorTickUnit(5);
        gravitéTir.setBlockIncrement(4.9);
        gravitéTir.setStyle("-fx-background-color: linear-gradient(to right, #2D819D 0%, #969696 0%);");

        Label textePoudreNoir = new Label("Masse de poudre noir : " + 1 + "g");
        textePoudreNoir.setTextFill(Color.RED);
        Label texteMasseBalle = new Label("Masse de la balle : "+ 1 + "g");
        texteMasseBalle.setTextFill(Color.RED);
        Label texteGravité = new Label("Gravité: " + 9.8 + ("m/s^2"));
        texteGravité.setTextFill(Color.RED);
        Label texteVent = new Label("Vent: " + 0 + ("Km/h"));
        texteVent.setTextFill(Color.RED);

        Label urss = new Label("Ultimate Rifle Shooting Simulation");
        urss.setTranslateZ(0);
        urss.setTextFill(Color.RED);
        urss.setTranslateY(130);
        urss.setTranslateX(screenSize.getWidth() / 2.1);
        urss.setScaleX(screenSize.getWidth() / 400);
        urss.setScaleY(screenSize.getWidth() / 400);

        Label urss2 = new Label("Ultimate Rifle Shooting Simulation");
        urss2.setTranslateZ(0);
        urss2.setTextFill(Color.RED);
        urss2.setTranslateY(130);
        urss2.setTranslateX(screenSize.getWidth() / 2.1);
        urss2.setScaleX(screenSize.getWidth() / 400);
        urss2.setScaleY(screenSize.getWidth() / 400);

        Label précision = new Label("Précision des tirs : " + "" + "%");
        précision.setTextFill(Color.RED);

        Label dernierTir = new Label("Point dernier tir : " + "");
        dernierTir.setTextFill(Color.RED);
        précision.setTranslateX(screenSize.getWidth() / 1.5);
        précision.setTranslateY(screenSize.getHeight() / 3);
        dernierTir.setTranslateX(screenSize.getWidth() / 1.5);
        dernierTir.setTranslateY(screenSize.getHeight() / 3.5);

        Label vent = new Label("");
        Label affichageVent = new Label("Vent : " + 0 + "Km/h");
        affichageVent.setTextFill(Color.GOLD);
        affichageVent.setTranslateX(screenSize.getWidth() / 2);
        affichageVent.setTranslateY(screenSize.getHeight() / 16);

        // Bouton
        Button buttonNiveauPratique = new Button("Introduction", fondNiveau1);
        buttonNiveauPratique.setScaleX(0.45);
        buttonNiveauPratique.setScaleY(0.45);
        buttonNiveauPratique.setTranslateX(-400);
        buttonNiveauPratique.setTranslateY(-200);

        Button jouer = new Button("Play");

        Button code = new Button("Cheat");

        Button menuUtilisateur = new Button("Contrôle");

        //Musique
        final URL resource = getClass().getResource("Package_texture/HeroicMusic.mp3");
        final javafx.scene.media.Media media = new javafx.scene.media.Media(resource.toString());
        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();

        //Groupe
        VBox menuVertical = new VBox(jouer, menuUtilisateur, code);
        menuVertical.setTranslateX(screenSize.getWidth() / 2);
        menuVertical.setTranslateY(screenSize.getHeight() / 4);
        menuVertical.setSpacing(30);

        VBox groupeDeDonnées = new VBox(texteGravité, gravitéTir, texteVent, ventTir, texteMasseBalle, masseBalle);
        groupeDeDonnées.setTranslateX(screenSize.getWidth() / 4);
        groupeDeDonnées.setTranslateY(screenSize.getHeight() / 2.5);

        final Group[] root = new Group[10];


        root[0] = new Group(murGauche, murDroit/*,light*/, toit, balle[0], table, cible, piedsChaiseGauche, piedsChaiseDroit/*,tourCible,mire*/, viseur, viseurLineHorizontal, viseurLineVertical, affichageVent);
        root[1] = new Group(info, cible2, groupeDeDonnées, urss, dernierTir, précision);
        root[2] = new Group(carreMenuFond, menuVertical, urss2);
        root[3] = new Group();
        root[4] = new Group(carreCheatFond, password);
        root[5] = new Group(buttonNiveauPratique);

        //Création stage
        stage1.setTitle("URSS");
        stage1.setHeight(screenSize.getHeight());
        stage1.setWidth(screenSize.getWidth());
        stage1.setResizable(false);

        //listener
        quantitéPoudreNoir.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            controle.setPoudreNoir((double) newValue);
            textePoudreNoir.setText("Masse de poudre noir : " + newValue.intValue() + " g");
        });

        masseBalle.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            controle.setMasse((double) newValue);
            texteMasseBalle.setText("Masse de la balle : " + newValue.intValue() + " g");
        });

        gravitéTir.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            controle.setGravité((double) newValue);
            texteGravité.setText("Gravité : " + newValue.intValue() + (" m/s^2"));
        });

        ventTir.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            controle.setVent((double) newValue);
            texteVent.setText("Vent : " + newValue.intValue() + (" Km/h"));
            affichageVent.setText("Vent : " + newValue + " Km/h");
        });

        //Scene
        Scene niveau1 = new Scene(root[0], screenSize.getWidth(), screenSize.getHeight(), true, SceneAntialiasing.BALANCED);
        niveau1.setCursor(Cursor.NONE);
        niveau1.setCamera(camera1);

        Scene scene2 = new Scene(root[1], screenSize.getWidth(), screenSize.getHeight(), true, SceneAntialiasing.BALANCED);
        scene2.setCursor(Cursor.NONE);
        scene2.setFill(new ImagePattern(fondurl));
        scene2.setCamera(camera2);

        Scene niveau2 = new Scene(root[3], screenSize.getWidth(), screenSize.getHeight(), true, SceneAntialiasing.BALANCED);
        niveau2.setFill(new ImagePattern(aide));

        Scene menu = new Scene(root[2], screenSize.getWidth(), screenSize.getHeight(), true, SceneAntialiasing.BALANCED);

        Scene codeDeTriche = new Scene(root[4], screenSize.getWidth(), screenSize.getHeight(), true, SceneAntialiasing.BALANCED);

        Scene selectionNiveau = new Scene(root[5], screenSize.getWidth(), screenSize.getHeight(), true, SceneAntialiasing.BALANCED);
        selectionNiveau.setFill(new ImagePattern(fondurl));

        stage1.setResizable(false);
        stage1.setMaximized(true);
        stage1.setScene(menu);
        stage1.show();
        //Liste
        ArrayList listeDeCode = new ArrayList();
        ArrayList niveau = new ArrayList();
        ArrayList<Boolean> niveauCompleter = new ArrayList<>();
        niveau.add(niveau1);
        niveau.add(niveau2);
        listeDeCode.add("G93D");
        listeDeCode.add("L91A");
        for (int i = 0; i < 10; i++)
            niveauCompleter.add(false);


        // gestions des évènements

        scene2.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.I) {
                stage1.setScene(niveau1);
                root[0].getChildren().setAll(murGauche/*,light*/, balle[0], murDroit,/*mire,*/toit, table, cible, piedsChaiseGauche, piedsChaiseDroit,/*,tourCible,*/viseur, viseurLineHorizontal, viseurLineVertical, affichageVent);
            }
        });
        niveau2.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) ;
            stage1.setScene(menu);
        });

        buttonNiveauPratique.setOnMouseClicked(event -> {
            stage1.setScene(niveau1);
        });

        jouer.setOnMouseClicked(event -> {
            niveau1.setFill(new ImagePattern(fondurl, 0, 0, 1, 0, true));
            niveau1.setCamera(camera1);
            stage1.setScene(selectionNiveau);
        });

        // vérifie si les niveaux sont débloquer, mais pas utilisé, seulement pour le futur
        selectionNiveau.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.A && niveauCompleter.get(1).equals(true)) {
                stage1.setScene(niveau1);
            }
            if (event.getCode() == KeyCode.B && niveauCompleter.get(1).equals(true)) {
                stage1.setScene(niveau2);
            }
            if (event.getCode() == KeyCode.C && niveauCompleter.get(1).equals(true)) {

            }
            if (event.getCode() == KeyCode.ESCAPE) ;
            stage1.setScene(menu);
        });

        menuUtilisateur.setOnMouseClicked(event -> {
            mediaPlayer.stop();                  // arrete la musique si on appuis dans contrôle
            stage1.setScene(niveau2);
        });

        code.setOnMouseClicked(event -> {
            mediaPlayer.stop();
            stage1.setScene(codeDeTriche);
        });

        codeDeTriche.setOnKeyPressed((event -> {
            if (event.getCode() == KeyCode.ENTER) {
                for (int i = 0; i < listeDeCode.size(); i++)
                    if (password.getCharacters().toString().equals(listeDeCode.get(i).toString()))
                        stage1.setScene((Scene) niveau.get(i));
            }
            if (event.getCode() == KeyCode.ESCAPE) ;
            stage1.setScene((Scene) menu);
        }));

        // respiration
        Timeline resp = new Timeline(new KeyFrame(Duration.millis(20), new EventHandler<ActionEvent>() {
            int frame = 0;

            @Override
            public void handle(ActionEvent event) {
                frame++;
                if (frame > 200) {
                    frame = 0;
                }
                controle.Respiration(camera1, frame);
                controle.Respiration(viseur, frame);
                controle.Respiration(viseurLineHorizontal, frame);
                controle.Respiration(viseurLineVertical, frame);
            }
        }));
        resp.setCycleCount(Animation.INDEFINITE);
        resp.play();

        Timeline modVent = new Timeline(new KeyFrame(Duration.seconds(0.4), new EventHandler<ActionEvent>() {
            int temps = -10;

            @Override
            public void handle(ActionEvent event) {
                int randomVent = (int) (Math.random() * 100) % 4;
                if (randomVent == 0) {
                    controle.setVent(controle.getVent() + 4);
                    affichageVent.setText("Vent: " + controle.getVent() + " Km/h");
                } else if (randomVent == 1) {
                    controle.setVent(controle.getVent() - 4);
                    affichageVent.setText("Vent: " + controle.getVent() + " Km/h");
                } else if (randomVent == 2) {
                    controle.setVent(controle.getVent() + 1);
                    affichageVent.setText("Vent: " + controle.getVent() + " Km/h");
                } else if (randomVent == 3) {
                    controle.setVent(controle.getVent() - 1);
                    affichageVent.setText("Vent: " + controle.getVent() + " Km/h");
                }

            }

            ;
        }));
        modVent.setCycleCount(Animation.INDEFINITE);
        modVent.play();

        // suite de la gestion des évènements

        niveau1.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.R) {
                int variableVent;
                variableVent = randomVent.nextInt(150);
                if (variableVent > 75)
                    controle.setVent(variableVent / 2);
                if (75 >= variableVent)
                    controle.setVent(-variableVent / 2);
            }
            if (event.getCode() == KeyCode.I) {
                stage1.setScene(scene2);
            }
            if (event.getCode() == KeyCode.DOWN) {
                ajustement[1] = ajustement[1] + 10;
            }
            if (event.getCode() == KeyCode.UP) {
                ajustement[1] = ajustement[1] - 10;
            }
            if (event.getCode() == KeyCode.RIGHT) {
                ajustement[0] = ajustement[0] + 10;
            }
            if (event.getCode() == KeyCode.LEFT) {
                ajustement[0] = ajustement[0] - 10;
            }
            if (event.getCode() == KeyCode.SPACE) {

                if (!respiration[0]) {
                    resp.play();
                    respiration[0] = true;

                } else {
                    resp.pause();
                    respiration[0] = false;
                }
            }
            if (event.getCode() == KeyCode.R) {
                int variableVent;
                variableVent = randomVent.nextInt(150);
                if (variableVent > 75) {
                    controle.setVent(variableVent / 2);
                    vent.setText(Integer.toString(variableVent / 2));
                    affichageVent.setText("Vent: " + vent.getText() + " Km/h");
                }
                if (75 >= variableVent) {
                    controle.setVent(-variableVent / 2);
                    vent.setText(Integer.toString(-variableVent / 2));
                    affichageVent.setText("Vent: " + vent.getText() + " Km/h");
                }


            }
        });

        // tir et trajectoire
        niveau1.setOnMouseClicked(event -> {

            //set balle
            balle[0].setTranslateZ(-500);
            balle[0].setTranslateY(camera1.getTranslateY());
            balle[0].setTranslateX(camera1.getTranslateX());
            // enreistrement du tir
            root[1].getChildren().add((controle.Visée((camera1.getTranslateX() + controle.Vent() + (screenSize.getWidth() / 2) + ajustement[0]), (camera1.getTranslateY() + (screenSize.getHeight() / 2) + (controle.getDeltaY()) + ajustement[1]), 1)));


            //précision du tir (si le projectile touche la cible)

            double variationX = camera1.getTranslateX() + (controle.Vent()) + ajustement[0];
            double variationY = camera1.getTranslateY() + (controle.getDeltaY()) + ajustement[1];
            double vecteur = Math.sqrt((variationX * variationX) + (variationY * variationY));

            //Distribution de points en fonction de la précision du tir dans la cible
            int point = 0;
            int réussi = 0;

            if (100 > vecteur) {
                réussi++;
                point++;
            }
            if (87.5 > vecteur)
                point++;
            if (75 > vecteur)
                point++;
            if (62.5 > vecteur)
                point++;
            if (50 > vecteur)
                point++;
            if (37.5 > vecteur)
                point++;
            if (25 > vecteur)
                point++;
            if (12.5 > vecteur)
                point++;
            if (0.0 == vecteur)
                point = point + 100;
            compteurDePoint[0] = compteurDePoint[0] + point;
            compteurDePoint[1] = compteurDePoint[1]++;
            compteurDePoint[3] = point;
            compteurDePoint[2] = compteurDePoint[2];
            RatioDePoint[0] = RatioDePoint[0] + réussi;
            RatioDePoint[1] = RatioDePoint[1] + 1;
            tir[0] = Math.round((RatioDePoint[0] / RatioDePoint[1]) * 100);
            précision.setText("Précision des tirs : " + tir[0] + "%");
            dernierTir.setText("Points au dernier tir : " + point + " points");

            // trajectoire
            double tempsAnimation = ((controle.distance) / controle.vitesse) * 1000;
            double[] tempsInitial = new double[1];
            tempsInitial[0] = System.currentTimeMillis();
            traj.stop();
            traj = new Timeline(new KeyFrame(Duration.millis(10), new EventHandler<ActionEvent>() {

                double tempsFinal = 0;

                @Override
                public void handle(ActionEvent event) {

                    tempsFinal = System.currentTimeMillis() - tempsInitial[0];

                    if (tempsFinal < tempsAnimation || balle[0].getTranslateZ()<5000) {
                        balle[0].setTranslateY(balle[0].getTranslateY() + ((controle.getDeltaY() / tempsAnimation) * 10));
                        balle[0].setTranslateZ(balle[0].getTranslateZ() + ((5000 / tempsAnimation) * 10));
                        balle[0].setTranslateX(balle[0].getTranslateX() + ((controle.Vent()/tempsAnimation)*10));

                    }
                }

                ;
            }));
            traj.setCycleCount(Animation.INDEFINITE);
            traj.play();
        });
    }
}

