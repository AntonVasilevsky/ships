package com.example.pier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ShipGeneratorTest {
    @Test
    void generateShipTest_generates_ship() {
        //Arrange
        ShipGenerator shipGenerator = new ShipGenerator();
        Ship arrangedShip = new Ship(10);

        //Act
        Ship shipGenerated = shipGenerator.generateShip();

        //Assert
        Assertions.assertEquals(arrangedShip, shipGenerated);
    }

}
