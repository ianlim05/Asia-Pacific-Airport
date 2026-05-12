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
    private final Semaphore groundSlots = new Semaphore(3,false);
            
    // ATC Role: Grants landing clearance and assigns a specific gate
    public int atcRequestLanding(String planeName, boolean isEmergency) throws InterruptedException {
        if (isEmergency) {
            synchronized(this) {
                emergencyWaiting++;
                System.out.println("[ATC] !! EMERGENCY DECLARED by " + planeName + " (Priority Level: High) !!");
            }
        }
        
        // acquire 1 of 3 ground slots
        groundSlots.acquire();
        
        synchronized (this) {
            // Normal planes must wait if an emergency plane is currently in the queue
            while (runwayBusy || (!isEmergency && emergencyWaiting > 0)) {
                if (!isEmergency && emergencyWaiting > 0) {
                    System.out.println("[ATC] " + planeName + " is giving way to the Emergency Plane.");
                }
                wait();
            }

            if (isEmergency) {
                emergencyWaiting--;
            }

            runwayBusy = true;

            for (int i = 0; i < gates.length; i++) {
                if (gates[i]) {
                    gates[i] = false;
                    System.out.println("[ATC] " + planeName + " cleared for Runway 1. Taxi to GATE " + (i + 1));
                    return i;
                }
            }
        }
        return -1; 
    }

    public synchronized void atcClearRunway(String planeName) {
        runwayBusy = false;
        System.out.println("[ATC] Runway 1 is now VACANT (Cleared by " + planeName + ")");
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

    public synchronized void atcConfirmDeparture(int gateId) {
        gates[gateId] = true;
        groundSlots.release();
        runwayBusy = false;
        System.out.println("[ATC] Gate " + (gateId + 1) + " is now free.");
        notifyAll();
    }

    public synchronized boolean validateAllGatesEmpty() {
        for (boolean g : gates) if (!g) return false;
        return true;
    }
}
