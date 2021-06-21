lazy val root = (project in file("."))
  .enablePlugins(PlayJava)
  .enablePlugins(PlayEbean)
  .settings(
    name := """recipe-manager-api""",
    version := "2.0",
    scalaVersion := "2.13.4",
    libraryDependencies ++= Seq(
      guice,
      // Test Database
      "com.h2database" % "h2" % "1.4.200",
      // Testing libraries for dealing with CompletionStage...
      "org.assertj" % "assertj-core" % "3.14.0" % Test,
      "org.awaitility" % "awaitility" % "4.0.1" % Test,
      "org.glassfish.jaxb" % "jaxb-core" % "2.3.0.1",
      "org.glassfish.jaxb" % "jaxb-runtime" % "2.3.2",
      evolutions,
      jdbc
    ),
    javacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-parameters",
      "-Xlint:unchecked",
      "-Xlint:deprecation",
      "-Werror"
    ),
    // Make verbose tests
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )
