  
name := "spark-etl-pipeline"

version := "1.0"

scalaVersion := "2.11.8"


libraryDependencies ++= Seq(

  "org.apache.spark" %% "spark-core" % "2.2.0",
  "com.typesafe" % "config" % "1.2.1"
)

conflictManager := ConflictManager.latestRevision

//mainClass := Some("rdd.WordCount")

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}