package ru.bmstu.airport.pair.find.delay;

import java.io.Serializable;

public class FlightSerializable implements Serializable {
    private boolean cancelFlag;
    private float delayTime;

    public FlightSerializable(boolean cancelFlag, float delayTime) {
        this.cancelFlag = cancelFlag;
        this.delayTime = delayTime;
    }

    public boolean getCancelFlag() {
        return cancelFlag;
    }

    public float getDelayTime() {
        return delayTime;
    }
}