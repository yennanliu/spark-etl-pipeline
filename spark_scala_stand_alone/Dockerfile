#------------------------
# SPARK 2.1.0  SCALA
#------------------------

# https://github.com/SRDC-Docker/spark/blob/master/2.1.0/Dockerfile

FROM srdc/scala:2.11.7
MAINTAINER SRDC Corp. <technical@srdc.com.tr>

ENV SPARK_VERSION 2.1.0

RUN curl http://ftp.itu.edu.tr/Mirror/Apache/spark/spark-$SPARK_VERSION/spark-$SPARK_VERSION-bin-hadoop2.7.tgz | tar -xz -C /opt
RUN cd /opt && ln -s ./spark-$SPARK_VERSION-bin-hadoop2.7 spark

ENV SPARK_HOME /opt/spark
ENV PATH $SPARK_HOME/bin:$PATH

CMD ["bash"]