package io.nuls.event.util;

import io.nuls.consensus.poc.protocol.entity.Agent;
import io.nuls.consensus.poc.storage.po.PunishLogPo;
import io.nuls.event.model.AgentPunishDTO;
import io.nuls.kernel.utils.AddressTool;

public class AgentUtil {

    public static AgentPunishDTO toAgentPunishDTO(PunishLogPo po, Agent agent){
        return new AgentPunishDTO.AgentPunishDTOBuilder(
                AddressTool.getStringAddressByBytes(agent.getAgentAddress())
                ,po.getType()
                ,po.getType())
                .setAgentHash(agent.getTxHash().getDigestHex())
                .build();
    }
}
