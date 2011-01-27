package modeller.generic;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: andrew
 * Date: Jan 27, 2011
 * Time: 2:19:49 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ModellerAction extends AbstractAction {

    private KeyStroke keystroke;
    public ModellerAction(String name, KeyStroke keystroke){
        super(name);
        this.keystroke = keystroke;
    }
    public KeyStroke getKeystroke() {
        return keystroke;
    }

    public void setKeystroke(KeyStroke keystroke) {
        this.keystroke = keystroke;
    }
    
}
