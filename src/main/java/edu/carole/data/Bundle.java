package edu.carole.data;

public class Bundle<E> {

    E[] data;
    public Bundle(E... data) {
        this.data = data;
    }

    public E[] getData() {
        return data;
    }
}
