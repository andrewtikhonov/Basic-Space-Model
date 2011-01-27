package space;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.Vector;


/**
 * Created by IntelliJ IDEA.
 * User: andrew
 * Date: Oct 15, 2010
 * Time: 3:09:21 PM
 * To change this template use File | Settings | File Templates.
 */

public class SpaceObject {

    public String name = "undefined";
    public int viewsize = 0;
    public Color color = Color.BLUE;
    public Color tracecolor = Color.BLUE;
    public double mass = 0;
    public Location coords = new Location(0, 0);
    public Velocity velocity = new Velocity(0, 0);
    public Acceleration accel = new Acceleration(0, 0, 0);
    private Vector<Location> trace = new Vector<Location>();

    public SpaceObject(String name, double mass, Location coords, Velocity velocity, Acceleration accel,
            int viewsize, Color color){

        this.name = name;
        this.color = color;
        this.tracecolor = color.darker().darker();
        this.viewsize = viewsize;
        this.mass = mass;
        this.coords = coords;
        this.accel = accel;
        this.velocity = velocity;
    }

    public SpaceObject(SpaceObject toCopy){
        this(toCopy.name, toCopy.mass, new Location(toCopy.coords.x, toCopy.coords.y),
                new Velocity(toCopy.velocity.vx, toCopy.velocity.vy),
                new Acceleration(toCopy.accel.a, toCopy.accel.ax, toCopy.accel.ay),
                toCopy.viewsize,
                toCopy.color);
    }

    public void move(int time){
        coords.x = coords.x + velocity.vx * time;
        coords.y = coords.y + velocity.vy * time;
    }

    public void accelerate(SpaceObject obj2, int time){

        double Rpow2 = Math.pow(Math.abs(coords.x - obj2.coords.x), 2) +
                Math.pow(Math.abs(coords.y - obj2.coords.y), 2);

        double F = Const.G * mass * obj2.mass / Rpow2;


        // F = ma;
        // a = F/m
        // ax/ay = x/y
        // ax = x/y * ay
        // ax^2 + ay^2 = a^2
        // (x/y * ay)^2 + ay^2 = a ^2
        // ay^2 = a^2 / ((x/y)^2 + 1)
        //

        accel.a = F / mass;

        double dx = obj2.coords.x - coords.x;
        double dy = obj2.coords.y - coords.y;

        accel.ay = Math.sqrt(Math.pow(accel.a, 2) / (Math.pow((dx/dy),2) + 1));
        accel.ax = accel.ay * Math.abs(dx/dy);

        accel.ay = accel.ay * (dy >= 0 ? 1 : -1); // set vector direction
        accel.ax = accel.ax * (dx >= 0 ? 1 : -1); //

        velocity.vx = velocity.vx + accel.ax * 1;
        velocity.vy = velocity.vy + accel.ay * 1;
    }

    public void trace(double scale){
        //if (true) { return; }

        if (trace.size() == 0) {
            trace.add(new Location(coords.x, coords.y));

            //trace.clone()
        } else {
            Location l = trace.get(trace.size() - 1);

            if ((Math.abs(l.x - coords.x)/scale > 10) ||
                    (Math.abs(l.y - coords.y)/scale > 10)) {

                trace.add(new Location(coords.x, coords.y));
            }
        }

        if (trace.size() > 550) {
            trace.removeElementAt(0);
        }
    }

    public void paint(Graphics2D g2d, int x, int y) {
        g2d.setColor(color);
        g2d.fillOval(x - viewsize/2, y-viewsize/2, viewsize, viewsize);
    }

    public void print(Graphics2D g2d, double scale) {

        Shape s = g2d.getClip();

        if (s instanceof Rectangle2D.Double) {
            Rectangle2D.Double r = (Rectangle2D.Double) s;

            int w = (int) r.width;
            int h = (int) r.height;

            int x0 = w/2;
            int y0 = h/2;

            int x = (int) (coords.x/scale);
            int y = (-1) * (int) (coords.y/scale);

            if (x0 + x < 0 || x0 + x > w) {
                g2d.drawString("object X out of bounds x=" + x, 10 + viewsize, 10 + viewsize);
            }

            if (y0 + y < 0 || x0 + y > h) {
                g2d.drawString("object Y out of bounds y=" + y, 10 + viewsize, 30 + viewsize);
            }

            //g2d.setColor(Color.RED.darker().darker());
            //g2d.setColor(Color.RED.darker().darker());
            g2d.setColor(tracecolor);

            for(Location l : trace) {
                //System.out.println("trace.size()="+trace.size());
                int d = 3;

                g2d.fillOval(x0 + (int) (l.x/scale) - d/2,
                        y0 - (int) (l.y/scale) - d/2, d, d);
            }

            paint(g2d, x0 + x, y0 + y);

        } else {
            System.out.println("Error: getClip is not rectangle, " + s.getClass().getCanonicalName());
        }
    }

    public void notifyObjectUpdated(){
    }

    public void reset() {
    }

    public Acceleration getAccel() {
        return accel;
    }

    public void setAccel(Acceleration accel) {
        this.accel = accel;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Location getCoords() {
        return coords;
    }

    public void setCoords(Location coords) {
        this.coords = coords;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector<Location> getTrace() {
        return trace;
    }

    public void setTrace(Vector<Location> trace) {
        this.trace = trace;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }

    public int getViewsize() {
        return viewsize;
    }

    public void setViewsize(int viewsize) {
        this.viewsize = viewsize;
    }


}

