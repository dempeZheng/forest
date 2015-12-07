package com.dempe.ketty.ha.listener;

import java.util.EventListener;


public interface HAListener extends EventListener {
    void handleEvent(HAEvent event) throws Exception;
}