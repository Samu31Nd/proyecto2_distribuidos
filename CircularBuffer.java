public class CircularBuffer<T> {
    private final int capacity;
    private final Object[] buffer;
    private int start = 0;
    private int size = 0;

    public CircularBuffer(int capacity){
        this.capacity = capacity;
        this.buffer = new Object[capacity];
    }

    public void add(T element){
        int end = (start + size) % capacity;
        buffer[end] = element;
        if (size == capacity)
            start = (start + 1) % capacity;
        else size++;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        if(index < 0 || index >= size)
            throw new IndexOutOfBoundsException();
        return (T) buffer[(start + index) % capacity];
    }

    public int size(){ return size; }

    @Override
    public String toString() {
        String s = "Elements: {";
        for(int i = 0; i < size; i++){
            s += get(i) + ", ";
        }
        s += "}";
        return s;
    }
}