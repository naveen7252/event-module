package io.nuls.event.service;

import io.nuls.kernel.model.Result;

public interface EventService {

     Result getLatestBlock();

     Result getLatestBlock(int height);

     Result getTxByHash(String hash);

     Result getAgentPunish(long height);
}
