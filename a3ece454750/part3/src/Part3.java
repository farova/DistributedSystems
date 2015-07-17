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
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;
import org.apache.hadoop.util.GenericOptionsParser;

public class Part3 {

  public static class Part3Mapper extends Mapper<Object, Text, Text, GeneWritable> 
  {

    private Text geneID = new Text();
    private GeneWritable gene = new GeneWritable();


    public void map(Object key, Text value, Context context ) throws IOException, InterruptedException 
    {
      
      StringTokenizer itr1 = new StringTokenizer(value.toString(),",");
      Integer geneCounter = 0;
      double tokenValue;
      String sampleID;

      while (itr1.hasMoreTokens()) 
      {

        if (geneCounter == 0) {
          gene.sampleID = itr1.nextToken();
          geneCounter++;
          continue;
        }

        tokenValue = Double.parseDouble(itr1.nextToken());

        if(tokenValue == 0.0){
          geneCounter++;
          continue;
        }

        gene.geneID = geneCounter;
        gene.geneValue = tokenValue;

        geneID.set("gene_" + geneCounter);

        context.write(geneID, gene);
          
        //for (int j = geneCounter; j < itr1.countTokens(); j++) {}

        geneCounter++;
      }

    }

  }


  public static class Part3Reducer extends Reducer<Text, GeneWritable,Text, DoubleWritable> 
  {
    
    private Text sampleID = new Text();
    private DoubleWritable result = new DoubleWritable();

    private ArrayList<GeneWritable> cache = new ArrayList<GeneWritable>();

    public void reduce(Text key, Iterable<GeneWritable> values, Context context) throws IOException, InterruptedException 
    {
      
      for (GeneWritable val : values) {
        GeneWritable temp = new GeneWritable();
        temp.sampleID = val.sampleID;
        temp.geneID = val.geneID;
        temp.geneValue = val.geneValue;

        cache.add(temp);
      }

      GeneWritable gene1, gene2;
      
      for(int i = 0; i < cache.size(); i++) {

        gene1 = cache.get(i);

        for(int j = i; j < cache.size(); j++) {

          gene2 = cache.get(j);

          if(gene1.sampleID.equals(gene2.sampleID)) {
            continue;
          }
          
          sampleID.set(gene1.sampleID + "," + gene2.sampleID);
          result.set(gene1.geneValue * gene2.geneValue);

          context.write(sampleID, result);
        }
      }

      cache.clear();
    }
  }


  public static class Part3Mapper2 extends Mapper<Object, Text, Text, DoubleWritable> 
  {
  
    private Text sampleIDs = new Text();
    private DoubleWritable result = new DoubleWritable();

    public void map(Object key, Text value, Context context ) throws IOException, InterruptedException 
    {

      StringTokenizer itr1 = new StringTokenizer(value.toString(),",");
      double tokenValue;
      String sampleID1 = "";
      String sampleID2 = "";
      int counter = 0;

      while (itr1.hasMoreTokens()) 
      {
        if (counter == 0) {
          sampleID1 = itr1.nextToken();
          counter++;
          continue;
        } 
        else if (counter == 1) {
          sampleID2 = itr1.nextToken();
          counter++;
          continue;
        }

        result.set(Double.parseDouble(itr1.nextToken()));
        sampleIDs.set(sampleID1+","+sampleID2);

        context.write(sampleIDs, result);
        
        counter++;
      }
    }

  }

  public static class Part3Reducer2 extends Reducer<Text, DoubleWritable,Text, DoubleWritable> 
  {
    
    private Text sampleID = new Text();
    private DoubleWritable result = new DoubleWritable();

    public void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException 
    {
      double sum = 0;

      for (DoubleWritable val : values) {
        sum += val.get();
      }

      result.set(sum);

      context.write(key, result);
    }
  }

  private static final String OUTPUT_PATH = "intermediate_output";

  public static void main(String[] args) throws Exception 
  {
    Configuration conf = new Configuration();
    conf.set("mapreduce.output.textoutputformat.separator", ",");

    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();

    if (otherArgs.length != 2) {
      System.err.println("Usage: Part3");
      System.exit(2);
    }


    Job job = new Job(conf, "Part3_1");
    job.setJarByClass(Part3.class);
    job.setMapperClass(Part3Mapper.class);
    job.setReducerClass(Part3Reducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(GeneWritable.class);

    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
    FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH));

    job.waitForCompletion(true);


    Job job2 = new Job(conf, "Part3_2");
    job2.setJarByClass(Part3.class);

    job2.setMapperClass(Part3Mapper2.class);
    job2.setReducerClass(Part3Reducer2.class);

    job2.setOutputKeyClass(Text.class);
    job2.setOutputValueClass(DoubleWritable.class);

    job2.setInputFormatClass(TextInputFormat.class);
    job2.setOutputFormatClass(TextOutputFormat.class);

    TextInputFormat.addInputPath(job2, new Path(OUTPUT_PATH));
    TextOutputFormat.setOutputPath(job2, new Path(otherArgs[1]));


    System.exit(job2.waitForCompletion(true) ? 0 : 1);
  }

}
