/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package smb.schema;

import org.apache.avro.specific.SpecificData;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class TotalSales extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 6666939121626904119L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"TotalSales\",\"namespace\":\"smb.schema\",\"fields\":[{\"name\":\"userId\",\"type\":\"int\"},{\"name\":\"totalOrder\",\"type\":\"int\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<TotalSales> ENCODER =
      new BinaryMessageEncoder<TotalSales>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<TotalSales> DECODER =
      new BinaryMessageDecoder<TotalSales>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<TotalSales> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<TotalSales> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<TotalSales>(MODEL$, SCHEMA$, resolver);
  }

  /** Serializes this TotalSales to a ByteBuffer. */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /** Deserializes a TotalSales from a ByteBuffer. */
  public static TotalSales fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public int userId;
  @Deprecated public int totalOrder;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public TotalSales() {}

  /**
   * All-args constructor.
   * @param userId The new value for userId
   * @param totalOrder The new value for totalOrder
   */
  public TotalSales(java.lang.Integer userId, java.lang.Integer totalOrder) {
    this.userId = userId;
    this.totalOrder = totalOrder;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return userId;
    case 1: return totalOrder;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: userId = (java.lang.Integer)value$; break;
    case 1: totalOrder = (java.lang.Integer)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'userId' field.
   * @return The value of the 'userId' field.
   */
  public java.lang.Integer getUserId() {
    return userId;
  }

  /**
   * Sets the value of the 'userId' field.
   * @param value the value to set.
   */
  public void setUserId(java.lang.Integer value) {
    this.userId = value;
  }

  /**
   * Gets the value of the 'totalOrder' field.
   * @return The value of the 'totalOrder' field.
   */
  public java.lang.Integer getTotalOrder() {
    return totalOrder;
  }

  /**
   * Sets the value of the 'totalOrder' field.
   * @param value the value to set.
   */
  public void setTotalOrder(java.lang.Integer value) {
    this.totalOrder = value;
  }

  /**
   * Creates a new TotalSales RecordBuilder.
   * @return A new TotalSales RecordBuilder
   */
  public static smb.schema.TotalSales.Builder newBuilder() {
    return new smb.schema.TotalSales.Builder();
  }

  /**
   * Creates a new TotalSales RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new TotalSales RecordBuilder
   */
  public static smb.schema.TotalSales.Builder newBuilder(smb.schema.TotalSales.Builder other) {
    return new smb.schema.TotalSales.Builder(other);
  }

  /**
   * Creates a new TotalSales RecordBuilder by copying an existing TotalSales instance.
   * @param other The existing instance to copy.
   * @return A new TotalSales RecordBuilder
   */
  public static smb.schema.TotalSales.Builder newBuilder(smb.schema.TotalSales other) {
    return new smb.schema.TotalSales.Builder(other);
  }

  /**
   * RecordBuilder for TotalSales instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<TotalSales>
    implements org.apache.avro.data.RecordBuilder<TotalSales> {

    private int userId;
    private int totalOrder;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(smb.schema.TotalSales.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.userId)) {
        this.userId = data().deepCopy(fields()[0].schema(), other.userId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.totalOrder)) {
        this.totalOrder = data().deepCopy(fields()[1].schema(), other.totalOrder);
        fieldSetFlags()[1] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing TotalSales instance
     * @param other The existing instance to copy.
     */
    private Builder(smb.schema.TotalSales other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.userId)) {
        this.userId = data().deepCopy(fields()[0].schema(), other.userId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.totalOrder)) {
        this.totalOrder = data().deepCopy(fields()[1].schema(), other.totalOrder);
        fieldSetFlags()[1] = true;
      }
    }

    /**
      * Gets the value of the 'userId' field.
      * @return The value.
      */
    public java.lang.Integer getUserId() {
      return userId;
    }

    /**
      * Sets the value of the 'userId' field.
      * @param value The value of 'userId'.
      * @return This builder.
      */
    public smb.schema.TotalSales.Builder setUserId(int value) {
      validate(fields()[0], value);
      this.userId = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'userId' field has been set.
      * @return True if the 'userId' field has been set, false otherwise.
      */
    public boolean hasUserId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'userId' field.
      * @return This builder.
      */
    public smb.schema.TotalSales.Builder clearUserId() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'totalOrder' field.
      * @return The value.
      */
    public java.lang.Integer getTotalOrder() {
      return totalOrder;
    }

    /**
      * Sets the value of the 'totalOrder' field.
      * @param value The value of 'totalOrder'.
      * @return This builder.
      */
    public smb.schema.TotalSales.Builder setTotalOrder(int value) {
      validate(fields()[1], value);
      this.totalOrder = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'totalOrder' field has been set.
      * @return True if the 'totalOrder' field has been set, false otherwise.
      */
    public boolean hasTotalOrder() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'totalOrder' field.
      * @return This builder.
      */
    public smb.schema.TotalSales.Builder clearTotalOrder() {
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TotalSales build() {
      try {
        TotalSales record = new TotalSales();
        record.userId = fieldSetFlags()[0] ? this.userId : (java.lang.Integer) defaultValue(fields()[0]);
        record.totalOrder = fieldSetFlags()[1] ? this.totalOrder : (java.lang.Integer) defaultValue(fields()[1]);
        return record;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<TotalSales>
    WRITER$ = (org.apache.avro.io.DatumWriter<TotalSales>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<TotalSales>
    READER$ = (org.apache.avro.io.DatumReader<TotalSales>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}
