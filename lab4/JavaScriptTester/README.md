# Лабораторная работа 4

## Готовность: сделана

***

***

### Запуск

1. Запустить JavaScriptTester.java
2. POST запрос:
		```curl --header "Content-Type: application/json" --request POST --data '{ "packageId":11, "jsScript":"var divideFn = function(a,b) { return a/b} ", "functionName":"divideFn", "tests": [ {"testName":"test1", "expectedResult":"2.0", "params":[2,1] },{"testName":"test2","expectedResult":"2.0","params":[4,2]}, {"testName":"test3","expectedResult":"3.0","params":[6,2]},{"testName":"test4","expectedResult":"12.0","params":[6,2]}] }' localhost:8080}```
3. GET запрос:
		```curl --header "Content-Type: application/json" --request POST --data '{ "packageId":11, "jsScript":"var divideFn = function(a,b) { return a/b} ", "functionName":"divideFn", "tests": [ {"testName":"test1", "expectedResult":"2.0", "params":[2,1] },{"testName":"test2","expectedResult":"2.0","params":[4,2]}, {"testName":"test3","expectedResult":"3.0","params":[6,2]},{"testName":"test4","expectedResult":"12.0","params":[6,2]}] }' localhost:8080}```

