package modeller.generic;

import modeller.util.ImageLoaderCached;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: andrew
 * Date: Jan 27, 2011
 * Time: 1:55:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class JButtonEx extends JButton {

    private Cursor hand = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    
    public JButtonEx(String text, String imagePath0, String imagePath1, ModellerAction action) {

        setCursor(hand);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusable(false);

        setAction(action);
        setText(text);

        ImageIcon icon0 = new ImageIcon(ImageLoaderCached.load(imagePath0));
        setIcon(icon0);

        // a lot of code to do a simple thing
        //
        if (imagePath1 != null) {
            ImageIcon icon1 = new ImageIcon(ImageLoaderCached.load(imagePath1));
            Vector<ImageIcon> icons = new Vector<ImageIcon>();
            icons.add(icon0);
            icons.add(icon1);
            addActionListener(new IconChanger(this, icons));
        }

        setToolTipText(action.getValue(Action.NAME) +
                " (" + getAcceleratorText(action.getKeystroke()) +")");

        setPreferredSize(new Dimension(34, 34));
    }


    // a lot of code to do a simple thing
    //
    class IconChanger implements ActionListener {
        private JButton b;
        private Vector<ImageIcon> icons;
        private int index = 0;
        public IconChanger(JButton b, Vector<ImageIcon> icons){
            this.b = b;
            this.icons = icons;
        }

        public void actionPerformed(ActionEvent event) {
            index = (++index >= (icons.size()) ? 0 : index);
            b.setIcon(icons.get(index));
        }
    }


    private String getAcceleratorText(KeyStroke ks) {
        String acceleratorText = "";
        String acceleratorDelimiter = "+";
        int modifiers = ks.getModifiers();
        int keyCode = ks.getKeyCode();
        if (modifiers > 0) {
            acceleratorText = KeyEvent.getKeyModifiersText(modifiers);
            acceleratorText += acceleratorDelimiter;
        }
        if (keyCode != 0) {
            acceleratorText += KeyEvent.getKeyText(keyCode);
        }

        return acceleratorText;
    }

}
