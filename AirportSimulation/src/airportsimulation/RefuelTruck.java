/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airportsimulation;

/**
 *
 * @author USER
 */
class RefuelTruck {
    private boolean isAvailable = true;

    public synchronized void useTruck(String planeName) throws InterruptedException {
        while (!isAvailable) {
            System.out.println("[" + planeName + "] Waiting for refueling truck...");
            wait();
        }
        isAvailable = false;
        System.out.println("[" + planeName + "] Refueling started.");
        Thread.sleep(1500); // Simulate refueling time
        isAvailable = true;
        System.out.println("[" + planeName + "] Refueling finished. Truck released.");
        notifyAll();
    }
}
