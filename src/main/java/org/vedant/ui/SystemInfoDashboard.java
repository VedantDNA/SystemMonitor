package org.vedant.ui;

import org.vedant.model.SystemSnapshot;
import org.vedant.service.MonitoringOrchestrator;
import oshi.software.os.OSProcess;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SystemInfoDashboard extends JFrame {

    private final MonitoringOrchestrator orchestrator;

    private JLabel lblModel, lblOS, lblSerial;
    private JProgressBar totalCpuBar;
    private List<JProgressBar> coreBars;
    private JProgressBar ramBar, swapBar;
    private JLabel lblRamInfo, lblSwapInfo;
    private DefaultTableModel model;
    private JTable table;

    public SystemInfoDashboard(MonitoringOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
        this.coreBars = new ArrayList<>();

        setupFrame();

        JPanel headerPanel = createHeaderPanel();
        JPanel cpuPanel = createCpuPanel(orchestrator.getStaticHardwareReport().physicalCores());
        JPanel memoryPanel = createMemoryPanel();
        JPanel processesPanel = createProcessesTable();

        JPanel topMetricsPanel = new JPanel(new BorderLayout());
        topMetricsPanel.add(headerPanel, BorderLayout.NORTH);
        topMetricsPanel.add(cpuPanel, BorderLayout.CENTER);
        topMetricsPanel.add(memoryPanel, BorderLayout.SOUTH);

        add(topMetricsPanel, BorderLayout.NORTH);
        add(processesPanel, BorderLayout.CENTER);

        startMonitoring();

        setVisible(true);
    }

    private void setupFrame() {
        setTitle("M4 System Monitor");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new GridLayout(1, 3, 10, 10));
        header.setBorder(BorderFactory.createTitledBorder("Hardware Identity"));

        var staticInfo = orchestrator.getStaticHardwareReport();
        lblModel = new JLabel("Model: " + staticInfo.model());
        lblOS = new JLabel("OS: " + staticInfo.osName());
        lblSerial = new JLabel("Serial: " + staticInfo.serialNumber());

        header.add(lblModel);
        header.add(lblOS);
        header.add(lblSerial);

        return header;
    }

    private JPanel createCpuPanel(int cores) {
        JPanel cpuMaster = new JPanel(new BorderLayout());
        cpuMaster.setBorder(BorderFactory.createTitledBorder("CPU Performance:"));

        totalCpuBar = new JProgressBar(0, 100);
        totalCpuBar.setStringPainted(true);
        cpuMaster.add(totalCpuBar, BorderLayout.NORTH);

        JPanel coresGrid = new JPanel(new GridLayout(1, cores, 5, 5));
        for (int i = 0; i < cores; i++) {
            JProgressBar bar = new JProgressBar(JProgressBar.VERTICAL, 0, 100);
            bar.setStringPainted(true);
            bar.setFont(new Font("Arial", Font.PLAIN, 10));
            coreBars.add(bar);
            coresGrid.add(bar);
        }
        cpuMaster.add(coresGrid, BorderLayout.CENTER);

        return cpuMaster;
    }

    private JPanel createMemoryPanel() {
        JPanel memPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        memPanel.setBorder(BorderFactory.createTitledBorder("Memory & Swap Management"));

        JPanel ramContainer = new JPanel(new BorderLayout());
        ramBar = new JProgressBar(0, 100);
        ramBar.setStringPainted(true);
        lblRamInfo = new JLabel("RAM: Loading...");
        ramContainer.add(ramBar, BorderLayout.CENTER);
        ramContainer.add(lblRamInfo, BorderLayout.SOUTH);

        JPanel swapContainer = new JPanel(new BorderLayout());
        swapBar = new JProgressBar(0, 100);
        swapBar.setStringPainted(true);
        lblSwapInfo = new JLabel("Swap: Loading...");
        swapContainer.add(swapBar, BorderLayout.CENTER);
        swapContainer.add(lblSwapInfo, BorderLayout.SOUTH);

        memPanel.add(ramContainer);
        memPanel.add(swapContainer);

        return memPanel;
    }

    private JPanel createProcessesTable() {
        JPanel processesPanel = new JPanel(new BorderLayout());
        processesPanel.setBorder(BorderFactory.createTitledBorder("Processes"));

        String[] columns = {"PID", "Name", "CPU %", "RAM (MB)", "Threads"};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setAutoCreateRowSorter(true);
        table.setFillsViewportHeight(true);

        processesPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        return processesPanel;
    }

    private void startMonitoring() {
        Timer timer = new Timer(1000, e -> {
            SystemSnapshot snapshot = orchestrator.takeSnapShot();
            updateUI(snapshot);
        });
        timer.start();
    }

    private void updateUI(SystemSnapshot snapshot) {
        totalCpuBar.setValue((int) snapshot.cpuMetrics().totalUsage());
        totalCpuBar.setString("Total Load: " + snapshot.cpuMetrics().totalUsage() + "%");

        double[] coreLoads = snapshot.cpuMetrics().perCoreLoad();
        for (int i = 0; i < coreLoads.length && i < coreBars.size(); i++) {
            coreBars.get(i).setValue((int) coreLoads[i]);
            coreBars.get(i).setString(String.valueOf((int) coreLoads[i]));
        }

        ramBar.setValue((int) snapshot.memoryMetrics().ramPercent());
        lblRamInfo.setText(String.format("Used: %.2f GB / Total: %.2f GB",
                snapshot.memoryMetrics().usedRam() / 1e+9, snapshot.memoryMetrics().totalRam() / 1e+9));

        swapBar.setValue((int) snapshot.memoryMetrics().swapPercent());
        lblSwapInfo.setText(String.format("Swap Used: %.2f GB", snapshot.memoryMetrics().usedSwap() / 1e+9));

        List<OSProcess> processes = snapshot.osProcesses();

        model.setRowCount(0);

        for (OSProcess p : processes) {
            model.addRow(new Object[]{
                    p.getProcessID(),
                    p.getName(),
                    String.format("%.1f", 100d * p.getProcessCpuLoadCumulative()),
                    p.getResidentSetSize() / (1024 * 1024),
                    p.getThreadCount()
            });
        }
    }
}