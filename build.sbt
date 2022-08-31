ThisBuild / version := "0.1"

ThisBuild / scalaVersion := "3.1.3"

lazy val root =
  (project in file("."))
    .settings(
      name := "education-scala",
      scalacOptions ++= Seq("-Xcheck-macros"),
      libraryDependencies ++= Seq(
        "org.scalameta" %% "munit" % "0.7.29" % Test
      )
    )
