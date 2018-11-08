package samples.spark

import java.io.FileInputStream
import java.util.Properties

import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}
import org.scalatest.FlatSpec

class TestKafka extends FlatSpec{
    "A basic Kafka producer" should "" in {
        val props = new Properties()
        props.load(new FileInputStream("src/test/resources/kafka.properties"))

        val callback = new Callback {
            override def onCompletion(metadata: RecordMetadata, ex: Exception) = {
                if (ex != null) ex.printStackTrace()
                else println(s"Successfully sent message : $metadata")
                this.notify()
            }
        }

        val producer = new KafkaProducer[Int, String](props)
        val message = new ProducerRecord[Int, String]("test", 1, "Hello!")
        producer.send(message, callback)

        callback.wait()
    }
}
