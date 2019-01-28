import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableUtils;

/**
 * Wraps and Tags a String in an object that can be processed by hadoop for a join-statement.
 */
public class DatasetTagger implements Writable {
	/**
	 * Enum to determine on which side the wrapped value is.
	 */
	public static enum Side {RIGHT, LEFT};
	
	private String value;
	private Side side;
 
	public DatasetTagger() {
	}
 
	public DatasetTagger(String value, Side side) {
		this.value = value;
		this.side = side;
	}
 
	public void readFields(DataInput dataInput) throws IOException {
		value = WritableUtils.readString(dataInput);
		side = WritableUtils.readEnum(dataInput, Side.class);
	}
 
	public void write(DataOutput dataOutput) throws IOException {
		WritableUtils.writeString(dataOutput, value);
		WritableUtils.writeEnum(dataOutput, side);
	}
 
	public String getValue() {
		return value;
	}
 
	public void setValue(String value) {
		this.value = value;
	}

	public Side getSide() {
		return side;
	}

	public void setSide(Side side) {
		this.side = side;
	}

	@Override
	public String toString() {
		return "DatasetTag [value=" + value + ", side=" + side + "]";
	}
	
	
}
