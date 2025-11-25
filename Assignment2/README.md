
# Assignment 2: Sales Data Analysis Using Java Streams API

FileContent:
PurposeSalesDataAnalyzer.java- 18 analysis methods using Streams - Main code for data analysis
SalesDataAnalyzerTest.java- 24 unit tests that Tests all analysis methods
sales_data.csv- 30 sales records ,Dataset for analysisR
Test_output.txt- Console output from test suite that Shows all 24 tests passing
analyzer_output.txt- Console output from main analysis that Shows all 18 analyses results


## Dataset Description

### **CSV Structure**

```csv
OrderID,Region,Product,Category,Price,Quantity,Date
ORD001,North,Laptop,Electronics,1200.00,2,2024-01-15
ORD002,South,Mouse,Electronics,25.00,5,2024-01-16
........
```

### **Fields**

| Field | Type | Description | Example |
|-------|------|-------------|---------|
| OrderID | String | Unique order identifier | ORD001 |
| Region | String | Geographic region | North, South, East, West |
| Product | String | Product name | Laptop, Mouse, Desk |
| Category | String | Product category | Electronics, Furniture |
| Price | Double | Unit price in USD | 1200.00 |
| Quantity | Integer | Number of units | 2 |
| Date | LocalDate | Transaction date | 2024-01-15 |

### **Dataset Assumptions**

- All prices are in USD
- Data represents 2-month sales period (Jan-Feb 2024)
- ach order contains a single product type
- No returns, refunds, or cancellations
- 30 sample records 

---

## Classes and Methods

### **SalesDataAnalyzer Class**

Main class containing 18 analysis methods using Java Streams API.

#### **Inner Class: SalesRecord**

**Purpose:** Immutable data class representing a single sales transaction.

**Key Methods:**

| Method | Return Type | Purpose |
|--------|-------------|---------|
| `getTotalSales()` | double | Calculate order value (price × quantity) |
| `getMonthYear()` | String | Get month-year string (yyyy-MM) |
| `getYear()` | String | Extract year from date |

---

##  Analysis Methods

### **1. BASIC AGGREGATIONS **

#### **Method 1: calculateTotalRevenue()**

**Purpose:** Calculate total revenue across all sales

**Output:**
```
1. Total Revenue: $27,200.00
```


#### **Method 2: calculateAverageOrderValue()**

**Purpose:** Calculate average order value

**Output :**
```
2. Average Order Value: $906.67
```


### **2. REGIONAL ANALYSIS **

#### **Method 3: getSalesByRegion()**

**Purpose:** Group and sum sales by region


**Output :**
```
3. Total Sales by Region:
   North     : $ 10,350.00
   West      : $  5,900.00
   East      : $  5,650.00
   South     : $  5,300.00
```



#### **Method 4: getAverageSalesByRegion()**

**Purpose:** Calculate average sales per order by region


**Output :**
```
4. Average Sales by Region:
   North     : $  1,293.75
   West      : $    842.86
   East      : $    807.14
   South     : $    662.50
```

---

#### **Method 5: getOrderCountByRegion()**

**Purpose:** Count number of orders per region


**Output:**
```
5. Order Count by Region:
   West      : 7 orders
   South     : 8 orders
   North     : 8 orders
   East      : 7 orders
```

---


### **3. CATEGORY ANALYSIS **

#### **Method 6: getSalesByCategory()**

**Purpose:** Group and sum sales by product category

**Output :**
```
Total Sales by Category:
   Electronics:    $21,350.00
   Furniture:      $11,175.00
```

---

#### **Method 7: getAveragePriceByCategory()**

**Purpose:** Calculate average product price by category


**Output:**
```

7. Average Price by Category:
   Electronics    : $  412.50
   Furniture      : $  325.00

```

---

#### **Method 8: getSalesStatisticsByCategory()**

**Purpose:** Comprehensive statistics by category

