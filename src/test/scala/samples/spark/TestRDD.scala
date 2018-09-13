package samples.spark

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession
import org.scalatest.FlatSpec

class TestRDD extends FlatSpec{
    val conf = new SparkConf().setAppName("RDD Sample").setMaster("local[2]")
    val sc = new SparkContext(conf)

    "spark RDD" should "be able to convert a Seq to RDD" in {
        val rdd = sc.parallelize(1 to 10)

        println(rdd)
        println(rdd.collect)
        println(rdd.map(n => (n, 1)).reduceByKey( _ + _))
    }
}
