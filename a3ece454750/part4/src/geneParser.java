package part4;
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

public class geneParser extends EvalFunc<DataBag>
{

	public static double toDouble(byte[] bytes) throws IOException{
		LoadCaster lc = new Utf8StorageConverter();
		return lc.bytesToDouble(bytes);
	}
	
	public DataBag exec(Tuple input) throws IOException {
		BagFactory mBagFactory = BagFactory.getInstance();
		TupleFactory mTupleFactory = TupleFactory.getInstance();
		DataBag returnBag = mBagFactory.newDefaultBag();
		DataBag genes_array = (DataBag) input.get(0);
		int gene = 1;
		for (Iterator<Tuple> iterator = genes_array.iterator(); iterator.hasNext();) {
			Tuple t = iterator.next();
			double x = toDouble(((DataByteArray)t.get(0)).get());
			Tuple currentTuple = mTupleFactory.newTuple(2);
			currentTuple.set(0, "gene_"+gene);
			currentTuple.set(1, x);
			returnBag.add(currentTuple);
			gene++;
		}
		return returnBag;
	}
}