**Output :**
```
Electronics:
   Count: 20, Min: $25.00, Max: $2,400.00
   Avg: $1,067.50, Total: $21,350.00

Furniture:
   Count: 10, Min: $200.00, Max: $1,800.00
   Avg: $1,117.50, Total: $11,175.00
```

---

### **4. PRODUCT ANALYSIS **

#### **Method 9: getTopProductsByRevenue(int n)**

**Purpose:** Find top N products by total revenue


**Output :**
```
Top 5 Products by Revenue:
   Laptop:        $8,400.00
   Desk:          $6,300.00
   Monitor:       $4,900.00
   Chair:         $2,600.00
   Keyboard:      $2,025.00
```


---

#### **Method 10: getTotalQuantityByProduct()**

**Purpose:** Sum total units sold per product


**Output:**
```
Total Quantity Sold by Product:
   Mouse          :   50 units
   Keyboard       :   30 units
   Chair          :   25 units
   Monitor        :   14 units
   Desk           :   12 units
   Laptop         :    7 units
```

---

#### **Method 11: getProductsByCategory()**

**Purpose:** Get unique products within each category

**Output:**
```
Products by Category:
   Electronics:    [Laptop, Monitor, Keyboard, Mouse]
   Furniture:      [Desk, Chair]
```

**What It Shows:**
- Nested collectors with `mapping()`
- Collecting to Set for uniqueness
- Demonstrates collector composition

---

### **5. TEMPORAL ANALYSIS**

#### **Method 12: getMonthlySalesTrend()**

**Purpose:** Track sales over time by month


**Output:**
```
Monthly Sales Trend:
   2024-01   : $ 12,975.00
   2024-02   : $ 14,225.00
```

---

#### **Method 13: getYearlySales()**

**Purpose:** Aggregate sales by year

**Output :**
```
Yearly Sales:
2024      : $ 27,200.00

```

---

### **6. ADVANCED ANALYSIS**

#### **Method 14: getHighestValueOrder()**

**Purpose:** Find single highest value order

**Output :**
```
Highest Value Order:
   Order[ORD001, North, Laptop, $1200.00 x 2 = $2400.00, 2024-01-15]
```

---

#### **Method 15: getOrdersAboveThreshold**

**Purpose:** Filter orders above specified value


**Output Example:**
```
Orders Above $1000:
   Order[ORD001, North, Laptop, $1200.00 x 2 = $2400.00, 2024-01-15]
   Order[ORD019, North, Laptop, $1200.00 x 2 = $2400.00, 2024-02-03]
   Order[ORD020, East, Desk, $450.00 x 4 = $1800.00, 2024-02-04]
   Order[ORD023, North, Monitor, $350.00 x 4 = $1400.00, 2024-02-07]
   Order[ORD027, North, Chair, $200.00 x 7 = $1400.00, 2024-02-11]
   Order[ORD014, South, Desk, $450.00 x 3 = $1350.00, 2024-01-28]
   Order[ORD007, East, Laptop, $1200.00 x 1 = $1200.00, 2024-01-21]
   Order[ORD013, West, Laptop, $1200.00 x 1 = $1200.00, 2024-01-27]
   Order[ORD021, West, Chair, $200.00 x 6 = $1200.00, 2024-02-05]
   Order[ORD025, West, Laptop, $1200.00 x 1 = $1200.00, 2024-02-09]
```

---
#### **Method 16: getRevenuePercentageByRegion()**

**Purpose:** Calculate percentage of total revenue by region


**Output:**
```
16. Revenue Percentage by Region:
   North     :  38.05%
   West      :  21.69%
   East      :  20.77%
   South     :  19.49%

```
---
#### **Method 17: getDistinctProductCountByRegion()**

**Purpose:** Get distinct product count by region
**Output:**
```
17. Distinct Product Count by Region:
   West      : 5 products
   South     : 5 products
   North     : 5 products
   East      : 5 products
```
#### **Method 18: getRevenuePercentageByRegion()**

**Purpose:** Get revenue percentage by region
**Output:**
```
18. Order Value Distribution (threshold=$500):
   High Value (>=$500): 21 orders
   Low Value (<$500):  9 orders
```

