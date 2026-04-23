package org.vedant.model;

public record MemoryMetrics(
        long usedRam,
        long totalRam,
        double ramPercent,
        long usedSwap,
        long totalSwap,
        double swapPercent
) {
    public static final MemoryMetrics EMPTY = new MemoryMetrics(
            0,
            0,
            0.0,
            0,
            0,
            0.0);
}