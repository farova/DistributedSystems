register sampleMax.jar;
rawSamples = load '/user/twhart/input/1k_samples/1.txt' using PigStorage(',');
sampleGenes = FOREACH rawSamples GENERATE $0, (bag{tuple()}) TOBAG($1 ..) AS genes:bag{t:tuple()};
sampleMax = FOREACH sampleGenes GENERATE $0, part1Max.sampleMax(genes);

dump sampleMax;
