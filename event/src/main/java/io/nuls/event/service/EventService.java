package io.nuls.event.service;

import io.nuls.kernel.model.Result;

public interface EventService {

     Result getLatestBlock();

     Result getBlockByHeight(int height);

     Result getTxByHash(String hash);

     Result getAgentPunish(long height);

     Result getContractTxByHash(String hash);
}
