package smb.schema

import org.apache.avro.Schema
import org.apache.avro.generic.{GenericData, GenericRecord}

object Schemas {
  lazy val TotalSalesSchema: Schema = new Schema.Parser().parse(
    """
      |{
      |    "name": "TotalSales",
      |    "namespace": "smb.examples",
      |    "type": "record",
      |    "fields": [
      |        {
      |          "name": "userId", "type": "int"
      |        },
      |        {
      |          "name": "totalOrder", "type": "int"
      |        }
      |    ]}
      |""".stripMargin
  )

  lazy val CustomersSchema: Schema = new Schema.Parser().parse(
    """
      |{
      |    "name": "Customers",
      |    "namespace": "smb.examples",
      |    "type": "record",
      |    "fields": [
      |        {
      |          "name": "userId", "type": "int"
      |        },
      |        {
      |            "name": "country",
      |            "type": ["null", {"type": "string", "avro.java.string": "String"}]
      |        }
      |    ]}
      |""".stripMargin
  )

  def totalSales(userId: Int, totalOrder: Int): GenericRecord = {
    val gr = new GenericData.Record(TotalSalesSchema)
    gr.put("userId", userId)
    gr.put("totalOrder", totalOrder)
    gr
  }

  def customer(userId: Int, country: String): GenericRecord = {
    val gr = new GenericData.Record(CustomersSchema)
    gr.put("userId", userId)
    gr.put("country", country)
    gr
  }
}
