package back;

import java.io.Serializable;
import java.util.*;

public class TSBHashTableDA<K,V> implements Map<K,V>, Cloneable, Serializable {

    private final static int MAX_SIZE = Integer.MAX_VALUE;

    private Object table[];

    private int initial_capacity;

    private int count;

    private float load_factor;

    private transient Set<K> keySet = null;
    private transient Set<Map.Entry<K,V>> entrySet = null;
    private transient Collection<V> values = null;

    protected transient int modCount;



    public TSBHashTableDA()
    {
        this(5, 0.5f);
    }

    public TSBHashTableDA(int initial_capacity, float load_factor)
    {
        if(load_factor <= 0) { load_factor = 0.5f; }
        if(initial_capacity <= 0) { initial_capacity = 11; }
        else
        {
            if(initial_capacity > TSBHashTableDA.MAX_SIZE)
            {
                initial_capacity = TSBHashTableDA.MAX_SIZE;
            }
        }

        this.table = new Object[initial_capacity];;
        for(int i=0; i<table.length; i++)
        {
            table[i] = new Entry<>();
        }

        this.initial_capacity = initial_capacity;
        this.load_factor = load_factor;
        this.count = 0;
        this.modCount = 0;
    }

    @Override
    public int size() {
        return this.count;
    }

    @Override
    public boolean isEmpty() {
        return (this.count == 0);
    }

    @Override
    public boolean containsKey(Object key) {
        return (this.get((K)key) != null);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.contains(value);
    }

    private boolean contains(Object value) {
        if(value == null) return false;

        for (int i = 0; i < this.table.length; i++)
        {
            Entry<K, V> entry = (Entry<K, V>) this.table[i];
            if(value.equals(entry.getValue()) && entry.estado==2) return true;
        }
        return false;
    }

