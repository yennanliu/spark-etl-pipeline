from pyspark.streaming import StreamingContext
from pyspark import SparkConf, SparkContext
from pyspark.sql import SQLContext, Row
import socket

# Lazily instantiated global instance of SQLContext
def getSqlContextInstance(sparkContext):
    if ('sqlContextSingletonInstance' not in globals()):
        globals()['sqlContextSingletonInstance'] = SQLContext(sparkContext)
    return globals()['sqlContextSingletonInstance']

def process(time, rdd):
    print("========= %s =========" % str(time))
    try:
        # Get the singleton instance of SQLContext
        sqlContext = getSqlContextInstance(rdd.context)

        # Convert RDD[String] to RDD[Row] to DataFrame
        rowRdd = rdd.map(lambda w: Row(word=w))
        wordsDataFrame = sqlContext.createDataFrame(rowRdd)

        # Register as table
        wordsDataFrame.registerTempTable("words")

        # Do word count on table using SQL and print it
        wordCountsDataFrame = sqlContext.sql("select word, count(*) as total from words group by word")
        wordCountsDataFrame.show()
    except:
        pass

if __name__ == '__main__':
	sc = SparkContext('local[2]', 'NetworkWordCount')
	print (sc)	  
	ssc = StreamingContext(sc, 60) # with batch duration = e.g. 60s
	# set where the data streaming will come from e.g. localhost:9999
	#lines = ssc.socketTextStream('localhost', 9999)
	lines = ssc.socketTextStream(socket.gethostname(), 1243)
	# split the 'lines' with a whitespace into a list of words
	words = lines.flatMap(lambda line: line.split(' '))
	# create a tuple of each word and 1 using 'map'
	# e.g. word_0 --> (word_0, 1)
	pairs = words.map(lambda word: (word, 1))
	# count the words using reduceByKey e.g. by 'word_0', 'word_1'
	word_counts = pairs.reduceByKey(lambda num1, num2: num1 + num2)
	# print elements of the RDD
	word_counts.pprint()
	print (word_counts)
	words.foreachRDD(process)
	ssc.start()
	ssc.awaitTermination()  # Wait for the computation to terminate