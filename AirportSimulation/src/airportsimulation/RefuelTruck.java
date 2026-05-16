/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airportsimulation;

/**
 *
 * @author USER
 */
class RefuelTruck extends Thread {
    private boolean isAvailable = true;

    @Override
    public void run() {
        // Truck stays running throughout simulation
        System.out.println("[RefuelTruck] Refueling truck is ready and standing by.");
    }
    
    public synchronized void useTruck(String planeName) throws InterruptedException {
        while (!isAvailable) {
            System.out.println("[" + planeName + "] Waiting for refueling truck...");
            wait();
        }
        isAvailable = false;
        System.out.println("[" + planeName + "] Refueling started.");
        Thread.sleep(4000); // Simulate refueling time
        isAvailable = true;
        System.out.println("[" + planeName + "] Refueling finished. Truck released.");
        notifyAll();
    }
}
