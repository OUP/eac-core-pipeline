package com.oup.eac.log4j;

import java.util.Date;
import java.util.LinkedList;

import org.apache.log4j.Logger;

public class BurstDetector  {

    private static final Logger LOG = Logger.getLogger(BurstDetector.class);
    
    protected enum State {
        NORMAL {
            @Override
            public boolean increment(BurstDetector detector, long now) {
                return detector.incrementNormal(now);
            }
        },
        IN_BURST {
            @Override
            public boolean increment(BurstDetector detector, long now) {
                return detector.incrementInBurst(now);
            }
        };
        public abstract boolean increment(BurstDetector detector, long now);

    }
    
    //CONFIG
    private final long numberOfEvents;
    private final long window;
    
    //STATE
    protected final LinkedList<Long> events = new LinkedList<Long>();
    protected State state;
    protected long burstTime;
    protected long inBurstCount;
    
    BurstDetector(long numberOfEvents, long window) {
        this.numberOfEvents = numberOfEvents;
        this.window = window;
        String msg = String.format("BurstDetector initialised with [%d]#events in [%d]ms",numberOfEvents,window);
        LOG.info(msg);
        setState(State.NORMAL, System.currentTimeMillis());
    }
    
    protected LinkedList<Long> getEvents() {
        return events;
    }
    
    protected void setState(State state, long now) {
        if (LOG.isInfoEnabled()) {
            String msg = String.format("State set to [%s] at [%s]", state, new Date(now));
            LOG.info(msg);
        }
        this.state = state;
        this.events.clear();
        this.burstTime = now;
        this.inBurstCount=0;
    }
    
    public synchronized boolean  increment() {       
        long now = System.currentTimeMillis();
        boolean result =  this.state.increment(this, now);
        LOG.info("increment result " + result);
        return result;
    }

    private boolean incrementInBurst(long now) {
        if(LOG.isInfoEnabled()){
            String msg = String.format("IN BURST [%d]", this.inBurstCount);
            LOG.info(msg);
        }        
        inBurstCount++;
        long timeSinceBurst = now - burstTime;
        boolean withinWindow = timeSinceBurst < window;
        if(inBurstCount > numberOfEvents && withinWindow ){
            setState(State.IN_BURST, now);
            return false;
        }else if(!withinWindow){
            setState(State.NORMAL, now);
            return this.incrementNormal(now);
        }
        return false;
    }

    private boolean incrementNormal(long now) {
        long oldestTime = now - window;        
        
        //  Remove anything older than 'limit'
        while(events.isEmpty() == false && events.getLast() < oldestTime) {                   
            events.removeLast();
        }
        events.addFirst(now);
        if(events.size() > numberOfEvents) {
            setState(State.IN_BURST, now);
           return false;
        }else{
           return true;
        }
    }

    

}
