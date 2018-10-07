package io.nuls.event.model;

import io.nuls.consensus.poc.protocol.constant.PunishType;

/**
 * Data transfer Object DTO for Agent punishments details
 * @author Naveen(naveen.balamuri@gmail.com)
 */
public class AgentPunishDTO {

    private String agentHash;

    private String agentAddress;

    private String packingAddress;

    private String rewardAddress;

    private long deposit;

    private double commissionRate;

    private String agentName;

    private String agentId;

    private String punishType;

    private long time;

    private long height;

    private short reasonCode;

    private String evidence;

    private AgentPunishDTO(AgentPunishDTOBuilder builder) {
        this.agentAddress = builder.agentAddress;
        this.punishType = builder.punishType;
        this.time = builder.time;
    }

    public String getAgentHash() {
        return agentHash;
    }

    public String getAgentAddress() {
        return agentAddress;
    }

    public String getPackingAddress() {
        return packingAddress;
    }

    public String getRewardAddress() {
        return rewardAddress;
    }

    public long getDeposit() {
        return deposit;
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    public String getAgentName() {
        return agentName;
    }

    public String getAgentId() {
        return agentId;
    }

    public String getPunishType() {
        return punishType;
    }

    public long getTime() {
        return time;
    }

    public long getHeight() {
        return height;
    }

    public short getReasonCode() {
        return reasonCode;
    }

    public String getEvidence() {
        return evidence;
    }

    public static class AgentPunishDTOBuilder{

        private String agentHash;

        private String agentAddress;

        private String punishType;

        private long time;

        private short reasonCode;

        private String evidence;

        private String agentName;

        private String agentId;

        private long height;

        private String packingAddress;

        private String rewardAddress;

        private long deposit;

        private double commissionRate;

        public AgentPunishDTOBuilder(String agentAddress, int punishType, long time){
            this.agentAddress = agentAddress;
           if(punishType == PunishType.RED.getCode()){
               this.punishType = "RED";
           }else if(punishType == PunishType.YELLOW.getCode()){
               this.punishType = "YELLOW";
           }
           this.time = time;
        }

        public AgentPunishDTOBuilder(String address,String type){
            this.agentAddress = address;
            this.punishType = type;
            this.time = System.currentTimeMillis();
        }

        public AgentPunishDTOBuilder setAgentHash(String agentHash){
            this.agentHash = agentName;
            return this;
        }

        public AgentPunishDTOBuilder setReasonCode(short reasonCode){
            this.reasonCode = reasonCode;
            return this;
        }

        public AgentPunishDTOBuilder setEvidence(String evidence){
            this.evidence = evidence;
            return this;
        }

        public AgentPunishDTOBuilder setAgentName(String agentName){
            this.agentName = agentName;
            return this;
        }

        public AgentPunishDTOBuilder setAgentId(String agentId){
            this.agentId = agentId;
            return this;
        }

        public AgentPunishDTOBuilder setHeight(int height){
            this.height = height;
            return this;
        }

        public AgentPunishDTOBuilder setPackingAddress(String packingAddress){
            this.packingAddress = packingAddress;
            return this;
        }

        public AgentPunishDTOBuilder setRewardAddress(String rewardAddress){
            this.rewardAddress = rewardAddress;
            return this;
        }

        public AgentPunishDTOBuilder setDeposit(long deposit){
            this.deposit = deposit;
            return this;
        }

        public AgentPunishDTOBuilder setCommissionRate(double commRate){
            this.commissionRate = commRate;
            return this;
        }

        public AgentPunishDTO build(){
            return  new AgentPunishDTO(this);
        }
    }
}
