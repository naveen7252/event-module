package io.nuls.event.service.impl;

import io.nuls.event.constant.EventResourceConstant;
import io.nuls.event.service.EventService;
import io.nuls.kernel.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public Result getLatestBlock(){
		return restTemplate.getForObject(EventResourceConstant.NEWEST_BLOCK,Result.class);
    }

    @Override
    public Result getLatestBlock(int height) {
        return restTemplate.getForObject(EventResourceConstant.BLOCK_BY_HEIGHT+height,Result.class);
    }

}