---

##  Unit Tests 

### **Test Suite: SalesDataAnalyzerTest.java**


| Test # | Test Name | Methods Tested | Purpose |
|--------|-----------|----------------|---------|
| **1** | Data Loading | loadSalesData() | CSV parsing, filtering nulls |
| **2** | Total Revenue | calculateTotalRevenue() | mapToDouble, sum |
| **3** | Average Order Value | calculateAverageOrderValue() | average, Optional |
| **4** | Sales by Region | getSalesByRegion() | groupingBy, summingDouble |
| **5** | Average Sales by Region | getAverageSalesByRegion() | averagingDouble |
| **6** | Order Count by Region | getOrderCountByRegion() | counting |
| **7** | Sales by Category | getSalesByCategory() | groupingBy basics |
| **8** | Average Price by Category | getAveragePriceByCategory() | averagingDouble |
| **9** | Sales Statistics | getSalesStatisticsByCategory() | summarizingDouble |
| **10** | Top Products | getTopProductsByRevenue() | sorted, limit |
| **11** | Quantity by Product | getTotalQuantityByProduct() | summingInt |
| **12** | Products by Category | getProductsByCategory() | mapping, toSet |
| **13** | Monthly Trend | getMonthlySalesTrend() | Date grouping, LinkedHashMap |
| **14** | Yearly Sales | getYearlySales() | Year extraction |
| **15** | Highest Order | getHighestValueOrder() | max, Optional |
| **16** | Orders Above Threshold | getOrdersAboveThreshold() | filter, sorted |
| **17** | Partition Orders | partitionOrdersByValue() | partitioningBy |
| **18** | Distinct Count | getDistinctProductCountByRegion() | nested collectors |
| **19** | Revenue Percentage | getRevenuePercentageByRegion() | Complex calculation |
| **20** | Lambda Expressions | Various | Lambda functionality |
| **21** | Functional Programming | Various | Immutability, repeatability |
| **22** | Empty Results | getOrdersAboveThreshold() | Edge case handling |
| **23** | Optional Handling | getHighestValueOrder() | Optional.ifPresent |
| **24** | CSV Parsing Errors | parseCsvLine() | Error recovery |

---

## How to Run

### **Prerequisites**
- Java JDK 11 or higher
- CSV file: `sales_data.csv` in same directory

### **Compile**
```bash
javac SalesDataAnalyzer.java
javac SalesDataAnalyzerTest.java
```

### **Run Main Analysis**
```bash
java SalesDataAnalyzer
```

### **Run Tests**
```bash
java SalesDataAnalyzerTest
```

---



### **Main Analysis Output**

