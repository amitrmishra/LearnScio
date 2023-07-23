package smb

import com.spotify.scio.{ContextAndArgs, ScioContext}
import smb.schema.Sales

object RegularAggregate {

  def pipeline(cmdLineArgs: Array[String]): ScioContext = {
    val (sc, args) = ContextAndArgs(cmdLineArgs)

    val sales = sc
      .textFile(args("sales"))
      .map(line =>
        Sales
          .newBuilder()
          .setUserId(line.split(",")(0).toInt)
          .setOrderValue(line.split(",")(1).toInt)
          .build()
      )

    sales
      .map(s => (s.getUserId, s.getOrderValue))
      .reduceByKey(_ + _)
      .map { case (userId, totalOrder) =>
        s"$userId,$totalOrder"
      }
      .saveAsTextFile(args("regularJoinOutput"))

    sc
  }

  def main(cmdLineArgs: Array[String]): Unit = {
    val sc = pipeline(cmdLineArgs)
    sc.run().waitUntilDone()
    ()
  }
}
