import exception.*;

import java.util.*;
import java.util.concurrent.*;

/**
 *  Implements a timing clock which makes a callback on every tick to the listeners
 *
 *  Example:
 *        Clock clock = new Clock(1000); // Period = 1 second
 *        TickListener listener = new TickListener() { // Create a listener
 *            public void onTick() {
 *                // To do stuff
 *            }
 *        };
 *        clock.addListener(listener); // Add the listener to the clock
 *        clock.start(); // Start the clock. The onTick() method of the listener will be executed on each second
 *
 *
 *
 */
public class Clock {

    private long millis;
    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    private final List<TickListener> listeners = new ArrayList<>(); // Listeners
    private boolean run;

    private ExecutorService executor;
    private ScheduledFuture<?> tickHandler;

    /** Initialize a clock without a period, it must be defined later */
    public Clock() {
        this(-1);
    }

    /** Initialize a clock with the given period in milliseconds */
    public Clock(long milliseconds) {
        run = false;
        millis = milliseconds;
    }

    /** Initialize a clock with the given listeners and without a period */
    public Clock(Collection<? extends TickListener> listeners) {
        this(-1, listeners);
    }

    /** Initialize a clock with the given period and the given listeners */
    public Clock(long milliseconds, Collection<? extends TickListener> listeners) {
        this(milliseconds);
        this.listeners.addAll(listeners);
    }

    /** Add a listener to the clock */
    public void addListener(TickListener listener) {
        if (listener == null)
            return;
        if (!listeners.contains(listener)) {
            listeners.add(listener);
            if (isRunning()) {
                executor = Executors.newFixedThreadPool(listeners.size());
            }
        }
    }

    /** Remove a listener from the clock */
    public void removeListener(TickListener listener) {
        listeners.remove(listener);
        if (isRunning()) {
            executor = Executors.newFixedThreadPool(listeners.size());
        }
    }

    /** Returns the period on milliseconds */
    public long getPeriod() {
        return millis;
    }

    /** Sets the period on milliseconds */
    public void setPeriod(long milliseconds) {
        millis = milliseconds;
    }

    private final Runnable callback = () ->
    {
        for (TickListener listener : listeners)
            executor.submit(() -> listener.onTick());
    };

    /** Start clock */
    public void start() throws PeriodNotSetException, ClockRunningException {
        if (isRunning())
            throw new ClockRunningException("Clock is already running");
        if (millis <= 0)
            throw new PeriodNotSetException("Period has not been set!");
        run = true;
        executor = Executors.newFixedThreadPool(listeners.size());
        tickHandler = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(callback, 0, millis, timeUnit);
    }

    public void stop() {
        executor.shutdownNow();
        tickHandler.cancel(true);
        run = false;
    }

    public boolean isRunning() {
        return run;
    }
}
