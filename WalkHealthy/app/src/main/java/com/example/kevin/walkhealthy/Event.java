package com.example.kevin.walkhealthy;

/**
 * Created by Steath on 10/10/2017.
 */

public class Event {
    public String EventMonth;
    public String EventName;
    public String EventStartingLocation;
    public String EventEndingLocation;
    public String EventDay;




    public Event(){};

    public Event(String EventDay, String EventMonth, String EventName, String EventStartingLocation, String EventEndingLocation){
        this.EventDay = EventDay;
        this.EventMonth = EventMonth;
        this.EventName=EventName;
        this.EventEndingLocation=EventStartingLocation;
        this.EventEndingLocation=EventEndingLocation;
    }



    public void setDay(int EventDay){EventDay=EventDay;}
    public void setMonth(String EventMonth){this.EventMonth=EventMonth;}
    public void setEventName(String eventName) {
        EventName = eventName;
    }
    public void setEventStartingLocation(String eventStartingLocation) {EventStartingLocation = eventStartingLocation;}
    public void setEventEndingLocation(String eventEndingLocation) {EventEndingLocation = eventEndingLocation;}


    public String getDay(){return EventDay;}
    public String getMonth(){return EventMonth;}
    public String getEventName() {
        return EventName;
    }
    public String getEventStartingLocation() {
        return EventStartingLocation;
    }
    public String getEventEndingLocation() {
        return EventEndingLocation;
    }
}
