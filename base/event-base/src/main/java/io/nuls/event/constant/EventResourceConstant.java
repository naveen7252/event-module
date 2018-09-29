package io.nuls.event.constant;

public interface EventResourceConstant {

    //Web Socket Subscriptions
    String NEW_BLOCK_SUBSCRIPTION = "/block/latest";

    String TX_TRANSFER_SUBSCRIPTION = "/tx/receiveToken/";

    String TX_COINBASE_SUBSCRIPTION = "/tx/receiveReward/";

    String AGENT_YELLOWCARD_SUBSCRIPTION = "/agent/yellowCard/";

    String AGENT_REDCARD_SUBSCRIPTION = "/agent/redCard/";

    //Rest API  End Points
    String NEWEST_BLOCK = "/api/block/newest";

    String BLOCK_BY_HEIGHT = "/api/block/height/";

    String TX_BY_HASH = "/api/tx/hash/";

    String TX_BY_HASH_BYTES = "/api/tx/bytes";

    String YELLOW = "YELLOW";

    String RED = "RED";

}
