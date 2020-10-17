package ru.bmstu.airport.flight.join;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class AirportFlightReducer extends Reducer<AirportWritableComparable, Text, Text, Text> {
    private static final String defaultAirportName = "Airport: ";
    private static final String defaultAirportStats = "Delays: ";
    private static final String MIN_STRING = "Min: ";
    private static final String MAX_STRING = " Max: ";
    private static final String AVERAGE_STRING = " Average: ";

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
            airportStats += MIN_STRING + minDelay;
            airportStats += MAX_STRING + maxDelay;
            airportStats += AVERAGE_STRING + (sumOfAllDelays / (float)counterOfDelays);


            context.write(new Text(airportName), new Text(airportStats));
        }

    }
}
