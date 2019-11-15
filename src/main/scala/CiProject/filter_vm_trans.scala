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

object filter_vm_trans{

    def main(args: Array[String]){

        val conf = new SparkConf()
                   .setMaster("local[*]")
                   .setAppName("filter_vm_trans")

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

        val dataSchema = (new StructType)
                        .add("group_company_code" ,StringType)
                        .add("wireless_sales_slip_no" ,StringType)
                        .add("column_no" ,StringType)
                        .add("product_code" ,StringType)
                        .add("full_tank_number" ,StringType)
                        .add("selling_price" ,StringType)
                        .add("meter_no" ,StringType)
                        .add("hot_cold_classification" ,StringType)
                        .add("last_visit_inventory" ,StringType)
                        .add("sales_quantity" ,StringType)
                        .add("sales_after_supply" ,StringType)
                        .add("product_code_before_replacement" ,StringType)
                        .add("full_tank_quantity_before_product_replacement" ,StringType)
                        .add("number_of_products_introduced_before_product_replacement" ,StringType)
                        .add("price_before_replacement" ,StringType)
                        .add("meter_no_before_product_replacement" ,StringType)
                        .add("hot_cold_classification_before_product_replacement" ,StringType)
                        .add("site_code" ,StringType)
                        .add("department_code" ,StringType)
                        .add("route_code" ,StringType)
                        .add("customer_number" ,StringType)
                        .add("branch_number" ,StringType)
                        .add("equipment_code" ,StringType)
                        .add("sales_date" ,StringType)
                        .add("last_visit_date" ,StringType)
                        .add("last_calibration_date" ,StringType)
                        .add("number_of_sales_update_failure" ,StringType)
                        .add("buying_place_code" ,StringType)
                        .add("sales_system_representative_code" ,StringType)
                        .add("sales_method_detail_code" ,StringType)
                        .add("in_out_classification" ,StringType)
                        .add("annual_contribution_sales_capacity_conversion" ,StringType)
                        .add("open_closed_classification" ,StringType)
                        .add("contents_manufacturer_code" ,StringType)
                        .add("installation_date" ,StringType)
                        .add("number_of_adjacent_vm_cc" ,StringType)
                        .add("number_of_adjacent_vm_k" ,StringType)
                        .add("number_of_adjacent_vm_a" ,StringType)
                        .add("number_of_adjacent_vm_dy" ,StringType)
                        .add("number_of_adjacent_vm_it" ,StringType)
                        .add("number_of_adjacent_vm_po" ,StringType)
                        .add("number_of_adjacent_vm_ot" ,StringType)
                        .add("number_of_adjacent_vm_sf" ,StringType)
                        .add("other_num" ,StringType)

        // load s3 data 
        var s3_file_path = "s3a://suntory-data/etl_test_data/filtered_10_vm_transaction_2019.csv.zip"
        // val df = spark.read.format("csv")
        //                    .schema(dataSchema)
        //                    .option("sep", "\t")
        //                    .option("header", "true")
        //                    .option("inferSchema", "true")
        //                    .option("delimiter", ",")
        //                    .option("compression","gzip")
        //                    .load(s3_file_path)
        //                    // .option("multiline", "true")

          val df = spark.read
                        .option("sep", "\t")
                        .option("header", "true")
                        .schema(dataSchema)
                        .csv(s3_file_path)

        df.printSchema()
        df.createOrReplaceTempView("transaction")
        var df_ = spark.sql("SELECT * FROM transaction limit 10")
        spark.sql("SELECT * FROM transaction limit 10").show()

        println (">>>>>>>>>> write to s3...")
        var current_time = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm").format(LocalDateTime.now)
        var s3_file_name = "s3a://suntory-data/etl_output/filter_vm_transaction_outout_" + current_time
        df_.coalesce(1)
           .write
           .format("com.databricks.spark.csv")
           .option("header", "true")
           .save(s3_file_name)

        sc.stop()
}
 } 