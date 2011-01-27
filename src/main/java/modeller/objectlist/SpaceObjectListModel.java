/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modeller.objectlist;

import java.util.Vector;
import javax.swing.AbstractListModel;
import space.SpaceObject;

/**
 * Created by IntelliJ IDEA.
 * User: andrew
 * Date: Oct 15, 2010
 * Time: 3:09:23 PM
 * To change this template use File | Settings | File Templates.
 */

public class SpaceObjectListModel extends AbstractListModel {

    private Vector<SpaceObject> items;

    public SpaceObjectListModel(){
        items = new Vector<SpaceObject>();
    }

    public int getSize() {
        return items.size();
    }

    public Vector<SpaceObject> getObjects() {
        return items;
    }

    public void setObjects(Vector<SpaceObject> items) {
        this.items = items;
    }

    public Object getElementAt(int i) {
        return items.get(i);
    }

    public void addElement(Object o) {
        int index = items.size();
        items.add((SpaceObject)o);

        fireIntervalAdded(this, index, index);
    }

    public void insertElementAt(Object o, int index) {
        items.insertElementAt((SpaceObject)o, index);

        fireIntervalAdded(this, index, index);
    }


    public void removeElementAt(int i) {
        items.removeElementAt(i);

        fireIntervalRemoved(this, i, i);
    }

    public void removeAllElements() {
        int index = items.size() - 1;

        if (index >= 0){
            items.removeAllElements();

            fireIntervalRemoved(this, 0, index);
        }
    }

}
