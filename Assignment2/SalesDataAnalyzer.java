import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Sales Data Analyzer using Java Streams API
 * Performs comprehensive data aggregation and grouping operations on CSV sales data
 * Demonstrates functional programming paradigms and lambda expressions
 * 
 * Dataset Structure:
 * - OrderID: Unique order identifier (e.g., ORD001)
 * - Region: Geographic sales region (North, South, East, West)
 * - Product: Product name
 * - Category: Product category grouping
 * - Price: Unit price in USD
 * - Quantity: Number of units sold
 * - Date: Transaction date (YYYY-MM-DD format)
 * 
 * Assumptions:
 * - All prices are in USD
 * - Data represents sales transactions over a period
 * - Each order contains a single product type
 * - No returns, refunds, or cancellations
 * - Regions represent geographic territories
 * - Categories group related products
 */
public class SalesDataAnalyzer {
    
    /**
     * Sales record class representing each CSV row
     * Immutable data class following functional programming principles
     */
    public static class SalesRecord {
        private final String orderId;
        private final String region;
        private final String product;
        private final String category;
        private final double price;
        private final int quantity;
        private final LocalDate date;
        
        public SalesRecord(String orderId, String region, String product, String category, 
                          double price, int quantity, LocalDate date) {
            this.orderId = orderId;
            this.region = region;
            this.product = product;
            this.category = category;
            this.price = price;
            this.quantity = quantity;
            this.date = date;
        }
        
        // Getters
        public String getOrderId() { return orderId; }
        public String getRegion() { return region; }
        public String getProduct() { return product; }
        public String getCategory() { return category; }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
        public LocalDate getDate() { return date; }
        public double getTotalSales() { return price * quantity; }
        
        // Utility methods for date-based grouping
        public String getMonthYear() { 
            return date.format(DateTimeFormatter.ofPattern("yyyy-MM")); 
        }
        
        public String getYear() { 
            return String.valueOf(date.getYear()); 
        }
        
        @Override
        public String toString() {
            return String.format("Order[%s, %s, %s, $%.2f x %d = $%.2f, %s]", 
                orderId, region, product, price, quantity, getTotalSales(), date);
        }
    }
    
    // Package-private for testing
    List<SalesRecord> salesData;
    
    /**
     * Constructor - loads data from CSV file
     * @param csvFilePath Path to CSV file
     * @throws IOException if file cannot be read
     */
    public SalesDataAnalyzer(String csvFilePath) throws IOException {
        this.salesData = loadSalesData(csvFilePath);
    }
    
