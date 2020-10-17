package ru.bmstu.airport.flight.join;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class AirportFlightReducer extends Reducer<AirportWritableComparable, Text, Text, Text> {
    public float getDelay(String delay) {
        return Float.parseFloat(delay);
    }

    @Override
    protected void reduce(AirportWritableComparable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        String airportName = "Airport: ";
        String airportStats = " Delays: ";
        float sum = 0;
        Iterator<Text> val_iterator = values.iterator();
        airportName += val_iterator.next().toString();
        if (val_iterator.hasNext()) {
            String stringDelay = val_iterator.next().toString();
            float minDelay = getDelay(stringDelay);
            float maxDelay = getDelay(stringDelay);
            sum = getDelay(stringDelay);
            int counter = 1;
            while (val_iterator.hasNext()) {
                float delay = getDelay(val_iterator.next().toString());
                if (delay < minDelay) {
                    minDelay = delay;
                }
                if (delay > maxDelay) {
                    maxDelay = delay;
                }
                sum += delay;
                counter++;
            }
            airportStats += "Min=" + minDelay;
            airportStats += "Max=" + maxDelay;
            airportStats += "Average=" + (sum / (float)counter);

            if (sum == 0) {
                context.write(new Text(airportName), new Text(airportStats));
            }
        }

    }
}
