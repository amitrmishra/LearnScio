package smb

import com.spotify.scio.{ContextAndArgs, ScioContext}
import org.apache.beam.sdk.extensions.smb.{
  AvroSortedBucketIO,
  TargetParallelism
}
import org.apache.beam.sdk.values.TupleTag
import smb.schema.{Sales, TotalSales}

object SmbAggregate {
  import com.spotify.scio.smb._

  def pipeline(cmdLineArgs: Array[String]): ScioContext = {
    val (sc, args) = ContextAndArgs(cmdLineArgs)

    // #SortMergeBucketExample_transform
    sc.sortMergeTransform(
      classOf[Integer],
      AvroSortedBucketIO
        .read(new TupleTag[Sales]("sales"), classOf[Sales])
        .from(args("salesSmb")),
      // With, TargetParallelism.auto() number of output files (ie. buckets) may be different from
      //   the number of source buckets.
      // Eg: source may have 8 but target will have 4 (both will be power of 2 however and,
      //   hence compatible)
      TargetParallelism.max()
    ).to(
      AvroSortedBucketIO
        .transformOutput(classOf[Integer], "userId", classOf[TotalSales])
        .to(args("smbAggregateOutput"))
    ).via { case (key, sales, outputCollector) =>
      outputCollector.accept(
        TotalSales
          .newBuilder()
          .setUserId(key)
          .setTotalOrder(sales.map(_.getOrderValue).reduce(_ + _))
          .build()
      )
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
