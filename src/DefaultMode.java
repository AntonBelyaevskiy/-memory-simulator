import javax.swing.*;
import java.io.Serializable;
import java.util.Vector;


public class DefaultMode<E> extends AbstractListModel<E> implements MutableComboBoxModel<E>, Serializable {
    private Vector<E> objects;
    private Object selectedObject;


    DefaultMode(final E items[]) {
        objects = new Vector<>(items.length);

        int i,c;
        for ( i=0,c=items.length;i<c;i++ )
            objects.addElement(items[i]);

        if ( getSize() > 0 ) {
            selectedObject = getElementAt( 0 );
        }
    }


    public void setSelectedItem(Object anObject) {
        if ((selectedObject != null && !selectedObject.equals( anObject )) ||
                selectedObject == null && anObject != null) {
            selectedObject = anObject;
            fireContentsChanged(this, -1, -1);
        }
    }

    public Object getSelectedItem() {
        return selectedObject;
    }

    public int getSize() {
        return objects.size();
    }

    public E getElementAt(int index) {
        if ( index >= 0 && index < objects.size() )
            return objects.elementAt(index);
        else
            return null;
    }


    public void addElement(E anObject) {
        objects.addElement(anObject);
        fireIntervalAdded(this,objects.size()-1, objects.size()-1);
        if ( objects.size() == 1 && selectedObject == null && anObject != null ) {
            setSelectedItem( anObject );
        }
    }

    public void insertElementAt(E anObject,int index) {
        objects.insertElementAt(anObject,index);
        fireIntervalAdded(this, index, index);
    }

    public void removeElementAt(int index) {
        if ( getElementAt( index ) == selectedObject ) {
            if ( index == 0 ) {
                setSelectedItem( getSize() == 1 ? null : getElementAt( index + 1 ) );
            }
            else {
                setSelectedItem( getElementAt( index - 1 ) );
            }
        }

        objects.removeElementAt(index);

        fireIntervalRemoved(this, index, index);
    }

    public void removeElement(Object anObject) {
        int index = objects.indexOf(anObject);
        if ( index != -1 ) {
            removeElementAt(index);
        }
    }

}
