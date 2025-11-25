import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Standalone unit tests for SalesDataAnalyzer (No JUnit required)
 * Run directly with: java SalesDataAnalyzerTestStandalone
 */
public class SalesDataAnalyzerTest {
    
    private static final String TEST_CSV_FILE = "test_sales_data.csv";
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     SALES DATA ANALYZER - STANDALONE TEST SUITE           ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        try {
            // Setup
            createTestData();
            SalesDataAnalyzer analyzer = new SalesDataAnalyzer(TEST_CSV_FILE);
            
            // Run all tests
            test1_DataLoading(analyzer);
            test2_TotalRevenue(analyzer);
            test3_AverageOrderValue(analyzer);
            test4_SalesByRegion(analyzer);
            test5_AverageSalesByRegion(analyzer);
            test6_OrderCountByRegion(analyzer);
            test7_SalesByCategory(analyzer);
            test8_AveragePriceByCategory(analyzer);
            test9_SalesStatisticsByCategory(analyzer);
            test10_TopProductsByRevenue(analyzer);
            test11_TotalQuantityByProduct(analyzer);
            test12_ProductsByCategory(analyzer);
            test13_MonthlySalesTrend(analyzer);
            test14_YearlySales(analyzer);
            test15_HighestValueOrder(analyzer);
            test16_OrdersAboveThreshold(analyzer);
            test17_PartitionOrdersByValue(analyzer);
            test18_DistinctProductCountByRegion(analyzer);
            test19_RevenuePercentageByRegion(analyzer);
            test20_LambdaExpressions(analyzer);
            test21_FunctionalProgramming(analyzer);
            test22_EmptyResults(analyzer);
            test23_OptionalHandling(analyzer);
            test24_CSVParsingErrors();
            
            // Cleanup
            Files.deleteIfExists(Paths.get(TEST_CSV_FILE));
            
            // Summary
            printSummary();
            
        } catch (Exception e) {
            System.err.println("❌ Test suite failed with exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ========== TEST HELPER METHODS ==========
    
    private static void createTestData() throws IOException {
        // Total should be: 1200 + 800 + 250 + 400 + 150 + 300 + 1200 + 450 + 75 + 250 = 5075
        String csvContent = 
            "OrderID,Region,Product,Category,Price,Quantity,Date\n" +
            "ORD001,North,Laptop,Electronics,1200.00,1,2024-01-15\n" +
            "ORD002,South,Smartphone,Electronics,800.00,1,2024-01-20\n" +
            "ORD003,East,Desk Chair,Furniture,250.00,1,2024-02-05\n" +
            "ORD004,North,Monitor,Electronics,400.00,1,2024-02-10\n" +
            "ORD005,West,Keyboard,Electronics,75.00,2,2024-02-15\n" +
            "ORD006,South,Bookshelf,Furniture,300.00,1,2024-03-01\n" +
            "ORD007,East,Laptop,Electronics,1200.00,1,2024-03-10\n" +
            "ORD008,North,Desk,Furniture,450.00,1,2024-03-15\n" +
            "ORD009,West,Mouse,Electronics,25.00,3,2024-04-01\n" +
            "ORD010,South,Chair,Furniture,125.00,2,2024-04-05\n";
        
        Files.write(Paths.get(TEST_CSV_FILE), csvContent.getBytes());
    }
    
    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected.equals(actual)) {
            pass(message);
        } else {
            fail(message + " | Expected: " + expected + ", Got: " + actual);
        }
    }
    
    private static void assertEquals(double expected, double actual, double delta, String message) {
        if (Math.abs(expected - actual) <= delta) {
            pass(message);
        } else {
            fail(message + " | Expected: " + expected + ", Got: " + actual);
        }
    }
    
    private static void assertTrue(boolean condition, String message) {
        if (condition) {
            pass(message);
        } else {
            fail(message);
        }
    }
    
    private static void assertFalse(boolean condition, String message) {
        assertTrue(!condition, message);
    }
    
    private static void assertNotNull(Object obj, String message) {
        assertTrue(obj != null, message);
    }
    
    private static void pass(String message) {
        testsPassed++;
        System.out.println("  ✓ " + message);
    }
    
    private static void fail(String message) {
        testsFailed++;
        System.out.println("  ✗ " + message);
    }
    
    private static void testHeader(String testName) {
        System.out.println("\n" + testName);
        System.out.println("─────────────────────────────────────────────────");
    }
    
