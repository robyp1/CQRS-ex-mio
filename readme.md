
**PER PROVARE L'APP**

**PREPARARE L'AMBIENTE CON KAFKA**:
* scaricare kafka eseguibile .tgz versione 1.0.1

* esempio estraggo tar e salvo in :
* salvo in C:/ProgrammiStandalone/kafka-1.1.0/kafka_2.11-1.1.0/

* eseguo dal lina di comando:
* 1. avvio zookeeper
* C:/ProgrammiStandalone/kafka-1.1.0/kafka_2.11-1.1.0/bin/windows/zookeeper-server-start.bat C:\ProgrammiStandalone\kafka-1.1.0\kafka_2.11-1.1.0\config\zookeeper.properties

* 2. avvio kafka broker
* C:/ProgrammiStandalone/kafka-1.1.0/kafka_2.11-1.1.0/bin/windows/kafka-server-start.bat C:\ProgrammiStandalone\kafka-1.1.0\kafka_2.11-1.1.0\config\server.properties

* 3. creo la mia topic
* C:/ProgrammiStandalone/kafka-1.1.0/kafka_2.11-1.1.0/bin/windows/kafka-topics.bat --zookeeper localhost:2128 --replication-factor 1 --partitions 1 --topic doc-event-handler

* 4. compilare il progetto con maven
* mvn clean install

* 5. avviare ils erver wilsfly
* eseguire standalone.bat

* 6. fare il deploy su wildfly
* wildfly:deploy

* i logs e eventuali componenti di kafka di default vengono salvate in C:\tmp\kafka-logs


* fonte: https://kafka.apache.org/quickstart

**PROVE MANUALI**


per provare manualmente una volta tirato su il broker e zookeeper:

* 1. la topic TEST va prima creata con
* kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic TEST

* 2. aprire console per inviare messaggi sulla topic TEST
* kafka-console-producer.bat --broker-list localhost:9092 --topic TEST

* 3. legge dalla topic test
* kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic TEST --from-beginning



