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

        // Загрузка
        JavaRDD<String> distFile = sc.textFile("war-and-peace-1.txt");

        //Разбиение строки на слова
        JavaRDD<String> splitted = distFile.flatMap(
                s -> Arrays.stream(s.split(" ")).iterator()
        );

        // Отображение слов в пару (слово, 1)
        JavaPairRDD<String, Long> wordsWithCount =
                splitted.mapToPair(
                        s -> new Tuple2<>(s, 1L)
                );

        //Считаем одинаковые слова
        JavaPairRDD<String, Long> collectedWords = wordsWithCount.reduceByKey (
                (a, b) -> a + b
        );

        // Загружаем в словарь

        JavaRDD<String> dictionaryFile = sc.textFile("words.txt");
        JavaPairRDD<String, Long> dictionary =
                dictionaryFile.mapToPair(
                        s -> new Tuple2<>(s,1l)
                );

        // Производим операцию join со словарем
    }
}
