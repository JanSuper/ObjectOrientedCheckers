package ObjectUI;

import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class mcVisualizerStage extends Stage {

    public mcVisualizerGroup group;

    public mcVisualizerStage(mcVisualizerGroup g){
        this.group = g;
        addEvents();
    }

    private void addEvents()
    {
        addEventHandler(KeyEvent.KEY_PRESSED, event ->{
            switch (event.getCode()) {
                case A -> group.zoomIn();
                case Z -> group.zoomOut();

                case E -> group.mcVisExpand();
                case S -> group.simulate();
                case Q -> group.selectNode();
                case P -> group.printField();
                case D -> group.deleteChildren();

                case NUMPAD8 -> group.goUp();
                case NUMPAD5 -> group.goDown();
                case NUMPAD4 -> group.goLeft();
                case NUMPAD6 -> group.goRight();
                case ESCAPE -> Main.board.ResetUI();
            }
        });
    }
}
