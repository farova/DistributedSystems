hdfs dfs -rm -r -skipTrash /user/mfarova/output
pig -param input=/user/mfarova/input/ -param output=/user/mfarova/output/ Part4_1.pig

