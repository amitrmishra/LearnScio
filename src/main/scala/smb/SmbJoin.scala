package smb

import com.spotify.scio.{ContextAndArgs, ScioContext}
import org.apache.beam.sdk.extensions.smb.{
  AvroSortedBucketIO,
  TargetParallelism
}
import org.apache.beam.sdk.values.TupleTag
import smb.schema.{Customer, TotalSales}

object SmbJoin {
  import com.spotify.scio.smb._

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

    // #SortMergeBucketExample_join
    sc.sortMergeJoin(
      classOf[Integer],
      AvroSortedBucketIO
        .read(
          new TupleTag[TotalSales]("lhs"),
          classOf[TotalSales]
        )
        .from(args("totalSalesSmb")),
      AvroSortedBucketIO
        .read(new TupleTag[Customer]("rhs"), classOf[Customer])
        .from(args("customersSmb")),
      TargetParallelism.max()
    ).map(mapFn) // Apply mapping function
      .saveAsTextFile(args("smbJoinOutput"))
    // #SortMergeBucketExample_join

    sc
  }

  def main(cmdLineArgs: Array[String]): Unit = {
    val sc = pipeline(cmdLineArgs)
    sc.run().waitUntilDone()
    ()
  }
}
