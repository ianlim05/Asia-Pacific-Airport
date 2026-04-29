/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package airportsimulation;

/**
 *
 * @author USER
 */
class Plane extends Thread{
    private final AirportControl atc;
    private final RefuelTruck truck;
    private final Statistics stats;
    private final boolean isEmergency;
    private final int passengers;

    public Plane(String name, AirportControl atc, RefuelTruck truck, Statistics stats, boolean isEmergency) {
        super(name);
        this.atc = atc;
        this.truck = truck;
        this.stats = stats;
        this.isEmergency = isEmergency;
        this.passengers = (int) (Math.random() * 50) + 1;
    }

    @Override
    public void run() {
        long arrivalTime = System.currentTimeMillis();
        try {
            // 1. Landing & Coasting
            int gateId = atc.atcRequestLanding(getName(), isEmergency);
            System.out.println("[" + getName() + "] Landing on Runway 1...");
            Thread.sleep(500); // Physical landing

            System.out.println("[" + getName() + "] Coasting to Gate " + (gateId + 1) + ".");
            atc.atcClearRunway(getName());

            stats.recordPlane(System.currentTimeMillis() - arrivalTime, passengers);

            // 2. TRUE Concurrent Gate Activities (3 Threads)
            Thread refuel = new Thread(() -> {
                try {
                    truck.useTruck(getName());
                } catch (InterruptedException e) {
                }
            }, getName() + "-Refueler");

            Thread passHandler = new Thread(() -> {
                System.out.println("[" + getName() + "] Disembarking and boarding " + passengers + " passengers.");
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                }
            }, getName() + "-PassHandler");

            Thread cleaning = new Thread(() -> {
                System.out.println("[" + getName() + "] Cleaning/Restocking cabin.");
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                }
            }, getName() + "-Cleaners");

            refuel.start();
            passHandler.start();
            cleaning.start();
            refuel.join();
            passHandler.join();
            cleaning.join();

            // 3. Undocking & Takeoff
            System.out.println("[" + getName() + "] Preparation complete. Undocking from Gate " + (gateId + 1));
            atc.atcRequestTakeoff(getName(), gateId);
            Thread.sleep(500); // Physical takeoff

            atc.atcConfirmDeparture(gateId);
            System.out.println("[" + getName() + "] Takeoff successful. Left Airspace.");

        } catch (InterruptedException e) {
            System.err.println(getName() + " Error: " + e.getMessage());
        }
    }
}
