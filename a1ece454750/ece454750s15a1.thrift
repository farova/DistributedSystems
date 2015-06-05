namespace java ece454750s15a1

exception ServiceUnavailableException { 
	1: string msg 
}

struct PerfCounters {
	1: i32 numSecondsUp,			// number of seconds since service startup
	2: i32 numRequestsReceived,		// total number of requests received by service handler
	3: i32 numRequestsCompleted		// total number of requests completed by service handler
}

struct JoinRequestData {
	1: string host,
	2: i32 pport,
	3: i32 mport,
	4: i16 ncores,
	5: bool isBE = true;
}

struct JoinAckData {
	1: bool isAcked
}

struct NodeData {
	1: string host,
	2: i32 pport,
	3: i32 mport,
	4: i16 ncores
}

struct GossipData {
	1: list<NodeData> BEnodes,
	2: list<NodeData> FEnodes
}

service A1Password {

	string hashPassword (
		1: string password, 
		2: i16 logRounds
	) throws (
		1: ServiceUnavailableException e
	)

	bool checkPassword (
		1: string password, 
		2: string hash
	) throws (
		1: ServiceUnavailableException e
	)

}

service A1Management {

	PerfCounters getPerfCounters ()

	list<string> getGroupMembers ()

	void joinRequest(1: JoinRequestData data)
	
	void joinAck(1: JoinAckData data)
	
	void recieveGossip(1: GossipData gossipData)
	
}

