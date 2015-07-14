import java.io.IOException;
import java.util.StringTokenizer;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;


public class Part3 {

  public static class Part3Mapper extends Mapper<Object, Text, Text, IntWritable> 
  {

    private Text geneID = new Text();
    private IntWritable count1 = new IntWritable();
    private IntWritable count0 = new IntWritable();

    public void map(Object key, Text value, Context context ) throws IOException, InterruptedException 
    {
      /*
      count1.set(1);
      count0.set(0);

      ArrayList<Integer> genes = new ArrayList<Integer>();

      StringTokenizer itr = new StringTokenizer(value.toString(),",");
      Integer geneCounter = 0;
      double tokenValue;

      while (itr.hasMoreTokens()) 
      {
        if (geneCounter == 0) {
          itr.nextToken();
          geneCounter++;
          continue;
        }

        tokenValue = Double.parseDouble(itr.nextToken());

        geneID.set("gene_"+geneCounter);

        if (tokenValue > 0.5) {
          context.write(geneID, count1);
        } else {
          context.write(geneID, count0);
        }

        geneCounter++;
      }

      */
    }

  }


  public static class Part3Reducer extends Reducer<Text,IntWritable,Text,DoubleWritable> 
  {
    
    private DoubleWritable result = new DoubleWritable();

    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException 
    {
      /*
      int totalCount = 0;
      int relatedCount = 0;

      for (IntWritable val : values) {
        relatedCount += val.get();
        totalCount++;
      }

      double relation = (double) relatedCount / totalCount;

      result.set(relation);
      context.write(key, result);
      */
    }

  }


  public static void main(String[] args) throws Exception 
  {
    Configuration conf = new Configuration();
    conf.set("mapreduce.output.textoutputformat.separator", ",");

    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

    if (otherArgs.length != 2) {
      System.err.println("Usage: Part3");
      System.exit(2);
    }

    Job job = new Job(conf, "Part3");
    job.setJarByClass(Part3.class);
    job.setMapperClass(Part3Mapper.class);
    job.setReducerClass(Part3Reducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);

    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }

}
