import javax.swing.*;
import java.awt.event.ActionEvent;

public class KeyAction extends AbstractAction {

    private final GUI gui;
    public enum Action {
        SEARCH,
        GENERATE,
    }
    private final Action currentAction;

    public KeyAction(Action action, GUI gui){
        this.gui = gui;
        currentAction = action;
    }

    public void actionPerformed(ActionEvent e){
        switch (currentAction){
            case SEARCH -> gui.search();
            case GENERATE -> Main.generateRewards(gui.getGoldValue());
        }
    }
}
