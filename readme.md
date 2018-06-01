
* PER PROVARE L'APP*

PER KAFKA:
scaricare kafka eseguibile .tgz versione 1.0.1

esempio estraggo tar e salvo in :
salvo in C:/ProgrammiStandalone/kafka-1.1.0/kafka_2.11-1.1.0/

eseguo dal lina di comando:
-avvio zookeeper
C:/ProgrammiStandalone/kafka-1.1.0/kafka_2.11-1.1.0/bin/windows/zookeeper-server-start.bat C:\ProgrammiStandalone\kafka-1.1.0\kafka_2.11-1.1.0\config\zookeeper.properties

--avvio kafka broker
C:/ProgrammiStandalone/kafka-1.1.0/kafka_2.11-1.1.0/bin/windows/kafka-server-start.bat C:\ProgrammiStandalone\kafka-1.1.0\kafka_2.11-1.1.0\config\server.properties

--creo la mia topic
C:/ProgrammiStandalone/kafka-1.1.0/kafka_2.11-1.1.0/bin/windows/kafka-topics.bat --zookeeper localhost:2128 --replication-factor 1 --partitions 1 --topic doc-event-handler

--compilare il progetto con maven
mvn clean install

--avviare ils erver wilsfly
eseguire standalone.bat

--fare il deploy su wildfly
wildfly:deploy


fonte: https://kafka.apache.org/quickstart

**PROVE MANUALI**

i logs dovrebbero essere in C:\tmp\kafka-logs
per provare manualmente una volta tirato su il broker e zookeeper:

--la topic TEST va prima creata con
kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic TEST

-apre console per inviare messaggi sulla topic TEST
kafka-console-producer.bat --broker-list localhost:9092 --topic TEST

--legge dalla topic test
kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic TEST --from-beginning





15:42:34,526 SEVERE [com.cadit.cqrs.kafka.EventConsumer] (EE-ManagedExecutorService-default-Thread-4) si � verificata una Exception che non � una wakeupException! d != java.lang.String
15:42:34,527 ERROR [stderr] (EE-ManagedExecutorService-default-Thread-4) java.util.IllegalFormatConversionException: d != java.lang.String
15:42:34,528 ERROR [stderr] (EE-ManagedExecutorService-default-Thread-4) 	at java.util.Formatter$FormatSpecifier.failConversion(Formatter.java:4302)
15:42:34,528 ERROR [stderr] (EE-ManagedExecutorService-default-Thread-4) 	at java.util.Formatter$FormatSpecifier.printInteger(Formatter.java:2793)
15:42:34,528 ERROR [stderr] (EE-ManagedExecutorService-default-Thread-4) 	at java.util.Formatter$FormatSpecifier.print(Formatter.java:2747)
15:42:34,528 ERROR [stderr] (EE-ManagedExecutorService-default-Thread-4) 	at java.util.Formatter.format(Formatter.java:2520)
15:42:34,529 ERROR [stderr] (EE-ManagedExecutorService-default-Thread-4) 	at java.util.Formatter.format(Formatter.java:2455)
15:42:34,532 ERROR [stderr] (EE-ManagedExecutorService-default-Thread-4) 	at java.lang.String.format(String.java:2940)
15:42:34,532 ERROR [stderr] (EE-ManagedExecutorService-default-Thread-4) 	at com.cadit.cqrs.kafka.EventConsumer.printConsumerInfo(EventConsumer.java:71)
15:42:34,532 ERROR [stderr] (EE-ManagedExecutorService-default-Thread-4) 	at com.cadit.cqrs.kafka.EventConsumer.consume(EventConsumer.java:62)
15:42:34,532 ERROR [stderr] (EE-ManagedExecutorService-default-Thread-4) 	at com.cadit.cqrs.kafka.EventConsumer.run(EventConsumer.java:42)
15:42:34,534 ERROR [stderr] (EE-ManagedExecutorService-default-Thread-4) 	at org.jboss.as.ee.concurrent.ControlPointUtils$ControlledRunnable.run(ControlPointUtils.java:105)
15:42:34,540 ERROR [stderr] (EE-ManagedExecutorService-default-Thread-4) 	at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
15:42:34,540 ERROR [stderr] (EE-ManagedExecutorService-default-Thread-4) 	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
15:42:34,540 ERROR [stderr] (EE-ManagedExecutorService-default-Thread-4) 	at org.glassfish.enterprise.concurrent.internal.ManagedFutureTask.run(ManagedFutureTask.java:141)
15:42:34,540 ERROR [stderr] (EE-ManagedExecutorService-default-Thread-4) 	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
15:42:34,541 ERROR [stderr] (EE-ManagedExecutorService-default-Thread-4) 	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
15:42:34,580 ERROR [stderr] (EE-ManagedExecutorService-default-Thread-4) 	at java.lang.Thread.run(Thread.java:745)
15:42:34,580 ERROR [stderr] (EE-ManagedExecutorService-default-Thread-4) 	at org.glassfish.enterprise.concurrent.ManagedThreadFactoryImpl$ManagedThread.run(ManagedThreadFactoryImpl.java:250)
