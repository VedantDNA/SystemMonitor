package org.vedant.model;

public record CpuMetrics(
        double totalUsage,
        double[] perCoreLoad,
        int threadCount,
        String architecture
) {
    public static final CpuMetrics EMPTY = new CpuMetrics(
            -1.0,
            new double[0],
            0,
            "Unknown"
    );
}
