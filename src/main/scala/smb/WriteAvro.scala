package smb

import com.spotify.scio.avro._
import com.spotify.scio.{ContextAndArgs, ScioContext}
import smb.schema.Sales

object WriteAvro {

  def pipeline(cmdLineArgs: Array[String]): ScioContext = {
    val (sc, args) = ContextAndArgs(cmdLineArgs)

    sc.textFile(args("sales"))
      .map(line =>
        Sales
          .newBuilder()
          .setUserId(line.split(",")(0).toInt)
          .setOrderValue(line.split(",")(1).toInt)
          .build()
      )
      .saveAsAvroFile(args("salesAvroOut"))

    sc
  }

  def main(cmdLineArgs: Array[String]): Unit = {
    val sc = pipeline(cmdLineArgs)
    sc.run().waitUntilDone()
  }
}
