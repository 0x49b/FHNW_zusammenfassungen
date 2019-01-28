import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * Implementing a map-reduce-join with apache hadoop.
 * This algorithm joins two csv-files using the first index of each.
 * Join-Type: Reduce-Side-Join
 */
public class Join {
	
	private static int leftIndex = 0;
	private static int rightIndex = 0;
	
	/**
	 * Input-Mapper for the first file. This file must be a csv.
	 * KeyOut is the join key. ValueOut is the according line in the csv without the join key.
	 */
	public static class RightInputMapper extends Mapper<LongWritable, Text, Text, DatasetTagger> {
		private Text word = new Text();
		
		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			// fetching one line in the csv
			String line = value.toString();
			// extracting columns by splitting the String
			String[] cols = line.split(";");
			// wrapping the value of the first column in a Text
			word.set(cols[rightIndex]);
			// Rest of the line will be stored in a custom datastructure with a tag from which file it comes
			String rest = assembleLineWithoutIndex(cols, rightIndex);
			DatasetTagger ckw = new DatasetTagger(rest, DatasetTagger.Side.RIGHT);
			
			context.write(word, ckw);
		}
	}
	
	/**
	 * Input-Mapper for the second file. This file must be a csv.
	 * KeyOut is the join key. ValueOut is the according line in the csv without the join key.
	 */
	public static class LeftInputMapper extends Mapper<LongWritable, Text, Text, DatasetTagger> {
		private Text word = new Text();
		
		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			// one line in the csv
			String line = value.toString();
			// get columns by splitting String
			String[] cols = line.split(";");
			// first column = mnr
			word.set(cols[leftIndex]);
			String rest = assembleLineWithoutIndex(cols, leftIndex);
			DatasetTagger ckw = new DatasetTagger(rest, DatasetTagger.Side.LEFT);
			context.write(word, ckw);
		}
	}
	
	/**
	 * Assembles the splitted line of a csv-File excluding the joining index.
	 * @param line Line of a csv-File splitted over the semicolon
	 * @param index Joining Index
	 * @return Resulting String.
	 */
	private static String assembleLineWithoutIndex(String[] line, int index) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < line.length; i++) {
			if (i != index) {
				sb.append(line[i] + ";");
			}
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	/**
	 * KeyOut is the join key. ValueOut is the sorted lines concatenated to several strings.
	 */
	public static class Reduce extends Reducer<Text, DatasetTagger, Text, Text> {
		
		@Override
		protected void reduce(Text key, Iterable<DatasetTagger> values, Context c) throws IOException, InterruptedException {
			// Values of the mapper which processes the file which is on the right side of the join
			List<String> right = new ArrayList<String>();
			// Values of the mapper which processes the file which is on the left side of the join
			List<String> left = new ArrayList<String>();
			
			// Fill the two Lists with the values of the Iterable
			Iterator<DatasetTagger> it = values.iterator();
			DatasetTagger v;
			while(it.hasNext()) {
				v = it.next();
				if (v.getSide() == DatasetTagger.Side.LEFT) {
					left.add(v.getValue());
				} else if (v.getSide() == DatasetTagger.Side.RIGHT) {
					right.add(v.getValue());
				}
			}
			
			// Concatenate the output according to the join-rules
			for (String l : left) {
				for (String r : right) {
					String res = rearrangeOutput(l, r);
					Text val = new Text(res);
					c.write(key, val);
				}
			}
		}
	}
	
	/**
	 * Rearrange the output from the two sides so that it displays tabs instead of a semicolon between the columns
	 * @param right String from the right side of the join.
	 * @param left String from the left side of the join.
	 * @return String ready to be displayed.
	 */
	private static String rearrangeOutput(String right, String left) {
		StringBuilder res = new StringBuilder();
		String[] r = right.split(";");
		String[] l = left.split(";");
		for (String string : r) {
			res.append(string + "\t");
		}
		for (String string : l) {
			res.append(string + "\t");
		}
		return res.toString();
	}
	
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 5) {
			System.err.println("Usage: join.jar <left-csv-File> <right-csv-File> <output-Directory> <left-key> <right-key>");
			System.exit(2);
		}
		Job job = Job.getInstance(conf, "join");
		
		// Left csv
		Path in1 = new Path(otherArgs[0]);
		// Right csv
		Path in2 = new Path(otherArgs[1]);
		// Output
		Path out = new Path(otherArgs[2]);
		// Left joining index
		leftIndex = Integer.parseInt(otherArgs[3]);
		// Right joining index
		rightIndex = Integer.parseInt(otherArgs[4]);
		
		job.setReducerClass(Reduce.class);
		
		// Setting these is neccessary. Otherwise it does not recognize the custom class
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(DatasetTagger.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setOutputFormatClass(TextOutputFormat.class);
		
		FileOutputFormat.setOutputPath(job, out);
		
		MultipleInputs.addInputPath(job, in1, TextInputFormat.class, LeftInputMapper.class);
		MultipleInputs.addInputPath(job, in2, TextInputFormat.class, RightInputMapper.class);
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
	
	
}
