package io.nuls.event.util;

import io.nuls.consensus.poc.protocol.entity.Agent;
import io.nuls.consensus.poc.protocol.tx.RedPunishTransaction;
import io.nuls.consensus.poc.protocol.tx.YellowPunishTransaction;
import io.nuls.consensus.poc.protocol.util.PoConvertUtil;
import io.nuls.consensus.poc.storage.po.PunishLogPo;
import io.nuls.event.constant.EventConstant;
import io.nuls.event.model.AgentPunishDTO;
import io.nuls.kernel.exception.NulsException;
import io.nuls.kernel.model.Transaction;
import io.nuls.kernel.utils.AddressTool;
import io.nuls.kernel.utils.NulsByteBuffer;

import java.util.Base64;

public class EventUtil {

    public static AgentPunishDTO buildAgentPunishDTO(Agent agent,PunishLogPo po,String alias){
        return new AgentPunishDTO.AgentPunishDTOBuilder(
                AddressTool.getStringAddressByBytes(agent.getAgentAddress())
                ,po.getType()
                ,po.getTime())
                .setAgentHash(agent.getTxHash().getDigestHex())
                .setAgentName(alias)
                .setAgentId(PoConvertUtil.getAgentId(agent.getTxHash()))
                .build();

    }

    public static Transaction getTransaction(int type, String byteStr){
        byte[] txBytes = Base64.getDecoder().decode(byteStr);
        Transaction tx = null;
        if(type == EventConstant.TX_TYPE_YELLOW_PUNISH){
            tx = new YellowPunishTransaction();
        }else if(type == EventConstant.TX_TYPE_RED_PUNISH)
            tx = new RedPunishTransaction();
        try {
            tx.parse(new NulsByteBuffer(txBytes));
        } catch (NulsException e) {
            e.printStackTrace();
        }
        return tx;
    }
}
