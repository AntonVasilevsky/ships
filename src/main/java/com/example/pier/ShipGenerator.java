package com.example.pier;

import java.util.Random;

public class ShipGenerator {
    public Ship generateShip() {
        int capacity;
        String type;
        Random random = new Random();
        int randomNumber = random.nextInt(9);
        if(randomNumber < 3)
            capacity = 10;
        else if (randomNumber > 3 && randomNumber < 6)
            capacity = 50;
        else capacity = 100;
        if(randomNumber < 3)
            type = "fruits";
        else if (randomNumber > 3 && randomNumber < 6)
            type = "toys";
        else type = "stones";

        return new Ship(capacity, type);
    }
}