    @Override
    public V get(Object key) {
        if(key == null) throw new NullPointerException("get(): parámetro null");

        int ib = this.h(key.hashCode());
        int cont = 0;
        while(cont < this.table.length){
            Entry<K, V> x = (Entry<K, V>) this.table[ib];
            if (x.estado == 0){
                return null;
            }
            else{
                if (key.equals(x.getKey())){
                    if (x.estado==1){
                        return null;
                    }
                    else{
                        return x.getValue();
                    }
                }
            }
            ib++;
            if (ib==this.table.length) ib=0;
            cont++;
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        if(key == null || value == null) throw new NullPointerException("put(): parámetro null");

        int ib = this.h(key);
        boolean i = false;
        V old = null;
        int cont = 0;

        do {
            Entry<K, V> x = (Entry<K, V>) this.table[ib];
            if (key.equals(x.getKey())){
                old = x.getValue();
                x.setValue(value);
                break;
            }
            ib++;
            if (ib == this.table.length) ib = 0;
            cont++;
        }while (cont < this.table.length);

        if (old == null){
            if(this.averageLength() >= this.load_factor) this.rehash();
            ib = this.h(key);
            Entry<K, V> entry = new Entry<>(key, value);
            this.table[ib] = entry;
            this.count++;
            this.modCount++;
        }
        return old;
    }

    @Override
    public V remove(Object key) {
        if(key == null) throw new NullPointerException("remove(): parámetro null");
        int ib = this.h(key.hashCode());
        int cont = 0;
        do {
            Entry<K, V> x = (Entry<K, V>) this.table[ib];
            if (key.equals(x.getKey())){
                x.setEstado(1);
                this.count--;
                this.modCount++;
                return x.getValue();
            }
            ib++;
            if (ib==this.table.length)ib=0;
            cont++;
        }while (cont< this.table.length);

        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for(Map.Entry<? extends K, ? extends V> e : m.entrySet())
        {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void clear() {
        this.table = new Object[this.initial_capacity];
        for(int i = 0; i < this.table.length; i++)
        {
            this.table[i] = new Entry<>();
        }
        this.count = 0;
        this.modCount++;
    }

    @Override
    public Set<K> keySet() {
        if(keySet == null)
        {
            // keySet = Collections.synchronizedSet(new KeySet());
            keySet = new KeySet();
        }
        return keySet;
    }

    @Override
    public Collection<V> values() {
        if(values==null)
        {
            // values = Collections.synchronizedCollection(new ValueCollection());
            values = new ValueCollection();
        }
        return values;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        if(entrySet == null)
        {
            // entrySet = Collections.synchronizedSet(new EntrySet());
            entrySet = new EntrySet();
        }
        return entrySet;
    }

    public Object clone() throws CloneNotSupportedException
    {
        TSBHashTableDA<K, V> t = (TSBHashTableDA<K, V>)super.clone();
        t.table = new Object[table.length];
        for (int i = 0; i < table.length; i++) {
            t.table[i] = (Entry<K, V>) table[i];
        }
        t.keySet = null;
        t.entrySet = null;
        t.values = null;
        t.modCount = 0;
        return t;
    }

    //************************ HASH.

    private int h(int k)
    {
        return h(k, this.table.length);
    }

    private int h(K key) {
        return h(key.hashCode(), this.table.length);
    }

    private int h(K key, int t)
    {
        return h(key.hashCode(), t);
    }

    private int h(int k, int t)
    {
        if(k < 0) k *= -1;
        return k % t;
    }

    private float averageLength()
    {
        return (float) count / this.table.length;
    }

    protected void rehash() {
        int old_length = this.table.length;

        int new_length = old_length * 2 + 1;

        if (new_length > TSBHashTableDA.MAX_SIZE) {
            new_length = TSBHashTableDA.MAX_SIZE;
        }

        Object temp[] = new Object[new_length];
        for (int j = 0; j < temp.length; j++) {
            temp[j] = new Entry<>();
        }

        this.modCount++;

        for (int i = 0; i < this.table.length; i++) {
            Entry<K, V> x = (Entry<K, V>) this.table[i];

            K key = x.getKey();
            if (key != null) {
                int y = this.h(key, temp.length);
                Entry<K, V> h = (Entry<K, V>) temp[y];
                while (h.estado != 0 && h.estado != 1) {
                    y++;
                    h = (Entry<K, V>) temp[y];
                }
                temp[y] = x;
            }
        }
        this.table = temp;
    }

    private class Entry<K, V> implements Map.Entry<K, V>
    {
        private K key;
        private V value;
        private int estado;

        public Entry(K key, V value)
        {
            if(key == null || value == null)
            {
                throw new IllegalArgumentException("Entry(): parámetro null...");
            }
            this.key = key;
            this.value = value;
            this.estado = 2;
        }

        public Entry() {
            this.estado=0;
        }

        @Override
        public K getKey()
        {
            return key;
        }

        @Override
        public V getValue()
        {
            return value;
        }

        @Override
        public V setValue(V value)
        {
            if(value == null)
            {
                throw new IllegalArgumentException("setValue(): parámetro null...");
            }

            V old = this.value;
            this.value = value;
            return old;
        }

        public int getEstado()
        {
            return estado;
        }

        public void setEstado(int value)
        {
            if(value == 1 || value == 0 || value == 2)
            {
                this.estado = value;
            }
            else{
                throw new IllegalArgumentException("setValue(): parámetro null...");
            }
        }

        @Override
        public int hashCode()
        {
            int hash = 7;
            hash = 61 * hash + Objects.hashCode(this.key);
            hash = 61 * hash + Objects.hashCode(this.value);
            return hash;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj) { return true; }
            if (obj == null) { return false; }
            if (this.getClass() != obj.getClass()) { return false; }

            final Entry other = (Entry) obj;
            if (!Objects.equals(this.key, other.key)) { return false; }
            if (!Objects.equals(this.value, other.value)) { return false; }
            return true;
        }

        @Override
        public String toString()
        {
            return "(" + key.toString() + ", " + value.toString() + ")";
        }
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        for (int i = 0; i < this.table.length; i++) {

            Entry<K, V> entry = (Entry<K, V>) this.table[i];
            if (entry.estado == 2) {
                hash = 61 * hash + Objects.hashCode(entry.key);
                hash = 61 * hash + Objects.hashCode(entry.value);

            }
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) { return true; }
        if (obj == null) { return false; }
        if (this.getClass() != obj.getClass()) { return false; }

        for (int i = 0; i < this.table.length; i++) {

            Entry<K, V> entry = (Entry<K, V>) this.table[i];
            Entry<K, V> entry2 = (Entry<K, V>) ((TSBHashTableDA<?, ?>) obj).table[i];
            if (!Objects.equals(entry.key, entry2.key)||entry.estado!=entry2.estado) {
                return false;
            }
            if (!Objects.equals(entry.value, entry2.value)) {
                return false;
            }
        }
        return true;
    }


    private class KeySet extends AbstractSet<K>
    {

        @Override
        public Iterator<K> iterator()
        {
            return new KeySetIterator();
        }

        @Override
        public int size()
        {
            return TSBHashTableDA.this.count;
        }

        @Override
        public boolean contains(Object o)
        {
            return TSBHashTableDA.this.containsKey(o);
        }

        @Override
        public boolean remove(Object o)
        {
            return (TSBHashTableDA.this.remove(o) != null);
        }

        @Override
        public void clear()
        {
            TSBHashTableDA.this.clear();
        }

        private class KeySetIterator implements Iterator<K> {
            private int current_bucket;

            private int last_bucket;

            private boolean next_ok;

            private int expected_modCount;

            public KeySetIterator() {
                current_bucket = 0;
                last_bucket = 0;
                next_ok = false;
                expected_modCount = TSBHashTableDA.this.modCount;
            }

            @Override
            public boolean hasNext() {
                Object t[] = TSBHashTableDA.this.table;

                if (TSBHashTableDA.this.isEmpty()) {
                    return false;
                }
                if (current_bucket >= t.length) {
                    return false;
                }

                Entry<K, V> bucket = (Entry<K, V>) t[current_bucket];
                if ( bucket.estado == 2) {
                    int next_bucket = current_bucket + 1;
                    bucket = (Entry<K, V>) t[next_bucket];
                    while (next_bucket < t.length-1 && (bucket.estado == 0 || bucket.estado == 1)) {
                        next_bucket++;
                        bucket = (Entry<K, V>) t[next_bucket];
                    }
                    if (next_bucket == t.length-1) {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public K next() {
                if (TSBHashTableDA.this.modCount != expected_modCount) {
                    throw new ConcurrentModificationException("next(): modificación inesperada de tabla...");
                }

                if (!hasNext()) {
                    throw new NoSuchElementException("next(): no existe el elemento pedido...");
                }

                Object t[] = TSBHashTableDA.this.table;

                Entry<K, V> bucket = (Entry<K, V>) t[current_bucket];
                int next = current_bucket;
                if (bucket.estado == 0 || bucket.estado == 1) {
                    last_bucket = current_bucket;
                    next++;
                    bucket = (Entry<K, V>) t[next];
                    while (bucket.estado == 0 || bucket.estado == 1) {
                        next++;
                        bucket = (Entry<K, V>) t[next];
                    }
                }

                if (next==current_bucket) {
                    next++;
                    bucket = (Entry<K, V>) t[next];
                    while (bucket.estado == 0 || bucket.estado == 1) {
                        next++;
                        bucket = (Entry<K, V>) t[next];
                    }
                }

                next_ok = true;

                current_bucket = next;
                bucket = (Entry<K, V>) t[current_bucket];
                K key = bucket.getKey();
                return key;
            }

            @Override
            public void remove() {
                if (!next_ok) {
                    throw new IllegalStateException("remove(): debe invocar a next() antes de remove()...");
                }

                TSBHashTableDA.this.table[current_bucket] = new Entry<>();

                if (last_bucket != current_bucket) {
                    current_bucket = last_bucket;
                }

                next_ok = false;

                TSBHashTableDA.this.count--;

                TSBHashTableDA.this.modCount++;
                expected_modCount++;
            }
        }
    }

    private class EntrySet extends AbstractSet<Map.Entry<K, V>>
    {

        @Override
        public Iterator<Map.Entry<K, V>> iterator()
        {
            return new EntrySetIterator();
        }

        @Override
        public boolean contains(Object o)
        {
            if(o == null) { return false; }
            if(!(o instanceof Entry)) { return false; }

            Entry<K, V> entry = (Entry<K,V>)o;
            K key = entry.getKey();
            Object t[] = TSBHashTableDA.this.table;

            for (int i = 0; i < t.length; i++) {
                Entry<K, V> x = (Entry<K,V>)t[i];
                if (key.equals(x.getKey())) {
                    if (x.estado == 1) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public boolean remove(Object o)
        {
            if(o == null) { throw new NullPointerException("remove(): parámetro null");}
            if(!(o instanceof Entry)) { return false; }

            Entry<K, V> entry = (Entry<K, V>) o;
            K key = entry.getKey();
            Object t[] = TSBHashTableDA.this.table;
            for (int i = 0; i < t.length; i++) {
                Entry<K, V> x = (Entry<K,V>)t[i];
                if (key.equals(x.getKey())) {
                    x.setEstado(1);
                    TSBHashTableDA.this.count--;
                    TSBHashTableDA.this.modCount++;
                    return true;
                }
            }
            return false;
        }

        @Override
        public int size()
        {
            return TSBHashTableDA.this.count;
        }

        @Override
        public void clear()
        {
            TSBHashTableDA.this.clear();
        }

        private class EntrySetIterator implements Iterator<Map.Entry<K, V>> {
            private int current_bucket;

            private int last_bucket;

            private boolean next_ok;

            private int expected_modCount;

            public EntrySetIterator() {
                current_bucket = 0;
                last_bucket = 0;
                next_ok = false;
                expected_modCount = TSBHashTableDA.this.modCount;
            }

            public boolean hasNext() {
                Object t[] = TSBHashTableDA.this.table;

                if (TSBHashTableDA.this.isEmpty()) {
                    return false;
                }
                if (current_bucket >= t.length) {
                    return false;
                }

                Entry<K, V> bucket = (Entry<K, V>) t[current_bucket];
                if ( bucket.estado == 2) {
                    int next_bucket = current_bucket + 1;
                    bucket = (Entry<K, V>) t[next_bucket];
                    while (next_bucket < t.length-1 && (bucket.estado == 0 || bucket.estado == 1)) {
                        next_bucket++;
                        bucket = (Entry<K, V>) t[next_bucket];
                    }
                    if (next_bucket == t.length-1) {
                        return false;
                    }
                }

                return true;
            }

            @Override
            public Entry<K, V> next() {
                if (TSBHashTableDA.this.modCount != expected_modCount) {
                    throw new ConcurrentModificationException("next(): modificación inesperada de tabla...");
                }

                if (!hasNext()) {
                    throw new NoSuchElementException("next(): no existe el elemento pedido...");
                }

                Object t[] = TSBHashTableDA.this.table;

                Entry<K, V> bucket = (Entry<K, V>) t[current_bucket];
                int next = current_bucket;
                if (bucket.estado == 0 || bucket.estado == 1) {
                    last_bucket = current_bucket;

                    next++;
                    bucket = (Entry<K, V>) t[next];
                    while (bucket.estado == 0 || bucket.estado == 1) {
                        next++;
                        bucket = (Entry<K, V>) t[next];
                    }
                }

                if (next==current_bucket) {
                    next++;
                    bucket = (Entry<K, V>) t[next];
                    while (bucket.estado == 0 || bucket.estado == 1) {
                        next++;
                        bucket = (Entry<K, V>) t[next];
                    }
                }

                next_ok = true;

                current_bucket = next;
                bucket = (Entry<K, V>) t[current_bucket];
                Entry key = bucket;
                return key;
            }

            @Override
            public void remove() {
                if (!next_ok) {
                    throw new IllegalStateException("remove(): debe invocar a next() antes de remove()...");
                }

                Entry<K, V> garbage = (Entry<K, V>) TSBHashTableDA.this.table[current_bucket];
                TSBHashTableDA.this.table[current_bucket] = new Entry<>();

                if (last_bucket != current_bucket) {
                    current_bucket = last_bucket;
                }

                next_ok = false;

                TSBHashTableDA.this.count--;

                TSBHashTableDA.this.modCount++;
                expected_modCount++;
            }
        }
    }

    private class ValueCollection extends AbstractCollection<V>
    {

        @Override
        public Iterator<V> iterator()
        {
            return new ValueCollectionIterator();
        }

        @Override
        public int size()
        {
            return TSBHashTableDA.this.count;
        }

        @Override
        public boolean contains(Object o)
        {
            return TSBHashTableDA.this.containsValue(o);
        }

        @Override
        public void clear()
        {
            TSBHashTableDA.this.clear();
        }

        private class ValueCollectionIterator implements Iterator<V>
        {
            private int current_bucket;

            private int last_bucket;

            private boolean next_ok;

            private int expected_modCount;

            public ValueCollectionIterator()
            {
                current_bucket = 0;
                last_bucket = 0;
                next_ok = false;
                expected_modCount = TSBHashTableDA.this.modCount;
            }
            public boolean hasNext() {
                Object t[] = TSBHashTableDA.this.table;

                if (TSBHashTableDA.this.isEmpty()) {
                    return false;
                }
                if (current_bucket >= t.length) {
                    return false;
                }

                Entry<K, V> bucket = (Entry<K, V>) t[current_bucket];
                if ( bucket.estado == 2) {
                    int next_bucket = current_bucket + 1;
                    bucket = (Entry<K, V>) t[next_bucket];
                    while (next_bucket < t.length-1 && (bucket.estado == 0 || bucket.estado == 1)) {
                        next_bucket++;
                        bucket = (Entry<K, V>) t[next_bucket];
                    }
                    if (next_bucket == t.length-1) {
                        return false;
                    }
                }

                return true;
            }

            @Override
            public V next() {
                if (TSBHashTableDA.this.modCount != expected_modCount) {
                    throw new ConcurrentModificationException("next(): modificación inesperada de tabla en uso...");
                }

                if (!hasNext()) {
                    throw new NoSuchElementException("next(): no existe el elemento solicitado...");
                }

                Object t[] = TSBHashTableDA.this.table;

                Entry<K, V> bucket = (Entry<K, V>) t[current_bucket];
                int next = current_bucket;
                if (bucket.estado == 0 || bucket.estado == 1) {
                    last_bucket = current_bucket;

                    next++;
                    bucket = (Entry<K, V>) t[next];
                    while (bucket.estado == 0 || bucket.estado == 1) {
                        next++;
                        bucket = (Entry<K, V>) t[next];
                    }
                }

                if (next==current_bucket) {
                    next++;
                    bucket = (Entry<K, V>) t[next];
                    while (bucket.estado == 0 || bucket.estado == 1) {
                        next++;
                        bucket = (Entry<K, V>) t[next];
                    }
                }

                next_ok = true;

                current_bucket = next;
                bucket = (Entry<K, V>) t[current_bucket];
                V key = bucket.getValue();
                return key;
            }

            @Override
            public void remove() {
                if (!next_ok) {
                    throw new IllegalStateException("remove(): debe invocar a next() antes de remove()...");
                }

                TSBHashTableDA.this.table[current_bucket] = new Entry<>();

                if (last_bucket != current_bucket) {
                    current_bucket = last_bucket;
                }

                next_ok = false;

                TSBHashTableDA.this.count--;

                TSBHashTableDA.this.modCount++;
                expected_modCount++;
            }
        }
    }
}