# Java Metrics Analyzer

A static analysis tool for Java codebases that computes code quality metrics, detects dead code, and generates a rich HTML report — all from the command line.

---

## Features

- **Cyclomatic Complexity (CC)** — measures decision-point density per method
- **Maintainability Index (MI)** — composite score (0–100) indicating how easy the code is to maintain
- **Lines of Code (LOC)** — per file and project-wide totals
- **Dead Method Detection** — identifies methods defined but never called within the scanned codebase
- **Code Smell Detection** — flags issues such as methods with too many parameters
- **HTML Report** — self-contained report with color-coded complexity badges and per-method breakdowns
- Built with **JavaParser** for accurate AST-level analysis

---

## Sample Output

```
Analyzing Java files in: F:\java-metrics-analyzer\samples
Report generated:        F:\java-metrics-analyzer\report.html
Analysis complete!
  Files analyzed:          2
  Total LOC:               113
  Avg Complexity:          3.00
  Avg Maintainability Index: 96.49
  Dead methods found:      8
```


### Output Screen Shorts

```
![image alt](https://github.com/GnanaDatta1/Java-Metrics-Analyzer/blob/65bd25dcb36dd2ea67daacd03b6b0e23532b6c6b/Java-Metrics-Analyzer/java-metrics-analyzer/ScreenShorts/Screenshot%202026-05-28%20142751.png)

### Report Highlights

| Metric | Value |
|---|---|
| Total LOC | 113 |
| Avg Complexity | 3.0 (Low) |
| Avg Maintainability | 96 (Good) |
| Complexity Distribution | 8 Low / 2 Med / 0 High |

---

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6+

### Build

```bash
mvn clean package
```

This produces `target/java-metrics-analyzer-1.0.0.jar`.

### Run

```bash
java -jar target/java-metrics-analyzer-1.0.0.jar <source-dir> <output-report.html>
```

**Example:**

```bash
java -jar "F:\java-metrics-analyzer\target\java-metrics-analyzer-1.0.0.jar" \
     "F:\java-metrics-analyzer\samples" \
     "F:\java-metrics-analyzer\report.html" 2>&1
```

---

## Metrics Reference

### Cyclomatic Complexity (CC)

Counts independent paths through a method. Each `if`, `else if`, `for`, `while`, `case`, `catch`, and ternary `?:` adds 1.

| Score | Rating |
|---|---|
| 1–5 | Low (green) |
| 6–10 | Medium (yellow) |
| 11+ | High (red) |

### Maintainability Index (MI)

Derived from Halstead volume, cyclomatic complexity, and LOC. Higher is better.

| Score | Rating |
|---|---|
| 85–100 | Good |
| 65–84 | Moderate |
| 0–64 | Low |

### Dead Methods

Methods that are defined but have no call sites anywhere in the scanned directory. Shown with strikethrough styling in the report.

### Code Smells

| Smell | Condition |
|---|---|
| Too many parameters | Method has more than 5 parameters |

---

## Project Structure

```
java-metrics-analyzer/
├── src/
│   └── main/java/
│       └── ...          # Analyzer source code
├── samples/
│   ├── Calculator.java  # Sample with high-complexity method
│   └── DataProcessor.java
├── pom.xml
└── report.html          # Generated after running the tool
```

---

## Sample Files

The `samples/` directory contains two demo classes used for testing:

- **`Calculator.java`** — demonstrates varying complexity levels (`add`, `subtract` at CC 1; `classifyNumber` at CC 10) and a code smell (6-parameter `processData`)
- **`DataProcessor.java`** — demonstrates stream-based filtering, exception handling, and a switch-based calculation (`performCalculation` at CC 6)

---

## Built With

- [JavaParser](https://javaparser.org/) — Java AST parsing library
- Maven — build and dependency management

---

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.
