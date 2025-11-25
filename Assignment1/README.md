# Assignment 1: Producer-Consumer Pattern Implementation

FileContent:
PurposeProducerConsumerDemo.java - This file has Producer-Consumer implementation which has Main code for thread synchronization
ProducerConsumerTest.java - Tests all concurrent functionality
program_output.txt - Console output from main program which Shows producer-consumer demo results
test_ouput.txt - Console output from test suite which Shows all 8 tests passing

1. ProducerConsumerDemo is the main class containing the producer-consumer implementation.

Inner Class: Producer Reads items from a source container and places them into a shared blocking queue.

**Key Methods:**

| Method | Purpose | Implementation Details |
|--------|---------|----------------------|
| 'run()' | Main producer logic | Iterates through source items, simulates processing with random delays, puts items in queue, increments atomic counter |

**Thread Synchronization Features:**
- Uses 'BlockingQueue.put()' - blocks if queue is full (wait mechanism)
- Handles 'InterruptedException' for graceful shutdown
- Updates 'AtomicInteger' counter thread-safely

**Inner Class: Consumer**

**Purpose:** Takes items from the shared queue and stores them in a destination container.

**Key Methods:**

| Method | Purpose | Implementation Details |
|--------|---------|----------------------|
| 'run()' | Main consumer logic | Continuously takes items from queue, checks for poison pill termination signal, processes items, stores in synchronized destination |

**Thread Synchronization Features:**
- Uses 'BlockingQueue.take()' - blocks if queue is empty (wait mechanism)
- Implements poison pill pattern for clean shutdown
- Synchronizes access to destination container
- Updates 'AtomicInteger' counter thread-safely

#### **Main Method**

**Purpose:** Orchestrates the producer-consumer demonstration with configurable parameters.

**Configuration:**
QUEUE_CAPACITY = 5        // Maximum items in queue
NUM_PRODUCERS = 2         // Number of producer threads
NUM_CONSUMERS = 2         // Number of consumer threads
TOTAL_ITEMS = 20         // Total items to process


**Workflow:**
1. Creates source data (20 items)
2. Initializes blocking queue with capacity 5
3. Starts 2 producer threads (each handles 10 items)
4. Starts 2 consumer threads
5. Waits for producers to complete
6. Sends poison pill to signal consumers
7. Waits for consumers to finish
8. Displays final results


## Unit Tests


All tests validate thread synchronization, concurrent programming, blocking queues, and atomic operations.

| Test # | Test Name | Purpose | What It Validates |
|--------|-----------|---------|-------------------|
| **1** | Producer produces all items correctly | Basic producer functionality | - Producer reads all source items - Items placed in queue correctly - Atomic counter accurate- Items in correct order |
| **2** | Consumer consumes until termination signal | Basic consumer functionality | - Consumer processes all items- Poison pill pattern works- Items stored in destination- Consumer stops cleanly |
| **3** | Complete flow works correctly | End-to-end pipeline | - Producer → Queue → Consumer flow<br>- Data integrity maintained- All items transferred- Counts match |
| **4** | Multiple producers/consumers synchronize | Concurrent access | - 2 producers + 2 consumers work together- No race conditions- No data loss- Proper synchronization |
| **5** | Blocking queue respects capacity | Queue behavior | - Queue accepts items up to capacity- Rejects when full- Accepts after space available |
| **6** | Shared destination is thread-safe | Thread safety | - 20 items from 2 producers- No data loss- No corruption- Synchronized access works |
| **7** | Threads handle interruption gracefully | Error handling | - Thread can be interrupted- Cleans up properly- No hanging- Exception caught |
| **8** | Atomic counters maintain accuracy | Concurrent counting | - 50 items from 2 producers- Counter exactly 50- No lost increments- Thread-safe operations |

---

## How to Run

### **Prerequisites**
- Java JDK 11 or higher
- No external dependencies required (uses standard Java libraries)

### **Compile**

javac ProducerConsumerDemo.java
javac ProducerConsumerTest.java


### **Run Main Program**

java ProducerConsumerDemo


### **Run Tests**

