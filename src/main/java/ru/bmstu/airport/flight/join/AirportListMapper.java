package ru.bmstu.airport.flight.join;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class AirportListMapper extends Mapper<LongWritable, Text, AirportWritableComparable, Text> {
    private static final String REGEX_SPLITTER_CVS = ",";
    private static final String REGEX_FOR_QUOTES = "^\"+|\"+$";
    private static final String REPLACEMENT = "";
    private static final int ID_AIRPORT_COLUMN = 0;
    private static final int NAME_AIRPORT_COLUMN = 1;
    private static final int INDICATOR_AIRPORT_MAPPER = 0;
    private static final String FLAG_FIRST_STRING = "Code";

    private String[] getArrayOfValues(String strValue) {
        return strValue.split(REGEX_SPLITTER_CVS);
    }

    private int getAirportID(String airportValue) {
        return Integer.parseInt(airportValue);
    }

    private String deleteQuotes(String str) {
        return str.replaceAll(REGEX_FOR_QUOTES, REPLACEMENT);
    }

    private boolean isFirstString(String str) {
        return str.equals(FLAG_FIRST_STRING);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String airportList = value.toString();
        String[] airportValues = getArrayOfValues(airportList);
        if (!isFirstString(airportValues[ID_AIRPORT_COLUMN])) {

            AirportWritableComparable keyFromAirport = new AirportWritableComparable();

            airportValues[ID_AIRPORT_COLUMN] = deleteQuotes(airportValues[ID_AIRPORT_COLUMN]);
            airportValues[NAME_AIRPORT_COLUMN] = deleteQuotes(airportValues[NAME_AIRPORT_COLUMN]);

            int airportID = getAirportID(airportValues[ID_AIRPORT_COLUMN]);

            keyFromAirport.setIndicator(INDICATOR_AIRPORT_MAPPER);
            keyFromAirport.setAirportID(airportID);

            context.write(keyFromAirport, new Text(airportValues[1]));
        }
    }
}
