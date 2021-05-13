# SubmissionManager

Install Docker and docker compose
install java sdk 11

Clone docker compose kafka project

**git clone https://github.com/conduktor/kafka-stack-docker-compose.git**
**cd kafka-stack-docker-compose**
start docker compose
**docker-compose -f zk-single-kafka-single.yml up**

open another terminal

install nexflow https://www.nextflow.io

**clone https://github.com/andreagia/SubmissionQueue.git project**   
set on applictaion.property the directory of nextflow installation


install kafka 
view topic:
**./bin/kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic reflectoring-sendjobs --from-beginning**
**./bin/kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic reflectoring-receivejobs --from-beginning**

view topics
**./bin/kafka-topics.sh --list --zookeeper localhost:2181**