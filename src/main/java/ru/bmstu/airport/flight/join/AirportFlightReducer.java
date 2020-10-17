package ru.bmstu.airport.flight.join;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class AirportFlightReducer extends Reducer<AirportWritableComparable, Text, IntWritable, Text> {
    @Override
    protected void reduce(AirportWritableComparable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        String airportName = "Airport: ";
        String delays = " Delays: ";
        Iterator<Text> val_iterator = values.iterator();
        airportName += val_iterator.next().toString();

        while (val_iterator.hasNext()) {
            Text delay = val_iterator.next();
            delays += delay.toString() + " ";
        }
        context.write(new IntWritable(key.getAirportID()), new Text(delays));

    }
}