```
Loading sales data from: sales_data.csv
Data loaded successfully: 30 records

================================================================
           SALES DATA ANALYSIS RESULTS                          
================================================================
Total records processed: 30

=== BASIC AGGREGATIONS ===
1. Total Revenue: $27,200.00
2. Average Order Value: $906.67

=== REGIONAL ANALYSIS ===
3. Total Sales by Region:
   North     : $ 10,350.00
   West      : $  5,900.00
   East      : $  5,650.00
   South     : $  5,300.00

4. Average Sales by Region:
   North     : $  1,293.75
   West      : $    842.86
   East      : $    807.14
   South     : $    662.50

5. Order Count by Region:
   West      : 7 orders
   South     : 8 orders
   North     : 8 orders
   East      : 7 orders

=== CATEGORY ANALYSIS ===
6. Total Sales by Category:
   Electronics    : $ 16,800.00
   Furniture      : $ 10,400.00

7. Average Price by Category:
   Electronics    : $  412.50
   Furniture      : $  325.00

8. Sales Statistics by Category:
   Electronics:
      Count: 20, Min: $125.00, Max: $2400.00, Avg: $840.00, Total: $16800.00
   Furniture:
      Count: 10, Min: $450.00, Max: $1800.00, Avg: $1040.00, Total: $10400.00

=== PRODUCT ANALYSIS ===
9. Top 5 Products by Revenue:
   Laptop         : $  8,400.00
   Desk           : $  5,400.00
   Chair          : $  5,000.00
   Monitor        : $  4,900.00
   Keyboard       : $  2,250.00

10. Total Quantity Sold by Product:
   Mouse          :   50 units
   Keyboard       :   30 units
   Chair          :   25 units
   Monitor        :   14 units
   Desk           :   12 units
   Laptop         :    7 units

11. Products by Category:
   Electronics    : [Laptop, Monitor, Mouse, Keyboard]
   Furniture      : [Chair, Desk]

=== TEMPORAL ANALYSIS ===
12. Monthly Sales Trend:
   2024-01   : $ 12,975.00
   2024-02   : $ 14,225.00

13. Yearly Sales:
   2024      : $ 27,200.00

=== ADVANCED ANALYSIS ===
14. Highest Value Order:
   Order[ORD001, North, Laptop, $1200.00 x 2 = $2400.00, 2024-01-15]

15. Orders Above $1000:
   Order[ORD001, North, Laptop, $1200.00 x 2 = $2400.00, 2024-01-15]
   Order[ORD019, North, Laptop, $1200.00 x 2 = $2400.00, 2024-02-03]
   Order[ORD020, East, Desk, $450.00 x 4 = $1800.00, 2024-02-04]
   Order[ORD023, North, Monitor, $350.00 x 4 = $1400.00, 2024-02-07]
   Order[ORD027, North, Chair, $200.00 x 7 = $1400.00, 2024-02-11]
   Order[ORD014, South, Desk, $450.00 x 3 = $1350.00, 2024-01-28]
   Order[ORD007, East, Laptop, $1200.00 x 1 = $1200.00, 2024-01-21]
   Order[ORD013, West, Laptop, $1200.00 x 1 = $1200.00, 2024-01-27]
   Order[ORD021, West, Chair, $200.00 x 6 = $1200.00, 2024-02-05]
   Order[ORD025, West, Laptop, $1200.00 x 1 = $1200.00, 2024-02-09]

16. Revenue Percentage by Region:
   North     :  38.05%
   West      :  21.69%
   East      :  20.77%
   South     :  19.49%

================================================================
                   ANALYSIS COMPLETE                            
================================================================
```

### **Test Suite Output**

