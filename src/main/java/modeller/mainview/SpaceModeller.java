/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modeller.mainview;

import modeller.generic.JButtonEx;
import modeller.generic.ModellerAction;
import modeller.util.KeyUtil;
import space.Acceleration;
import space.Location;
import space.SpaceObject;
import space.Velocity;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import modeller.objectlist.SpaceObjectListModel;
import modeller.objectlist.SpaceObjectListRenderer;

/**
 * Created by IntelliJ IDEA.
 * User: andrew
 * Date: Oct 14, 2010
 * Time: 10:52:53 AM
 * To change this template use File | Settings | File Templates.
 */

public class SpaceModeller implements Runnable {


    private JPanel modelcanvas;
    private JPanel modelview;
    private JPanel contolview;

    //double m = 7.36 * Math.pow(10, 22);
    //double M = 5.9742 * Math.pow(10, 24);

    private double scale = 2000;

    // m земли 5.9742 × 10^24
    // m солнца 1.98892 × 10^30
    // масса Солнца в 333 000 раза превышает массу Земли
    // масса Солнца в 332 270 раза превышает массу Земли
    //
    // масса Земли в 81,8 раза превышает массу Луны
    // масса луны 7.36 × 10^22

    private long lastgraphicsupdate = 0;

    private BufferedImage image = null;

    private JButton startButton;
    private JButton resetButton;


    public void updateGraphics(){
        if (System.currentTimeMillis() > lastgraphicsupdate + 50) {
            lastgraphicsupdate = System.currentTimeMillis();

            Dimension d = modelcanvas.getSize();

            // prepare buffer
            if (image == null || d.width != image.getWidth() || d.height != image.getHeight()) {
                image = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
            }

            Graphics2D g2d = image.createGraphics();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            Rectangle2D r = new Rectangle2D.Double(0, 0, (double)d.width, (double)d.height);

            g2d.setClip(r);

            g2d.setColor(Color.WHITE);
            g2d.fill(r);

            g2d.setColor(Color.DARK_GRAY);
            g2d.drawRect(0, 0, (int) d.width - 1, (int)d.height - 1);

            for (SpaceObject o : objectListModel.getObjects()) {
                o.print(g2d, scale);
            }

            g2d.dispose();

            Graphics2D panelg2d = (Graphics2D) (modelcanvas.getGraphics());

            panelg2d.drawImage(image, 0, 0, null);

            panelg2d.dispose();
        }
    }

    public void notifyObjectUpdated() {
        getObjectList().repaint();
        updateGraphics();
    }

    public void update() {

        Vector<SpaceObject> objects =
                objectListModel.getObjects();

        // move objects
        //
        for (SpaceObject o :objects) {
            o.move(1);
        }

        int cnt = objects.size();

        // recalculate force /
        // acceleration / velocity superposition
        //
        for(int i = 0; i < cnt; i++) {
            for(int j = 0; j < cnt; j++) {
                if (i == j) {
                    continue;
                } else {
                    objects.get(i).accelerate(objects.get(j), 1);
                }
            }
        }

        // update trajectory
        //
        for (SpaceObject o : objects) {
            o.trace(scale);
        }

        // notify listeners
        //
        for (SpaceObject o : objects) {
            o.notifyObjectUpdated();
        }

        updateGraphics();

        objectViewer.updateFields();
    }

    public boolean isThreading = true;
    public boolean isRunning = false;

