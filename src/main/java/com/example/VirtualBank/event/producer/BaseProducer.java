package com.example.VirtualBank.event.producer;

import com.example.VirtualBank.event.model.BaseEvent;
import java.util.concurrent.CompletableFuture;

public interface BaseProducer {
    
    CompletableFuture<Void> sendEvent(String topic, BaseEvent event);
    
    CompletableFuture<Void> sendEvent(String topic, String key, BaseEvent event);
    
}