package sparkhelloworld 

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession 

object SparkRDDHelloworld{

	def main(args: Array[String]){

		var conf = new SparkConf().setAppName("SparkRDDHelloworld")
								  .setMaster("spark://172.1.1.1.:7007")
		var spark : SparkSession = SparkSession.builder
											   .config(conf)
											   .config("spark.master", "local")
											   .getOrCreate()
		print (">>>>>>>>>>")
		var rdd = spark.sparkContext.parallelize(Array(1 to 30))
		rdd.collect()
		rdd.count()
		spark.stop()
		print ("SparkRDDHelloworld")
		print (">>>>>>>>>>")
	}
}