package io.nuls.event.constant;

public interface EventConstant {

    short MODULE_ID_EVENT = 13;

    int TX_TYPE_COINBASE = 1;

    int TX_TYPE_TRANSFER = 2;

    int TX_TYPE_ACCOUNT_ALIAS = 3;

    int TX_TYPE_REGISTER_AGENT = 4;

    int TX_TYPE_JOIN_CONSENSUS = 5;

    int TX_TYPE_CANCEL_DEPOSIT = 6;

    int TX_TYPE_STOP_AGENT = 9;

    int TX_TYPE_YELLOW_PUNISH = 7;

    int TX_TYPE_RED_PUNISH = 8;
}
