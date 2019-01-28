import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * Implementing a group by with count in a select statement with apache hadoop.
 * This algorithm is similar to the wordcount example.
 */
public class GroupByWithCount {
	
	private static int index = 0;
	
	/**
	 * Mapper which is able to process a csv-file. 
	 * The first column must be the key in the group by statement.
	 * Stores the number one with every line it maps.
	 */
	public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();
		
		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			// one line in the csv
			String line = value.toString();
			// get columns by splitting String
			String[] cols = line.split(";");
			// first column = mnr
			word.set(cols[index]);
			context.write(word, one);
		}
	}
	
	/**
	 * Reducer which sums up the integers stored by the mapper.
	 */
	public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
		
		@Override
		protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			int sum = 0;
			Iterator<IntWritable> it = values.iterator();
			while (it.hasNext()) {
				sum += it.next().get();
			}
			context.write(key, new IntWritable(sum));
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 3) {
			System.err.println("Usage: groupbywithcount.jar <csv-File> <output-Directory> <key>");
			System.exit(2);
		}
		
		index = Integer.parseInt(otherArgs[2]);
		
		Job job = Job.getInstance(conf, "groupbywithcount");
		
		job.setInputFormatClass(TextInputFormat.class);
		
		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
