package ru.bmstu.airport.flight.join;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class AirportPartition extends Partitioner<AirportWritableComparable, Text> {
    @Override
    public int getPartition(AirportWritableComparable airportWritableComparable, Text text, int i) {
        return airportWritableComparable.getAirportID() % i;
    }
}
