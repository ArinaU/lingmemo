name := "lingmemo"

version := "1.0"

lazy val `lingmemo` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  ehcache,
  ws,
  specs2 % Test,
  guice)


libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "4.0.0",
  "com.github.tminglei" %% "slick-pg" % "0.19.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "4.0.0"
)

libraryDependencies += "org.webjars" % "jquery" % "2.1.3"

libraryDependencies += "org.postgresql" % "postgresql" % "9.3-1100-jdbc41"


unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")
      