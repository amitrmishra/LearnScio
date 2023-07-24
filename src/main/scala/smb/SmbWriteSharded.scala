package smb

import com.spotify.scio.{ContextAndArgs, ScioContext}
import org.apache.avro.file.CodecFactory
import org.apache.beam.sdk.extensions.smb.AvroSortedBucketIO
import org.apache.beam.sdk.extensions.smb.BucketMetadata.HashType
import smb.schema.Sales

object SmbWrite {
  import com.spotify.scio.smb._

  def pipeline(cmdLineArgs: Array[String]): ScioContext = {
    val (sc, args) = ContextAndArgs(cmdLineArgs)

    sc.textFile(args("sampleSales"))
      .map(line =>
        Sales
          .newBuilder()
          .setUserId(line.split(",")(0).toInt)
          .setOrderValue(line.split(",")(1).toInt)
          .build()
      )
      .saveAsSortedBucket(
        // Output may also be stored in parquet, which is a more efficient storage when not all the
        //   columns are needed during the read operations
        AvroSortedBucketIO
          .write[Integer, Sales](
            classOf[Integer],
            "userId",
            classOf[Sales]
          )
          .to(args("sampleSalesSmbOut"))
          // Insufficient value may lead to below error:
          //   InMemorySorter buffer exceeded memoryMb limit.
          //   Transferring from in-memory to external sort.
          .withSorterMemoryMb(128)
          .withTempDirectory(sc.options.getTempLocation)
          .withCodec(
            CodecFactory.deflateCodec(CodecFactory.DEFAULT_DEFLATE_LEVEL)
          )
          .withHashType(HashType.MURMUR3_32)
          .withNumBuckets(4)
          .withNumShards(2)
      )

    sc
  }

  def main(cmdLineArgs: Array[String]): Unit = {
    val sc = pipeline(cmdLineArgs)
    sc.run().waitUntilDone()
  }
}
