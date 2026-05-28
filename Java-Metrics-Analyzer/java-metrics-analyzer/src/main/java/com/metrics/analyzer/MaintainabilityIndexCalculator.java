package com.metrics.analyzer;

import com.metrics.model.MethodMetrics;

public class MaintainabilityIndexCalculator {

    public double calculate(MethodMetrics mm) {
        double volume = mm.getHalsteadVolume() > 0 ? mm.getHalsteadVolume() : 1;
        double loc = mm.getLoc() > 0 ? mm.getLoc() : 1;

        double mi = 171 - 5.2 * Math.log(volume)
                - 0.23 * mm.getCyclomaticComplexity()
                - 16.2 * Math.log(loc);

        return Math.max(0, Math.min(100, mi));
    }

    public String getMiLabel(double mi) {
        if (mi >= 65) return "Good";
        if (mi >= 40) return "Moderate";
        return "Poor";
    }

    public String getMiColor(double mi) {
        if (mi >= 65) return "#28a745";
        if (mi >= 40) return "#ffc107";
        return "#dc3545";
    }
}