    /**
     * Load sales data from CSV file using Streams
     * Demonstrates: Stream operations, lambda expressions, method references
     */
    private List<SalesRecord> loadSalesData(String csvFilePath) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(csvFilePath))) {
            return lines.skip(1) // Skip header row
                .map(this::parseCsvLine)
                .filter(Objects::nonNull) // Filter out parsing errors
                .collect(Collectors.toList());
        }
    }
    
    /**
     * Parse a single CSV line into SalesRecord
     * Handles parsing errors gracefully
     */
    private SalesRecord parseCsvLine(String line) {
        try {
            String[] fields = line.split(",");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            
            return new SalesRecord(
                fields[0].trim(),
                fields[1].trim(),
                fields[2].trim(),
                fields[3].trim(),
                Double.parseDouble(fields[4].trim()),
                Integer.parseInt(fields[5].trim()),
                LocalDate.parse(fields[6].trim(), formatter)
            );
        } catch (DateTimeParseException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Warning: Could not parse line: " + line);
            return null;
        }
    }
    
    // ========== BASIC AGGREGATIONS ==========
    
    /**
     * 1. Calculate total revenue across all sales
     * Demonstrates: mapToDouble, sum
     */
    public double calculateTotalRevenue() {
        return salesData.stream()
            .mapToDouble(SalesRecord::getTotalSales)
            .sum();
    }
    
    /**
     * 2. Calculate average order value
     * Demonstrates: mapToDouble, average, orElse
     */
    public double calculateAverageOrderValue() {
        return salesData.stream()
            .mapToDouble(SalesRecord::getTotalSales)
            .average()
            .orElse(0.0);
    }
    
    // ========== REGIONAL ANALYSIS ==========
    
    /**
     * 3. Get total sales by region
     * Demonstrates: groupingBy, summingDouble
     */
    public Map<String, Double> getSalesByRegion() {
        return salesData.stream()
            .collect(Collectors.groupingBy(
                SalesRecord::getRegion,
                Collectors.summingDouble(SalesRecord::getTotalSales)
            ));
    }
    
    /**
     * 4. Get average sales by region
     * Demonstrates: groupingBy, averagingDouble
     */
    public Map<String, Double> getAverageSalesByRegion() {
        return salesData.stream()
            .collect(Collectors.groupingBy(
                SalesRecord::getRegion,
                Collectors.averagingDouble(SalesRecord::getTotalSales)
            ));
    }
    
    /**
     * 5. Get order count by region
     * Demonstrates: groupingBy, counting
     */
    public Map<String, Long> getOrderCountByRegion() {
        return salesData.stream()
            .collect(Collectors.groupingBy(
                SalesRecord::getRegion,
                Collectors.counting()
            ));
    }
    
    // ========== CATEGORY ANALYSIS ==========
    
    /**
     * 6. Get total sales by category
     * Demonstrates: groupingBy, summingDouble
     */
    public Map<String, Double> getSalesByCategory() {
        return salesData.stream()
            .collect(Collectors.groupingBy(
                SalesRecord::getCategory,
                Collectors.summingDouble(SalesRecord::getTotalSales)
            ));
    }
    
    /**
     * 7. Get average price by category
     * Demonstrates: groupingBy, averagingDouble
     */
    public Map<String, Double> getAveragePriceByCategory() {
        return salesData.stream()
            .collect(Collectors.groupingBy(
                SalesRecord::getCategory,
                Collectors.averagingDouble(SalesRecord::getPrice)
            ));
    }
    
    /**
     * 8. Get sales statistics by category
     * Demonstrates: groupingBy, summarizingDouble
     */
    public Map<String, DoubleSummaryStatistics> getSalesStatisticsByCategory() {
        return salesData.stream()
            .collect(Collectors.groupingBy(
                SalesRecord::getCategory,
                Collectors.summarizingDouble(SalesRecord::getTotalSales)
            ));
    }
    
    // ========== PRODUCT ANALYSIS ==========
    
    /**
     * 9. Get top N products by revenue
     * Demonstrates: groupingBy, sorted, limit
     */
    public List<Map.Entry<String, Double>> getTopProductsByRevenue(int n) {
        return salesData.stream()
            .collect(Collectors.groupingBy(
                SalesRecord::getProduct,
                Collectors.summingDouble(SalesRecord::getTotalSales)
            ))
            .entrySet()
            .stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(n)
            .collect(Collectors.toList());
    }
    
    /**
     * 10. Get total quantity sold by product
     * Demonstrates: groupingBy, summingInt
     */
    public Map<String, Integer> getTotalQuantityByProduct() {
        return salesData.stream()
            .collect(Collectors.groupingBy(
                SalesRecord::getProduct,
                Collectors.summingInt(SalesRecord::getQuantity)
            ));
    }
    
    /**
     * 11. Get products by category
     * Demonstrates: groupingBy, mapping, toSet
     */
    public Map<String, Set<String>> getProductsByCategory() {
        return salesData.stream()
            .collect(Collectors.groupingBy(
                SalesRecord::getCategory,
                Collectors.mapping(SalesRecord::getProduct, Collectors.toSet())
            ));
    }
    
    // ========== TEMPORAL ANALYSIS ==========
    
    /**
     * 12. Get monthly sales trend
     * Demonstrates: groupingBy with date formatting, sorted
     */
    public Map<String, Double> getMonthlySalesTrend() {
        return salesData.stream()
            .collect(Collectors.groupingBy(
                SalesRecord::getMonthYear,
                Collectors.summingDouble(SalesRecord::getTotalSales)
            ))
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
    }
    
    /**
     * 13. Get yearly sales
     * Demonstrates: groupingBy with year extraction
     */
    public Map<String, Double> getYearlySales() {
        return salesData.stream()
            .collect(Collectors.groupingBy(
                SalesRecord::getYear,
                Collectors.summingDouble(SalesRecord::getTotalSales)
            ));
    }
    
    // ========== ADVANCED ANALYSIS ==========
    
    /**
     * 14. Find highest value order
     * Demonstrates: max with comparator, Optional
     */
    public Optional<SalesRecord> getHighestValueOrder() {
        return salesData.stream()
            .max(Comparator.comparingDouble(SalesRecord::getTotalSales));
    }
    
    /**
     * 15. Get orders above threshold
     * Demonstrates: filter, sorted
     */
    public List<SalesRecord> getOrdersAboveThreshold(double threshold) {
        return salesData.stream()
            .filter(record -> record.getTotalSales() > threshold)
            .sorted(Comparator.comparingDouble(SalesRecord::getTotalSales).reversed())
            .collect(Collectors.toList());
    }
    
    /**
     * 16. Partition orders by value
     * Demonstrates: partitioningBy
     */
    public Map<Boolean, List<SalesRecord>> partitionOrdersByValue(double threshold) {
        return salesData.stream()
            .collect(Collectors.partitioningBy(
                record -> record.getTotalSales() >= threshold
            ));
    }
    
    /**
     * 17. Get distinct product count by region
     * Demonstrates: nested collectors
     */
    public Map<String, Long> getDistinctProductCountByRegion() {
        return salesData.stream()
            .collect(Collectors.groupingBy(
                SalesRecord::getRegion,
                Collectors.mapping(
                    SalesRecord::getProduct,
                    Collectors.collectingAndThen(Collectors.toSet(), set -> (long) set.size())
                )
            ));
    }
    
    /**
     * 18. Get revenue percentage by region
     * Demonstrates: complex calculation with streams
     */
    public Map<String, Double> getRevenuePercentageByRegion() {
        double totalRevenue = calculateTotalRevenue();
        return getSalesByRegion().entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> (entry.getValue() / totalRevenue) * 100
            ));
    }
    
    /**
     * Print all analysis results to console
     */
    /**
     * Print all analysis results to console
     */
    public void runAllAnalyses() {
        System.out.println("================================================================");
        System.out.println("           SALES DATA ANALYSIS RESULTS                          ");
        System.out.println("================================================================");
        System.out.println("Total records processed: " + salesData.size());
        
        // Basic Aggregations
        System.out.println("\n=== BASIC AGGREGATIONS ===");
        System.out.printf("1. Total Revenue: $%,.2f%n", calculateTotalRevenue());
        System.out.printf("2. Average Order Value: $%,.2f%n", calculateAverageOrderValue());
        
        // Regional Analysis
        System.out.println("\n=== REGIONAL ANALYSIS ===");
        System.out.println("3. Total Sales by Region:");
        getSalesByRegion().entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .forEach(e -> System.out.printf("   %-10s: $%,10.2f%n", e.getKey(), e.getValue()));
        
        System.out.println("\n4. Average Sales by Region:");
        getAverageSalesByRegion().entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .forEach(e -> System.out.printf("   %-10s: $%,10.2f%n", e.getKey(), e.getValue()));
        
        System.out.println("\n5. Order Count by Region:");
        getOrderCountByRegion().forEach((region, count) -> 
            System.out.printf("   %-10s: %d orders%n", region, count));
        
        // Category Analysis
        System.out.println("\n=== CATEGORY ANALYSIS ===");
        System.out.println("6. Total Sales by Category:");
        getSalesByCategory().entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .forEach(e -> System.out.printf("   %-15s: $%,10.2f%n", e.getKey(), e.getValue()));
        
        System.out.println("\n7. Average Price by Category:");
        getAveragePriceByCategory().forEach((cat, avg) -> 
            System.out.printf("   %-15s: $%8.2f%n", cat, avg));
        
        System.out.println("\n8. Sales Statistics by Category:");
        getSalesStatisticsByCategory().forEach((cat, stats) -> {
            System.out.printf("   %s:%n", cat);
            System.out.printf("      Count: %d, Min: $%.2f, Max: $%.2f, Avg: $%.2f, Total: $%.2f%n",
                stats.getCount(), stats.getMin(), stats.getMax(), stats.getAverage(), stats.getSum());
        });
        
        // Product Analysis
        System.out.println("\n=== PRODUCT ANALYSIS ===");
        System.out.println("9. Top 5 Products by Revenue:");
        getTopProductsByRevenue(5).forEach(e -> 
            System.out.printf("   %-15s: $%,10.2f%n", e.getKey(), e.getValue()));
        
        System.out.println("\n10. Total Quantity Sold by Product:");
        getTotalQuantityByProduct().entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(10)
            .forEach(e -> System.out.printf("   %-15s: %4d units%n", e.getKey(), e.getValue()));
        
        System.out.println("\n11. Products by Category:");
        getProductsByCategory().forEach((cat, prods) -> 
            System.out.printf("   %-15s: %s%n", cat, prods));
        
        // Temporal Analysis
        System.out.println("\n=== TEMPORAL ANALYSIS ===");
        System.out.println("12. Monthly Sales Trend:");
        getMonthlySalesTrend().forEach((month, total) -> 
            System.out.printf("   %-10s: $%,10.2f%n", month, total));
        
        System.out.println("\n13. Yearly Sales:");
        getYearlySales().forEach((year, total) -> 
            System.out.printf("   %-10s: $%,10.2f%n", year, total));
        
        // Advanced Analysis
        System.out.println("\n=== ADVANCED ANALYSIS ===");
        System.out.println("14. Highest Value Order:");
        getHighestValueOrder().ifPresent(record -> 
            System.out.printf("   %s%n", record));
        
        System.out.println("\n15. Orders Above $1000:");
        getOrdersAboveThreshold(1000.0).stream()
            .limit(10)
            .forEach(r -> System.out.printf("   %s%n", r));
        
        System.out.println("\n16. Revenue Percentage by Region:");
        getRevenuePercentageByRegion().entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .forEach(e -> System.out.printf("   %-10s: %6.2f%%%n", e.getKey(), e.getValue()));
        
        System.out.println("\n17. Distinct Product Count by Region:");
        getDistinctProductCountByRegion().forEach((region, count) -> 
        System.out.printf("   %-10s: %d products%n", region, count));
    
        System.out.println("\n18. Order Value Distribution (threshold=$500):");
        Map<Boolean, List<SalesRecord>> partition = partitionOrdersByValue(500.0);
        System.out.printf("   High Value (>=$500): %d orders%n", partition.get(true).size());
        System.out.printf("   Low Value (<$500):  %d orders%n", partition.get(false).size());
    
        System.out.println("\n================================================================");
        System.out.println("                   ANALYSIS COMPLETE                            ");
        System.out.println("================================================================");
    }
    
    /**
     * Main method to run the analysis
     */
    public static void main(String[] args) {
        try {
            String csvPath = "sales_data.csv";
            System.out.println("Loading sales data from: " + csvPath);
            SalesDataAnalyzer analyzer = new SalesDataAnalyzer(csvPath);
            System.out.println("Data loaded successfully: " + analyzer.salesData.size() + " records\n");
            
            analyzer.runAllAnalyses();
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            System.err.println("Please ensure 'sales_data.csv' exists in the current directory.");
            e.printStackTrace();
        }
    }
}
