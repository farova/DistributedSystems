/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.thrift.TException;
import org.mindrot.jbcrypt.BCrypt;


// Generated code
import ece454750s15a1.*;

import java.util.List;
import java.util.Arrays;

public class FEPasswordHandler implements A1Password.Iface {
	
	public FEPasswordHandler() {}

	@Override
	public String hashPassword(String password, short logRounds) {
		
		//m_counters.numRequestsReceived++;
		
		return BCrypt.hashpw(password, BCrypt.gensalt(logRounds));
	}

	@Override
	public boolean checkPassword(String password, String hash) {
		
		//m_counters.numRequestsReceived++;
		
		return BCrypt.checkpw(password, hash);
	}

}


