package back;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;


public class TSBArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, Serializable
{

    private Object[] items;


    private int initial_capacity;


    private int count;


    public TSBArrayList()
    {
        this(10);
    }


    public TSBArrayList(Collection<? extends E> c)
    {
        this.items = c.toArray();
        initial_capacity = c.size();
        count = c.size();
    }


    public TSBArrayList(int initialCapacity)
    {
        if (initialCapacity <= 0)
        {
            initialCapacity = 10;
        }
        items = new Object[initialCapacity];
        initial_capacity = initialCapacity;
        count = 0;
    }


    @Override
    public void add(int index, E e)
    {
        if(index > count || index < 0)
        {
            throw new IndexOutOfBoundsException("add(): índice fuera de rango...");
        }

        if(e == null) return;

        if(count == items.length) this.ensureCapacity(items.length * 2);

        int t = count - index;
        System.arraycopy(items, index, items, index+1, t);
        items[index] = e;
        count++;


        this.modCount++;
    }


    @Override
    public void clear()
    {
        items = new Object[initial_capacity];
        count = 0;


        this.modCount = 0;
    }


    @Override
    public Object clone() throws CloneNotSupportedException
    {
        TSBArrayList<?> temp = (TSBArrayList<?>) super.clone();
        temp.items = new Object[count];
        System.arraycopy(this.items, 0, temp.items, 0, count);


        temp.modCount = 0;

        return temp;
    }


    @Override
    public boolean contains(Object e)
    {
        if(e == null) return false;

        for(int i=0; i<count; i++)
        {
            if(e.equals(items[i])) return true;
        }
        return false;
    }


    public void ensureCapacity(int minCapacity)
    {
        if(minCapacity == items.length) return;
        if(minCapacity < count) return;

        Object[] temp = new Object[minCapacity];
        System.arraycopy(items, 0, temp, 0, count);
        items = temp;
    }


    @Override
    public E get(int index)
    {
        if (index < 0 || index >= count)
        {
            throw new IndexOutOfBoundsException("get(): índice fuera de rango...");
        }
        return (E) items[index];
    }

    /**
     * Devuelve true si la lista no contiene elementos.
     * @return true si la lista está vacía.
     */
    @Override
    public boolean isEmpty()
    {
        return (count == 0);
    }


    @Override
    public E remove(int index)
    {
        if(index >= count || index < 0)
        {
            throw new IndexOutOfBoundsException("remove(): índice fuera de rango...");
        }

        int t = items.length;
        if(count < t/2) this.ensureCapacity(t/2);

        Object old = items[index];
        int n = count;
        System.arraycopy(items, index+1, items, index, n-index-1);
        count--;
        items[count] = null;

        // detección rápida de fallas en el iterador (fail-fast iterator)...
        // modCount se hereda desde AbstractList y es protected...
        this.modCount++;

        return (E) old;
    }


    @Override
    public E set(int index, E element)
    {
        if (index < 0 || index >= count)
        {
            throw new IndexOutOfBoundsException("set(): índice fuera de rango...");
        }
        Object old = items[index];
        items[index] = element;
        return (E) old;
    }


    @Override
    public int size()
    {
        return count;
    }

    @Override
    public String toString()
    {
        StringBuilder buff = new StringBuilder();
        buff.append('{');
        for (int i=0; i<count; i++)
        {
            buff.append(items[i]);
            if(i < count-1)
            {
                buff.append(", ");
            }
        }
        buff.append('}');
        return buff.toString();
    }


    public void trimToSize()
    {
        if(count == items.length) return;

        Object temp[] = new Object[count];
        System.arraycopy(items, 0, temp, 0, count);
        items = temp;
    }
}
