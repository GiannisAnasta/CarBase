package service;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

@Named("messenger")

@ApplicationScoped

public class Messenger {

    public static class CompanyFlushEvent {

    }
    @Inject
    Event<CompanyFlushEvent> events;

    public void flushCompanies() {

        events.fire(new CompanyFlushEvent());

    }

}
