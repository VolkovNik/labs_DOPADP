package ru.bmstu.airport.pair.find.delay;

import java.io.Serializable;

public class FlightDataCombined implements Serializable {
    private float maxDelay;
    private int counterFlight;
    private int counterCancelled;
    private int counterDelayed;

    public FlightDataCombined(float maxDelay, int counterFlight, int counterCancelled, int counterDelayed) {
        this.maxDelay = maxDelay;
        this.counterFlight = counterFlight;
        this.counterCancelled = counterCancelled;
        this.counterDelayed = counterDelayed;
    }

    public float getMaxDelay() {
        return maxDelay;
    }

    public int getCounterDelayed() {
        return counterDelayed;
    }

    public int getCounterCancelled() {
        return counterCancelled;
    }

    public int getCounterFlight() {
        return counterFlight;
    }

    public static FlightDataCombined addValue (FlightDataCombined data, float maxDelay,
                                               int counterCancelled, int counterDelayed) {
        return new FlightDataCombined(Math.max(data.getMaxDelay(), maxDelay),
                data.getCounterFlight() + 1,
                data.getCounterCancelled() + counterCancelled,
                data.getCounterDelayed() + counterDelayed);
    }

    public static FlightDataCombined add (FlightDataCombined first, FlightDataCombined second) {
        return new FlightDataCombined(
                Math.max(first.getMaxDelay(), second.getMaxDelay()),
                first.getCounterFlight() + second.getCounterFlight(),
                first.getCounterCancelled() + second.getCounterCancelled(),
                first.getCounterDelayed() + second.getCounterDelayed());
    }

}