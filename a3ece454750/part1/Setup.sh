#!/bin/sh
export HADOOP_CLASSPATH=$(pwd)/Part1.jar
hdfs dfs -rm -r -skipTrash /user/mfarova/output
