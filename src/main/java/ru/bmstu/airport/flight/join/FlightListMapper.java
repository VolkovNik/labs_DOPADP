package ru.bmstu.airport.flight.join;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlightListMapper extends Mapper<LongWritable, Text, AirportWritableComparable, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String flightList = value.toString();
        String[] flightValues = flightList.split(",");
        if (!flightValues[0].equals("\"YEAR\"")) {
            float codeDelay = Float.parseFloat(flightValues[19]);
            if (codeDelay == 0) {
                if (0 < Float.parseFloat(flightValues[18])) {
                    AirportWritableComparable keyFlightList = new AirportWritableComparable();
                    int airportID = Integer.parseInt(flightValues[14]);
                    keyFlightList.setAirportID(airportID);
                    keyFlightList.setIndicator(1);
                    context.write(keyFlightList, new Text(flightValues[18]));
                }
            }
        }
    }
}