java ProducerConsumerTest

### **Main Program Output**

```
=== Producer-Consumer Demo ===
Queue Capacity: 5
Producers: 2
Consumers: 2
Total Items: 20
================================

Producer-1 produced: Item-1 (Total produced: 2)
Producer-2 produced: Item-11 (Total produced: 1)
Producer-2 produced: Item-12 (Total produced: 3)
Consumer-1 consumed: Item-11 (Total consumed: 1)
Producer-1 produced: Item-2 (Total produced: 4)
Consumer-1 consumed: Item-12 (Total consumed: 2)
Consumer-2 consumed: Item-1 (Total consumed: 3)
Producer-1 produced: Item-3 (Total produced: 5)
Producer-2 produced: Item-13 (Total produced: 6)
Consumer-1 consumed: Item-2 (Total consumed: 4)
Producer-1 produced: Item-4 (Total produced: 7)
Consumer-2 consumed: Item-3 (Total consumed: 5)
Producer-1 produced: Item-5 (Total produced: 8)
Producer-2 produced: Item-14 (Total produced: 9)
Consumer-1 consumed: Item-13 (Total consumed: 6)
Consumer-2 consumed: Item-4 (Total consumed: 7)
Producer-1 produced: Item-6 (Total produced: 10)
Producer-2 produced: Item-15 (Total produced: 11)
Consumer-1 consumed: Item-5 (Total consumed: 8)
Consumer-2 consumed: Item-14 (Total consumed: 9)
Producer-1 produced: Item-7 (Total produced: 12)
Producer-2 produced: Item-16 (Total produced: 13)
Consumer-1 consumed: Item-6 (Total consumed: 10)
Consumer-2 consumed: Item-15 (Total consumed: 11)
Consumer-1 consumed: Item-7 (Total consumed: 12)
Producer-1 produced: Item-8 (Total produced: 14)
Consumer-2 consumed: Item-16 (Total consumed: 13)
Producer-2 produced: Item-17 (Total produced: 15)
Consumer-1 consumed: Item-8 (Total consumed: 14)
Producer-1 produced: Item-9 (Total produced: 16)
Consumer-2 consumed: Item-17 (Total consumed: 15)
Producer-2 produced: Item-18 (Total produced: 17)
Consumer-1 consumed: Item-9 (Total consumed: 16)
Producer-1 produced: Item-10 (Total produced: 18)
Producer-1 finished producing.
Producer-2 produced: Item-19 (Total produced: 19)
Consumer-1 consumed: Item-10 (Total consumed: 17)
Consumer-2 consumed: Item-18 (Total consumed: 18)
Producer-2 produced: Item-20 (Total produced: 20)
Producer-2 finished producing.

All producers finished. Sending termination signal...
Consumer-1 consumed: Item-19 (Total consumed: 19)
Consumer-1 received termination signal.
Consumer-1 finished consuming.
Consumer-2 consumed: Item-20 (Total consumed: 20)
Consumer-2 received termination signal.
Consumer-2 finished consuming.

=== Final Results ===
Total Items Produced: 20
Total Items Consumed: 20
Source Container Size: 20
Destination Container Size: 20
Items Match: true

Destination Contents:
  Item-11
  Item-12
  Item-1
  Item-2
  Item-3
  Item-13
  Item-4
  Item-5
  Item-14
  Item-6
  Item-15
  Item-7
  Item-16
  Item-8
  Item-17
  Item-9
  Item-10
  Item-18
  Item-19
  Item-20
```

### **Test Output**

