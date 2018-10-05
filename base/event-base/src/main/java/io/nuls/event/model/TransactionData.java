package io.nuls.event.model;

public class TransactionData {

    private String address;

    private long nulsValue;

    private ContractData contractData;

    public TransactionData(){

    }

    public TransactionData(ContractData contractData){
        this.contractData = contractData;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
                "address='" + address + '\'' +
                ", nulsValue=" + nulsValue +
                ", contractData=" + contractData +
                '}';
    }
}
