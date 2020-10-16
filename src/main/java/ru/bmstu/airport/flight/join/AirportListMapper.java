package ru.bmstu.airport.flight.join;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class AirportListMapper extends Mapper<LongWritable, Text, AirportWritableComparable, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String airportList = value.toString();
        String[] airportValues = airportList.split(",");
        System.out.println(airportValues);
        if (!airportValues[0].equals("Code")) {
            AirportWritableComparable keyFromAirport = new AirportWritableComparable();
            int airportID = Integer.parseInt(airportValues[0]);
            keyFromAirport.setIndicator(0);
            keyFromAirport.setAirportID(airportID);
            context.write(keyFromAirport, new Text(airportValues[1]));
        }
    }
}
