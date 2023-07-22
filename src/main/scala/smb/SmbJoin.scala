package smb

import com.spotify.scio.{ContextAndArgs, ScioContext}
import com.spotify.scio.coders.Coder
import org.apache.avro.generic.GenericRecord
import org.apache.beam.sdk.extensions.smb.{
  AvroSortedBucketIO,
  TargetParallelism
}
import org.apache.beam.sdk.values.TupleTag
import smb.schema.{Account, Customer, Schemas, TotalSales}

object SmbJoin {
  import com.spotify.scio.smb._

//  implicit val coder: Coder[GenericRecord] =
//    Coder.avroGenericRecordCoder(Schemas.UserDataSchema)

  case class UserSalesCountry(
      userId: String,
      totalOrder: Int,
      country: String
  ) {
    override def toString: String = s"$userId\t$totalOrder\t$country"
  }

  def pipeline(cmdLineArgs: Array[String]): ScioContext = {
    val (sc, args) = ContextAndArgs(cmdLineArgs)

    val mapFn: ((String, (TotalSales, Customer))) => UserSalesCountry = {
      case (userId, (totalSales, customer)) =>
        UserSalesCountry(
          userId,
          totalSales.getTotalOrder,
          customer.getCountry.toString
        )
    }

    // #SortMergeBucketExample_join
    sc.sortMergeJoin(
      classOf[String],
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
