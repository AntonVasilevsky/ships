package com.example.pier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootApplication
public class PierApplication {


    public static void main(String[] args) throws InterruptedException {
        Lock mediterraneanLock = new ReentrantLock();
        Lock tunnelLock = new ReentrantLock();
        Lock redSeaLock = new ReentrantLock();
        Queue<Ship> mediterranean = new ArrayDeque<>();
        Queue<Ship> redSea = new ArrayDeque<>();
        ShipGenerator shipGenerator = new ShipGenerator();
        BlockingQueue<Ship> tunnel = new ArrayBlockingQueue<>(5);
        Pier pier = new Pier();


        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.submit(() -> {
            try {
                mediterraneanLock.lock();
                for (int i = 0; i < 100; i++) {
                    mediterranean.offer(shipGenerator.generateShip());
                }
            } finally {
                mediterraneanLock.unlock();
            }
            while (true) {
                try {
                    Thread.sleep(4000);
                    mediterraneanLock.lock();
                    mediterranean.offer(shipGenerator.generateShip());
                  //  System.out.println("A ship comes from mediterranean side");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    mediterraneanLock.unlock();
                }
            }
        });
        executorService.submit(() -> {
            // Loader
            while (true) {
                try {
                    Thread.sleep(2000);
                    mediterraneanLock.lock();
                    tunnelLock.lock();
                    if (tunnel.size() > 4) {
                        mediterraneanLock.unlock();
                        tunnelLock.unlock();
                        continue;
                    }
                    if (!mediterranean.isEmpty()) {
                        tunnel.offer(mediterranean.poll());
                      //  System.out.println("Ship enters the tunnel. Tunnel Size: " + tunnel.size());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mediterraneanLock.unlock();
                    tunnelLock.unlock();
                }
            }
        });
        executorService.submit(() -> {
            // Unloader
            while (true) {
                try {
                    Thread.sleep(5000);
                    tunnelLock.lock();
                    redSeaLock.lock();
                    if (!tunnel.isEmpty()) {
                        redSea.offer(tunnel.poll());
                        System.out.println("Ship is in the red sea now. Red sea size: " + redSea.size());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    tunnelLock.unlock();
                    redSeaLock.unlock();
                    System.out.println("Red sea lock released");
                }
            }
        });
        executorService.submit(() -> {
            // pier
            while (true) {
                try {
                    redSeaLock.lock();
                    System.out.println("pier takes the lock");
                    Ship ship = pier.loadShip(Objects.requireNonNull(redSea.poll()));
                    System.out.println("The ship is unloaded and returns to the sea");
                    redSea.offer(ship);

                } finally {
                    redSeaLock.unlock();
                    System.out.println("pier releases the lock");
                }
            }
        });

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);


    }

}

