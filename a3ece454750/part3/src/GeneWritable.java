import java.io.IOException;
import java.util.StringTokenizer;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Writable;

 public class GeneWritable implements Writable{

  public String sampleID;
  public int geneID = -1;
  public double geneValue =-1;

  public void write(DataOutput out) throws IOException {
    out.writeInt(geneID);
    out.writeDouble(geneValue);
    out.writeBytes(sampleID);
  }

  public void readFields(DataInput in) throws IOException {
    geneID = in.readInt();
    geneValue = in.readDouble();
    sampleID = in.readLine();
  }

  @Override
  public String toString() {
    return sampleID + "," + geneID + "," + geneValue;
  }

  public static GeneWritable read(DataInput in) throws IOException {
    GeneWritable s = new GeneWritable();
    s.readFields(in);
    return s;
  }
}
