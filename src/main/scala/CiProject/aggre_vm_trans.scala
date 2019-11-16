package CiProject 
// spark 
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession 
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.functions.{col, udf}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{StructType, StructField, StringType, IntegerType, FloatType}
import org.apache.spark.sql.Row
import org.apache.spark.sql.DataFrame
// scala 
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
// UDF 
import util.SparkS3Utility

object aggre_vm_trans{

    def main(args: Array[String]){

        val conf = new SparkConf()
                   .setMaster("local[*]")
                   .setAppName("aggre_vm_trans")

        var sc = new SparkContext(conf)

        val spark = SparkSession
                    .builder
                    .appName("aggre_vm_trans")
                    .config("spark.master", "local")
                    .getOrCreate()

        import spark.implicits._

        val accessKeyId = System.getenv("AWS_ACCESS_KEY_ID")
        val secretAccessKey = System.getenv("AWS_SECRET_ACCESS_KEY")
        sc.hadoopConfiguration.set("fs.s3n.awsAccessKeyId", accessKeyId)
        sc.hadoopConfiguration.set("fs.s3n.awsSecretAccessKey", secretAccessKey)

        // load s3 data 
        var s3_file_path = "s3a://suntory-data/etl_test_data/filtered_10_vm_transaction_2019.csv"

        var df = SparkS3Utility.load_s3_file_2_df(s3_file_path)

        df.printSchema()

        println (">>>>>>>>>> write to s3...")
        df.createOrReplaceTempView("transaction")
        
        var query = """ SELECT 
                sales_date AS sales_date,
                sum(sales_quantity) AS day_sale_quantity,
                sum(sales_after_supply) AS day_sales_after_supply
                FROM TRANSACTION
                GROUP BY 1
                ORDER BY 1
                LIMIT 10"""
        var df_ = spark.sql(query)
        var current_time = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm").format(LocalDateTime.now)
        var s3_file_name = "s3a://suntory-data/etl_output/aggre_vm_trans_" + current_time

        SparkS3Utility.upload_df_2_s3(s3_file_name, df_)

        sc.stop()
}
 } 