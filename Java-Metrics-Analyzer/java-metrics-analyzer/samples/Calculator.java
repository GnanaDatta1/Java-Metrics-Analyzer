package samples;

/**
 * A simple calculator demonstrating various code patterns
 * for cyclomatic complexity analysis.
 */
public class Calculator {

    // Dead method - never called within the project
    private void unusedHelper() {
        System.out.println("This method is never called.");
    }

    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    /**
     * Computes factorial recursively.
     * Contains a loop and conditional, increasing complexity.
     */
    public int factorial(int n) {
        if (n <= 1) {
            return 1;
        }
        int result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    /**
     * Complex method with many decision points.
     * This method has high cyclomatic complexity.
     */
    public String classifyNumber(int x, int y, String mode) {
        if (mode.equals("sum")) {
            int sum = x + y;
            if (sum > 100) {
                return "Large Sum";
            } else if (sum > 50) {
                return "Medium Sum";
            } else {
                return "Small Sum";
            }
        } else if (mode.equals("product")) {
            int prod = x * y;
            if (prod > 500) {
                return "Large Product";
            } else if (prod > 100) {
                return "Medium Product";
            } else if (prod > 0) {
                return "Small Product";
            } else {
                return "Non-positive Product";
            }
        } else if (mode.equals("difference")) {
            int diff = x - y;
            return diff >= 0 ? "Positive Diff" : "Negative Diff";
        } else {
            return "Unknown mode";
        }
    }

    /**
     * Method with excessive parameters - code smell.
     */
    public String processData(String name, int age, String address, String phone, String email, boolean active) {
        if (active) {
            return name + " is active";
        }
        return name + " is inactive";
    }
}