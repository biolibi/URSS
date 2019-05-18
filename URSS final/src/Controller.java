import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import java.awt.*;

public class Controller {
    double masse = 1;                                      // a definir avant le tir
    double poudreNoir =1;                                 // a definir avant le tir
    double energie = poudreNoir*(1/*constante*/);
    double vitesse = 600;
    double gravité = 9.8;                                    // a definir avant le tir
    double distance = 0+1300;                                   // a definir avant le tir (start at 1300)
    double temps = (distance/vitesse);
    double deltaT = 0;

    public void setDeltaT(double deltaT) {
        this.deltaT = deltaT;
    }

    double vent = 0;
    double deltaY = ((gravité*(temps*temps))/2)*masse ;//delta Y = vi*temps+((a*temps^2)/2)

    public double getVent() {
        return vent;
    }

    public void setMasse(double masse) {
        this.masse = masse;
    }

    public void setPoudreNoir(double poudreNoir) {
        this.poudreNoir = poudreNoir;
        this.energie = this.poudreNoir*(1/*constante*/);
    }

    public void setGravité(double gravité) {
        this.gravité = gravité;
    }

    public void setDistance(double distance) {
        this.distance = distance;
        this.temps = (distance/vitesse);
    }

    public void setVitesse(double vitesse) {
        this.vitesse = vitesse;
        temps = (distance/vitesse);
    }

    public void setVent(double vent) {
        this.vent = vent;
    }

    public Ellipse Visée(Double posX, Double posY, int info){
        Ellipse balle = new Ellipse(posX,posY,4,4);
        balle.setFill(Color.WHITE);
        balle.setStroke(Color.BLACK);
        if(info==0){
            balle.setTranslateZ(5000);
            balle.setRadiusX(8);
            balle.setRadiusY(8);
        }
        return balle;
    };
    public Double getDeltaY(){
        deltaY = (((gravité*masse)*(temps*temps))/2) ;//delta Y = vi*temps+((a*temps^2)/2)
        return deltaY;
    };
    public Double Vent(){
        double deltaX = (vent*(temps*temps))/2;  //même formule que la gravité...   delta X = vi*temps+((a*temps^2)/2)
        return deltaX;
    }

    public void Respiration(Node centreVisée, int frame){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double sWidth = screenSize.getWidth();
        double sHeight = screenSize.getHeight();

        centreVisée.setTranslateX(MouseInfo.getPointerInfo().getLocation().getX()-(sWidth/2));
        int valueTest =(int)((Math.ceil(frame/100))%2);
        double respiration = 0;
        if(valueTest==0){
            if(frame<=50){
                respiration = frame*0.35;
                centreVisée.setTranslateY(MouseInfo.getPointerInfo().getLocation().getY()-(sHeight/2)-respiration);
            }else if(frame<=100){
                respiration = frame*0.35;
                centreVisée.setTranslateY(MouseInfo.getPointerInfo().getLocation().getY()-(sHeight/2)-35+respiration);
            }
        }else{
            if(frame<=150){
                respiration = (frame-100)*0.35;
                centreVisée.setTranslateY(MouseInfo.getPointerInfo().getLocation().getY()-(sHeight/2)+respiration);
            }else if(frame<=200){
                respiration = (frame-100)*0.35;
                centreVisée.setTranslateY(MouseInfo.getPointerInfo().getLocation().getY()-(sHeight/2)+35-respiration);
            }
        }
    }
}