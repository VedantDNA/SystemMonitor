package org.vedant.model;

public record HardwareInfo(
        String manufacturer,    // e.g., "Apple Inc."
        String model,           // e.g., "Mac16,10"
        String serialNumber,    // e.g., "VM4-XXXX"
        String cpuName,         // e.g., "Apple M4"
        int physicalCores,      // 10
        int logicalCores,       // 10
        String microArch,       // "Firestorm + Icestorm"
        long maxFrequencyHz,    // Clock speed for scaling graphs
        long totalRamBytes,     // Raw bytes for precise math
        String osName,          // e.g., "macOS Sequoia"
        int bitness             // 64
) {

    public static final HardwareInfo EMPTY = new HardwareInfo(
            "Unknown",
            "Unknown",
            "N/A",
            "Unknown CPU",
            0,
            0,
            "Unknown",
            0L,
            0L,
            "Unknown OS",
            64
    ) ;

    @Override
    public String toString() {
        return "HardwareInfo{" +
                "manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", cpuName='" + cpuName + '\'' +
                ", physicalCores=" + physicalCores +
                ", logicalCores=" + logicalCores +
                ", microArch='" + microArch + '\'' +
                ", maxFrequencyHz=" + maxFrequencyHz +
                ", totalRamBytes=" + totalRamBytes +
                ", osName='" + osName + '\'' +
                ", bitness=" + bitness +
                '}';
    }
}
