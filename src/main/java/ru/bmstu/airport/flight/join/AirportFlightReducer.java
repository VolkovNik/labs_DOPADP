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
        Iterator<Text> iter = values.iterator();
        
        StringBuilder naruto = new StringBuilder("lab2: ");
        while (iter.hasNext()) {
            iter.next();
            naruto.append(iter.toString()).append(" ");

        }
        context.write(new IntWritable(key.getAirportID()), new Text(naruto.toString()));

    }
}
