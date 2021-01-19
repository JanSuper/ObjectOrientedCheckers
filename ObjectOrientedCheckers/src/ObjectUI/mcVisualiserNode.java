package ObjectUI;

import MonteCarlo.mcNode;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

import java.util.ArrayList;

public class mcVisualiserNode extends Sphere {

    private int depth;
    private int childNum;
    private mcNode mcnode;
    ArrayList<mcVisualiserNode> children = new ArrayList<>();

    public mcVisualiserNode(){
        super(50);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(Color.GREY);
        setMaterial(material);

        setOnMouseClicked(e -> {
            //selected node
            if(mcVisualizerGroup.selectedNode != null){
                PhongMaterial m = new PhongMaterial();
                m.setDiffuseColor(Color.GREY);
                mcVisualizerGroup.selectedNode.setMaterial(m);
            }
            material.setDiffuseColor(Color.PURPLE);
            this.setMaterial(material);
            mcVisualizerGroup.selectedNode = this;
            //change color
        });
    }

    public void setDepth(int d){this.depth = d;}

    public int getDepth(){return this.depth;}

    public void setChildNum(int c){this.childNum = c;}

    public int getChildNum(){return this.childNum;}

    public void setMcnode(mcNode node){this.mcnode = node;}

    public mcNode getMcnode(){return this.mcnode;}

}
