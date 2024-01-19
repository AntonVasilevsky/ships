package com.example.pier;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
@Getter
@Setter
public class Pier {
    private final BlockingQueue<Ship> dock;
    private String name;

    public Pier() {
        dock = new ArrayBlockingQueue<>(1);
    }
    public Ship loadShip(Ship ship) {
        for (int i = 0; i < ship.getCapacity(); i+=10) {
            try {
                Thread.sleep(1000);
                System.out.println("LOADING " + i);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return ship;
    }

}
