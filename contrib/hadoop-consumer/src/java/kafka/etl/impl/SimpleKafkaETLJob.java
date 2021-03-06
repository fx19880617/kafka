/*
 * Copyright 2010 LinkedIn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kafka.etl.impl;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextOutputFormat;

import kafka.etl.KafkaETLJob;
import kafka.etl.Props;

/**
 * This is a simple Kafka ETL job which pull text events generated by
 * DataGenerator and store them in hdfs
 */
@SuppressWarnings("deprecation")
public class SimpleKafkaETLJob extends KafkaETLJob {

	public SimpleKafkaETLJob(String name, Props props) throws Exception {
		super(name, props);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getMapperClass() {
		return SimpleKafkaETLMapper.class;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getReducerClass() {
		return SimpleKafkaETLReducer.class;
	}

	@Override
	protected int getNumReducers() throws Exception {
		return 1;
	}

	@Override
	protected JobConf createJobConf() throws Exception {
		JobConf jobConf = super.createJobConf();
		jobConf.setOutputKeyClass(NullWritable.class);
		jobConf.setOutputValueClass(Text.class);
		jobConf.setOutputFormat(TextOutputFormat.class);
		TextOutputFormat.setCompressOutput(jobConf, false);
		return jobConf;
	}
	
	/**
	 * for testing only
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		if (args.length < 1)
			throw new Exception("Usage: - config_file");

		Props props = new Props(args[0]);
		SimpleKafkaETLJob job = new SimpleKafkaETLJob("SimpleKafkaETLJob",
				props);
		job.run();
	}

}
