package ru.bmstu.airport.pair.find.delay;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class AiportFindDelayApp {
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

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("lab3");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> airportsTable = sc.textFile("AirportList.csv");
        JavaRDD<String> flightTable = sc.textFile("FlightList.csv");

        JavaRDD<String[]> airportArray = airportsTable.flatMap(line -> line.split("\t"));

        //JavaRDD<String> flightString = flightTable.toString();

        //flightString.saveAsTextFile("output");


    }
}
//ru.bmstu.airport.pair.find.delay