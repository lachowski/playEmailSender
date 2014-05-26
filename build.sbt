name := "playEmailSender"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-email" % "1.3.2",
  cache
)     

play.Project.playJavaSettings
