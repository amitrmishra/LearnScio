package smb

import com.spotify.scio.{ContextAndArgs, ScioContext}
import smb.schema.{Customer, TotalSales}

object RegularJoin {

  case class UserSalesCountry(
      userId: Integer,
      totalOrder: Int,
      country: String
  ) {
    override def toString: String = s"$userId\t$totalOrder\t$country"
  }

  def pipeline(cmdLineArgs: Array[String]): ScioContext = {
    val (sc, args) = ContextAndArgs(cmdLineArgs)

    val mapFn: ((Integer, (TotalSales, Customer))) => UserSalesCountry = {
      case (userId, (totalSales, customer)) =>
        UserSalesCountry(
          userId,
          totalSales.getTotalOrder,
          customer.getCountry.toString
        )
    }

    val totalSales = sc
      .textFile(args("totalSales"))
      .map(line =>
        TotalSales
          .newBuilder()
          .setUserId(line.split(",")(0).toInt)
          .setTotalOrder(line.split(",")(1).toInt)
          .build()
      )

    val customers = sc
      .textFile(args("customers"))
      .map(line =>
        Customer
          .newBuilder()
          .setUserId(line.split(",")(0).toInt)
          .setCountry(line.split(",")(1))
          .build()
      )

    totalSales
      .keyBy(_.getUserId)
      .join(customers.keyBy(_.getUserId))
      .map(mapFn)
      .saveAsTextFile(args("regularJoinOutput"))

    sc
  }

  def main(cmdLineArgs: Array[String]): Unit = {
    val sc = pipeline(cmdLineArgs)
    sc.run().waitUntilDone()
    ()
  }
}
