package Controller;

import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class Controller {
    public Ellipse Visée(Double posX, Double posY){
        Ellipse balle = new Ellipse(posX,posY,5,5);
        balle.setFill(Color.WHITE);
        balle.setStroke(Color.BLACK);
        return balle;
    };
    public Double Gravité(double poudreNoir,double masseBalle ,double distance ){
        double E = poudreNoir*(1/*constante*/);
        double v = E*masseBalle;
        double T = (distance/v);
        double g = 2;
        double deltaY = T+((g*(T*T))/2) ;//delta Y = vi*temps+((a*temps^2)/2)
        return deltaY;
    };
}
