package UI;

import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class SmartStage extends Stage
{
    SmartGroup group;
    public SmartStage(SmartGroup smartGroup)
    {
        super();
        this.group = smartGroup;
        addEvents();
    }

    private void addEvents()
    {
        addEventHandler(KeyEvent.KEY_PRESSED, event ->{
            switch (event.getCode())
            {
                case Q:
                    group.rotateByY(-10);
                    break;
                case E:
                    group.rotateByY(10);
                    break;
                case W:
                    group.rotateByX(-10);
                    break;
                case S:
                    group.rotateByX(10);
                    break;
                case A:
                    group.zoomIn();
                    break;
                case D:
                    group.zoomOut();
                    break;
                case NUMPAD8:
                    group.goUp();
                    break;
                case NUMPAD5:
                    group.goDown();
                    break;
                case NUMPAD4:
                    group.goLeft();
                    break;
                case NUMPAD6:
                    group.goRight();
                    break;
            }
        });
    }

}
