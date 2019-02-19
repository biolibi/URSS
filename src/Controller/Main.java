package Controller;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application{

    public static void main(String[] args) {launch(args);}
    public void start(Stage stage1){
        Ellipse cible = new Ellipse(395,200,25,25);
        Rectangle tourCible = new Rectangle(370,150,310,310);
        tourCible.setOpacity(0);
        Ellipse cible2 = new Ellipse(500,200,100,100);


        final PhongMaterial ciel = new PhongMaterial();
        final PhongMaterial couleurTable = new PhongMaterial();
        couleurTable.setDiffuseColor(Color.SADDLEBROWN);
        couleurTable.setSpecularColor(Color.SADDLEBROWN);

        ciel.setDiffuseColor(Color.LIGHTBLUE);
        ciel.setSpecularColor(Color.GREEN);
        Box murGauche = new Box(20,500,2000);
        Box toit = new Box(800,10,2000);
        Box table = new Box(800,40,100);
        Box piedsChaiseGauche = new Box(20,125,100);
        Box piedsChaiseDroit = new Box(20,125,100);
        piedsChaiseGauche.setTranslateY(380);
        piedsChaiseGauche.setTranslateX(200);
        piedsChaiseGauche.setMaterial(couleurTable);
        piedsChaiseDroit.setTranslateY(380);
        piedsChaiseDroit.setTranslateX(585);
        piedsChaiseDroit.setMaterial(couleurTable);
        table.setMaterial(couleurTable);
        table.setTranslateX(200);
        table.setTranslateY(300);
        toit.setTranslateX(200);
        toit.setMaterial(ciel);
        murGauche.setTranslateX(180);
        murGauche.setTranslateY(200);
        murGauche.setTranslateZ(100);
        Box murDroit = new Box(20,500,2000);
        murDroit.setMaterial(ciel);
        murGauche.setMaterial(ciel);
        murDroit.setTranslateX(605);
        murDroit.setTranslateY(200);
        murDroit.setTranslateZ(100);
        AmbientLight light = new AmbientLight();
        light.setColor(Color.rgb(0,204,204));
        light.setOpacity(1.0);

        final Group[] root = new Group[2];
         Controller controle= new Controller();
        root[0] = new Group(tourCible,murGauche,murDroit,light,toit,table,cible,piedsChaiseGauche,piedsChaiseDroit);
        root[1] = new Group(cible2);
        Camera camera = new PerspectiveCamera();
        camera.setTranslateZ(200);


        stage1.setTitle("URSS");
        stage1.setHeight(500);
        stage1.setWidth(800);
        stage1.setResizable(true);

        Scene scene1 = new Scene(root[0],500,800, true, SceneAntialiasing.BALANCED);
        scene1.setFill(Color.DARKOLIVEGREEN);
        scene1.setCamera(camera);

        stage1.setScene(scene1);
        stage1.show();

        tourCible.setOnMouseClicked(event -> {
            double positionX = event.getSceneX();
            double positionY = event.getSceneY();
            root[1].getChildren().add(controle.Visée(positionX+300,positionY+(controle.Gravité(10,10,0))));
            /*
            root[0].getChildren().setAll(cible, controle.Visée((positionX),positionY+(controle.Gravité(10,10,0))), root[1],tourCible);
            */

        });
    }

}

