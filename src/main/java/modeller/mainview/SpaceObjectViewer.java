/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modeller.mainview;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import space.SpaceObject;
import sun.awt.VerticalBagLayout;


/**
 * Created by IntelliJ IDEA.
 * User: andrew
 * Date: Oct 15, 2010
 * Time: 3:19:20 PM
 * To change this template use File | Settings | File Templates.
 */

public class SpaceObjectViewer extends JPanel {

    public  SpaceObject object = null;

    public  JTextField objname = null;
    public  JSlider    objsize = null;
    public  JObjView   objview = null;

    public  JTextField objmass = null;

    public  JTextField objcoordX = null;
    public  JTextField objcoordY = null;

    public  JTextField objaccX = null;
    public  JTextField objaccY = null;

    public  JTextField objvelX = null;
    public  JTextField objvelY = null;

    private long lastfieldupdate = 0;

    private Cursor hand = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

    private SpaceModeller modeller;

    private static final int SIZE_MIN = 1;
    private static final int SIZE_MAX = 100;
    private static final int SIZE_INIT = 15;

    class JObjView extends JLabel {

        public void paintComponent(Graphics g) {

            g.setColor(getBackground());

            ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g.fillRect(1, 1, w-2, h-2);

            if (SpaceObjectViewer.this.object != null) {
                SpaceObjectViewer.this.object.paint((Graphics2D)g, w/2, h/2);
            }
        }
    }

    public SpaceObjectViewer(SpaceModeller modeller){

        this.modeller = modeller;

        setLayout(new BorderLayout());

        objname = new JTextField(); //object.getName()
        objsize = new JSlider(JSlider.HORIZONTAL,
                SIZE_MIN, SIZE_MAX, SIZE_INIT);

        objsize.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent event) {
                JSlider source = (JSlider)event.getSource();
                if (!source.getValueIsAdjusting()) {
                    int size = (int) source.getValue();

                    SpaceObjectViewer.this.object.setViewsize(size);
                    notifyObjectChanged();
                }
            }
        });

        objview = new JObjView();
        objview.setOpaque(false);
        objview.setPreferredSize(new Dimension(48,48));
        objview.setCursor(hand);
        objview.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent event) {
                Color newcolor = JColorChooser.showDialog(
                             SpaceObjectViewer.this,
                             "Object Color",
                             SpaceObjectViewer.this.object.getColor());

                if (newcolor != null) {
                    SpaceObjectViewer.this.object.setColor(newcolor);
                    notifyObjectChanged();
                }

            }
        });


        JPanel visualview = new JPanel(new BorderLayout());
        visualview.setOpaque(false);
        visualview.setBorder(new TitledBorder(" VISUALS "));

        visualview.add(objname, BorderLayout.NORTH);
        visualview.add(objview, BorderLayout.CENTER);
        visualview.add(objsize, BorderLayout.SOUTH);

        objmass = new JTextField();
        objcoordX = new JTextField();
        objcoordY = new JTextField();
        JPanel massview = new JPanel(new BorderLayout());
        massview.setOpaque(false);
        massview.setBorder(new TitledBorder(" MASS & LOCATION "));

        JPanel massgrid = new JPanel(new GridLayout(3, 1, 0, 3));
        massgrid.setOpaque(false);
        massgrid.add(objmass);
        massgrid.add(objcoordX);
        massgrid.add(objcoordY);
        massview.add(massgrid, BorderLayout.CENTER);

        objaccX = new JTextField();
        objaccY = new JTextField();
        JPanel accelview = new JPanel(new GridLayout(2, 1, 0, 3));
        accelview.setOpaque(false);
        accelview.setBorder(new TitledBorder(" ACCELERATION "));
        accelview.add(objaccX);
        accelview.add(objaccY);

        objvelX = new JTextField();
        objvelY = new JTextField();
        JPanel veloview = new JPanel(new GridLayout(2, 1, 0, 3));
        veloview.setOpaque(false);
        veloview.setBorder(new TitledBorder(" VELOCITY "));
        veloview.add(objvelX);
        veloview.add(objvelY);

        JButton update = new JButton(" save ");
        update.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateObject();
            }
        });

        JPanel buttonview = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonview.setOpaque(false);
        buttonview.add(update);


        JPanel viewgrid = new JPanel(new VerticalBagLayout()); //5, 1
        viewgrid.setOpaque(false);
        viewgrid.add(visualview);
        viewgrid.add(massview);
        viewgrid.add(accelview);
        viewgrid.add(veloview);
        viewgrid.add(buttonview);

        JTabbedPane objview = new JTabbedPane();
        objview.addTab(" Object Viewer ", viewgrid);

        add(objview, BorderLayout.NORTH);

        updateFields();

        setPreferredSize(new Dimension(400,550));
    }

    public SpaceObject getObject() {
        return this.object;
    }

    public void setObject(SpaceObject object) {
        this.object = object;
        updateFieldsImmediately();
    }

    public void updateFieldsImmediately() {

        EventQueue.invokeLater(new Runnable() {
            public void run() {

                SpaceObject obj = getObject();

                objname.setText(obj.getName());
                objsize.setValue(obj.getViewsize());
                objview.repaint();

                objmass.setText(Double.toString(obj.mass));

                objcoordX.setText(Double.toString(obj.coords.x));
                objcoordY.setText(Double.toString(obj.coords.y));

                objaccX.setText(Double.toString(obj.accel.ax));
                objaccY.setText(Double.toString(obj.accel.ay));

                objvelX.setText(Double.toString(obj.velocity.vx));
                objvelY.setText(Double.toString(obj.velocity.vy));
            }
        });
    }


    public void updateFields() {

        if (System.currentTimeMillis() > lastfieldupdate + 500) {
            lastfieldupdate = System.currentTimeMillis();
            updateFieldsImmediately();
        }
    }


    public void notifyObjectChanged() {
        SpaceObjectViewer.this.objview.repaint();
        SpaceObjectViewer.this.modeller.notifyObjectUpdated();
    }

    public void updateObject() {

        SpaceObject obj = getObject();

        obj.name = objname.getText();

        obj.viewsize = objsize.getValue();

        obj.mass = Double.parseDouble(objmass.getText());

        obj.coords.x = Double.parseDouble(objcoordX.getText());
        obj.coords.y = Double.parseDouble(objcoordY.getText());

        obj.accel.ax = Double.parseDouble(objaccX.getText());
        obj.accel.ay = Double.parseDouble(objaccY.getText());

        obj.velocity.vx = Double.parseDouble(objvelX.getText());
        obj.velocity.vy = Double.parseDouble(objvelY.getText());

        notifyObjectChanged();
    }

}
