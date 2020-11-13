package ru.bmstu.airport.pair.find.delay;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class AiportFindDelayApp {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("lab3");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> airportsTable = sc.textFile("AirportList.csv");
        JavaRDD<String> flightTable = sc.textFile("FlightList.csv");

        
    }
}
