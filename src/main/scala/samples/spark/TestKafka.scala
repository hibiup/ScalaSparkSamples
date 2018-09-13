package samples.spark

import scalaz.Monad

import scala.io.Source

trait Config[A] { self =>
    def content: A
    // Monad
    def flatMap[B](f: A => Config[B]): Config[B] = new Config[B] {
        def content = f(self.content).content
    }
}

object Config {
    def apply[A](a: A) = new Config[A] { def content = a }
    implicit object bagMonad extends Monad[Config] {
        /** point 函数生成高阶类型: Bag[A] **/
        def point[A](a: => A) = Config(a)

        /** bind 从 point 生成的 Bag[A] 中读取数据，然后根据 Bag.flatMap 提供的 f 转成 Bag[B] **/
        def bind[A,B](bag: Config[A])(f: A => Config[B]): Config[B] = bag flatMap f
    }
}

        /*val configFile = Source.fromFile(path, "UTF-8")
        val config = new Configuration[String] { def content = configFile.getLines.mkString }
        configFile.close()
        config*/


/*    object Config {
        val topic = "my-topic"
        val config = {
        val props = new Properties
        props.put ("bootstrap.servers", "192.168.107.3:9092")
        props.put ("zookeeper.connect", "192.168.107.3:2181")
        props.put ("group.id", "Group_01")
        props.put ("client.id", "Client_01")
        props.put ("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer")
        props.put ("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
        props
}   }

    object Produce {
        val props = Config()
        val producer = new KafkaProducer[Integer, String](props)
    }*/
