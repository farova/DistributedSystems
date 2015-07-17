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


  public static class Part3Combiner extends Reducer<Text, GeneWritable,Text, DoubleWritable> 
  {
  

    public void reduce(Text key, Iterable<GeneWritable> values, Context context) throws IOException, InterruptedException 
    {

      

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
    job.setCombinerClass(Part3Combiner.class);
    job.setReducerClass(Part3Reducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(GeneWritable.class);

    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }

}
