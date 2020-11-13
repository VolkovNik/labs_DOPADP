import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;

public class Example {
    public static void main(String[] args) {
        //Инициализация
        SparkConf conf = new SparkConf().setAppName("example");
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Загрузка
        JavaRDD<String> distFile = sc.textFile("war-and-peace-1.txt");

        //Разбиение строки на слова
        JavaRDD<String> splitted = distFile.flatMap(
                s -> Arrays.stream(s.split(" ")).iterator()
        );

        // Отображение слова
    }
}