    private static void printSummary() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                      TEST SUMMARY                          ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.printf("║  Tests Passed: %-43d ║%n", testsPassed);
        System.out.printf("║  Tests Failed: %-43d ║%n", testsFailed);
        System.out.printf("║  Total Tests:  %-43d ║%n", testsPassed + testsFailed);
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        
        if (testsFailed == 0) {
            System.out.println("║  Result: ✓ ALL TESTS PASSED! 🎉                           ║");
        } else {
            System.out.println("║  Result: ✗ SOME TESTS FAILED                              ║");
        }
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
    
    // ========== TEST CASES ==========
    
    private static void test1_DataLoading(SalesDataAnalyzer analyzer) {
        testHeader("Test 1: Data Loading");
        assertEquals(10, analyzer.salesData.size(), "Should load 10 records");
        assertNotNull(analyzer.salesData.get(0), "First record should not be null");
        assertEquals("ORD001", analyzer.salesData.get(0).getOrderId(), "First order ID");
        assertEquals("Laptop", analyzer.salesData.get(0).getProduct(), "First product");
        assertEquals(1200.00, analyzer.salesData.get(0).getPrice(), 0.001, "First price");
    }
    
    private static void test2_TotalRevenue(SalesDataAnalyzer analyzer) {
        testHeader("Test 2: Calculate Total Revenue");
        double totalRevenue = analyzer.calculateTotalRevenue();
        assertEquals(5075.0, totalRevenue, 0.01, "Total revenue calculation");
    }
    
    private static void test3_AverageOrderValue(SalesDataAnalyzer analyzer) {
        testHeader("Test 3: Calculate Average Order Value");
        double avgOrderValue = analyzer.calculateAverageOrderValue();
        assertEquals(507.5, avgOrderValue, 0.01, "Average order value calculation");
    }
    
    private static void test4_SalesByRegion(SalesDataAnalyzer analyzer) {
        testHeader("Test 4: Total Sales by Region");
        Map<String, Double> salesByRegion = analyzer.getSalesByRegion();
        
        assertEquals(4, salesByRegion.size(), "Should have 4 regions");
        assertTrue(salesByRegion.containsKey("North"), "Should contain North");
        assertTrue(salesByRegion.containsKey("South"), "Should contain South");
        assertTrue(salesByRegion.containsKey("East"), "Should contain East");
        assertTrue(salesByRegion.containsKey("West"), "Should contain West");
        assertEquals(2050.0, salesByRegion.get("North"), 0.01, "North sales");
    }
    
    private static void test5_AverageSalesByRegion(SalesDataAnalyzer analyzer) {
        testHeader("Test 5: Average Sales by Region");
        Map<String, Double> avgByRegion = analyzer.getAverageSalesByRegion();
        
        assertEquals(4, avgByRegion.size(), "Should have 4 regions");
        assertTrue(avgByRegion.values().stream().allMatch(avg -> avg > 0), 
            "All averages should be positive");
    }
    
    private static void test6_OrderCountByRegion(SalesDataAnalyzer analyzer) {
        testHeader("Test 6: Order Count by Region");
        Map<String, Long> countByRegion = analyzer.getOrderCountByRegion();
        
        assertEquals(4, countByRegion.size(), "Should have 4 regions");
        long totalOrders = countByRegion.values().stream().mapToLong(Long::longValue).sum();
        assertEquals(10L, totalOrders, "Total orders");
    }
    
    private static void test7_SalesByCategory(SalesDataAnalyzer analyzer) {
        testHeader("Test 7: Total Sales by Category");
        Map<String, Double> salesByCategory = analyzer.getSalesByCategory();
        
        assertEquals(2, salesByCategory.size(), "Should have 2 categories");
        assertTrue(salesByCategory.containsKey("Electronics"), "Should contain Electronics");
        assertTrue(salesByCategory.containsKey("Furniture"), "Should contain Furniture");
        assertEquals(3825.0, salesByCategory.get("Electronics"), 0.01, "Electronics sales");
    }
    
    private static void test8_AveragePriceByCategory(SalesDataAnalyzer analyzer) {
        testHeader("Test 8: Average Price by Category");
        Map<String, Double> avgPriceByCategory = analyzer.getAveragePriceByCategory();
        
        assertEquals(2, avgPriceByCategory.size(), "Should have 2 categories");
        assertTrue(avgPriceByCategory.get("Electronics") > 0, "Electronics avg > 0");
        assertTrue(avgPriceByCategory.get("Furniture") > 0, "Furniture avg > 0");
    }
    
