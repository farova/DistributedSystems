package part3;
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

public class geneParser extends EvalFunc<Tuple>
{

	public static double toDouble(byte[] bytes) throws IOException{
		LoadCaster lc = new Utf8StorageConverter();
		return lc.bytesToDouble(bytes);
	}
	
	public Tuple exec(Tuple input) throws IOException {
		BagFactory mBagFactory = BagFactory.getInstance();
		TupleFactory mTupleFactory = TupleFactory.getInstance();
	
		String sampleOne = (String) input.get(0);
		DataBag sampleOne_genes = (DataBag) input.get(1);
		String sampleTwo = (String) input.get(2);
		DataBag sampleTwo_genes = (DataBag) input.get(3);
	
		ArrayList<Double> firstBag = new ArrayList<Double>();
		ArrayList<Double> secondBag = new ArrayList<Double>();

		double sum = 0;
		int listIndex = 0;

		if (sampleTwo.compareTo(sampleOne) < 0) {
			return 0;
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

		

		
		return some_tuple;
}
