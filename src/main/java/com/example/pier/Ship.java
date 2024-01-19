package com.example.pier;

public class Ship {
    private int capacity;
    public Ship (int capacity) {
        this.capacity = capacity;
    }
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass();
    }

    public int getCapacity() {
        return capacity;
    }
}
