package part4;
import java.io.IOException;
import java.util.Iterator;
import java.nio.ByteBuffer;
import org.apache.pig.EvalFunc;
import org.apache.hadoop.io.WritableComparable;
import org.apache.pig.data.Tuple;
import org.apache.pig.LoadCaster;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataByteArray;
import org.apache.pig.builtin.Utf8StorageConverter;

public class sampleMax extends EvalFunc<String>
{

	public static double toDouble(byte[] bytes) throws IOException{
		LoadCaster lc = new Utf8StorageConverter();
		return lc.bytesToDouble(bytes);
	}
	
	public String exec(Tuple input) throws IOException {
		StringBuilder sb = new StringBuilder();
		DataBag genes_array = (DataBag) input.get(0);	
		double max = 0.00;
		int gene = 1;
		for (Iterator<Tuple> iterator = genes_array.iterator(); iterator.hasNext();) {
			Tuple t = iterator.next();
			double x = toDouble(((DataByteArray)t.get(0)).get());
			if (x > max) {
				max = x;
				sb.setLength(0);
				sb.append("gene_"+gene);
			}
			else if (x == max) {
				sb.append(",gene_"+gene);
			}
			gene++;
		}
		return sb.toString();
	}
}
