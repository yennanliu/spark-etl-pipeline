package sparkhelloworld

// import required spark classes
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
 
// define main method (Spark entry point)
object SparkHelloWorld {
  def main(args: Array[String]) {
 
    // initialise spark context
    val conf = new SparkConf().setAppName("SparkHelloWorld")
                              .setMaster("spark://172.1.1.1:7077")
    val spark: SparkSession = SparkSession.builder
                                          .config(conf)
                                          .config("spark.master", "local")
                                          .getOrCreate()
    // do stuff
    println("************")
    println("************")
    println("Hello, world!")
    val rdd = spark.sparkContext.parallelize(Array(1 to 10))
    rdd.count()
    println("************")
    println("************")   
    // terminate spark context
    spark.stop()
    
  }
}