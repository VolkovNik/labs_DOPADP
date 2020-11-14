package ru.bmstu.airport.pair.find.delay;

import java.io.Serializable;

public class FlightSerializable implements Serializable {
    private final boolean cancelFlag;
    private final float delayTime;

    FlightSerializable(boolean cancelFlag, float delayTime) {
        this.cancelFlag = cancelFlag;
        this.delayTime = delayTime;
    }

    boolean getCancelFlag() {
        return cancelFlag;
    }

    float getDelayTime() {
        return delayTime;
    }
}