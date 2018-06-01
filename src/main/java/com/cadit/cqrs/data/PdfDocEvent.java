package com.cadit.cqrs.data;

import java.util.GregorianCalendar;

public class PdfDocEvent extends DocEvent{


    public PdfDocEvent(Integer documentId, GregorianCalendar eventTime) {
        super(documentId, eventTime);
    }

    public PdfDocEvent(Integer documentId) {
        super(documentId);
    }

    @Override
    public String toString() {
        return "PdfDocEvent, " + super.toString().replace("DocEvent","");
    }
}
