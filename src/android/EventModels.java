package com.amankumar.cordova.BackgroundServiceAndroid;

public class EventModels {
    public String eventtype,message;
    public EventModels(String eventtype, String message) {
        this.eventtype=eventtype;
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEventtype() {
        return eventtype;
    }

    public void setEventtype(String eventtype) {
        this.eventtype = eventtype;
    }
}