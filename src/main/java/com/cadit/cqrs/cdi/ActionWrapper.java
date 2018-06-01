package com.cadit.cqrs.cdi;

import com.cadit.cqrs.data.DocEvent;

import javax.enterprise.event.Event;

public class  ActionWrapper<T extends Event<C>,C extends DocEvent> {


    private final T localEvent;

    public ActionWrapper(T event) {
      localEvent  = event;
    }

    public  void accept(C event){
        localEvent.fire(event);
    }
}
