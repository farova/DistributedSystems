register part4.jar;
rawSamples = LOAD ‘$input’ USING PigStorage(‘,’);
sampleGenes = FOREACH rawSamples GENERATE $0, (bag{tuple()}) TOBAG($1 ..) AS genes:bag{t:tuple()};
sampleMax = FOREACH sampleGenes GENERATE $0, part4.sampleMax(genes);
bracketRemove = FOREACH sampleMax GENERATE FLATTEN($0);
STORE bracketRemove INTO ‘$output’ USING PigStorage(‘,’);
