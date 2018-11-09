package samples.spark

import java.io.FileInputStream
import java.time.Duration
import java.util.{Collections, Properties}

import org.apache.kafka.clients.consumer.{KafkaConsumer}
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}
import org.scalatest.FlatSpec

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global

class TestKafka extends FlatSpec{
    def productMessage(message:String, client_id:String)(implicit props:Properties): Unit = {
        implicit val callback = new Callback {
            override def onCompletion(metadata: RecordMetadata, ex: Exception) = {
                if (ex != null) ex.printStackTrace()
                else println(s"Successfully sent message : $metadata")
            }
        }

        props.put("client.id", client_id)
        val producer = new KafkaProducer[Int, String](props)

        producer send(
          new ProducerRecord[Int, String]("test", "Hello!"),
          callback
        )
        producer send(
          new ProducerRecord[Int, String]("test", "World!"),
          callback
        )
        producer send(
          new ProducerRecord[Int, String]("test", "End!"),
          callback
        )
    }

    def consumerMessage(client_id:String)(implicit p:Promise[Boolean], props:Properties): Unit = {
        import util.control.Breaks._

        props.put("client.id", client_id)
        val consumer = new KafkaConsumer[Int, String](props)
        consumer.subscribe(Collections.singletonList("test"))
        breakable { while(true) {
            val consumerRecords = consumer poll Duration.ofMillis(1000)
            val records = consumerRecords.iterator()
            while (records.hasNext) {
                val r = records.next()
                println(s"[${r.timestamp()}] - ${r.topic()}-${r.partition()}: ${r.key()}=${r.value()}")
                if (r.value() == "End!") {
                    p.success(true)
                    break
                }
            }
            consumer.commitSync
        }}
    }

    "A basic Kafka producer/consumer" should "" in {
        implicit val props = new Properties()
        props.load(new FileInputStream("src/test/resources/kafka.properties"))

        implicit val promise = Promise[Boolean]()
        val f = Future {
            consumerMessage("test-consumer-1")
            promise.future
        }
        f.onComplete(x => println(s"Done by $x"))

        productMessage("Hello!", "test-producer-1")
        Await.result(f, scala.concurrent.duration.Duration.Inf)
    }
}
