  
name := "spark-etl-pipeline"

version := "1.0"

scalaVersion := "2.11.8"

val sparkVersion = "2.3.0"

libraryDependencies ++= Seq(

  "org.apache.spark" %% "spark-core" % "2.3.0",
  "com.typesafe" % "config" % "1.2.1",
  "org.scalactic" %% "scalactic" % "3.0.8",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "org.apache.spark" %% "spark-sql" % "2.3.0",
  "org.apache.spark" %% "spark-mllib" % "2.2.0"
)

conflictManager := ConflictManager.latestRevision

//mainClass := Some("rdd.WordCount")

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}