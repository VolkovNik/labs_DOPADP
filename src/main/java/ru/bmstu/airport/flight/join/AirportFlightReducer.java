package ru.bmstu.airport.flight.join;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class AirportFlightReducer extends Reducer<AirportWritableComparable, Text, Text, Text> {
    private static String defaultAirportName = "Airport: ";
    private static String defaultAirportStats = "Delays: ";
    private static String MIN_STRING = "Min:"

    public float getDelay(String delay) {
        return Float.parseFloat(delay);
    }

    private String getAirportName(Text str) {
        return str.toString();
    }

    @Override
    protected void reduce(AirportWritableComparable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        String airportName = defaultAirportName;
        String airportStats = defaultAirportStats;
        float sumOfAllDelays = 0;
        int counterOfDelays = 0;

        Iterator<Text> val_iterator = values.iterator();
        airportName += getAirportName(val_iterator.next());

        if (val_iterator.hasNext()) {

            String stringDelay = val_iterator.next().toString();
            float minDelay = getDelay(stringDelay);
            float maxDelay = getDelay(stringDelay);
            sumOfAllDelays += getDelay(stringDelay);
            counterOfDelays++;

            while (val_iterator.hasNext()) {
                float delay = getDelay(val_iterator.next().toString());
                if (delay < minDelay) {
                    minDelay = delay;
                }
                if (delay > maxDelay) {
                    maxDelay = delay;
                }
                sumOfAllDelays += delay;
                counterOfDelays++;
            }
            airportStats += "Min=" + minDelay;
            airportStats += " Max=" + maxDelay;
            airportStats += " Average=" + (sumOfAllDelays / (float)counterOfDelays);


            context.write(new Text(airportName), new Text(airportStats));
        }

    }
}
