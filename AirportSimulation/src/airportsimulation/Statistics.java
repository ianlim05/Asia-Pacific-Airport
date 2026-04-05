/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airportsimulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author USER
 */
class Statistics {
    private int totalPlanes = 0;
    private int totalPassengers = 0;
    private List<Long> waitTimes = new ArrayList<>();

    public synchronized void recordPlane(long waitTime, int passengers) {
        totalPlanes++;
        totalPassengers += passengers;
        waitTimes.add(waitTime);
    }

    public void printReport() {
        System.out.println("\n--- FINAL AIRPORT STATISTICS ---");
        System.out.println("Total Planes Served: " + totalPlanes);
        System.out.println("Total Passengers Boarded: " + totalPassengers);
        if (!waitTimes.isEmpty()) {
            long max = Collections.max(waitTimes);
            long min = Collections.min(waitTimes);
            double avg = waitTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
            System.out.println("Max Waiting Time: " + max + "ms");
            System.out.println("Min Waiting Time: " + min + "ms");
            System.out.printf("Average Waiting Time: %.2fms\n", avg);
        }
    }
}
