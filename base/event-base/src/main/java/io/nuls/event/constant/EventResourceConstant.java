package io.nuls.event.constant;

/**
 * Constants for API calls and web socket end points
 * @author Naveen(naveen.balamuri@gmail.com)
 */
public interface EventResourceConstant {

    //Web Socket Subscriptions
    String NEW_BLOCK_SUBSCRIPTION = "/block/latest";

    String TOKEN_RECEIVE_SUBSCRIPTION = "/tx/receiveToken/";

    String TX_COINBASE_SUBSCRIPTION = "/tx/receiveReward/";

    String AGENT_YELLOWCARD_SUBSCRIPTION = "/agent/yellowCard/";

    String AGENT_REDCARD_SUBSCRIPTION = "/agent/redCard/";

    String CONTRACT_CREATE_SUBSCRIPTION = "/contract/create/";

    String CONTRACT_DELETE_SUBSCRIPTION = "/contract/delete/";

    //Rest API  End Points
    String NEWEST_BLOCK = "/api/block/newest";

    String BLOCK_BY_HEIGHT = "/api/block/height/";

    String TX_BY_HASH = "/api/tx/hash/";

    String TX_BY_HASH_BYTES = "/api/tx/bytes";

    String CONTRACT_TX_BY_HASH = "/api/contract/tx/";

    String YELLOW = "YELLOW";

    String RED = "RED";

}
