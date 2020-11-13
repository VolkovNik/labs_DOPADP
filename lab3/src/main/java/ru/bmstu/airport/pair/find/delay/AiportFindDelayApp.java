package ru.bmstu.airport.pair.find.delay;

import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.io.Serializable;

public class AiportFindDelayApp {
    private static final String REGEX_SPLITTER_CVS = ",";
    //private static final String FLAG_FIRST_STRING = "\"YEAR\"";
    private static final int ZERO_COLUMN = 0;
    //private static final int ID_AIRPORT_COLUMN = 14;
    private static final int CANCELLED_COLUMN = 19;
    private static final int ARR_DELAY_COLUMN = 18;
    private static final String EMPTY_STRING = "";
    private static final float CANCEL_CODE = 1;
    private static final float ZERO_TIME = 0;
    private static final int INDICATOR_FLIGHT_MAPPER = 1;
    private static final String REGEX_FOR_QUOTES = "^\"+|\"+$";
    private static final String REPLACEMENT_TO_NULL_STR = "";
    private static final int ID_AIRPORT_COLUMN = 0;
    private static final int NAME_AIRPORT_COLUMN = 1;
    private static final int INDICATOR_AIRPORT_MAPPER = 0;
    private static final String FLAG_FIRST_STRING = "Code";

    private static boolean isFirstString(String str) {
        return str.contains(FLAG_FIRST_STRING);
    }

    private static String deleteQuotes(String str) {
        return str.replaceAll(REGEX_FOR_QUOTES, REPLACEMENT_TO_NULL_STR);
    }

    public static class FlightSerializable implements Serializable {
         private boolean cancelFlag;
         private boolean delayFlag;
         private float delayTime;

         FlightSerializable(boolean cancelFlag, boolean delayFlag, float delayTime) {
             this.cancelFlag = cancelFlag;
             this.delayFlag = delayFlag;
             this.delayTime = delayTime;
         }

         boolean getCancelFlag() {
             return cancelFlag;
         }

         boolean getDelayFlag() {
             return delayFlag;
         }

         float getDelayTime() {
             return delayTime;
         }
    }

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("lab3");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> airportsList = sc.textFile("AirportList.csv");
        JavaRDD<String> flightList = sc.textFile("FlightList.csv");

        JavaPairRDD<Integer, String> airportsInformation = airportsList.filter(string -> !isFirstString(string)).
                mapToPair(string -> {
                    String[] airportValues = string.split(REGEX_SPLITTER_CVS);
                    Integer airportId = Integer.parseInt(deleteQuotes(airportValues[ID_AIRPORT_COLUMN]));
                    String airportName = airportValues[NAME_AIRPORT_COLUMN];

                    return new Tuple2<>(airportId, airportName);
                });

//        JavaRDD<String> test = airportsList.filter(string -> !isFirstString(string)).
//                map(string -> {
//                    String[] airportValues = string.split(REGEX_SPLITTER_CVS);
//
//                    return deleteQuotes(airportValues[NAME_AIRPORT_COLUMN]);
//                });

        //JavaRDD<String> flightString = flightList.toString();
        //System.out.println("kek");
            //test.saveAsTextFile("output");
        //flightString.saveAsTextFile("output");

        airportsInformation.saveAsTextFile("output");


    }
}
//ru.bmstu.airport.pair.find.delay