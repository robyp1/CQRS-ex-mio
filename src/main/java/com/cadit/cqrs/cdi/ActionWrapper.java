package com.cadit.cqrs.cdi;

import com.cadit.cqrs.data.DocEvent;

import javax.enterprise.event.Event;

public class ActionWrapper {

     public <T extends Event<C>, C extends DocEvent> void accept(T localEvent, C event){
        localEvent.fire(event);
    }
}
