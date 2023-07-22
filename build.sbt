ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.18"

val beamVersion = "2.48.0"
val flinkVersion = "1.16.0"
val sparkVersion = "3.1.2"
val scioVersion = "0.13.1"

lazy val directRunnerDependencies = Seq(
  "org.apache.beam" % "beam-runners-direct-java" % beamVersion % Runtime
)

lazy val dataflowRunnerDependencies = Seq(
  "org.apache.beam" % "beam-runners-google-cloud-dataflow-java" % beamVersion % Runtime
)

lazy val sparkRunnerDependencies = Seq(
  "org.apache.beam" % "beam-runners-spark-3" % beamVersion % Runtime,
  "org.apache.spark" %% "spark-core" % sparkVersion % Runtime,
  "org.apache.spark" %% "spark-streaming" % sparkVersion % Runtime
)

lazy val flinkRunnerDependencies = Seq(
  "org.apache.beam" % "beam-runners-flink-1.16" % beamVersion % Runtime,
  "org.apache.flink" % "flink-clients" % flinkVersion % Runtime,
  "org.apache.flink" % "flink-streaming-java" % flinkVersion % Runtime
)

lazy val beamRunners = settingKey[String]("beam runners")
lazy val beamRunnersEval = settingKey[Seq[ModuleID]]("beam runners")

def beamRunnerSettings: Seq[Setting[_]] = Seq(
  beamRunners := "",
  beamRunnersEval := {
    Option(beamRunners.value)
      .filter(_.nonEmpty)
      .orElse(sys.props.get("beamRunners"))
      .orElse(sys.env.get("BEAM_RUNNERS"))
      .map(_.split(","))
      .map {
        _.flatMap {
          case "DirectRunner"   => directRunnerDependencies
          case "DataflowRunner" => dataflowRunnerDependencies
          case "SparkRunner"    => sparkRunnerDependencies
          case "FlinkRunner"    => flinkRunnerDependencies
          case _                => Nil
        }.toSeq
      }
      .getOrElse(directRunnerDependencies)
  },
  libraryDependencies ++= beamRunnersEval.value
)

lazy val root = (project in file("."))
  .settings(
    name := "LearnScio",
    // https://mvnrepository.com/artifact/com.spotify/scio-core
    libraryDependencies ++= Seq(
      "com.spotify" %% "scio-core" % scioVersion,
      "com.spotify" %% "scio-smb" % scioVersion,
      "com.spotify" %% "scio-avro" % scioVersion,
      "com.spotify" %% "scio-test" % scioVersion
    ),
    resolvers += "confluent" at "https://packages.confluent.io/maven/"
  )
  .settings(beamRunnerSettings)
  .settings(
    assembly / assemblyJarName := "learn-scio.jar",
    assembly / test := {},
    assembly / assemblyMergeStrategy ~= { old =>
      {
        case PathList(
              "org",
              "apache",
              "beam",
              "sdk",
              "extensions",
              "avro",
              _*
            ) =>
          // prefer beam avro classes from extensions lib instead of ones shipped in runners
          CustomMergeStrategy("BeamAvro") { conflicts =>
            import sbtassembly.Assembly._
            conflicts.collectFirst {
              case Library(
                    ModuleCoordinate(_, "beam-sdks-java-extensions-avro", _),
                    _,
                    t,
                    s
                  ) =>
                JarEntry(t, s)
            } match {
              case Some(e) => Right(Vector(e))
              case None    => Left("Error merging beam avro classes")
            }
          }
        case PathList("org", "checkerframework", _*) =>
          // prefer checker-qual classes packaged in checkerframework libs
          CustomMergeStrategy("CheckerQual") { conflicts =>
            import sbtassembly.Assembly._
            conflicts.collectFirst {
              case Library(
                    ModuleCoordinate("org.checkerframework", _, _),
                    _,
                    t,
                    s
                  ) =>
                JarEntry(t, s)
            } match {
              case Some(e) => Right(Vector(e))
              case None    => Left("Error merging checker-qual classes")
            }
          }
        case PathList("dev", "ludovic", "netlib", "InstanceBuilder.class") =>
          // arbitrary pick last conflicting InstanceBuilder
          MergeStrategy.last
        case s if s.endsWith(".proto") =>
          // arbitrary pick last conflicting proto file
          MergeStrategy.last
        case PathList("git.properties") =>
          // drop conflicting git properties
          MergeStrategy.discard
        case PathList("META-INF", "versions", "9", "module-info.class") =>
          // drop conflicting module-info.class
          MergeStrategy.discard
        case PathList(
              "META-INF",
              "gradle",
              "incremental.annotation.processors"
            ) =>
          // drop conflicting kotlin compiler info
          MergeStrategy.discard
        case PathList("META-INF", "io.netty.versions.properties") =>
          // merge conflicting netty property files
          MergeStrategy.filterDistinctLines
        case PathList("META-INF", "native-image", "native-image.properties") =>
          // merge conflicting native-image property files
          MergeStrategy.filterDistinctLines
        case s => old(s)
      }
    }
  )
