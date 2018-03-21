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

