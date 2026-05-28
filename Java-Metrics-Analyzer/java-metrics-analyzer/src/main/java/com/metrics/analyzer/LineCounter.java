package com.metrics.analyzer;

public class LineCounter {

    private int totalLines;
    private int codeLines;
    private int commentLines;
    private int blankLines;

    public LineCounter(String content) {
        analyze(content);
    }

    private void analyze(String content) {
        String[] lines = content.split("\n", -1);
        totalLines = lines.length;

        boolean inBlockComment = false;

        for (String line : lines) {
            String trimmed = line.trim();

            if (trimmed.isEmpty()) {
                blankLines++;
                continue;
            }

            if (inBlockComment) {
                commentLines++;
                if (trimmed.contains("*/")) {
                    inBlockComment = false;
                }
                continue;
            }

            if (trimmed.startsWith("//")) {
                commentLines++;
                continue;
            }

            if (trimmed.startsWith("/*")) {
                commentLines++;
                if (!trimmed.contains("*/") || !trimmed.endsWith("*/")) {
                    inBlockComment = true;
                }
                continue;
            }

            if (trimmed.startsWith("*") && inBlockComment) {
                commentLines++;
                if (trimmed.contains("*/")) {
                    inBlockComment = false;
                }
                continue;
            }

            codeLines++;
        }
    }

    public int getTotalLines() { return totalLines; }
    public int getCodeLines() { return codeLines; }
    public int getCommentLines() { return commentLines; }
    public int getBlankLines() { return blankLines; }
}