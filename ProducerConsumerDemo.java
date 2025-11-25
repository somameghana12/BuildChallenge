
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**Demonstrates thread synchronization and communication using blocking queues
Supports multiple producers and consumers with proper termination
*/

public class ProducerConsumerDemo {
    
    // Poison pill constant for signaling completion
    public static final String POISON_PILL = "END_OF_STREAM";
    
    //Producer thread that reads items from source and places them in shared queue
     
    static class Producer implements Runnable {
        private final List<String> sourceContainer;
        private final BlockingQueue<String> sharedQueue;
        private final String producerName;
        private final AtomicInteger itemsProduced;
        
        public Producer(List<String> sourceContainer, BlockingQueue<String> sharedQueue, 
                       String name, AtomicInteger itemsProduced) {
            this.sourceContainer = sourceContainer;
            this.sharedQueue = sharedQueue;
            this.producerName = name;
            this.itemsProduced = itemsProduced;
        }
        
        @Override
        public void run() {
            try {
                for (String item : sourceContainer) {
                    // Simulate processing time
                    Thread.sleep(ThreadLocalRandom.current().nextInt(50, 200));
                    
                    // Put item in shared queue (blocks if queue is full)
                    sharedQueue.put(item);
                    int produced = itemsProduced.incrementAndGet();
                    System.out.println(producerName + " produced: " + item + 
                                     " (Total produced: " + produced + ")");
                }
                System.out.println(producerName + " finished producing.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println(producerName + " was interrupted.");
            }
        }
    }
    
    //Consumer thread that reads items from shared queue and stores in destination
    static class Consumer implements Runnable {
        private final BlockingQueue<String> sharedQueue;
        private final List<String> destinationContainer;
        private final String consumerName;
        private final AtomicInteger itemsConsumed;
        
        public Consumer(BlockingQueue<String> sharedQueue, List<String> destinationContainer, 
                       String name, AtomicInteger itemsConsumed) {
            this.sharedQueue = sharedQueue;
            this.destinationContainer = destinationContainer;
            this.consumerName = name;
            this.itemsConsumed = itemsConsumed;
        }
        
        @Override
        public void run() {
            try {
                while (true) {
                    // Take item from queue (blocks if queue is empty)
                    String item = sharedQueue.take();
                    
                    // Check for poison pill to terminate
                    if (POISON_PILL.equals(item)) {
                        System.out.println(consumerName + " received termination signal.");
                        // Put poison pill back for other consumers
                        sharedQueue.put(POISON_PILL);
                        break;
                    }
                    
                    // Simulate processing time
                    Thread.sleep(ThreadLocalRandom.current().nextInt(50, 200));
                    
                    // Store in destination (thread-safe)
                    synchronized (destinationContainer) {
                        destinationContainer.add(item);
                    }
                    
                    int consumed = itemsConsumed.incrementAndGet();
                    System.out.println(consumerName + " consumed: " + item + 
                                     " (Total consumed: " + consumed + ")");
                }
                System.out.println(consumerName + " finished consuming.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println(consumerName + " was interrupted.");
            }
        }
    }
    
    // Demonstrates the producer-consumer pattern which supports configurable number of producers and consumers
    public static void main(String[] args) throws InterruptedException {
        // Configuration
        final int QUEUE_CAPACITY = 5;
        final int NUM_PRODUCERS = 2;
        final int NUM_CONSUMERS = 2;
        final int TOTAL_ITEMS = 20;
        
        System.out.println("=== Producer-Consumer Demo ===");
        System.out.println("Queue Capacity: " + QUEUE_CAPACITY);
        System.out.println("Producers: " + NUM_PRODUCERS);
        System.out.println("Consumers: " + NUM_CONSUMERS);
        System.out.println("Total Items: " + TOTAL_ITEMS);
        System.out.println("================================\n");
        
        // Source container with data
        List<String> sourceContainer = new ArrayList<>();
        for (int i = 1; i <= TOTAL_ITEMS; i++) {
            sourceContainer.add("Item-" + i);
        }
        
        // Shared blocking queue with capacity
        BlockingQueue<String> sharedQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        
        // Destination container (thread-safe)
        List<String> destinationContainer = Collections.synchronizedList(new ArrayList<>());
        
        // Atomic counters for tracking
        AtomicInteger totalProduced = new AtomicInteger(0);
        AtomicInteger totalConsumed = new AtomicInteger(0);
        
        // Create and start producer threads
        List<Thread> producerThreads = new ArrayList<>();
        for (int i = 0; i < NUM_PRODUCERS; i++) {
            // Divide source data among producers
            int startIdx = i * sourceContainer.size() / NUM_PRODUCERS;
            int endIdx = (i + 1) * sourceContainer.size() / NUM_PRODUCERS;
            List<String> producerData = new ArrayList<>(sourceContainer.subList(startIdx, endIdx));
            
            Producer producer = new Producer(producerData, sharedQueue, 
                                           "Producer-" + (i + 1), totalProduced);
            Thread producerThread = new Thread(producer);
            producerThreads.add(producerThread);
            producerThread.start();
        }
        
        // Create and start consumer threads
        List<Thread> consumerThreads = new ArrayList<>();
        for (int i = 0; i < NUM_CONSUMERS; i++) {
            Consumer consumer = new Consumer(sharedQueue, destinationContainer, 
                                           "Consumer-" + (i + 1), totalConsumed);
            Thread consumerThread = new Thread(consumer);
            consumerThreads.add(consumerThread);
            consumerThread.start();
        }
        
        // Wait for all producers to complete
        for (Thread producerThread : producerThreads) {
            producerThread.join();
        }
        
        // Send poison pill to signal consumers to terminate
        System.out.println("\nAll producers finished. Sending termination signal...");
        sharedQueue.put(POISON_PILL);
        
        // Wait for all consumers to complete
        for (Thread consumerThread : consumerThreads) {
            consumerThread.join();
        }
        
        // Display final results
        System.out.println("\n=== Final Results ===");
        System.out.println("Total Items Produced: " + totalProduced.get());
        System.out.println("Total Items Consumed: " + totalConsumed.get());
        System.out.println("Source Container Size: " + sourceContainer.size());
        System.out.println("Destination Container Size: " + destinationContainer.size());
        System.out.println("Items Match: " + (totalProduced.get() == totalConsumed.get()));
        System.out.println("\nDestination Contents:");
        destinationContainer.forEach(item -> System.out.println("  " + item));
    }
}