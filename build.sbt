
name := "ScalaSparkSamples"

version := "0.1"

scalaVersion := "2.11.12"

val scalaTestVersion = "3.0.5"
val sparkVersion = "2.3.0"
val scalazVersion = "7.1.17"
val kafkaVersion = "2.0.0"
val log4jVersion = "1.2.17"

/*resolvers ++= Seq(
    "apache-snapshots" at "https://repository.apache.org/content/repositories/releases/"
)*/

libraryDependencies ++= Seq(
    "org.scalactic" %% "scalactic" % scalaTestVersion,
    "org.apache.spark" %% "spark-core" % sparkVersion,
    "org.apache.spark" %% "spark-sql" % sparkVersion,
    "org.scalatest" %% "scalatest" % scalaTestVersion % "test",

    "org.scalaz" %% "scalaz-core" % scalazVersion,
    "org.scalaz" %% "scalaz-effect" % scalazVersion,
    "org.scalaz" %% "scalaz-typelevel" % scalazVersion,
    "org.scalaz" %% "scalaz-scalacheck-binding" % scalazVersion % "test",

    "org.apache.kafka" % "kafka-clients" % kafkaVersion,
    "log4j" % "log4j" % log4jVersion
)