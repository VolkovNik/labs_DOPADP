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
        if (!airportValues[0].equals("Code")) {
            AirportWritableComparable keyFromAirport = new AirportWritableComparable();
            airportValues[0] = airportValues[0].replaceAll("^\"+|\"+$", "");
            airportValues[1] = airportValues[1].replaceAll("^\"+|\"+$", "");
            int airportID = Integer.parseInt(airportValues[0]);
            keyFromAirport.setIndicator(0);
            keyFromAirport.setAirportID(airportID);
            context.write(keyFromAirport, new Text(airportValues[1]));
        }
    }
}
