package smb

import org.apache.beam.sdk.coders.CoderRegistry
import org.apache.beam.vendor.guava.v26_0_jre.com.google.common.hash.Hashing
import org.apache.beam.vendor.guava.v26_0_jre.com.google.common.primitives.UnsignedBytes
import smb.schema.Customer

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.util.Comparator

object CheckBucketingAndSorting {
  def main(args: Array[String]): Unit = {
    val comparator: Comparator[Array[Byte]] =
      UnsignedBytes.lexicographicalComparator()

    val hashFunction = Hashing.murmur3_32()

    val coder =
      CoderRegistry.createDefault().getCoder(classOf[java.lang.Integer])

    def encodeKeyBytes(key: java.lang.Integer): Array[Byte] = {
      val baos = new ByteArrayOutputStream()
      try coder.encode(key, baos)
      catch {
        case e: Exception =>
          throw new RuntimeException("Could not encode key " + key, e)
      }
      baos.toByteArray
    }

    def decodeKeyBytes(key: Array[Byte]): java.lang.Integer = {
      val bais = new ByteArrayInputStream(key)
      try coder.decode(bais)
      catch {
        case e: Exception =>
          throw new RuntimeException("Could not decode key " + key, e)
      }
    }

    val customers = List(
      Customer.newBuilder().setUserId(119201541).setCountry("US").build(),
      Customer.newBuilder().setUserId(313816962).setCountry("EN").build(),
      Customer.newBuilder().setUserId(234446596).setCountry("AU").build(),
      Customer.newBuilder().setUserId(920019077).setCountry("US").build()
    )

    val sortedCustomers = customers
      .map(_.get(0).asInstanceOf[java.lang.Integer])
      .map(encodeKeyBytes)
      .sortWith(comparator.compare(_, _) < 0)
      .map(decodeKeyBytes)

    /*
    The sorting is not in integer order
     */

    println("== id(s) in the insertion order ==")
    println(customers.map(_.getUserId))
    println("== sorted id(s) ==")
    println(sortedCustomers)

    /*
    The hash code is different from java.lang.Integer#hashCode
     */

    println("== bucket_id(s) of the keys ==")
    val partitions = customers
      .map(_.get(0).asInstanceOf[java.lang.Integer])
      .map(encodeKeyBytes)
      .map(keyBytes => Math.abs(hashFunction.hashBytes(keyBytes).asInt()) % 4)

    println(partitions)

    /*
    Observed sorting and hash code may be different from expected,
      but it is fine as long as all the values of a certain datatype is treated consistently
      across the Scio system during hashing and ordering.
     */
  }
}
