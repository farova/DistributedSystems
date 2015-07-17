data = LOAD '$input' USING PigStorage(',');

values =  FOREACH data GENERATE $0, (bag{tuple()}) TOBAG($1 ..) AS gene:bag{t:tuple()};



avg = FOREACH values GENERATE $0, AVG(gene);




STORE avg INTO '$output' USING PigStorage(',');