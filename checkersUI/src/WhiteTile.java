
import javafx.scene.image.Image;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class WhiteTile extends Box
{

    private static final double WIDTH = 100;
    private static final double HEIGHT = 100;
    private static final double DEPTH = 25;

    public WhiteTile()
    {
        super(WIDTH, HEIGHT, DEPTH);

        translateXProperty().set(this.getTranslateX()+50);
        translateYProperty().set(this.getTranslateY()+50);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(String.valueOf(getClass().getResource("/images/white tile.jpg"))));
        setMaterial(material);
    }
}