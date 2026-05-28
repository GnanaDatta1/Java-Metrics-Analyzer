package samples;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data processing service with various code patterns
 * for metrics analysis.
 */
public class DataProcessor {

    private final String source;

    public DataProcessor(String source) {
        this.source = source;
    }

    public List<String> filterResults(List<String> results, int maxResults) {
        return results.stream()
                .filter(r -> r != null)
                .filter(r -> !r.isEmpty())
                .limit(maxResults)
                .collect(Collectors.toList());
    }

    public Map<String, Integer> processBatch(Map<String, Integer> input, boolean sortResults) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }

        try {
            int result = performCalculation(input);
            return Map.of("result", result);
        } catch (Exception e) {
            System.err.println("Processing failed: " + e.getMessage());
            return Map.of("error", -1);
        }
    }

    private int performCalculation(Map<String, Integer> input) {
        int sum = 0;
        for (Map.Entry<String, Integer> entry : input.entrySet()) {
            switch (entry.getKey().toLowerCase()) {
                case "a":
                    sum += entry.getValue() * 1;
                    break;
                case "b":
                    sum += entry.getValue() * 2;
                    break;
                case "c":
                    sum += entry.getValue() * 3;
                    break;
                default:
                    sum += entry.getValue();
                    break;
            }
        }
        return sum;
    }

    public static String callCalculator() {
        Calculator calc = new Calculator();
        int total = 0;
        for (int i = 1; i <= 5; i++) {
            total += calc.factorial(i);
        }
        return "Total: " + total;
    }
}