package io.nuls.event.module;

import io.nuls.event.constant.EventConstant;
import io.nuls.kernel.module.BaseModuleBootstrap;

public abstract class AbstractEventModule extends BaseModuleBootstrap {

    protected AbstractEventModule(){
        super(EventConstant.MODULE_ID_EVENT);
    }
}
