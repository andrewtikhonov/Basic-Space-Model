/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modeller.objectlist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import space.SpaceObject;

/**
 * Created by IntelliJ IDEA.
 * User: andrew
 * Date: Oct 15, 2010
 * Time: 3:09:25 PM
 * To change this template use File | Settings | File Templates.
 */

public class SpaceObjectListRenderer extends JPanel implements ListCellRenderer {

    SpaceObject object = null;

    private ImageIcon objecticon = null;

    private JLabel objecttitle;
    private JLabel objectdetails;
    private JLabel objectimage;

    private JPanel textpanel;
    private JPanel imagepanel;

    private Color c1;
    private Color c2;
    private Color c3;
    private Color c4;
    private Color c5;

    private Cursor hand = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

    public SpaceObjectListRenderer() {
        super();

        setBorder(new EmptyBorder(4,4,4,4));

        setLayout(new BorderLayout());
        setOpaque(true);

        imagepanel = new JPanel(new BorderLayout(10, 10));
        textpanel = new JPanel(new GridLayout(2, 1));
        imagepanel.setOpaque(false);
        textpanel.setOpaque(false);


        objecttitle = new JLabel(" ");
        objectdetails = new JLabel(" ");
        objectimage = new JLabel(" ");

        objecttitle.setOpaque(false);
        objectdetails.setOpaque(false);
        objectimage.setOpaque(false);

        Font f = objectdetails.getFont();
        objectdetails.setFont(new Font(f.getFamily(), Font.PLAIN, 10));
        objectdetails.setForeground(Color.DARK_GRAY);

        textpanel.add(objecttitle);
        textpanel.add(objectdetails);


        imagepanel.add(objectimage, BorderLayout.EAST);

        add(imagepanel, BorderLayout.WEST);
        add(textpanel, BorderLayout.CENTER);
    }

    @Override
    public void paintComponent(Graphics g) {

        g.setColor(getBackground());

        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g.fillRect(1, 1, getWidth()-2, getHeight()-2);
    }

    public BufferedImage paintImage() {

        int size = 40;

        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        object.paint(g2d, size/2, size/2);

        g2d.dispose();

        return image;
    }

    public Component getListCellRendererComponent(JList list,Object value,
            int index, boolean isSelected,boolean cellHasFocus) {

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        object = (SpaceObject) value;

        //if (objecticon == null) {
            objecticon = new ImageIcon(paintImage());
            objectimage.setIcon(objecticon);
        //}

        objecttitle.setText("  " + object.getName());
        objectdetails.setText("  " + "MASS: " + object.getMass());

        int r0 = getBackground().getRed();
        int g0 = getBackground().getGreen();
        int b0 = getBackground().getBlue();

        c1 = new Color(r0,g0,b0,100);
        c2 = new Color(r0,g0,b0,250);

        c3 = new Color(r0,g0,b0,200);
        c4 = new Color(r0,g0,b0,100);

        c5 = Color.LIGHT_GRAY.darker().darker().darker();

        objecttitle.setForeground(getForeground());
        objectdetails.setForeground(getForeground());

        return this;
    }

}
