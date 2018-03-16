import akka.http.grpc.javadsl.JavaServerCodeGenerator
import protocbridge.Target

enablePlugins(JavaAgent)
enablePlugins(AkkaGrpcPlugin)

javaAgents += "org.mortbay.jetty.alpn" % "jetty-alpn-agent" % "2.0.7" % "runtime"

inConfig(Compile)(Seq(
  // does not seem to work :( added a symlink for now.
  PB.includePaths += new File("src/main/proto"),
  akkaGrpcCodeGenerators := GeneratorAndSettings(JavaServerCodeGenerator) :: Nil,
  akkaGrpcModelGenerators := Seq[Target](PB.gens.java -> sourceManaged.value),
))

val root = project.in(file("."))
  .dependsOn(
    ProjectRef(file("../"), "akka-grpc-runtime"),
    ProjectRef(file("../"), "akka-grpc-codegen"),
  )

val grpcVersion = "1.10.0"

// for loading of cert, issue #89
libraryDependencies += "io.grpc" % "grpc-testing" % grpcVersion
