package ru.bmstu.airport.flight.join;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlightListMapper extends Mapper<LongWritable, Text, AirportWritableComparable, Text> {
    private static final String REGEX_SPLITTER_CVS = ",";
    private static final String FLAG_FIRST_STRING = "\"YEAR\"";
    private static final int ZERO_COLUMN = 0;
    private static final int ID_AIRPORT_COLUMN = 14;
    private static final int CODE_DELAY_COLUMN = 19;
    private static final int ARR_DELAY_COLUMN = 18;
    private static final String EMPTY_STRING = "";
    private static final int INDICATOR = 1;

    private String[] getArrayOfValues(String strValue) {
        return strValue.split(REGEX_SPLITTER_CVS);
    }

    private boolean isFirstString(String str) {
        return str.equals(FLAG_FIRST_STRING);
    }

    private float getCodeDelay(String strCode) {
        return Float.parseFloat(strCode);
    }

    private boolean isNo


    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String flightList = value.toString();
        String[] flightValues = getArrayOfValues(flightList);

        if (!isFirstString(flightValues[ZERO_COLUMN])) {
            float codeDelay = getCodeDelay(flightValues[CODE_DELAY_COLUMN]);

            if (codeDelay == 0 && !flightValues[ARR_DELAY_COLUMN].equals(EMPTY_STRING)) {
                if ((float)0 < Float.parseFloat(flightValues[ARR_DELAY_COLUMN])) {
                    AirportWritableComparable keyFlightList = new AirportWritableComparable();
                    int airportID = Integer.parseInt(flightValues[ID_AIRPORT_COLUMN]);
                    keyFlightList.setAirportID(airportID);
                    keyFlightList.setIndicator(INDICATOR);
                    context.write(keyFlightList, new Text(flightValues[ARR_DELAY_COLUMN]));
                }
            }
        }
    }
}
