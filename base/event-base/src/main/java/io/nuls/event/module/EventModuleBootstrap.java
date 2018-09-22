package io.nuls.event.module;

import io.nuls.event.EventApplication;
import io.nuls.event.module.AbstractEventModule;

public class EventModuleBootstrap extends AbstractEventModule {


    @Override
    public void init() throws Exception {

    }

    @Override
    public void start() {
        String args[] = {};
        EventApplication.main(args);
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public String getInfo() {
        return null;
    }
}
