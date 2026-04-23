package org.vedant;

import org.vedant.service.CpuService;
import org.vedant.service.HardwareService;
import org.vedant.service.MemoryService;
import org.vedant.service.MonitoringOrchestrator;
import org.vedant.ui.SystemInfoDashboard;
import oshi.SystemInfo;

import javax.swing.*;
import java.time.Instant;

public class Main {
    public static void main (String[] args) throws InterruptedException {
        // 1. Initialize OSHI once
        SystemInfo si = new SystemInfo();

        // 2. Initialize Services
        HardwareService hardwareService = new HardwareService(si);
        CpuService cpuService = new CpuService(si);
        MemoryService memoryService = new MemoryService(si);

        // 3. Initialize Orchestrator
        MonitoringOrchestrator orchestrator = new MonitoringOrchestrator(
                hardwareService, cpuService, memoryService
        );

        // 4. Launch UI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new SystemInfoDashboard(orchestrator));
    }
}