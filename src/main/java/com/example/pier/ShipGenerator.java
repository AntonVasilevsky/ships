package com.example.pier;

import java.util.Random;

public class ShipGenerator {
    public Ship generateShip() {
        int capacity;
        Random random = new Random();
        int randomNumber = random.nextInt(4);
        if(randomNumber < 3)
            capacity = 10;
        else if (randomNumber > 3 && randomNumber < 6)
            capacity = 50;
        else capacity = 100;

        return new Ship(capacity);
    }
}
