rawSamples = load '/user/twhart/input/test_Samples/1.txt' using PigStorage(',');
genes = foreach rawSamples generate $0 as sampleID:chararray, (bag{tuple()}) TOBAG($1 ..) AS genes:bag{t:tuple()};
genes_2 = foreach rawSamples generate $0 as sampleID:chararray, (bag{tuple()}) TOBAG($1 ..) AS genes:bag{t:tuple()};
crossSamples = CROSS genes, genes_2;

dump crossSamples;

