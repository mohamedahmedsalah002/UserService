package com.example.VirtualBank.event.consumer;

import com.example.VirtualBank.event.model.BaseEvent;

public interface BaseConsumer {
    
    void processEvent(BaseEvent event);
    
    void handleError(BaseEvent event, Throwable throwable);
    
    boolean canHandle(String eventType);
    
}