# Лабораторная работа 4

## Готовность: сделана

***

***

### Запуск

1. Запустить JavaScriptTester.java
2. POST запрос
	```{r, engine='bash', curl --header "Content-Type: application/json" --request POST --data '{ "packageId":11, "jsScript":"var divideFn = function(a,b) { return a/b} ", "functionName":"divideFn", "tests": [ {"testName":"test1", "expectedResult":"2.0", "params":[2,1] },{"testName":"test2","expectedResult":"2.0","params":[4,2]}, {"testName":"test3","expectedResult":"3.0","params":[6,2]},{"testName":"test4","expectedResult":"12.0","params":[6,2]}] }' localhost:8080}
3. mvn package
4. hadoop fs -copyFromLocal AirportList.csv FlightList.csv /
5. spark-submit --class ru.bmstu.airport.pair.find.delay.AiportFindDelayApp  --master yarn-client --num-executors 3 /Users/nikitavolkov/Desktop/DOPADP/labs_DOPADP/lab3/target/spark-examples-1.0-SNAPSHOT.jar
6. hadoop fs -copyToLocal /output