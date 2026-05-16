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
        truck.start();
        
        // STEP 1: Occupy all 3 ground slots/gates
        for (int i = 1; i <= 3; i++) {
            Plane p = new Plane("Plane-" + i, atc, truck, stats, false);
            fleet.add(p);
            p.start();
            Thread.sleep(500); 
        }

        // STEP 2: Start 2 planes that must wait in the air (Planes 4 and 5)
        for (int i = 4; i <= 5; i++) {
            Plane p = new Plane("Plane-" + i, atc, truck, stats, false);
            fleet.add(p);
            p.start();
            Thread.sleep(200); 
        }

        // STEP 3: Plane-6 arrives as the Emergency Plane
        Thread.sleep(500);
        Plane emergency = new Plane("Plane-6-EMG", atc, truck, stats, true);
        fleet.add(emergency);
        emergency.start();
        
        // STEP 4: Cleanup
        for (Plane p : fleet) {
            p.join();
        }

        stats.printReport();
        System.out.println("Gate Empty Validation: " + (atc.validateAllGatesEmpty() ? "SUCCESS (All Gates Vacant)" : "FAILED"));
    }
    
}
