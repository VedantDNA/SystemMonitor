package org.vedant.ui;

import org.vedant.model.SystemSnapshot;
import org.vedant.service.MonitoringOrchestrator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SystemInfoDashboard extends JFrame {

    private final MonitoringOrchestrator orchestrator;

    // UI Components to be updated
    private JLabel lblModel, lblOS, lblSerial;
    private JProgressBar totalCpuBar;
    private List<JProgressBar> coreBars;
    private JProgressBar ramBar, swapBar;
    private JLabel lblRamInfo, lblSwapInfo;

    public SystemInfoDashboard(MonitoringOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
        this.coreBars = new ArrayList<>();

        setupFrame();
        initHeaderPanel();
        initCpuPanel();
        initMemoryPanel();

        // Start the Pulse
        startMonitoring();

        setVisible(true);
    }

    private void setupFrame() {
        setTitle("M4 System Monitor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
    }

    private void initHeaderPanel() {
        JPanel header = new JPanel(new GridLayout(1, 3, 10, 10));
        header.setBorder(BorderFactory.createTitledBorder("Hardware Identity"));

        var staticInfo = orchestrator.getStaticHardwareReport();
        lblModel = new JLabel("Model: " + staticInfo.model());
        lblOS = new JLabel("OS: " + staticInfo.osName());
        lblSerial = new JLabel("Serial: " + staticInfo.serialNumber());

        header.add(lblModel);
        header.add(lblOS);
        header.add(lblSerial);
        add(header, BorderLayout.NORTH);
    }

    private void initCpuPanel() {
        JPanel cpuMaster = new JPanel(new BorderLayout());
        cpuMaster.setBorder(BorderFactory.createTitledBorder("CPU Performance (10 Cores)"));

        // Total Usage Gauge
        totalCpuBar = new JProgressBar(0, 100);
        totalCpuBar.setStringPainted(true);
        cpuMaster.add(totalCpuBar, BorderLayout.NORTH);

        // Individual Cores
        JPanel coresGrid = new JPanel(new GridLayout(1, 10, 5, 5));
        for (int i = 0; i < 10; i++) {
            JProgressBar bar = new JProgressBar(JProgressBar.VERTICAL, 0, 100);
            bar.setStringPainted(true);
            bar.setFont(new Font("Arial", Font.PLAIN, 10));
            coreBars.add(bar);
            coresGrid.add(bar);
        }
        cpuMaster.add(coresGrid, BorderLayout.CENTER);
        add(cpuMaster, BorderLayout.CENTER);
    }

    private void initMemoryPanel() {
        JPanel memPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        memPanel.setBorder(BorderFactory.createTitledBorder("Memory & Swap Management"));

        // RAM Section
        JPanel ramContainer = new JPanel(new BorderLayout());
        ramBar = new JProgressBar(0, 100);
        ramBar.setStringPainted(true);
        lblRamInfo = new JLabel("RAM: Loading...");
        ramContainer.add(ramBar, BorderLayout.CENTER);
        ramContainer.add(lblRamInfo, BorderLayout.SOUTH);

        // Swap Section
        JPanel swapContainer = new JPanel(new BorderLayout());
        swapBar = new JProgressBar(0, 100);
        swapBar.setStringPainted(true);
        lblSwapInfo = new JLabel("Swap: Loading...");
        swapContainer.add(swapBar, BorderLayout.CENTER);
        swapContainer.add(lblSwapInfo, BorderLayout.SOUTH);

        memPanel.add(ramContainer);
        memPanel.add(swapContainer);
        add(memPanel, BorderLayout.SOUTH);
    }

    private void startMonitoring() {
        // The Swing Timer: 1000ms interval
        Timer timer = new Timer(1000, e -> {
            SystemSnapshot snapshot = orchestrator.takeSnapShot();
            updateUI(snapshot);
        });
        timer.start();
    }

    private void updateUI(SystemSnapshot snapshot) {
        // Update CPU
        totalCpuBar.setValue((int) snapshot.cpuMetrics().totalUsage());
        totalCpuBar.setString("Total Load: " + snapshot.cpuMetrics().totalUsage() + "%");

        double[] coreLoads = snapshot.cpuMetrics().perCoreLoad();
        for (int i = 0; i < coreLoads.length && i < coreBars.size(); i++) {
            coreBars.get(i).setValue((int) coreLoads[i]);
            coreBars.get(i).setString(String.valueOf((int) coreLoads[i]));

            // Color Logic: Red if core is hot
            if (coreLoads[i] > 80) coreBars.get(i).setForeground(Color.RED);
            else if (coreLoads[i] > 50) coreBars.get(i).setForeground(Color.ORANGE);
            else coreBars.get(i).setForeground(new Color(46, 204, 113)); // Emerald Green
        }

        // Update Memory
        ramBar.setValue((int) snapshot.memoryMetrics().ramPercent());
        lblRamInfo.setText(String.format("Used: %.2f GB / Total: %.2f GB",
                snapshot.memoryMetrics().usedRam() / 1e+9, snapshot.memoryMetrics().totalRam() / 1e+9));

        swapBar.setValue((int) snapshot.memoryMetrics().swapPercent());
        lblSwapInfo.setText(String.format("Swap Used: %.2f GB", snapshot.memoryMetrics().usedSwap() / 1e+9));
    }
}