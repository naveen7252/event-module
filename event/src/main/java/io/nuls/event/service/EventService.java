package io.nuls.event.service;

import io.nuls.kernel.model.Result;

public interface EventService {

    public Result getLatestBlock();

    public Result getLatestBlock(int height);
}
