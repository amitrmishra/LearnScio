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
      TargetParallelism.auto()
    ).to(
      AvroSortedBucketIO
        .transformOutput(classOf[Integer], "userId", classOf[TotalSales])
        .to(args("salesSmb"))
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