    public void run(){
        try {
            while(isThreading) {
                if (isRunning) {
                    update();
                    //Thread.yield();
                    Thread.sleep(10);
                    
                } else {
                    Thread.sleep(500);
                }
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public JTextField scaleField;

    private SpaceObjectListModel objectListModel = null;
    private JList objectList = null;

    public JList getObjectList() {
        return objectList;
    }

    private void addSpaceObjects(){

        objectListModel.addElement(new SpaceObject("Silos", 7.36 * Math.pow(10, 18), new Location(384400, 0),
            new Velocity(0, 600), new Acceleration(0,0,0), 8, Color.GRAY));

        objectListModel.addElement(new SpaceObject("Tyros", 5.9742 * Math.pow(10, 21), new Location(0,0),
            new Velocity(0, 0), new Acceleration(0,0,0), 28, Color.ORANGE));

        objectListModel.addElement(new SpaceObject("Europa", 7.36 * Math.pow(10, 19), new Location(-504400, 0),
            new Velocity(0, 700), new Acceleration(0,0,0), 15, Color.GREEN));

        objectListModel.addElement(new SpaceObject("Zed", 9.36 * Math.pow(10, 19), new Location(784400, 0),
            new Velocity(0, -700), new Acceleration(0,0,0), 18, Color.DARK_GRAY));

        objectListModel.addElement(new SpaceObject("Lira", 2.9742 * Math.pow(10, 16), new Location(824400, 0),
            new Velocity(0, -1100), new Acceleration(0,0,0), 4, Color.BLUE));

    }

    private SpaceObjectViewer objectViewer;

    class ScaleActionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            scale = Double.parseDouble(scaleField.getText());
        }
    }

    class SpaceObjectListMouseListener extends MouseAdapter {
        public void showPopupMenu(MouseEvent event) {
            if (event.isPopupTrigger()) {
                JPopupMenu menu = new JPopupMenu("Options");

                JMenuItem updateProjectItem = new JMenuItem();
                updateProjectItem.setAction(new AbstractAction("Disable") {
                    public void actionPerformed(ActionEvent event) {
                        //new Thread(updateProject).start();
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                        //return getSelectedProjects().size() == 1;
                    }
                });

                JMenuItem deleteProjectItem = new JMenuItem();
                deleteProjectItem.setAction(new AbstractAction("Delete") {
                    public void actionPerformed(ActionEvent event) {
                        //new Thread(deleteProject).start();
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                });

                menu.add(updateProjectItem);
                menu.add(deleteProjectItem);

                menu.show(event.getComponent(), event.getX(), event.getY());
            }
        }

        public void mousePressed(MouseEvent event) {
                showPopupMenu(event);
        }

        public void mouseClicked(MouseEvent event) {
                showPopupMenu(event);
        }

        public void mouseReleased(MouseEvent event) {
                showPopupMenu(event);
        }

    }

    class ObjectListSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent event) {
            objectViewer.setObject((SpaceObject) objectListModel.getElementAt(
                objectList.getSelectedIndex()));

            //event.getFirstIndex();
        }
    }

    public SpaceModeller(){

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());

        // init buttons
        initModellerControlls();

        contolview = new JPanel(new BorderLayout());
        contolview.setPreferredSize(new Dimension(300, 1000));

        scaleField = new JTextField();
        scaleField.setText(Double.toString(scale));
        scaleField.addActionListener(new ScaleActionListener());

        JPanel scaleview = new JPanel(new BorderLayout());
        scaleview.setBorder(new TitledBorder(" SCALE "));
        scaleview.add(scaleField, BorderLayout.NORTH);

        JPanel controlholder = new JPanel(new FlowLayout());
        controlholder.add(resetButton);
        controlholder.add(startButton);

        JPanel upperview = new JPanel(new GridLayout(2, 1));
        upperview.setBorder(new TitledBorder(" CONTROLLS "));
        upperview.add(controlholder);
        upperview.add(scaleview);

        objectListModel = new SpaceObjectListModel();
        objectList = new JList(objectListModel);

        objectList.setCellRenderer(new SpaceObjectListRenderer());
        objectList.setVisibleRowCount(10);
        objectList.setOpaque(false);
        objectList.setBackground(new Color(220, 220, 220));
        objectList.setSelectionForeground(Color.WHITE);
        objectList.setSelectionBackground(new Color(210, 180, 180));


        objectList.addMouseListener(new SpaceObjectListMouseListener());
        objectList.addListSelectionListener(new ObjectListSelectionListener());


