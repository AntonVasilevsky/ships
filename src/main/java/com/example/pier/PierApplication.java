package com.example.pier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootApplication
public class PierApplication {


    public static void main(String[] args) throws InterruptedException {
        Lock mediterraneanLock = new ReentrantLock();
        Lock tunnelLock = new ReentrantLock();
        ReentrantLock redSeaLock = new ReentrantLock();
        Queue<Ship> mediterranean = new ArrayDeque<>();
        Queue<Ship> redSea = new ArrayDeque<>();
        ShipGenerator shipGenerator = new ShipGenerator();
        BlockingQueue<Ship> tunnel = new ArrayBlockingQueue<>(5);
        Condition condition = redSeaLock.newCondition();


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
                    System.out.println("A ship comes from mediterranean side");
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
                        System.out.println("Ship enters the tunnel. Tunnel Size: " + tunnel.size());
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
                    Thread.sleep(1000);
                    tunnelLock.lock();
                    redSeaLock.lock();
                    redSea.offer(shipGenerator.generateShip());
                    if (!tunnel.isEmpty()) {
                        redSea.offer(tunnel.poll());
                        System.out.println("Ship is in the red sea now. Red sea size: " + redSea.size());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    tunnelLock.unlock();
                    redSeaLock.unlock();
                    boolean locked = redSeaLock.isLocked();
                    System.out.printf("Unloader in %s releases the lock is locked: %s\n", Thread.currentThread().getName(), locked);

                }
            }
        });
        executorService.submit(() -> {
            // pier
            Pier pier = new Pier();
            pier.setName("FRUITS PIER");
            while (true) {
                try {
                    Thread.sleep(2000);
                    redSeaLock.lock();
                    System.out.printf("pier %s takes the lock at \n", pier.getName());
                    Ship ship = null;
                    for (Ship s : redSea
                    ) {
                        if (s.getType().equals("fruits"))
                            ship = s;
                    }

                    if (ship != null) {
                        pier.loadShip(ship);
                        redSea.offer(ship);
                        System.out.printf("The TYPE: %s is unloaded and returns to the sea\n", ship.getType());
                    }

                } finally {
                    redSeaLock.unlock();
                    boolean locked = redSeaLock.isLocked();
                    System.out.printf("pier in %s releases the lock at %d, is locked: %s\n", pier.getName(), System.currentTimeMillis(), locked);
                }

            }
        });
        executorService.submit(() -> {
            // pier
            Pier pier = new Pier();
            pier.setName("TOYS PIER");
            while (true) {
                try {
                    Thread.sleep(2000);
                    redSeaLock.lock();
                    System.out.printf("pier %s takes the lock at \n", pier.getName());
                    Ship ship = null;
                    for (Ship s : redSea
                    ) {
                        if (s.getType().equals("toys"))
                            ship = s;
                    }

                    if (ship != null) {
                        pier.loadShip(ship);
                        redSea.offer(ship);
                        System.out.printf("The TYPE: %s is unloaded and returns to the sea\n", ship.getType());
                    }

                } finally {
                    redSeaLock.unlock();
                    boolean locked = redSeaLock.isLocked();
                    System.out.printf("pier in %s releases the lock at %d, is locked: %s\n", pier.getName(), System.currentTimeMillis(), locked);
                }

            }
        });
        executorService.submit(() -> {
            // pier
            Pier pier = new Pier();
            pier.setName("STONES PIER");
            while (true) {
                try {
                    Thread.sleep(2000);
                    redSeaLock.lock();
                    System.out.printf("pier %s takes the lock at \n", pier.getName());
                    Ship ship = null;
                    for (Ship s : redSea
                    ) {
                        if (s.getType().equals("stones"))
                            ship = s;
                    }

                    if (ship != null) {
                        pier.loadShip(ship);
                        redSea.offer(ship);
                        System.out.printf("The TYPE: %s is unloaded and returns to the sea\n", ship.getType());
                    }

                } finally {
                    redSeaLock.unlock();
                    boolean locked = redSeaLock.isLocked();
                    System.out.printf("pier in %s releases the lock at %d, is locked: %s\n", pier.getName(), System.currentTimeMillis(), locked);
                }

            }
        });

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.DAYS);


    }

}

