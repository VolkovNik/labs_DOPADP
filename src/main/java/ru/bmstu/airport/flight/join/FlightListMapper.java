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
        float codeDelay = Float.parseFloat(flightValues[19]);
        //flightValues[20] = flightValues[20].replaceAll("^\"+|\"+$", "");
        if (!flightValues[0].equals("\"YEAR\"") && codeDelay == 0) {
            if (0 < Float.parseFloat(flightValues[18])) {
                AirportWritableComparable keyFlightList = new AirportWritableComparable();
                int airportID = Integer.parseInt(flightValues[14]);
                //float delay = Float.parseFloat(flightValues[20]);
                keyFlightList.setAirportID(airportID);
                keyFlightList.setIndicator(1);
                context.write(keyFlightList, new Text(flightValues[18]));
            }
        }
//        AirportWritableComparable keyFromAirport = new AirportWritableComparable();
//        keyFromAirport.setIndicator(1);
//        keyFromAirport.setAirportID(228);
//        context.write(keyFromAirport, new Text(flightValues[1]));
    }
}
