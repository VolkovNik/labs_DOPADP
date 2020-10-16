package ru.bmstu.airport.flight.join;

import org.apache.hadoop.mapreduce.Partitioner;

public class AirportPartition extends Partitioner {
    @Override
    public int getPartition(Object o, Object o2, int i) {
        return 0;
    }
}
