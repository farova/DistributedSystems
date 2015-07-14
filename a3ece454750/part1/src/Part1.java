import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
public class Part1 {

  public static class Part1Mapper
       extends Mapper<Object, Text, Text, DoubleWritable>{

    private Text studentID = new Text();
    private DoubleWritable avgGrade = new DoubleWritable();
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString(),",");
      int entryNumber = 0;
      double avg = 0;
      while (itr.hasMoreTokens()) {
        if (entryNumber == 0) {
                studentID.set(itr.nextToken());
                entryNumber++;
                continue;
        }
        avg += Double.parseDouble(itr.nextToken());
        entryNumber++;
       }
       avgGrade.set(avg/(entryNumber-1));
       context.write(studentID, avgGrade);
    }
  }
  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    conf.set("mapreduce.output.textoutputformat.separator", ",");
    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
    if (otherArgs.length != 2) {
      System.err.println("Usage: studentaverage <in> <out>");
      System.exit(2);
    }
    Job job = new Job(conf, "Student Average");
    job.setJarByClass(Part1.class);
    job.setMapperClass(Part1Mapper.class);
    // Set no of reducers to 0
    job.setNumReduceTasks(0);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(DoubleWritable.class);
    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
