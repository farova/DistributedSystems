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

public class geneSimilarity extends EvalFunc<Tuple>
{

	public static double toDouble(byte[] bytes) throws IOException{
		LoadCaster lc = new Utf8StorageConverter();
		return lc.bytesToDouble(bytes);
	}

	public static String toString(byte[] bytes) throws IOException{
		LoadCaster lc = new Utf8StorageConverter();
		return lc.bytesToCharArray(bytes);
	}
	
	public Tuple exec(Tuple input) throws IOException {
		BagFactory mBagFactory = BagFactory.getInstance();
		TupleFactory mTupleFactory = TupleFactory.getInstance();
		String sampleOne = toString(((DataByteArray)input.get(0)).get());
		DataBag sampleOne_genes = (DataBag) input.get(1);
		String sampleTwo = toString(((DataByteArray)input.get(2)).get());
		DataBag sampleTwo_genes = (DataBag) input.get(3);
	
		ArrayList<Double> firstBag = new ArrayList<Double>();
		ArrayList<Double> secondBag = new ArrayList<Double>();

		Tuple returnTuple = mTupleFactory.newTuple(3);

		double sum = 0;
		int listIndex = 0;

		returnTuple.set(0, sampleOne);
		returnTuple.set(1, sampleTwo);

		if (sampleTwo.compareTo(sampleOne) <= 0) {
			returnTuple.set(2, sum);
			return returnTuple;
		}
			
		for (Iterator<Tuple> iterator = sampleOne_genes.iterator(); iterator.hasNext();) {
			Tuple t = iterator.next();
			double x = toDouble(((DataByteArray)t.get(0)).get());
			firstBag.add(x);
		}
		
		for (Iterator<Tuple> iterator = sampleTwo_genes.iterator(); iterator.hasNext();) {
			Tuple t = iterator.next();
			double x = toDouble(((DataByteArray)t.get(0)).get());
			sum += firstBag.get(listIndex)*x;
			listIndex++;
		}	
	
		returnTuple.set(2, sum);

		return returnTuple;
	}
}
