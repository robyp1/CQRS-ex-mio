
# PER CONFIGURARE L'APPLICAZIONE

## PREPARARE L'AMBIENTE CON KAFKA
### scaricare kafka eseguibile .tgz versione 1.0.1

### esempio estraggo tar e salvo in :
### salvo in C:/ProgrammiStandalone/kafka-1.1.0/kafka_2.11-1.1.0/ E trovo gli eseguibili in /bin/windows/
``` cd C:/ProgrammiStandalone/kafka-1.1.0/kafka_2.11-1.1.0/bin/windows/ ```

### eseguo dal lina di comando:
### 1) avvio zookeeper
``` zookeeper-server-start.bat C:\ProgrammiStandalone\kafka-1.1.0\kafka_2.11-1.1.0\config\zookeeper.properties ```

### 2) avvio kafka broker
``` kafka-server-start.bat C:\ProgrammiStandalone\kafka-1.1.0\kafka_2.11-1.1.0\config\server.properties ```

### 3) creo la mia topic
``` kafka-topics.bat --zookeeper localhost:2128 --replication-factor 1 --partitions 1 --topic doc-event-handler ```

### 4) compilare il progetto con maven
``` mvn clean install ```

### 5) avviare ils erver wildfly 9 o 10
``` eseguire standalone.bat ```

### 6) fare il deploy su wildfly
``` wildfly:deploy ```

### i logs e eventuali componenti di kafka di default vengono salvate in C:\tmp\kafka-logs


* fonte: https://kafka.apache.org/quickstart

## PROVE MANUALI DA CMD DOS


per provare manualmente una volta tirato su il broker e zookeeper:

``` cd C:/ProgrammiStandalone/kafka-1.1.0/kafka_2.11-1.1.0/bin/windows/ ```

### 1) la topic TEST va prima creata con
``` kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic TEST ```

### 2) aprire console per inviare messaggi sulla topic TEST
``` kafka-console-producer.bat --broker-list localhost:9092 --topic TEST ```

### 3) legge dalla topic test
``` kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic TEST --from-beginning ```

## Link utili
* https://kafka.apache.org/quickstart
* http://cloudurable.com/blog/kafka-architecture-consumers/index.html
* https://dzone.com/articles/kafka-producer-in-java
* https://dzone.com/articles/writing-a-kafka-consumer-in-java?fromrel=true
* https://www.ibm.com/support/knowledgecenter/en/SSZJPZ_11.7.0/com.ibm.swg.im.iis.conn.kafka.usage.doc/topics/Setting_kafka_connector.html
* https://kafka.apache.org/documentation/#connect
* https://www.confluent.io/blog/transactions-apache-kafka/
* http://notes.stephenholiday.com/Kafka.pdf
* https://dzone.com/articles/kafka-clients-at-most-once-at-least-once-exactly-o
* http://cloudurable.com/blog/kafka-tutorial-kafka-producer-advanced-java-examples/index.html
* https://stackoverflow.com/questions/46492707/consumer-how-to-specify-partition-to-read-kafka
* https://kafka.apache.org/0110/javadoc/index.html?org/apache/kafka/clients/consumer/KafkaConsumer.html

## altro
* https://dzone.com/articles/java-ee6-events-lightweight
* https://docs.oracle.com/javaee/7/tutorial/cdi-adv005.htm
* http://entjavastuff.blogspot.com/2011/02/ejb-transaction-management-going-deeper.html
* https://curator.apache.org/curator-test/

## examples:
* https://github.com/sdaschner/scalable-coffee-shop


## PREPARARE WILDFLY
Copiare il modulo org.hsql in modules/system/layers/base/org/hsql/main.

Il modo è costituito da :
### module.xml
### hsqldb.jar

### Contenuto module.xml:
```
<module xmlns="urn:jboss:module:1.3" name="org.hsqldb">

    <resources>
        <resource-root path="hsqldb.jar"/>
    </resources>
    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
        <module name="javax.servlet.api" optional="true"/>
    </dependencies>
</module>
```
Andare in standalone/configuration/standalone.xml ed aggiungere il datasource e i riferimenti al driver:
```
 <datasource jta="true" jndi-name="java:jboss/jdbc/testDS" pool-name="testDS" enabled="true" use-java-context="true">
        <connection-url>jdbc:hsqldb:hsql://localhost:9001/testDS</connection-url>
        <driver>hsqldb</driver>
        <security>
            <user-name>sa</user-name>
        </security>
    </datasource>
    <drivers>
        ...
        <driver name="hsqldb" module="org.hsqldb">
            <driver-class>org.hsqldb.jdbc.JDBCDriver</driver-class>
        </driver>
    </drivers>
```

**- Scaricare il server hsql (stessa versione della libreria jar), eventualmente come client scaricare Squirell
