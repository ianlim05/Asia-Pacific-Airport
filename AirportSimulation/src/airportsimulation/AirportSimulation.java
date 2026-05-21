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
    public static void main(String[] args) throws InterruptedException {
        AirportControl atc = new AirportControl();
        RefuelTruck truck = new RefuelTruck();
        Statistics stats = new Statistics();
        List<Plane> fleet = new ArrayList<>();

        System.out.println("=== ASIA PACIFIC AIRPORT MANAGEMENT SYSTEM STARTING ===\n");
        truck.start();

        // STEP 1: Plane-1 and Plane-2 occupy Gate 1 and Gate 2
        for (int i = 1; i <= 2; i++) {
            Plane p = new Plane("Plane-" + i, atc, truck, stats, false);
            fleet.add(p);
            p.start();
            Thread.sleep(800); // Give them time to land and park
        }

        // STEP 2: Plane-3 and Plane-4 arrive and are blocked by the semaphore (2 permits are taken)
        for (int i = 3; i <= 4; i++) {
            Plane p = new Plane("Plane-" + i, atc, truck, stats, false);
            fleet.add(p);
            p.start();
        }

        // STEP 3: Plane-5-EMG arrives, acquire the 3rd permit, and takes Gate 3
        Thread.sleep(600);
        Plane emergency = new Plane("Plane-5-EMG", atc, truck, stats, true);
        fleet.add(emergency);
        emergency.start();

        // STEP 4: Plane-6 arrives after
        Thread.sleep(2000);
        Plane p6 = new Plane("Plane-6", atc, truck, stats, false);
        fleet.add(p6);
        p6.start();

        // STEP 5: Cleanup
        for (Plane p : fleet) {
            p.join();
        }

        stats.printReport();
        System.out.println("Gate Empty Validation: " + (atc.validateAllGatesEmpty() ? "SUCCESS (All Gates Vacant)" : "FAILED"));
    }

}