    private static void test9_SalesStatisticsByCategory(SalesDataAnalyzer analyzer) {
        testHeader("Test 9: Sales Statistics by Category");
        Map<String, DoubleSummaryStatistics> stats = analyzer.getSalesStatisticsByCategory();
        
        assertEquals(2, stats.size(), "Should have 2 categories");
        DoubleSummaryStatistics electronicsStats = stats.get("Electronics");
        assertNotNull(electronicsStats, "Electronics statistics exist");
        assertTrue(electronicsStats.getCount() > 0, "Electronics has orders");
        assertTrue(electronicsStats.getMax() >= electronicsStats.getMin(), "Max >= Min");
    }
    
    private static void test10_TopProductsByRevenue(SalesDataAnalyzer analyzer) {
        testHeader("Test 10: Top Products by Revenue");
        List<Map.Entry<String, Double>> topProducts = analyzer.getTopProductsByRevenue(3);
        
        assertEquals(3, topProducts.size(), "Should return 3 products");
        assertEquals("Laptop", topProducts.get(0).getKey(), "Top product is Laptop");
        assertEquals(2400.0, topProducts.get(0).getValue(), 0.01, "Laptop revenue");
        
        // Verify descending order
        for (int i = 0; i < topProducts.size() - 1; i++) {
            assertTrue(topProducts.get(i).getValue() >= topProducts.get(i + 1).getValue(),
                "Results in descending order");
        }
    }
    
    private static void test11_TotalQuantityByProduct(SalesDataAnalyzer analyzer) {
        testHeader("Test 11: Total Quantity by Product");
        Map<String, Integer> quantityByProduct = analyzer.getTotalQuantityByProduct();
        
        assertTrue(quantityByProduct.containsKey("Laptop"), "Contains Laptop");
        assertEquals(2, quantityByProduct.get("Laptop").intValue(), "Laptop quantity");
        assertTrue(quantityByProduct.containsKey("Keyboard"), "Contains Keyboard");
        assertEquals(2, quantityByProduct.get("Keyboard").intValue(), "Keyboard quantity");
    }
    
    private static void test12_ProductsByCategory(SalesDataAnalyzer analyzer) {
        testHeader("Test 12: Products by Category");
        Map<String, Set<String>> productsByCategory = analyzer.getProductsByCategory();
        
        assertEquals(2, productsByCategory.size(), "Should have 2 categories");
        Set<String> electronics = productsByCategory.get("Electronics");
        assertTrue(electronics.contains("Laptop"), "Electronics contains Laptop");
        assertTrue(electronics.contains("Smartphone"), "Electronics contains Smartphone");
    }
    
    private static void test13_MonthlySalesTrend(SalesDataAnalyzer analyzer) {
        testHeader("Test 13: Monthly Sales Trend");
        Map<String, Double> monthlyTrend = analyzer.getMonthlySalesTrend();
        
        assertTrue(monthlyTrend.size() > 0, "Has monthly data");
        assertTrue(monthlyTrend.containsKey("2024-01"), "Contains Jan 2024");
        assertTrue(monthlyTrend.containsKey("2024-02"), "Contains Feb 2024");
        assertEquals(2000.0, monthlyTrend.get("2024-01"), 0.01, "January sales");
        assertTrue(monthlyTrend instanceof LinkedHashMap, "Is LinkedHashMap");
    }
    
    private static void test14_YearlySales(SalesDataAnalyzer analyzer) {
        testHeader("Test 14: Yearly Sales");
        Map<String, Double> yearlySales = analyzer.getYearlySales();
        
        assertTrue(yearlySales.containsKey("2024"), "Contains 2024");
        assertEquals(5075.0, yearlySales.get("2024"), 0.01, "2024 sales");
    }
    
    private static void test15_HighestValueOrder(SalesDataAnalyzer analyzer) {
        testHeader("Test 15: Highest Value Order");
        Optional<SalesDataAnalyzer.SalesRecord> highest = analyzer.getHighestValueOrder();
        
        assertTrue(highest.isPresent(), "Highest order exists");
        assertTrue(highest.get().getTotalSales() >= 1200.0, "Highest >= 1200");
    }
    
    private static void test16_OrdersAboveThreshold(SalesDataAnalyzer analyzer) {
        testHeader("Test 16: Orders Above Threshold");
        List<SalesDataAnalyzer.SalesRecord> orders = analyzer.getOrdersAboveThreshold(500.0);
        
        assertTrue(orders.size() > 0, "Found orders above $500");
        orders.forEach(order -> 
            assertTrue(order.getTotalSales() > 500.0, "Order meets threshold"));
        
        for (int i = 0; i < orders.size() - 1; i++) {
            assertTrue(orders.get(i).getTotalSales() >= orders.get(i + 1).getTotalSales(),
                "Sorted descending");
        }
    }
    
