package CiProject 
// spark 
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession 
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.functions.{col, udf}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{StructType, StructField, StringType, IntegerType, FloatType}
import org.apache.spark.sql.Row
// scala 
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime

object filter_vm_transaction{

    def main(args: Array[String]){

        val conf = new SparkConf()
                   .setMaster("local[*]")
                   .setAppName("filter_vm_transaction")

        var sc = new SparkContext(conf)

        val spark = SparkSession
                    .builder
                    .appName("Spark sql demo")
                    .config("spark.master", "local")
                    .getOrCreate()

        import spark.implicits._


        val accessKeyId = System.getenv("AWS_ACCESS_KEY_ID")
        val secretAccessKey = System.getenv("AWS_SECRET_ACCESS_KEY")
        sc.hadoopConfiguration.set("fs.s3n.awsAccessKeyId", accessKeyId)
        sc.hadoopConfiguration.set("fs.s3n.awsSecretAccessKey", secretAccessKey)



        println (">>>>>>>>>> write to csv...")
        //var current_time = DateTimeFormatter.ofPattern("yyyy-MM-dd-mm").format(LocalDateTime.now)
        // var current_time = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm").format(LocalDateTime.now)
        // var file_name = "output/SparkHelloWorld/output_" + current_time
        // age.saveAsTextFile(file_name)

        // println (">>>>>>>>>> write to s3...")
        // var s3_file_name = "s3a://suntory-data/etl_output/output_" + current_time
        // filledDF.coalesce(1).write
        //         .format("com.databricks.spark.csv")
        //         .option("header", "true")
        //         .save(s3_file_name)

        // sc.stop() 
}
 } 