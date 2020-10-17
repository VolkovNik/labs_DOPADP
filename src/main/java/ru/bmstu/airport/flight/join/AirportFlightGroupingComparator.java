package ru.bmstu.airport.flight.join;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class AirportFlightGroupingComparator extends WritableComparator {
    public AirportFlightGroupingComparator() {
        super(AirportWritableComparable.class, true);
    }

    @Override
    public int compare(Object a, Object b) {
        AirportWritableComparable left = (AirportWritableComparable)a;
        AirportWritableComparable right = (AirportWritableComparable)b;
        if (left.getAirportID() >  right.getAirportID()) {
            return 1;
        }
    }
}
