package ru.bmstu.airport.flight.join;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class AirportFlightGroupingComparator extends WritableComparator {
    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        return super.compare(a, b);
    }
}
