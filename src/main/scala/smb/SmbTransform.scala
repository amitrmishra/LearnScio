package smb

import com.spotify.scio.{ContextAndArgs, ScioContext}
import org.apache.avro.generic.GenericRecord
import org.apache.beam.sdk.extensions.smb.{
  AvroSortedBucketIO,
  TargetParallelism
}
import org.apache.beam.sdk.values.TupleTag
import smb.schema.{Account, Schemas}

object SmbTransform {
  import com.spotify.scio.smb._

  def pipeline(cmdLineArgs: Array[String]): ScioContext = {
    val (sc, args) = ContextAndArgs(cmdLineArgs)

    // #SortMergeBucketExample_transform
    val (readLhs, readRhs) = (
      AvroSortedBucketIO
        .read(
          new TupleTag[GenericRecord]("lhs"),
          Schemas.TotalSalesSchema
        )
        .from(args("users")),
      AvroSortedBucketIO
        .read(new TupleTag[Account]("rhs"), classOf[Account])
        .from(args("accounts"))
    )

    sc.sortMergeTransform(
      classOf[String],
      readLhs,
      readRhs,
      TargetParallelism.auto()
    ).to(
      AvroSortedBucketIO
        .transformOutput(classOf[String], "name", classOf[Account])
        .to(args("output"))
    ).via { case (key, (users, accounts), outputCollector) =>
      users.foreach { _ =>
        outputCollector.accept(
          Account
            .newBuilder()
            .setId(key.toInt)
            .setName(key)
            .setType("combinedAmount")
            .setAmount(accounts.foldLeft(0.0)(_ + _.getAmount))
            .build()
        )
      }
    }
    // #SortMergeBucketExample_transform
    sc
  }

  def main(cmdLineArgs: Array[String]): Unit = {
    val sc = pipeline(cmdLineArgs)
    sc.run().waitUntilDone()
    ()
  }
}
