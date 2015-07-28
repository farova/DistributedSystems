register part4.jar;
rawSamples = LOAD '$input' USING PigStorage(',');
genes = foreach rawSamples generate $0 as sampleID:chararray, (bag{tuple()}) TOBAG($1 ..) AS genes:bag{t:tuple()};
genes_2 = foreach rawSamples generate $0 as sampleID:chararray, (bag{tuple()}) TOBAG($1 ..) AS genes:bag{t:tuple()};
crossSamples = CROSS genes, genes_2;
crossSimilarity = FOREACH crossSamples GENERATE part4.geneSimilarity($0 ..) AS genes:tuple();
crossOutput = FILTER crossSimilarity BY (double)$0.$2 > 0;
bracketRemove = FOREACH crossOutput GENERATE FLATTEN($0);
STORE bracketRemove INTO '$output' USING PigStorage(',');
