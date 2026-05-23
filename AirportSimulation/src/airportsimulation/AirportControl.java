/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airportsimulation;

import java.util.concurrent.Semaphore;

/**
 *
 * @author USER
 */
public class AirportControl {
    private final boolean[] gates = {true, true, true}; 
    private boolean runwayBusy = false;
    private int emergencyWaiting = 0;
    private final Semaphore groundSlots = new Semaphore(2,false);
            
    // ATC Role: Grants landing clearance and assigns a specific gate
    public int atcRequestLanding(String planeName, boolean isEmergency) throws InterruptedException {
        if (isEmergency) {
            synchronized (this) {
                emergencyWaiting++;
                System.out.println("[ATC] !! EMERGENCY DECLARED by " + planeName + " (Priority Level: High) !!");
                notifyAll();
            }
            // Give waiting planes time to react
            Thread.sleep(500);
        } else {
            boolean yielded = false;
            // Poll until permit available, give way if emergency exists
            while (!groundSlots.tryAcquire()) {
                synchronized (this) {
                    if (emergencyWaiting > 0 && !yielded) {
                        System.out.println("[ATC] " + planeName + " is giving way to the Emergency Plane.");
                        yielded = true;
                    }
                }
                Thread.sleep(500);
            }
            // Secondary check after acquiring permit
            synchronized (this) {
                while (emergencyWaiting > 0) {
                    wait();
                }
            }
        }

        // Land on runway
        synchronized (this) {
            while (runwayBusy) {
                wait();
            }
            runwayBusy = true;
            for (int i = 0; i < gates.length; i++) {
                if (gates[i]) {
                    gates[i] = false;
                    System.out.println("[ATC] " + planeName + " cleared for the runway. Taxi to GATE " + (i + 1));
                    return i;
                }
            }
        }
        return -1;
    }

    public synchronized void atcClearRunway(String planeName) {
        runwayBusy = false;
        if (planeName.contains("EMG")) {
            emergencyWaiting--;
        }
        System.out.println("[ATC] The runway is now VACANT (Cleared by " + planeName + ")");
        notifyAll();
    }

    public synchronized void atcRequestTakeoff(String planeName, int gateId) throws InterruptedException {
        while (runwayBusy) {
            System.out.println("[ATC] " + planeName + " hold position at Gate " + (gateId + 1) + ". Runway busy.");
            wait();
        }
        runwayBusy = true;
        System.out.println("[ATC] " + planeName + " cleared for Takeoff from Gate " + (gateId + 1));
    }

    public synchronized void atcConfirmDeparture(int gateId, boolean isEmergency) {
        gates[gateId] = true;
        if (!isEmergency) {
            groundSlots.release(); // only release for normal planes
        }
        runwayBusy = false;
        System.out.println("[ATC] Gate " + (gateId + 1) + " is now free.");
        notifyAll();
    }

    public synchronized boolean validateAllGatesEmpty() {
        for (boolean g : gates) {
            if (!g) {
                return false;
            }
        }
        return true;
    }
}
