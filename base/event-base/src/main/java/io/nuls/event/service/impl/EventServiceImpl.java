package io.nuls.event.service.impl;

import io.nuls.account.service.AccountService;
import io.nuls.consensus.poc.context.PocConsensusContext;
import io.nuls.consensus.poc.protocol.entity.Agent;
import io.nuls.consensus.poc.protocol.tx.YellowPunishTransaction;
import io.nuls.consensus.poc.storage.po.PunishLogPo;
import io.nuls.event.constant.EventResourceConstant;
import io.nuls.event.model.AgentPunishDTO;
import io.nuls.event.service.EventService;
import io.nuls.event.util.EventUtil;
import io.nuls.kernel.constant.KernelErrorCode;
import io.nuls.kernel.context.NulsContext;
import io.nuls.kernel.exception.NulsException;
import io.nuls.kernel.model.Result;
import io.nuls.kernel.model.Transaction;
import io.nuls.kernel.utils.AddressTool;
import io.nuls.kernel.utils.NulsByteBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

/**
 * Implementation class for EventService. Provides functionality to get Nuls Blockchain information through REST API
 */
@Service
public class EventServiceImpl implements EventService {

    @Value("${NULS.API.URL}")
    private String API_SERVER_URL;

    @Autowired
    private RestTemplate restTemplate;

    private AccountService accountService = null;

    /**
     * Get latest block from Nuls Blockchain
     * @return Result
     */
    @Override
    public Result getLatestBlock(){
		return restTemplate.getForObject(EventResourceConstant.NEWEST_BLOCK,Result.class);
    }

    /**
     * Get block by height
     * @param height
     * @return Result
     */
    @Override
    public Result getBlockByHeight(int height) {
        return restTemplate.getForObject(EventResourceConstant.BLOCK_BY_HEIGHT+height,Result.class);
    }

    /**
     * Get transaction by hash. The return value is bytes form.
     * @param hash
     * @return Result
     */
    @Override
    public Result getTxByHash(String hash) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(API_SERVER_URL+EventResourceConstant.TX_BY_HASH_BYTES)
                .queryParam("hash",hash);
        return restTemplate.getForObject(uriComponentsBuilder.build().encode().toUri(),Result.class);
    }

    /**
     * Get Yellow or Red card from the given block height if the underlying transaction is Red/Yellow Punish type
     * @param height
     * @return Result
     */
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
            accountService = NulsContext.getServiceBean(AccountService.class);
            String alias = accountService.getAlias(address).getData();
            punishDTOS.add(EventUtil.buildAgentPunishDTO(new Agent(),po,alias));
        }
        return new Result(true,KernelErrorCode.SUCCESS,punishDTOS);
    }
}
