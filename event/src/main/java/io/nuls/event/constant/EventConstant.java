package io.nuls.event.constant;

public interface EventConstant {

    short MODULE_ID_EVENT = 8;

    public static final int TX_TYPE_COINBASE = 1;

    public static final int TX_TYPE_TRANSFER = 2;

    public static final int TX_TYPE_ACCOUNT_ALIAS = 3;

    public static final int TX_TYPE_REGISTER_AGENT = 4;

    public static final int TX_TYPE_JOIN_CONSENSUS = 5;

    public static final int TX_TYPE_CANCEL_DEPOSIT = 6;

    public static final int TX_TYPE_STOP_AGENT = 9;

    public static final int TX_TYPE_YELLOW_PUNISH = 7;

    public static final int TX_TYPE_RED_PUNISH = 8;
}
