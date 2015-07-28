package part2;
import java.io.IOException;
import java.util.Iterator;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.apache.pig.EvalFunc;
import org.apache.hadoop.io.WritableComparable;
import org.apache.pig.data.Tuple;
import org.apache.pig.LoadCaster;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.builtin.Utf8StorageConverter;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.data.BagFactory;

public class geneBooler extends EvalFunc<Integer>
{

	public static double toDouble(byte[] bytes) throws IOException{
		LoadCaster lc = new Utf8StorageConverter();
		return lc.bytesToDouble(bytes);
	}
	
	public Integer exec(Tuple input) throws IOException {
		double gene = (double)input.get(0);
		if (gene > 0.5d) {
			return 1;
		} else {
			return 0;
		}
	}
}
