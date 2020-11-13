import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class Example {
    public static void main(String[] args) {
        //Инициализация
        SparkConf conf = new SparkConf().setAppName("example");
        JavaSparkContext sc = new JavaSparkContext(conf);
    }
}
