package UI;

import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

public class SmartGroup extends Group
{

    Rotate r;
    Transform t = new Rotate();

    public SmartGroup()
    {
        super();
    }

    void rotateByX(int ang)
    {
        r = new Rotate(ang,Rotate.X_AXIS);
        t = t.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().add(t);
    }

    void rotateByY(int ang)
    {
        r = new Rotate(ang,Rotate.Y_AXIS);
        t = t.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().add(t);
    }

    void zoomIn()
    {
        translateZProperty().set(this.getTranslateZ()-100);
    }

    void zoomOut()
    {
        translateZProperty().set(this.getTranslateZ()+100);
    }

    void goUp()
    {
        translateYProperty().set(this.getTranslateY() - 100);
    }

    void goDown()
    {
        translateYProperty().set(this.getTranslateY() + 100);
    }

    void goLeft()
    {
        translateXProperty().set(this.getTranslateX() + 100);
    }

    void goRight()
    {
        translateXProperty().set(this.getTranslateX() - 100);
    }
}