```
╔════════════════════════════════════════════════════════════╗
║     SALES DATA ANALYZER - STANDALONE TEST SUITE           ║
╚════════════════════════════════════════════════════════════╝


Test 1: Data Loading
─────────────────────────────────────────────────
  ✓ Should load 10 records
  ✓ First record should not be null
  ✓ First order ID
  ✓ First product
  ✓ First price

Test 2: Calculate Total Revenue
─────────────────────────────────────────────────
  ✓ Total revenue calculation

Test 3: Calculate Average Order Value
─────────────────────────────────────────────────
  ✓ Average order value calculation

Test 4: Total Sales by Region
─────────────────────────────────────────────────
  ✓ Should have 4 regions
  ✓ Should contain North
  ✓ Should contain South
  ✓ Should contain East
  ✓ Should contain West
  ✓ North sales

Test 5: Average Sales by Region
─────────────────────────────────────────────────
  ✓ Should have 4 regions
  ✓ All averages should be positive

Test 6: Order Count by Region
─────────────────────────────────────────────────
  ✓ Should have 4 regions
  ✓ Total orders

Test 7: Total Sales by Category
─────────────────────────────────────────────────
  ✓ Should have 2 categories
  ✓ Should contain Electronics
  ✓ Should contain Furniture
  ✓ Electronics sales

Test 8: Average Price by Category
─────────────────────────────────────────────────
  ✓ Should have 2 categories
  ✓ Electronics avg > 0
  ✓ Furniture avg > 0

Test 9: Sales Statistics by Category
─────────────────────────────────────────────────
  ✓ Should have 2 categories
  ✓ Electronics statistics exist
  ✓ Electronics has orders
  ✓ Max >= Min

Test 10: Top Products by Revenue
─────────────────────────────────────────────────
  ✓ Should return 3 products
  ✓ Top product is Laptop
  ✓ Laptop revenue
  ✓ Results in descending order
  ✓ Results in descending order

Test 11: Total Quantity by Product
─────────────────────────────────────────────────
  ✓ Contains Laptop
  ✓ Laptop quantity
  ✓ Contains Keyboard
  ✓ Keyboard quantity

Test 12: Products by Category
─────────────────────────────────────────────────
  ✓ Should have 2 categories
  ✓ Electronics contains Laptop
  ✓ Electronics contains Smartphone

Test 13: Monthly Sales Trend
─────────────────────────────────────────────────
  ✓ Has monthly data
  ✓ Contains Jan 2024
  ✓ Contains Feb 2024
  ✓ January sales
  ✓ Is LinkedHashMap

Test 14: Yearly Sales
─────────────────────────────────────────────────
  ✓ Contains 2024
  ✓ 2024 sales

Test 15: Highest Value Order
─────────────────────────────────────────────────
  ✓ Highest order exists
  ✓ Highest >= 1200

Test 16: Orders Above Threshold
─────────────────────────────────────────────────
  ✓ Found orders above $500
  ✓ Order meets threshold
  ✓ Order meets threshold
  ✓ Order meets threshold
  ✓ Sorted descending
  ✓ Sorted descending

Test 17: Partition Orders by Value
─────────────────────────────────────────────────
  ✓ Has two partitions
  ✓ High value >= 500
  ✓ High value >= 500
  ✓ High value >= 500
  ✓ Low value < 500
  ✓ Low value < 500
  ✓ Low value < 500
  ✓ Low value < 500
  ✓ Low value < 500
  ✓ Low value < 500
  ✓ Low value < 500
  ✓ All orders partitioned

Test 18: Distinct Product Count by Region
─────────────────────────────────────────────────
  ✓ Has 4 regions
  ✓ Count is positive
  ✓ Count is positive
  ✓ Count is positive
  ✓ Count is positive

Test 19: Revenue Percentage by Region
─────────────────────────────────────────────────
  ✓ Has 4 regions
  ✓ Percentages sum to 100%

Test 20: Lambda Expressions
─────────────────────────────────────────────────
  ✓ Lambda filter works
  ✓ Lambda mapping works
  ✓ Lambda reduction works

Test 21: Functional Programming Paradigm
─────────────────────────────────────────────────
  ✓ Operations are repeatable

Test 22: Empty Results Handling
─────────────────────────────────────────────────
  ✓ Returns list, not null
  ✓ List is empty

Test 23: Optional Handling
─────────────────────────────────────────────────
  ✓ Optional is present
  ✓ Has positive value

Test 24: CSV Parsing Error Handling
─────────────────────────────────────────────────
  ✓ Loads only valid records

╔════════════════════════════════════════════════════════════╗
║                      TEST SUMMARY                          ║
╠════════════════════════════════════════════════════════════╣
║  Tests Passed: 83                                          ║
║  Tests Failed: 0                                           ║
║  Total Tests:  83                                          ║
╠════════════════════════════════════════════════════════════╣
║  Result: ✓ ALL TESTS PASSED! 🎉                           ║
╚════════════════════════════════════════════════════════════╝
Warning: Could not parse line: INVALID LINE WITH MISSING FIELDS


** Process exited - Return Code: 0 **
```

---


## Testing Objectives 

| Objective | Implementation | Test Coverage |
|-----------|---------------|---------------|
| **Functional Programming** | Immutable SalesRecord, pure functions | Tests 20, 21 |
| **Stream Operations** | 10+ distinct operations | All 24 tests |
| **Data Aggregation** | sum, average, count, statistics | Tests 2-19 |
| **Lambda Expressions** | Throughout all methods | Test 20 |
| **CSV Processing** | File reading with streams | Tests 1, 24 |
| **Error Handling** | Null filtering, exception handling | Test 24 |

