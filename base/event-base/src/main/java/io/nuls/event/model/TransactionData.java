package io.nuls.event.model;

public class TransactionData {

    private String toAddress;

    private long nulsValue;

    private ContractData contractData;

    public TransactionData(){

    }

    public TransactionData(ContractData contractData){
        this.contractData = contractData;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public long getNulsValue() {
        return nulsValue;
    }

    public void setNulsValue(long nulsValue) {
        this.nulsValue = nulsValue;
    }

    public ContractData getContractData() {
        return contractData;
    }

    public void setContractData(ContractData contractData) {
        this.contractData = contractData;
    }

    @Override
    public String toString() {
        return "TransactionData{" +
                "toAddress='" + toAddress + '\'' +
                ", nulsValue=" + nulsValue +
                ", contractData=" + contractData +
                '}';
    }
}
