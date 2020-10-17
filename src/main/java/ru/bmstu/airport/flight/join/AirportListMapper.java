package ru.bmstu.airport.flight.join;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class AirportListMapper extends Mapper<LongWritable, Text, AirportWritableComparable, Text> {
    private static final String REGEX_SPLITTER_CVS = ",";
    private static final String REGEX_FOR_QUOTES = "^\"+|\"+$";
    private static final String REPLACEMENT = "";
    private static final int ID_POSITION = 0;
    private static final int NAME_AIRPORT_POSITION = 1;
    private static final int INDICATOR = 0;

    private int getAirportID(String airportValue) {
        return Integer.parseInt(airportValue);
    }

    private String deleteQuotes(String str) {
        return str.replaceAll(REGEX_FOR_QUOTES, REPLACEMENT);
    }

    private boolean isFirstString(String str) {
        return str.equals("Code");
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String airportList = value.toString();
        String[] airportValues = airportList.split(REGEX_SPLITTER_CVS);
        if (!isFirstString(airportValues[ID_POSITION])) {

            AirportWritableComparable keyFromAirport = new AirportWritableComparable();

            airportValues[ID_POSITION] = deleteQuotes(airportValues[ID_POSITION]);
            airportValues[NAME_AIRPORT_POSITION] = deleteQuotes(airportValues[NAME_AIRPORT_POSITION]);

            int airportID = getAirportID(airportValues[ID_POSITION]);

            keyFromAirport.setIndicator(INDICATOR);
            keyFromAirport.setAirportID(airportID);
            
            context.write(keyFromAirport, new Text(airportValues[1]));
        }
    }
}
