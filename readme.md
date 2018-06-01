
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



