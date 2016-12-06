# Timing Clock
This project implements a timing clock, which has a collection of listeners which implement the TickListener interface. On each tick of the clock, the callback method of each listener of the clock will be executed.

Example of use:
```java
Clock clock = new Clock(1000); // Create a clock with a period of 1 second
TickListener listener = new TickListener() { 
    @Override
    public void onTick() { // This is the callback method. On each second it will be executed
        System.out.println("Hello World!");
    }
};
clock.addListener(listener); // Add the listener to the clock
clock.start(); // Start the clock
```
This will print "Hello World!" on each second. Of course, you can add more listeners and the callbacks of each one will be called at the same time.