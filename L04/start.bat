SET MEMORY=-Xms512M -Xmx512M
REM SET GC=-XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark -XX:+UseParNewGC
REM SET GC=-XX:+UseG1GC
REM SET GC=-XX:+UseParNewGC -XX:+UseConcMarkSweepGC
SET GC=-XX:+UseParallelGC -XX:+UseParallelOldGC
REM SET GC=-XX:+UseSerialGC
SET GC_LOG=-verbose:gc -Xlog:gc:out.log

java %MEMORY% %GC% %GC_LOG% -jar target/gt-lecture4.jar
