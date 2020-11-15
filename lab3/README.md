# Лабораторная работа 3

## Готовность: сделана

***

- результаты хронятся в папке output
- логи запуска хранятся в папке logs

***

### Команды для запуска

1. hdfs namenode -format
2. start-all.sh
3. mvn package
4. hadoop fs -copyFromLocal AirportList.csv FlightList.csv /
5. spark-submit --class ru.bmstu.airport.pair.find.delay.AiportFindDelayApp  --master yarn-client --num-executors 3 /Users/nikitavolkov/Desktop/DOPADP/labs_DOPADP/lab3/target/spark-examples-1.0-SNAPSHOT.jar
6. hadoop fs -copyToLocal /output