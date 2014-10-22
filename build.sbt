name := "chat-client"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka"      %% "akka-actor"            % "2.3.4",
  "com.typesafe.akka"      %% "akka-slf4j"            % "2.3.4",
  "com.typesafe.akka"      %% "akka-remote"           % "2.3.4",
  "com.typesafe.akka"      %% "akka-cluster"          % "2.3.4",
  "ch.qos.logback"          % "logback-classic"       % "1.0.13"
)

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-Ywarn-dead-code",
  "-language:_",
  "-target:jvm-1.7",
  "-encoding", "UTF-8"
)
