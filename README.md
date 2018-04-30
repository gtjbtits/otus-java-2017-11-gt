# Примеры для курса "Разработчик Java" в Otus.ru

Группа 2017-11

### Преподаватели
Vitaly Chibrikov (Виталий Чибриков)

chibrikov@otus.ru

Oleg Klimakov (Олег Климаков)

klimakov@otus.ru

### L01.1.1-maven
```bash
mvn package
java -jar L01.1.1-maven/target/gt-lecture1.jar
```
### L02.1-memory
```bash
mvn package
java -Xmx2014m -Xms1024m -jar L02.1-memory/target/gt-lecture2.jar
```

### L03: MyArrayList
```bash
mvn test
```

### L04: GC Test
Run `start.bat`

### L05: Test suite
Build:
```bash
cd L05
mvn clean package
```
Run:
```bash
# For package tests
java -jar target\gt-lecture5.jar package
# For signle class tests
java -jar target\gt-lecture5.jar
```

Test results:

#### G1 (-XX:+UseG1GC)

| Minute                              | 1st   | 2nd   | 3rd           | 4th          | 5th          |
|-------------------------------------|-------|-------|---------------|--------------|--------------|
| Young (count/total   ms/average ms) | 8/597/75 | 5/650/130 | 2/300/150    | 4/412/103       | 17/320/19  |
| Old (count/total   ms/average ms)   | 0     | 0     | 2/3136/1568 | 3/5929/1976 | 16/31452/1965 |

#### Concurrent (-XX:+UseParNewGC -XX:+UseConcMarkSweepGC)

| Minute                              | 1st   | 2nd   | 3rd           | 4th          | 5th          |
|-------------------------------------|-------|-------|---------------|--------------|--------------|
| Young (count/total   ms/average ms) | 1/970 | 1/470 | 2/1997/998    | 1/1087       | 6/9216/1536  |
| Old (count/total   ms/average ms)   | 0     | 0     | 11/21340/1940 | 21/19720/939 | 17/13818/812 |


#### Parallel (-XX:+UseParallelGC -XX:+UseParallelOldGC)

| Minute                              | 1st   | 2nd   | 3rd           | 4th          | 5th          |
|-------------------------------------|-------|-------|---------------|--------------|--------------|
| Young (count/total   ms/average ms) | 1/242 | 2/560/280 | 1/275    | 1/262       | 0  |
| Old (count/total   ms/average ms)   | 0     | 0     | 0 | 5/10529/2105 | 28/48272/1724 |

#### Serial (-XX:+UseSerialGC)

| Minute                              | 1st   | 2nd   | 3rd           | 4th          | 5th          |
|-------------------------------------|-------|-------|---------------|--------------|--------------|
| Young (count/total   ms/average ms) | 1/157 | 1/210 | 2/394/197    | 1/248       | 0  |
| Old (count/total   ms/average ms)   | 0     | 0     | 0 | 1/1149 | 9/10649/1183 |

`G1` has fastest young scavenge and very effective old scavenge\*. World stop pauses are almost imperceptible.
`Concurrent` is very similar, but the garbage collection in the young memory area is more lazy. Scavenging the old zone takes longer. World stop pauses still almost imperceptible.
`Parallel` and `Serial` have a similar result: young scavenge 1-4m and old scavenge 4-5m. World stop pauses are very noticeable.

\* - each test was completed in about 5 minutes, but for different collectors a different step of memory leakage was used

### L06
```bash
mvn test
```

### L08
```bash
mvn test
```

### L09
```bash
mvn test
```

### L10
```bash
mvn test
```

### L11
```bash
mvn test
```

### L12: Cache service statistics 
By default web server will starts at port `8080`. Please, use "-p" parameter, to choose different port

Use *any* login & password to pass in for first time (it will create user for you in the database)
```bash
mvn package
java -jar L12/target/gt-lecture12.jar [-p port]
```

### L13: Cache service statistics (Spring MVC implementation)
```bash
mvn package
```
Will outputs: `L14/target/gt-lecture13.war`. Please, deploy it to the yours external Jetty server.
Controller mapped to the root URL.

### L14: Parallel merge sort
```bash
mvn test
```

### L15: Simple WebSocket chat based on MessageSystem
```bash
mvn package
```
Will outputs: `L15/target/chat.war`. Please, deploy it to the yours external Jetty server.
Controller mapped to the root URL.
Frontend: https://github.com/serkanyersen/Basic-Chat

