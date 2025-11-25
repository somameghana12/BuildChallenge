import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 Tests: Thread synchronization, concurrent programming, blocking queues,
       wait/notify mechanism, atomic operations, thread safety
 */
public class ProducerConsumerTest {
    
    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;
    
    // Test helper methods
    private static void assertEquals(String message, Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " - Expected: " + expected + ", but got: " + actual);
        }
    }
    
    private static void assertTrue(String message, boolean condition) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
    
    private static void assertFalse(String message, boolean condition) {
        if (condition) {
            throw new AssertionError(message);
        }
    }
    
    private static void runTest(String testName, TestRunnable test) {
        totalTests++;
        System.out.println("\n[TEST " + totalTests + "] " + testName);
        try {
            test.run();
            passedTests++;
            System.out.println("[PASSED]");
        } catch (Exception e) {
            failedTests++;
            System.out.println("[FAILED]: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    interface TestRunnable {
        void run() throws Exception;
    }
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("PRODUCER-CONSUMER COMPREHENSIVE TEST SUITE");
        System.out.println("========================================");
        
        runTest("Producer produces all items correctly", () -> testProducerProducesAllItems());
        runTest("Consumer consumes all items until termination signal", () -> testConsumerConsumesAllItems());
        runTest("Complete producer-consumer flow works correctly", () -> testCompleteProducerConsumerFlow());
        runTest("Multiple producers and consumers synchronize correctly", () -> testMultipleProducersAndConsumers());
        runTest("Blocking queue respects capacity constraints", () -> testBlockingQueueCapacity());
        runTest("Shared destination container is thread-safe", () -> testThreadSafetyOfDestination());
        runTest("Threads handle interruption gracefully", () -> testThreadInterruption());
        runTest("Atomic counters maintain accuracy under concurrent access", () -> testAtomicCounterAccuracy());
        
        // Print summary
        System.out.println("\n========================================");
        System.out.println("TEST SUMMARY");
        System.out.println("========================================");
        System.out.println("Total Tests: " + totalTests);
        System.out.println("Passed: " + passedTests);
        System.out.println("Failed: " + failedTests);
        System.out.println("Success Rate: " + (passedTests * 100 / totalTests) + "%");
        System.out.println("========================================");
        
        if (failedTests == 0) {
            System.out.println("\nALL TESTS PASSED!");
        } else {
            System.out.println("\nSOME TESTS FAILED");
            System.exit(1); // Exit with error code
        }
    }
    
    /**
     * Test 1: Verify producer successfully produces all items
     * Tests: Thread synchronization, Blocking queue operations
     */
    public static void testProducerProducesAllItems() throws InterruptedException {
        List<String> sourceContainer = new ArrayList<>();
        BlockingQueue<String> sharedQueue = new LinkedBlockingQueue<>(10);
        AtomicInteger itemsProduced = new AtomicInteger(0);
        
        sourceContainer.addAll(Arrays.asList("Item-1", "Item-2", "Item-3"));
        
        ProducerConsumerDemo.Producer producer = 
            new ProducerConsumerDemo.Producer(sourceContainer, sharedQueue, 
                                             "TestProducer", itemsProduced);
        
        Thread producerThread = new Thread(producer);
        producerThread.start();
        producerThread.join(5000);
        
        assertEquals("Producer should produce exactly 3 items", 3, itemsProduced.get());
        assertEquals("Queue should contain 3 items", 3, sharedQueue.size());
        
        assertEquals("First item should be Item-1", "Item-1", sharedQueue.take());
        assertEquals("Second item should be Item-2", "Item-2", sharedQueue.take());
        assertEquals("Third item should be Item-3", "Item-3", sharedQueue.take());
    }
    
    /**
     * Test 2: Verify consumer successfully consumes all items until poison pill
     * Tests: Wait/Notify mechanism, Thread synchronization
     */
    public static void testConsumerConsumesAllItems() throws InterruptedException {
        BlockingQueue<String> sharedQueue = new LinkedBlockingQueue<>(10);
        List<String> destinationContainer = Collections.synchronizedList(new ArrayList<>());
        AtomicInteger itemsConsumed = new AtomicInteger(0);
        
        sharedQueue.put("Item-1");
        sharedQueue.put("Item-2");
        sharedQueue.put("Item-3");
        sharedQueue.put(ProducerConsumerDemo.POISON_PILL);
        
        ProducerConsumerDemo.Consumer consumer = 
            new ProducerConsumerDemo.Consumer(sharedQueue, destinationContainer, 
                                             "TestConsumer", itemsConsumed);
        
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
        consumerThread.join(5000);
        
        assertEquals("Consumer should consume exactly 3 items", 3, itemsConsumed.get());
        assertEquals("Destination should have 3 items", 3, destinationContainer.size());
        assertTrue("All items should be in destination",
                  destinationContainer.containsAll(Arrays.asList("Item-1", "Item-2", "Item-3")));
    }
    
    /**
     * Test 3: Verify complete producer-consumer flow with single producer and consumer
     * Tests: Concurrent programming, Data integrity
     */
    public static void testCompleteProducerConsumerFlow() throws InterruptedException {
        List<String> sourceContainer = new ArrayList<>();
        List<String> destinationContainer = Collections.synchronizedList(new ArrayList<>());
        BlockingQueue<String> sharedQueue = new LinkedBlockingQueue<>(10);
        AtomicInteger itemsProduced = new AtomicInteger(0);
        AtomicInteger itemsConsumed = new AtomicInteger(0);
        
        for (int i = 1; i <= 5; i++) {
            sourceContainer.add("Item-" + i);
        }
        
        ProducerConsumerDemo.Producer producer = 
            new ProducerConsumerDemo.Producer(sourceContainer, sharedQueue, 
                                             "TestProducer", itemsProduced);
        ProducerConsumerDemo.Consumer consumer = 
            new ProducerConsumerDemo.Consumer(sharedQueue, destinationContainer, 
                                             "TestConsumer", itemsConsumed);
        
        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);
        
        consumerThread.start();
        producerThread.start();
        
        producerThread.join(10000);
        sharedQueue.put(ProducerConsumerDemo.POISON_PILL);
        consumerThread.join(10000);
        
        assertEquals("Should produce 5 items", 5, itemsProduced.get());
        assertEquals("Should consume 5 items", 5, itemsConsumed.get());
        assertEquals("Source and destination sizes should match", 
                    sourceContainer.size(), destinationContainer.size());
        assertTrue("All source items should be in destination", 
                  destinationContainer.containsAll(sourceContainer));
    }
    
    /**
     * Test 4: Verify multiple producers and consumers work correctly
     * Tests: Thread synchronization with multiple threads, Concurrent programming
     */
    public static void testMultipleProducersAndConsumers() throws InterruptedException {
        List<String> source1 = Arrays.asList("P1-Item-1", "P1-Item-2", "P1-Item-3");
        List<String> source2 = Arrays.asList("P2-Item-1", "P2-Item-2", "P2-Item-3");
        
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(5);
        List<String> dest = Collections.synchronizedList(new ArrayList<>());
        
        AtomicInteger produced = new AtomicInteger(0);
        AtomicInteger consumed = new AtomicInteger(0);
        
        ProducerConsumerDemo.Producer producer1 = 
            new ProducerConsumerDemo.Producer(source1, queue, "P1", produced);
        ProducerConsumerDemo.Producer producer2 = 
            new ProducerConsumerDemo.Producer(source2, queue, "P2", produced);
        ProducerConsumerDemo.Consumer consumer1 = 
            new ProducerConsumerDemo.Consumer(queue, dest, "C1", consumed);
        ProducerConsumerDemo.Consumer consumer2 = 
            new ProducerConsumerDemo.Consumer(queue, dest, "C2", consumed);
        
        Thread p1 = new Thread(producer1);
        Thread p2 = new Thread(producer2);
        Thread c1 = new Thread(consumer1);
        Thread c2 = new Thread(consumer2);
        
        c1.start();
        c2.start();
        p1.start();
        p2.start();
        
        p1.join(15000);
        p2.join(15000);
        queue.put(ProducerConsumerDemo.POISON_PILL);
        
        c1.join(15000);
        c2.join(15000);
        
        assertEquals("Should produce 6 items total", 6, produced.get());
        assertEquals("Should consume 6 items total", 6, consumed.get());
        assertEquals("Destination should have 6 items", 6, dest.size());
    }
    
    /**
     * Test 5: Verify blocking queue capacity constraints
     * Tests: Blocking queue behavior, Wait mechanism
     */
    public static void testBlockingQueueCapacity() throws InterruptedException {
        BlockingQueue<String> smallQueue = new LinkedBlockingQueue<>(2);
        
        assertTrue("Should accept first item", smallQueue.offer("Item-1"));
        assertTrue("Should accept second item", smallQueue.offer("Item-2"));
        assertFalse("Should reject third item (queue full)", 
                   smallQueue.offer("Item-3", 100, TimeUnit.MILLISECONDS));
        
        assertEquals("Queue size should be 2", 2, smallQueue.size());
        
        smallQueue.take();
        
        assertTrue("Should accept item after space available", 
                  smallQueue.offer("Item-3", 100, TimeUnit.MILLISECONDS));
    }
    
    /**
     * Test 6: Verify thread safety of shared resources
     * Tests: Thread synchronization, Concurrent access
     */
    public static void testThreadSafetyOfDestination() throws InterruptedException {
        List<String> source = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            source.add("Item-" + i);
        }
        
        List<String> source1 = source.subList(0, 10);
        List<String> source2 = source.subList(10, 20);
        
        BlockingQueue<String> sharedQueue = new LinkedBlockingQueue<>(10);
        List<String> destinationContainer = Collections.synchronizedList(new ArrayList<>());
        AtomicInteger produced = new AtomicInteger(0);
        AtomicInteger consumed = new AtomicInteger(0);
        
        Thread p1 = new Thread(new ProducerConsumerDemo.Producer(
            new ArrayList<>(source1), sharedQueue, "P1", produced));
        Thread p2 = new Thread(new ProducerConsumerDemo.Producer(
            new ArrayList<>(source2), sharedQueue, "P2", produced));
        Thread c1 = new Thread(new ProducerConsumerDemo.Consumer(
            sharedQueue, destinationContainer, "C1", consumed));
        
        c1.start();
        p1.start();
        p2.start();
        
        p1.join(10000);
        p2.join(10000);
        sharedQueue.put(ProducerConsumerDemo.POISON_PILL);
        c1.join(10000);
        
        assertEquals("Should produce 20 items", 20, produced.get());
        assertEquals("Should consume 20 items", 20, consumed.get());
        assertEquals("No items should be lost", 20, destinationContainer.size());
    }
    
    /**
     * Test 7: Verify proper handling of interruption
     * Tests: Thread interruption handling
     */
    public static void testThreadInterruption() throws InterruptedException {
        List<String> sourceContainer = new ArrayList<>();
        BlockingQueue<String> sharedQueue = new LinkedBlockingQueue<>(10);
        AtomicInteger itemsProduced = new AtomicInteger(0);
        
        sourceContainer.addAll(Arrays.asList("Item-1", "Item-2", "Item-3"));
        ProducerConsumerDemo.Producer producer = 
            new ProducerConsumerDemo.Producer(sourceContainer, sharedQueue, 
                                             "TestProducer", itemsProduced);
        
        Thread producerThread = new Thread(producer);
        
        producerThread.start();
        Thread.sleep(50);
        producerThread.interrupt();
        producerThread.join(5000);
        
        assertFalse("Thread should terminate after interruption", 
                   producerThread.isAlive());
    }
    
    /**
     * Test 8: Verify atomic counter accuracy
     * Tests: Concurrent programming, Atomic operations
     */
    public static void testAtomicCounterAccuracy() throws InterruptedException {
        List<String> largeSource = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            largeSource.add("Item-" + i);
        }
        
        List<String> source1 = new ArrayList<>(largeSource.subList(0, 25));
        List<String> source2 = new ArrayList<>(largeSource.subList(25, 50));
        
        // Large queue to avoid blocking producers
        BlockingQueue<String> sharedQueue = new LinkedBlockingQueue<>(100);
        List<String> dest = Collections.synchronizedList(new ArrayList<>());
        AtomicInteger produced = new AtomicInteger(0);
        AtomicInteger consumed = new AtomicInteger(0);
        
        // Start consumer to prevent queue from filling up
        Thread consumer = new Thread(new ProducerConsumerDemo.Consumer(
            sharedQueue, dest, "Consumer", consumed));
        
        Thread p1 = new Thread(new ProducerConsumerDemo.Producer(
            source1, sharedQueue, "P1", produced));
        Thread p2 = new Thread(new ProducerConsumerDemo.Producer(
            source2, sharedQueue, "P2", produced));
        
        consumer.start();
        p1.start();
        p2.start();
        p1.join(10000);
        p2.join(10000);
        
        // Send poison pill to stop consumer
        sharedQueue.put(ProducerConsumerDemo.POISON_PILL);
        consumer.join(10000);
        
        assertEquals("Atomic counter should accurately count all items from both producers", 
                    50, produced.get());
        assertEquals("Consumer should consume all 50 items", 50, consumed.get());
    }
}