```
========================================
PRODUCER-CONSUMER COMPREHENSIVE TEST SUITE
========================================

[TEST 1] Producer produces all items correctly
TestProducer produced: Item-1 (Total produced: 1)
TestProducer produced: Item-2 (Total produced: 2)
TestProducer produced: Item-3 (Total produced: 3)
TestProducer finished producing.
[PASSED]

[TEST 2] Consumer consumes all items until termination signal
TestConsumer consumed: Item-1 (Total consumed: 1)
TestConsumer consumed: Item-2 (Total consumed: 2)
TestConsumer consumed: Item-3 (Total consumed: 3)
TestConsumer received termination signal.
TestConsumer finished consuming.
[PASSED]

[TEST 3] Complete producer-consumer flow works correctly
TestProducer produced: Item-1 (Total produced: 1)
TestProducer produced: Item-2 (Total produced: 2)
TestConsumer consumed: Item-1 (Total consumed: 1)
TestProducer produced: Item-3 (Total produced: 3)
TestConsumer consumed: Item-2 (Total consumed: 2)
TestProducer produced: Item-4 (Total produced: 4)
TestConsumer consumed: Item-3 (Total consumed: 3)
TestConsumer consumed: Item-4 (Total consumed: 4)
TestProducer produced: Item-5 (Total produced: 5)
TestProducer finished producing.
TestConsumer consumed: Item-5 (Total consumed: 5)
TestConsumer received termination signal.
TestConsumer finished consuming.
[PASSED]

[TEST 4] Multiple producers and consumers synchronize correctly
P1 produced: P1-Item-1 (Total produced: 1)
P2 produced: P2-Item-1 (Total produced: 2)
C2 consumed: P1-Item-1 (Total consumed: 1)
P1 produced: P1-Item-2 (Total produced: 3)
C1 consumed: P2-Item-1 (Total consumed: 2)
P2 produced: P2-Item-2 (Total produced: 4)
C1 consumed: P2-Item-2 (Total consumed: 3)
C2 consumed: P1-Item-2 (Total consumed: 4)
P1 produced: P1-Item-3 (Total produced: 5)
P1 finished producing.
P2 produced: P2-Item-3 (Total produced: 6)
P2 finished producing.
C1 consumed: P1-Item-3 (Total consumed: 5)
C1 received termination signal.
C1 finished consuming.
C2 consumed: P2-Item-3 (Total consumed: 6)
C2 received termination signal.
C2 finished consuming.
[PASSED]

[TEST 5] Blocking queue respects capacity constraints
[PASSED]

[TEST 6] Shared destination container is thread-safe
P1 produced: Item-1 (Total produced: 1)
P2 produced: Item-11 (Total produced: 2)
C1 consumed: Item-1 (Total consumed: 1)
P1 produced: Item-2 (Total produced: 3)
P2 produced: Item-12 (Total produced: 4)
P2 produced: Item-13 (Total produced: 5)
P2 produced: Item-14 (Total produced: 6)
P1 produced: Item-3 (Total produced: 7)
C1 consumed: Item-11 (Total consumed: 2)
P2 produced: Item-15 (Total produced: 8)
P1 produced: Item-4 (Total produced: 9)
C1 consumed: Item-2 (Total consumed: 3)
P1 produced: Item-5 (Total produced: 10)
P2 produced: Item-16 (Total produced: 11)
P2 produced: Item-17 (Total produced: 12)
C1 consumed: Item-12 (Total consumed: 4)
P1 produced: Item-6 (Total produced: 13)
P2 produced: Item-18 (Total produced: 14)
P2 produced: Item-19 (Total produced: 15)
C1 consumed: Item-13 (Total consumed: 5)
P1 produced: Item-7 (Total produced: 16)
C1 consumed: Item-14 (Total consumed: 6)
P2 produced: Item-20 (Total produced: 17)
P2 finished producing.
C1 consumed: Item-3 (Total consumed: 7)
P1 produced: Item-8 (Total produced: 18)
C1 consumed: Item-15 (Total consumed: 8)
P1 produced: Item-9 (Total produced: 19)
C1 consumed: Item-4 (Total consumed: 9)
P1 produced: Item-10 (Total produced: 20)
P1 finished producing.
C1 consumed: Item-5 (Total consumed: 10)
C1 consumed: Item-16 (Total consumed: 11)
C1 consumed: Item-17 (Total consumed: 12)
C1 consumed: Item-6 (Total consumed: 13)
C1 consumed: Item-18 (Total consumed: 14)
C1 consumed: Item-19 (Total consumed: 15)
C1 consumed: Item-7 (Total consumed: 16)
C1 consumed: Item-20 (Total consumed: 17)
C1 consumed: Item-8 (Total consumed: 18)
C1 consumed: Item-9 (Total consumed: 19)
C1 consumed: Item-10 (Total consumed: 20)
C1 received termination signal.
C1 finished consuming.
[PASSED]

[TEST 7] Threads handle interruption gracefully
TestProducer was interrupted.
[PASSED]

[TEST 8] Atomic counters maintain accuracy under concurrent access
P2 produced: Item-26 (Total produced: 1)
P1 produced: Item-1 (Total produced: 2)
P1 produced: Item-2 (Total produced: 3)
Consumer consumed: Item-26 (Total consumed: 1)
Consumer consumed: Item-1 (Total consumed: 2)
P2 produced: Item-27 (Total produced: 4)
P1 produced: Item-3 (Total produced: 5)
Consumer consumed: Item-2 (Total consumed: 3)
P2 produced: Item-28 (Total produced: 6)
P1 produced: Item-4 (Total produced: 7)
Consumer consumed: Item-27 (Total consumed: 4)
P2 produced: Item-29 (Total produced: 8)
Consumer consumed: Item-3 (Total consumed: 5)
P1 produced: Item-5 (Total produced: 9)
P2 produced: Item-30 (Total produced: 10)
P2 produced: Item-31 (Total produced: 11)
Consumer consumed: Item-28 (Total consumed: 6)
P2 produced: Item-32 (Total produced: 12)
P1 produced: Item-6 (Total produced: 13)
P2 produced: Item-33 (Total produced: 14)
Consumer consumed: Item-4 (Total consumed: 7)
P1 produced: Item-7 (Total produced: 15)
Consumer consumed: Item-29 (Total consumed: 8)
P2 produced: Item-34 (Total produced: 16)
P1 produced: Item-8 (Total produced: 17)
P1 produced: Item-9 (Total produced: 18)
Consumer consumed: Item-5 (Total consumed: 9)
P2 produced: Item-35 (Total produced: 19)
Consumer consumed: Item-30 (Total consumed: 10)
P1 produced: Item-10 (Total produced: 20)
Consumer consumed: Item-31 (Total consumed: 11)
P2 produced: Item-36 (Total produced: 21)
Consumer consumed: Item-32 (Total consumed: 12)
P1 produced: Item-11 (Total produced: 22)
Consumer consumed: Item-6 (Total consumed: 13)
P1 produced: Item-12 (Total produced: 23)
Consumer consumed: Item-33 (Total consumed: 14)
P2 produced: Item-37 (Total produced: 24)
P1 produced: Item-13 (Total produced: 25)
P1 produced: Item-14 (Total produced: 26)
P2 produced: Item-38 (Total produced: 27)
P1 produced: Item-15 (Total produced: 28)
Consumer consumed: Item-7 (Total consumed: 15)
Consumer consumed: Item-34 (Total consumed: 16)
P2 produced: Item-39 (Total produced: 29)
P1 produced: Item-16 (Total produced: 30)
Consumer consumed: Item-8 (Total consumed: 17)
P1 produced: Item-17 (Total produced: 31)
P2 produced: Item-40 (Total produced: 32)
P2 produced: Item-41 (Total produced: 33)
Consumer consumed: Item-9 (Total consumed: 18)
P1 produced: Item-18 (Total produced: 34)
Consumer consumed: Item-35 (Total consumed: 19)
P2 produced: Item-42 (Total produced: 35)
P1 produced: Item-19 (Total produced: 36)
P2 produced: Item-43 (Total produced: 37)
Consumer consumed: Item-10 (Total consumed: 20)
P2 produced: Item-44 (Total produced: 38)
P2 produced: Item-45 (Total produced: 39)
P1 produced: Item-20 (Total produced: 40)
Consumer consumed: Item-36 (Total consumed: 21)
P2 produced: Item-46 (Total produced: 41)
P2 produced: Item-47 (Total produced: 42)
P1 produced: Item-21 (Total produced: 43)
Consumer consumed: Item-11 (Total consumed: 22)
P2 produced: Item-48 (Total produced: 44)
P1 produced: Item-22 (Total produced: 45)
Consumer consumed: Item-12 (Total consumed: 23)
P1 produced: Item-23 (Total produced: 46)
P2 produced: Item-49 (Total produced: 47)
P1 produced: Item-24 (Total produced: 48)
Consumer consumed: Item-37 (Total consumed: 24)
P2 produced: Item-50 (Total produced: 49)
P2 finished producing.
Consumer consumed: Item-13 (Total consumed: 25)
P1 produced: Item-25 (Total produced: 50)
P1 finished producing.
Consumer consumed: Item-14 (Total consumed: 26)
Consumer consumed: Item-38 (Total consumed: 27)
Consumer consumed: Item-15 (Total consumed: 28)
Consumer consumed: Item-39 (Total consumed: 29)
Consumer consumed: Item-16 (Total consumed: 30)
Consumer consumed: Item-17 (Total consumed: 31)
Consumer consumed: Item-40 (Total consumed: 32)
Consumer consumed: Item-41 (Total consumed: 33)
Consumer consumed: Item-18 (Total consumed: 34)
Consumer consumed: Item-42 (Total consumed: 35)
Consumer consumed: Item-19 (Total consumed: 36)
Consumer consumed: Item-43 (Total consumed: 37)
Consumer consumed: Item-44 (Total consumed: 38)
Consumer consumed: Item-45 (Total consumed: 39)
Consumer consumed: Item-20 (Total consumed: 40)
Consumer consumed: Item-46 (Total consumed: 41)
Consumer consumed: Item-47 (Total consumed: 42)
Consumer consumed: Item-21 (Total consumed: 43)
Consumer consumed: Item-48 (Total consumed: 44)
Consumer consumed: Item-22 (Total consumed: 45)
Consumer consumed: Item-23 (Total consumed: 46)
Consumer consumed: Item-49 (Total consumed: 47)
Consumer consumed: Item-24 (Total consumed: 48)
Consumer consumed: Item-50 (Total consumed: 49)
Consumer consumed: Item-25 (Total consumed: 50)
Consumer received termination signal.
Consumer finished consuming.
[PASSED]

========================================
TEST SUMMARY
========================================
Total Tests: 8
Passed: 8
Failed: 0
Success Rate: 100%
========================================

ALL TESTS PASSED!
```

