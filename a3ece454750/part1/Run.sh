#!/bin/sh
export HADOOP_CLASSPATH=$(pwd)/Part1.jar
hdfs dfs -rm -r -skipTrash /user/mfarova/output
hadoop jar Part1.jar Part1 /user/mfarova/input/ /user/mfarova/output
