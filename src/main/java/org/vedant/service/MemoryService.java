package org.vedant.service;

import org.vedant.model.MemoryMetrics;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.VirtualMemory;

public class MemoryService {

    private final GlobalMemory memory;

    public MemoryService(SystemInfo systemInfo) {
        this.memory = systemInfo.getHardware().getMemory();
    }

    public MemoryMetrics fetch() {
        try {
            long totalRam = memory.getTotal();
            long availableRam = memory.getAvailable();
            long usedRam = totalRam - availableRam;

            double ramPercent = Math.round(((double) usedRam / totalRam) * 10000.0) / 100.0;

            VirtualMemory vm = memory.getVirtualMemory();
            long usedSwap = vm.getSwapUsed();
            long totalSwap = vm.getSwapTotal();

            double swapPercent = (totalSwap > 0) ?
                    Math.round(((double) usedSwap / totalSwap) * 10000.0) / 100.0 : 0.0;

            return new MemoryMetrics(
                    usedRam,
                    totalRam,
                    ramPercent,
                    usedSwap,
                    totalSwap,
                    swapPercent
            );

        } catch (Exception e) {
            System.err.println("Memory Fetch Error: " + e.getMessage());
            return MemoryMetrics.EMPTY;
        }
    }
}