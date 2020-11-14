package ru.bmstu.airport.pair.find.delay;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.io.Serializable;

public class AiportFindDelayApp {
    private static final String REGEX_SPLITTER_CVS = ",";
    private static final String FLAG_FIRST_STRING_FLIGHT_TABLE = "YEAR";
    private static final int ZERO_COLUMN = 0;
    private static final int ID_AIRPORT_COLUMN_FOR_DELAY = 14;
    private static final int ID_AIRPORT_ORIGIN_COLUMN = 11;
    private static final int CANCELLED_CODE_COLUMN = 19;
    private static final int ARR_DELAY_COLUMN = 18;
    private static final String EMPTY_STRING = "";
    private static final float CANCEL_CODE = 1;
    private static final float ZERO_TIME = 0;
    private static final int INDICATOR_FLIGHT_MAPPER = 1;
    private static final String REGEX_FOR_QUOTES = "^\"+|\"+$";
    private static final String REPLACEMENT_TO_NULL_STR = "";
    private static final int ID_AIRPORT_COLUMN_FOR_NAME = 0;
    private static final int NAME_AIRPORT_COLUMN = 1;
    private static final int INDICATOR_AIRPORT_MAPPER = 0;
    private static final String FLAG_FIRST_STRING_AIRPORT_TABLE = "Code";

    private static boolean isFirstStringAirportTable(String str) {
        return str.contains(FLAG_FIRST_STRING_AIRPORT_TABLE);
    }

    private static boolean isFirstStringFlightTable(String str) {
        return str.contains(FLAG_FIRST_STRING_FLIGHT_TABLE);
    }

    private static String deleteQuotes(String str) {
        return str.replaceAll(REGEX_FOR_QUOTES, REPLACEMENT_TO_NULL_STR);
    }

    private static String[] getArrayOfValues(String strValue) {
        return strValue.split(REGEX_SPLITTER_CVS);
    }

    private static float getCancelCode(String strCode) {
        return Float.parseFloat(strCode);
    }

    private static boolean isCancelled(float cancelled) {
        return cancelled == CANCEL_CODE;
    }

    private static boolean isDelayed(String strDelay) {
        return !strDelay.equals(EMPTY_STRING);
    }

    private static float getDelayTime(String str) {
        if (isDelayed(str)) {
            return Float.parseFloat(str);
        } else
            return ZERO_TIME;
    }

    public static class Testing implements Serializable {
        private float timeDelay;
        private int counterDelayed;
        private int counterCancelled;
        private int counterFlights;

        Testing(float timeDelay, int counterDelayed, int counterCancelled, int counterFlights) {
            this.timeDelay = timeDelay;
            this.counterDelayed = counterDelayed;
            this.counterCancelled = counterCancelled;
            this.counterFlights = counterFlights;
        }

        float getTimeDelay() {
            return timeDelay;
        }

        public int getCounterCancelled() {
            return counterCancelled;
        }

        public int getCounterDelayed() {
            return counterDelayed;
        }

        public int getCounterFlights() {
            return counterFlights;
        }
    }

    public static class TestingCombine implements Serializable {
        private float sumDelays;
        private int counter;

        TestingCombine(float sumDelays, int counter) {
            this.sumDelays = sumDelays;
            this.counter = counter;
        }

        public float getSumDelays() {
            return sumDelays;
        }

        public int getCounter() {
            return counter;
        }

        public static TestingCombine addValue(TestingCombine a, float delay) {
            return new TestingCombine(
                    a.getSumDelays() + delay, a.getCounter() + 1
            );
        }

        public static TestingCombine add(TestingCombine a, TestingCombine b) {
            return new TestingCombine(
                    a.getSumDelays() + b.getSumDelays(),
                    a.getCounter() + a.getCounter()
            );
        }

    }

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("lab3");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> airportsList = sc.textFile("AirportList.csv");
        JavaRDD<String> flightList = sc.textFile("FlightList.csv");

        JavaPairRDD<Integer, String> airportsInformation =
                airportsList.filter(string -> !isFirstStringAirportTable(string)).
                mapToPair(string -> {
                    String[] airportValues = string.split(REGEX_SPLITTER_CVS);
                    Integer airportId = Integer.parseInt(deleteQuotes(airportValues[ID_AIRPORT_COLUMN_FOR_NAME]));
                    String airportName = airportValues[NAME_AIRPORT_COLUMN];

                    return new Tuple2<>(airportId, airportName);
                });

        JavaPairRDD<Tuple2<Integer, Integer>, FlightSerializable> flightInformation =
                flightList.filter(string -> !isFirstStringFlightTable(string)).
                mapToPair(string -> {
                    String[] flightValues = getArrayOfValues(string);
                    Integer destAirportId = Integer.parseInt(flightValues[ID_AIRPORT_COLUMN_FOR_DELAY]);
                    Integer originalAirportId = Integer.parseInt(flightValues[ID_AIRPORT_ORIGIN_COLUMN]);

                    float cancelCode = getCancelCode(flightValues[CANCELLED_CODE_COLUMN]);
                    boolean cancelFlag = isCancelled(cancelCode);
                    float delayTime = getDelayTime(flightValues[ARR_DELAY_COLUMN]);

                    FlightSerializable flightDelayData = new FlightSerializable(cancelFlag, delayTime);

                    return new Tuple2<>(new Tuple2<>(destAirportId, originalAirportId), flightDelayData);

                });

        JavaPairRDD<Boolean, Float> test = flightInformation.values().mapToPair(
                value -> {
                    float time = value.getDelayTime();
                    boolean flag = value.getCancelFlag();
                    return new Tuple2<>(flag, time);
                }

        );

        JavaPairRDD<Tuple2<Integer, Integer>, TestingCombine> combine =
                flightInformation.combineByKey(
                        
                )

        test.saveAsTextFile("output");


    }
}