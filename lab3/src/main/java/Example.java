import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

public class Example {
    public static void main(String[] args) {
        //Инициализация
        SparkConf conf = new SparkConf().setAppName("example");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> inputFile = sc.textFile("war-and-peace-1.txt");
        JavaRDD<String> wordsList = inputFile.flatMap(content -> Arrays.asList(content.split(" ")).iterator());

        //JavaPairRDD<String, Long> wordCount = wordsList.mapToPair(t -> new Tuple2<>(t, 1L)).reduceByKey((x, y) -> x + y);

        wordsList.saveAsTextFile("WordCount");
    }
}