---


### **Thread Synchronization Mechanisms**

1. **BlockingQueue Operations**
   ```java
   sharedQueue.put(item);    // Blocks if queue is full (wait)
   String item = sharedQueue.take();  // Blocks if queue is empty (wait)
   ```

2. **Atomic Counters**
   ```java
   int produced = itemsProduced.incrementAndGet();  // Thread-safe increment
   ```

3. **Synchronized Blocks**
   ```java
   synchronized (destinationContainer) {
       destinationContainer.add(item);  // Thread-safe add
   }
   ```

4. **Poison Pill Pattern**
   ```java
   if (POISON_PILL.equals(item)) {
       sharedQueue.put(POISON_PILL);  // Pass to other consumers
       break;  // Exit cleanly
   }
   ```

### **Why This Design?**

- **BlockingQueue**: Handles wait/notify automatically, eliminates need for manual synchronization
- **Atomic Counters**: Provides thread-safe counting without locks
- **Poison Pill**: Enables clean shutdown without forceful thread termination
- **Random Delays**: Simulates real-world processing time, tests concurrent behavior

---

## Testing Objectives Met

| Objective | Implementation | Test Coverage |
|-----------|---------------|---------------|
| **Thread Synchronization** | BlockingQueue + synchronized blocks | Tests 3, 4, 6 |
| **Concurrent Programming** | Multiple producers/consumers | Tests 4, 6, 8 |
| **Blocking Queues** | LinkedBlockingQueue with capacity | Tests 1, 2, 5 |
| **Wait/Notify Mechanism** | Implicit in BlockingQueue operations | Tests 3, 5 |
| **Atomic Operations** | AtomicInteger for counters | Test 8 |
| **Error Handling** | InterruptedException handling | Test 7 |

---




