package samples.spark

import org.apache.spark.sql.SparkSession
import org.scalatest.FlatSpec
import java.util.Random

class TestGroupBy extends FlatSpec{
    "spark groupby()" should "" in {
        val spark = SparkSession
            .builder
            .appName("GroupBy Test")
            .master("local[2]")
            .getOrCreate()

        val numMappers = 2
        val numKVPairs = 1000
        val valSize = 1000
        val numReducers = numMappers

        val pairs1 = spark.sparkContext.parallelize(0 until numMappers, numMappers).flatMap { p =>
            val ranGen = new Random
            val arr1 = new Array[(Int, Array[Byte])](numKVPairs)
            for (i <- 0 until numKVPairs) {
                val byteArr = new Array[Byte](valSize)
                ranGen.nextBytes(byteArr)
                arr1(i) = (ranGen.nextInt(Int.MaxValue), byteArr)
            }
            arr1
        }.cache()
        // Enforce that everything has been calculated and in cache
        pairs1.count()

        println(pairs1.groupByKey(numReducers).count())

        spark.stop()
    }

    "spark word count" should "" in {
        val spark = SparkSession
                .builder
                .appName("GroupBy Test")
                .master("local[2]")
                .getOrCreate()

        val res = spark.sparkContext.textFile("README.md")
            .flatMap(_.split(" "))  // _.split("_") 以行为单位将内容按空格切割开返回一个包含每个词的 List（以行为单位）。 flatMap 扁平化这个 List 变成以单个词为单位
            .map((_,1))
            .reduceByKey(_ + _)
        println(res.collect.foreach(println) )
    }
}
