package com.example.pier;

import lombok.Getter;

@Getter
public class Ship {
    private final int capacity;
    private final String type;
    public Ship (int capacity, String type) {
        this.capacity = capacity;
        this.type = type;
    }
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.getClass() == obj.getClass();
    }

}