        addSpaceObjects();

        objectViewer = new SpaceObjectViewer(this);
        objectList.setSelectedIndex(0);

        JPanel objectview = new JPanel(new BorderLayout());
        objectview.setBorder(new TitledBorder(" OBJECTS "));
        objectview.add(new JScrollPane(objectList), BorderLayout.CENTER);
        objectview.add(objectViewer, BorderLayout.SOUTH);

        contolview.add(upperview, BorderLayout.NORTH);
        contolview.add(objectview, BorderLayout.CENTER);

        // F = m*a
        //Wn = ω^2*R
        // F = -mω^2*R = -mv^2/R

        //v1 = ω1 R1

        modelview = new JPanel(new BorderLayout());
        modelview.setBorder(new TitledBorder(" MODEL "));
        modelview.setPreferredSize(new Dimension(1000, 1000));
        modelcanvas = new JPanel(new BorderLayout());
        modelcanvas.setIgnoreRepaint(true);
        modelview.add(modelcanvas, BorderLayout.CENTER);

        frame.add(contolview, BorderLayout.EAST);
        frame.add(modelview, BorderLayout.CENTER);

        frame.setPreferredSize(new Dimension(1300, 1000));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        new Thread(this).start();

        EventQueue.invokeLater(new Runnable(){
            public void run(){
                updateGraphics();
            }
        });

    }


    // U T I L
    //
    //
    //

    public void registerKeyAction(String actionname) {

        /*
        EditorAction action = editorActions.get(actionname);
        KeyStroke ks = action.getKeystroke();

        if (ks != null) {
            editBuffer.getInputMap().put(ks, actionname);
            editBuffer.getActionMap().put(actionname, action);
        }
        */
    }

    private void initModellerControlls() {

        modellerActions.put(START, new StartAction("Start",
                KeyUtil.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.META_MASK)));

        modellerActions.put(RESET, new ResetAction("Reset",
                KeyUtil.getKeyStroke(KeyEvent.VK_BACK_SPACE, KeyEvent.META_MASK)));


        startButton    = new JButtonEx(null, "/images/media-playback-start.png",
                            "/images/media-playback-pause.png", modellerActions.get(START));

        resetButton    = new JButtonEx(null, "/images/media-skip-backward.png",
                            null, modellerActions.get(RESET));

        registerKeyAction(START);
        registerKeyAction(RESET);

    }


    // A C T I O N S
    //
    //
    //

    static final String START = "start";
    static final String RESET = "reset";


    private HashMap<String, ModellerAction> modellerActions = new HashMap<String, ModellerAction>();

    private Vector<SpaceObject> objectListClone = null;

    class StartAction extends ModellerAction {
        public StartAction(String name, KeyStroke ks){
            super(name, ks);
        }
        public void actionPerformed(ActionEvent e) {
            if (objectListClone == null) {
                objectListClone = new Vector<SpaceObject>();

                for (SpaceObject o : objectListModel.getObjects()) {
                    objectListClone.add(new SpaceObject(o));
                }

                //objectListClone = (Vector) objectListModel.getObjects().clone();
            }

            isRunning = !isRunning;
        }
        @Override
        public boolean isEnabled() {
            return true;
        }
    }

    class ResetAction extends ModellerAction {
        public ResetAction(String name, KeyStroke ks){
            super(name, ks);
        }
        public void actionPerformed(ActionEvent e) {
            if (objectListClone != null) {
                Vector<SpaceObject> clone = new Vector<SpaceObject>();

                for (SpaceObject o : objectListClone) {
                    clone.add(new SpaceObject(o));
                }

                objectListModel.setObjects(clone);
            }

            updateGraphics();
        }
        @Override
        public boolean isEnabled() {
            return true;
        }
    }

    // M A I N

    public static void main(String[] args) {
        // TODO code application logic here
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SpaceModeller();
            }
        });
    }

}

