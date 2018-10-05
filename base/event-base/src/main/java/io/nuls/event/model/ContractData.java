package io.nuls.event.model;

public class ContractData {

    private String contractAddress;

    private String sender;

    private String receiver;

    private String tokenValue;

    private String tokenName;

    private String symbol;

    private String remark;

    private String txHash;

    private long time;

    private int blockHeight;

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String value) {
        this.tokenValue = value;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(int blockHeight) {
        this.blockHeight = blockHeight;
    }

    @Override
    public String toString() {
        return "ContractData{" +
                "contractAddress='" + contractAddress + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", tokenValue='" + tokenValue + '\'' +
                ", tokenName='" + tokenName + '\'' +
                ", symbol='" + symbol + '\'' +
                ", remark='" + remark + '\'' +
                ", txHash='" + txHash + '\'' +
                ", time=" + time +
                ", blockHeight=" + blockHeight +
                '}';
    }
}
