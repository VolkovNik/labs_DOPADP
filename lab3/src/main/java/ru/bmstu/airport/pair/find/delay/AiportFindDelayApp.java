package ru.bmstu.airport.pair.find.delay;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.util.Map;

public class AiportFindDelayApp {
    private static final String REGEX_SPLITTER_CVS = ",";
    private static final String FLAG_FIRST_STRING_FLIGHT_TABLE = "YEAR";
    private static final int ID_AIRPORT_COLUMN_FOR_DELAY = 14;
    private static final int ID_AIRPORT_ORIGIN_COLUMN = 11;
    private static final int CANCELLED_CODE_COLUMN = 19;
    private static final int ARR_DELAY_COLUMN = 18;
    private static final String EMPTY_STRING = "";
    private static final float CANCEL_CODE = 1;
    private static final float ZERO_TIME = 0;
    private static final float ZERO = 0;
    private static final String REGEX_FOR_QUOTES = "^\"+|\"+$";
    private static final String REPLACEMENT_TO_NULL_STR = "";
    private static final int ID_AIRPORT_COLUMN_FOR_NAME = 0;
    private static final int NAME_AIRPORT_COLUMN = 1;
    private static final String FLAG_FIRST_STRING_AIRPORT_TABLE = "Code";
    private static final int ONE_HUNDRED_PERCENT = 100;
    private static final String MAX_DELAY_STRING = "MaxDelay: ";
    private static final String FLIGHTS_STRING = " Flights: ";
    private static final String CANCELLED_FLIGHTS_STRING = " Cancelled Flights: ";
    private static final String DELAYED_FLIGHT_STRING = " Delayed Flights: ";
    private static final String PERCENTAGE_OF_DELAYED_STRING = " Percentage of delayed: ";
    private static final String PERCENTAGE_OF_CANCELLED_STRING = " Percentage of cancelled: ";
    private static final String FROM_STRING = "From: ";
    private static final String TO_STRING = " To: ";
    private static final String END_OF_STRING = "\n";

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

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("lab3");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> airportsList = sc.textFile("/AirportList.csv");
        JavaRDD<String> flightList = sc.textFile("/FlightList.csv");

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

        JavaPairRDD<Tuple2<Integer, Integer>, FlightDataCombined> flightDataCombined =
                flightInformation.combineByKey(
                        value -> new FlightDataCombined(
                                value.getDelayTime(),
                                1,
                                value.getCancelFlag() ? 1 : 0,
                                value.getDelayTime() > ZERO_TIME ? 1 : 0
                        ),
                        (flight, value) -> FlightDataCombined.addValue(flight,
                                value.getDelayTime(),
                                value.getCancelFlag() ? 1 : 0,
                                value.getDelayTime() > ZERO_TIME ? 1 : 0
                        ),
                        FlightDataCombined::add
                );

        JavaPairRDD<Tuple2<Integer, Integer>, String> flightDataString =
                flightDataCombined.mapToPair(
                        value -> {

                            float percentageOfDelayed = 0;
                            float percentageOfCancelled = 0;

                            if (value._2().getCounterDelayed() != ZERO) {
                                percentageOfDelayed = (float) value._2().getCounterDelayed() / value._2().getCounterFlight() * ONE_HUNDRED_PERCENT;
                            }

                            if (value._2().getCounterCancelled() != ZERO) {
                                percentageOfCancelled = (float) value._2().getCounterCancelled() / value._2().getCounterFlight() * ONE_HUNDRED_PERCENT;
                            }

                            String dataInString =
                                    MAX_DELAY_STRING + value._2().getMaxDelay() +
                                    FLIGHTS_STRING + value._2().getCounterFlight() +
                                    CANCELLED_FLIGHTS_STRING + value._2().getCounterCancelled() +
                                    DELAYED_FLIGHT_STRING + value._2().getCounterDelayed() +
                                    PERCENTAGE_OF_DELAYED_STRING + percentageOfDelayed +
                                    PERCENTAGE_OF_CANCELLED_STRING + percentageOfCancelled;
                            return new Tuple2<>(value._1(), dataInString);
                        }
                );

        final Broadcast<Map<Integer, String>> airportBroadcasted = sc.broadcast(airportsInformation.collectAsMap());

        JavaRDD<String> dataToOutput = flightDataString.map(
                value -> {
                    Map<Integer, String> airportName = airportBroadcasted.getValue();

                    String airportFrom = airportName.get(value._1()._1());
                    String airportTo = airportName.get(value._1()._2());

                    return FROM_STRING + airportFrom + TO_STRING + airportTo + END_OF_STRING + value._2();

                }
        );

        dataToOutput.saveAsTextFile("/output");

    }
}