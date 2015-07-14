import java.io.IOException;
import java.util.StringTokenizer;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;


public class Part1 {

  public static class Part1Mapper extends Mapper<Object, Text, Text, Text> 
  {

    private Text sampleID = new Text();
    private Text geneSet = new Text();

    public void map(Object key, Text value, Context context ) throws IOException, InterruptedException 
    {
      
      ArrayList<Integer> genes = new ArrayList<Integer>();

      StringTokenizer itr = new StringTokenizer(value.toString(),",");
      Integer entryNumber = 0;
      double tokenValue;
      double maxValue = 0.0;

      while (itr.hasMoreTokens()) 
      {
        if (entryNumber == 0) {
          sampleID.set(itr.nextToken());
          entryNumber++;
          continue;
        }

        tokenValue = Double.parseDouble(itr.nextToken());

        if(tokenValue == 0.0) {
          entryNumber++;
          continue;
        }

        if(tokenValue > maxValue) {
          genes.clear();
          maxValue = tokenValue;
        }
        
        if(tokenValue == maxValue) {
          genes.add(entryNumber);
        }

        entryNumber++;
      }

      String tokenStringSet = "";

      for(Integer gene : genes) {
        tokenStringSet += "gene_" + gene + ",";
      }

      geneSet.set(tokenStringSet);

      context.write(sampleID, geneSet);
    }

  }

  public static void main(String[] args) throws Exception 
  {
    Configuration conf = new Configuration();
    conf.set("mapreduce.output.textoutputformat.separator", ",");

    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

    if (otherArgs.length != 2) {
      System.err.println("Usage: Part1");
      System.exit(2);
    }

    Job job = new Job(conf, "Part1");
    job.setJarByClass(Part1.class);
    job.setMapperClass(Part1Mapper.class);
    // Set no of reducers to 0
    job.setNumReduceTasks(0);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);

    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }

}
