/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package airportsimulation;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class AirportSimulation {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException{
        AirportControl atc = new AirportControl();
        RefuelTruck truck = new RefuelTruck();
        Statistics stats = new Statistics();
        List<Plane> fleet = new ArrayList<>();

        System.out.println("=== ASIA PACIFIC AIRPORT MANAGEMENT SYSTEM STARTING ===\n");

        // STEP 1: Occupy all 3 slots (Congestion)
        for (int i = 1; i <= 3; i++) {
            Plane p = new Plane("Plane-" + i, atc, truck, stats, false);
            fleet.add(p);
            p.start();
            Thread.sleep(100); 
        }

        // STEP 2: Planes 5 & 6 arrive and must wait (Congestion verified)
        for (int i = 5; i <= 6; i++) {
            Plane p = new Plane("Plane-" + i, atc, truck, stats, false);
            fleet.add(p);
            p.start();
        }

        // STEP 3: The Emergency Plane arrives while others are waiting
        Thread.sleep(300);
        Plane emergency = new Plane("Plane-4-EMG", atc, truck, stats, true);
        fleet.add(emergency);
        emergency.start();

        // STEP 4: Proper cleanup
        for (Plane p : fleet) {
            p.join();
        }

        stats.printReport();
        System.out.println("Gate Empty Validation: " + (atc.validateAllGatesEmpty() ? "SUCCESS (All Gates Vacant)" : "FAILED"));
    }
    
}
