package util

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

object SparkS3Utility {

        val spark = SparkSession
                    .builder
                    .appName("aggre_vm_trans")
                    .config("spark.master", "local")
                    .getOrCreate()

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
}
