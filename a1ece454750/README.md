A1
==================================================

Michaela Farova

Nishad Krishnan



TODO:

- Get perf counters to work correctly
- FE nodes dont try to connect to themselves
- FE nodes check all seeds and if nothing up determines tat they are sole FE node
- Nodes dont crash and print stack strace when cant connect to a server. SHould wait for a response for a while and then determine that the node is unavailable and down
- Gossip protocol
- Dont print stack traces
- If BE killed while processing request BE must forward o next available BE
- Report