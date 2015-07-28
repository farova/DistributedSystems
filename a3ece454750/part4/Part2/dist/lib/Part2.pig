register geneParser.jar;
rawSamples = load '/user/twhart/input/1k_samples/1.txt' using PigStorage(',');
sampleGenes = FOREACH rawSamples GENERATE $0, (bag{tuple()}) TOBAG($1 ..) AS genes:bag{t:tuple()};
parsedGenes = FOREACH sampleGenes GENERATE part2.geneParser($1 ..) as genes:bag{tuple()};
geneRows = FOREACH parsedGenes GENERATE FLATTEN(genes);
geneBooled = FOREACH geneRows GENERATE $0, part2.geneBooler($1);
geneGrouped = GROUP geneBooled by $0;
geneSum = FOREACH geneGrouped GENERATE $0, (float)SUM(geneBooled.$1)/(float)COUNT(geneBooled.$1);

dump geneSum;
