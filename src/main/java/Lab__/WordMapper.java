package Lab__;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordMapper extends Mapper<LongWritable, Text, Text, IntWritable>{

    private final String separator = " ";

    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context)
            throws IOException, InterruptedException {
        String line = value.toString();
        String[] words = replaceAndTrim(line).split(separator);

        for (String word: words) {
            context.write(new Text(word), new IntWritable(1));
        }
    }


    private String replaceAndTrim(String inputString) {
        return inputString
                .toLowerCase()
                .replaceAll("[—\\s]", separator)
                .replaceAll("[-\\s]", separator)
                .replaceAll("[\\W&&[^-'а-я]]", separator)
                .replaceAll(" +", separator)
                .trim();
    }
}
