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
    private static final int CANCELLED_COLUMN = 19;
    private static final int ARR_DELAY_COLUMN = 18;
    private static final String EMPTY_STRING = "";
    private static final float CANCEL_CODE = 1;
    private static final float ZERO_TIME = 0;
    private static final int INDICATOR_FLIGHT_MAPPER = 1;

    private String[] getArrayOfValues(String strValue) {
        return strValue.split(REGEX_SPLITTER_CVS);
    }

    private static boolean isFirstString(String str) {
        return str.equals(FLAG_FIRST_STRING);
    }

    private float getCancelCode(String strCode) {
        return Float.parseFloat(strCode);
    }

    private boolean isCancelled(float cancelled) {
        if (cancelled == CANCEL_CODE) {
            return true;
        }
        return false;
    }

    private boolean isDelayed(String strDelay) {
        return !strDelay.equals(EMPTY_STRING);
    }

    private boolean (String strArrTime) {
        return ZERO_TIME < Float.parseFloat(strArrTime);
    }

    private int getAirportID(String airportValue) {
        return Integer.parseInt(airportValue);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String flightList = value.toString();
        String[] flightValues = getArrayOfValues(flightList);

        if (!isFirstString(flightValues[ZERO_COLUMN])) {
            float cancelCode = getCancelCode(flightValues[CANCELLED_COLUMN]);

            if (!isCancelled(cancelCode) && isDelayed(flightValues[ARR_DELAY_COLUMN])) {
                if (isDelayTimeMoreZero(flightValues[ARR_DELAY_COLUMN])) {

                    AirportWritableComparable keyFlightList = new AirportWritableComparable();
                    int airportID = getAirportID(flightValues[ID_AIRPORT_COLUMN]);
                    keyFlightList.setAirportID(airportID);
                    keyFlightList.setIndicator(INDICATOR_FLIGHT_MAPPER);
                    context.write(keyFlightList, new Text(flightValues[ARR_DELAY_COLUMN]));
                }
            }
        }
    }
}
