package io.nuls.event.service.impl;

import io.nuls.account.service.AccountService;
import io.nuls.consensus.poc.context.PocConsensusContext;
import io.nuls.consensus.poc.protocol.entity.Agent;
import io.nuls.consensus.poc.protocol.util.PoConvertUtil;
import io.nuls.consensus.poc.rpc.model.AgentDTO;
import io.nuls.consensus.poc.storage.po.PunishLogPo;
import io.nuls.event.constant.EventResourceConstant;
import io.nuls.event.model.AgentPunishDTO;
import io.nuls.event.service.EventService;
import io.nuls.kernel.constant.KernelErrorCode;
import io.nuls.kernel.context.NulsContext;
import io.nuls.kernel.model.Result;
import io.nuls.kernel.utils.AddressTool;
import io.nuls.protocol.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private RestTemplate restTemplate;

    private AccountService accountService = NulsContext.getServiceBean(AccountService.class);

    @Override
    public Result getLatestBlock(){
		return restTemplate.getForObject(EventResourceConstant.NEWEST_BLOCK,Result.class);
    }

    @Override
    public Result getLatestBlock(int height) {
        return restTemplate.getForObject(EventResourceConstant.BLOCK_BY_HEIGHT+height,Result.class);
    }

    @Override
    public Result getTxByHash(String hash) {
        return restTemplate.getForObject("/api/tx/hash/"+hash,Result.class);
    }

    @Override
    public Result getAgentPunish(long height){
        List<PunishLogPo> list = PocConsensusContext.getChainManager().getMasterChain().getChain().getRedPunishList();
        List<PunishLogPo> targetPoList  = new ArrayList<>();
        for(PunishLogPo po : list){
            if(po.getHeight() ==  height){
                targetPoList.add(po);
            }
            if(po.getHeight() < height){
                break;
            }
        }
        if(targetPoList.isEmpty()){
            return new Result(false, KernelErrorCode.DATA_NOT_FOUND,null);
        }
        //List<Agent> allAgentList = PocConsensusContext.getChainManager().getMasterChain().getChain().getAgentList();
        List<AgentPunishDTO> punishDTOS = new ArrayList<>();
        for(PunishLogPo po : targetPoList){
            /*byte[] addressBytes = AddressTool.getAddress(po.getAddress());
            Agent targetAgent = null;
            for(Agent agent : allAgentList){
                if (Arrays.equals(agent.getAgentAddress(), addressBytes)) {
                    targetAgent = agent;
                    break;
                }
            }
            if(null == targetAgent){
               continue;
            }*/
            String address = AddressTool.getStringAddressByBytes(po.getAddress());
            String alias = accountService.getAlias(address).getData();
            punishDTOS.add(buildAgentPunishDTO(new Agent(),po,alias));
        }
        return new Result(true,KernelErrorCode.SUCCESS,punishDTOS);
    }

    private AgentPunishDTO buildAgentPunishDTO(Agent agent,PunishLogPo po,String alias){
        return new AgentPunishDTO.AgentPunishDTOBuilder(
                AddressTool.getStringAddressByBytes(agent.getAgentAddress())
                ,po.getType()
                ,po.getTime())
                .setAgentHash(agent.getTxHash().getDigestHex())
                .setAgentName(alias)
                .setAgentId(PoConvertUtil.getAgentId(agent.getTxHash()))
                .build();

    }
}