    private static void test17_PartitionOrdersByValue(SalesDataAnalyzer analyzer) {
        testHeader("Test 17: Partition Orders by Value");
        Map<Boolean, List<SalesDataAnalyzer.SalesRecord>> partition = 
            analyzer.partitionOrdersByValue(500.0);
        
        assertEquals(2, partition.size(), "Has two partitions");
        
        List<SalesDataAnalyzer.SalesRecord> high = partition.get(true);
        List<SalesDataAnalyzer.SalesRecord> low = partition.get(false);
        
        high.forEach(order -> 
            assertTrue(order.getTotalSales() >= 500.0, "High value >= 500"));
        low.forEach(order -> 
            assertTrue(order.getTotalSales() < 500.0, "Low value < 500"));
        
        assertEquals(10, high.size() + low.size(), "All orders partitioned");
    }
    
    private static void test18_DistinctProductCountByRegion(SalesDataAnalyzer analyzer) {
        testHeader("Test 18: Distinct Product Count by Region");
        Map<String, Long> distinctCount = analyzer.getDistinctProductCountByRegion();
        
        assertEquals(4, distinctCount.size(), "Has 4 regions");
        distinctCount.values().forEach(count -> 
            assertTrue(count > 0, "Count is positive"));
    }
    
    private static void test19_RevenuePercentageByRegion(SalesDataAnalyzer analyzer) {
        testHeader("Test 19: Revenue Percentage by Region");
        Map<String, Double> percentages = analyzer.getRevenuePercentageByRegion();
        
        assertEquals(4, percentages.size(), "Has 4 regions");
        double total = percentages.values().stream().mapToDouble(Double::doubleValue).sum();
        assertEquals(100.0, total, 0.01, "Percentages sum to 100%");
    }
    
    private static void test20_LambdaExpressions(SalesDataAnalyzer analyzer) {
        testHeader("Test 20: Lambda Expressions");
        List<SalesDataAnalyzer.SalesRecord> filtered = analyzer.getOrdersAboveThreshold(300.0);
        assertNotNull(filtered, "Lambda filter works");
        
        Map<String, Double> mapped = analyzer.getSalesByRegion();
        assertNotNull(mapped, "Lambda mapping works");
        
        double total = analyzer.calculateTotalRevenue();
        assertTrue(total > 0, "Lambda reduction works");
    }
    
    private static void test21_FunctionalProgramming(SalesDataAnalyzer analyzer) {
        testHeader("Test 21: Functional Programming Paradigm");
        double revenue1 = analyzer.calculateTotalRevenue();
        analyzer.getSalesByRegion();
        double revenue2 = analyzer.calculateTotalRevenue();
        
        assertEquals(revenue1, revenue2, 0.01, "Operations are repeatable");
    }
    
    private static void test22_EmptyResults(SalesDataAnalyzer analyzer) {
        testHeader("Test 22: Empty Results Handling");
        List<SalesDataAnalyzer.SalesRecord> empty = 
            analyzer.getOrdersAboveThreshold(1000000.0);
        
        assertNotNull(empty, "Returns list, not null");
        assertEquals(0, empty.size(), "List is empty");
    }
    
    private static void test23_OptionalHandling(SalesDataAnalyzer analyzer) {
        testHeader("Test 23: Optional Handling");
        Optional<SalesDataAnalyzer.SalesRecord> highest = analyzer.getHighestValueOrder();
        
        assertTrue(highest.isPresent(), "Optional is present");
        highest.ifPresent(record -> 
            assertTrue(record.getTotalSales() > 0, "Has positive value"));
    }
    
    private static void test24_CSVParsingErrors() throws IOException {
        testHeader("Test 24: CSV Parsing Error Handling");
        String badCsv = 
            "OrderID,Region,Product,Category,Price,Quantity,Date\n" +
            "ORD001,North,Laptop,Electronics,1200.00,1,2024-01-15\n" +
            "INVALID LINE WITH MISSING FIELDS\n" +
            "ORD002,South,Mouse,Electronics,25.00,2,2024-01-16\n";
        
        Files.write(Paths.get("bad_test.csv"), badCsv.getBytes());
        SalesDataAnalyzer badAnalyzer = new SalesDataAnalyzer("bad_test.csv");
        
        assertEquals(2, badAnalyzer.salesData.size(), "Loads only valid records");
        Files.deleteIfExists(Paths.get("bad_test.csv"));
    }
}
