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
        StringBuilder str = new StringBuilder();
        str.append("moduleName:");
        str.append(getModuleName());
        str.append(",moduleStatus:");
        str.append(getStatus());
        str.append(",ThreadCount:");
        return str.toString();
    }
}
