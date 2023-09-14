package Lab__;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class WordReducer extends Reducer<Text, IntWritable, Text, LongWritable> {
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, Text, LongWritable>.Context context)
            throws IOException, InterruptedException {

        long count = 0;
        Iterator<IntWritable> iterator = values.iterator();

        while (iterator.hasNext()){
            iterator.next();
            count++;
        }
        context.write(key, new LongWritable(count));
    }
}
