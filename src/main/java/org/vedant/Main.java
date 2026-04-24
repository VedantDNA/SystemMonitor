package org.vedant;

import org.vedant.service.CpuService;
import org.vedant.service.HardwareService;
import org.vedant.service.MemoryService;
import org.vedant.service.MonitoringOrchestrator;
import org.vedant.ui.SystemInfoDashboard;
import oshi.SystemInfo;

import javax.swing.*;

public class Main {
    public static void main (String[] args) throws InterruptedException {
        SystemInfo si = new SystemInfo();

        HardwareService hardwareService = new HardwareService(si);
        CpuService cpuService = new CpuService(si);
        MemoryService memoryService = new MemoryService(si);

        MonitoringOrchestrator orchestrator = new MonitoringOrchestrator(
                hardwareService, cpuService, memoryService
        );

        // included because the gui was sometimes breaking because of some thread problems and mull pointer exceptions:
        SwingUtilities.invokeLater(() -> new SystemInfoDashboard(orchestrator));
    }
}