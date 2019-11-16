package CiProject 
// spark 
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession 
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.functions.{col, udf, expr}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{StructType, StructField, StringType, IntegerType, FloatType}
import org.apache.spark.sql.Row
import org.apache.spark.sql.DataFrame
// scala 
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime

object get_feature_vm_trans{

    def main(args: Array[String]){

        val conf = new SparkConf()
                   .setMaster("local[*]")
                   .setAppName("get_feature_vm_trans")

        var sc = new SparkContext(conf)

        val spark = SparkSession
                    .builder
                    .appName("get_feature_vm_trans")
                    .config("spark.master", "local")
                    .getOrCreate()

        import spark.implicits._

        val accessKeyId = System.getenv("AWS_ACCESS_KEY_ID")
        val secretAccessKey = System.getenv("AWS_SECRET_ACCESS_KEY")
        sc.hadoopConfiguration.set("fs.s3n.awsAccessKeyId", accessKeyId)
        sc.hadoopConfiguration.set("fs.s3n.awsSecretAccessKey", secretAccessKey)

        // load s3 data 
        var s3_file_path = "s3a://suntory-data/etl_test_data/filtered_10_vm_transaction_2019.csv"

        // UDF 
        def load_s3_file_2_df( s3_file_path : String): DataFrame = {
            spark.read
                 .format("csv")
                 .option("header", "true")
                 .option("delimiter", ",")
                 .load(s3_file_path) }

        def upload_df_2_s3( s3_file_name : String, df : DataFrame): Unit = {
            df.coalesce(1)
               .write
               .format("com.databricks.spark.csv")
               .option("header", "true")
               .save(s3_file_name) }
              
        var df = load_s3_file_2_df(s3_file_path)    
        df.printSchema()

        println (">>>>>>>>>> process feature ...")
        df.createOrReplaceTempView("transaction")
        var query = """ SELECT 
                        sales_date,
                        sales_quantity,
                        sales_after_supply,
                        column_no,
                        selling_price
                        FROM TRANSACTION
                        LIMIT 100
                    """
        var df_ = spark.sql(query)
        // process feature 
        val myExpression = "sales_quantity+sales_after_supply"
        var df_feature = df_.withColumn("sales_sum_before_after_supply",expr(myExpression))

        println (">>>>>>>>>> write to s3...")
        var current_time = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm").format(LocalDateTime.now)
        var s3_file_name = "s3a://suntory-data/etl_output/get_feature_vm_trans_" + current_time
        upload_df_2_s3(s3_file_name, df_feature)

        sc.stop()
}
 } 