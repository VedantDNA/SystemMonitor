# Resource Monitor

A real-time system resource monitoring application built with Java, featuring a graphical dashboard to track CPU, Memory, and Process information.

## Overview
Resource Monitor (specifically labeled as "M4 System Monitor" in the UI) provides a comprehensive view of your system's hardware identity and performance metrics. It utilizes the OSHI library to fetch cross-platform system information.

### Key Features
- **Hardware Identity**: Displays system model, OS name, and serial number.
- **CPU Monitoring**: Visualizes total CPU load and per-core utilization using progress bars.
- **Memory Management**: Tracks RAM and Swap usage in real-time.
- **Process List**: Lists active processes with PID, Name, CPU usage, RAM (MB), and Thread count.

## Stack
- **Language**: Java 17
- **Frameworks/Libraries**:
    - **Swing**: For the graphical user interface.
    - **OSHI (6.11.1)**: For system and hardware information.
- **Package Manager**: Maven

## Requirements
- **JDK**: Version 17 or higher.
- **Maven**: To build and manage dependencies.

## Setup & Run

### 1. Clone the repository
```bash
git clone <repository-url>
cd resource_monitor
```

### 2. Build the project
```bash
mvn clean install
```

### 3. Run the application
You can run the application using the following command:
```bash
mvn exec:java -Dexec.mainClass="org.vedant.Main"
```

## Scripts & Commands
- `mvn clean compile`: Compiles the source code.
- `mvn package`: Packages the application into a JAR file in the `target/` directory.
- `mvn exec:java -Dexec.mainClass="org.vedant.Main"`: Starts the monitoring dashboard.

## Project Structure
```text
src/main/java/org/vedant/
├── Main.java                # Application entry point
├── model/                   # Data models (CpuMetrics, MemoryMetrics, etc.)
├── service/                 # Logic for fetching system info (CpuService, MemoryService, etc.)
└── ui/                      # Swing-based dashboard (SystemInfoDashboard)
```

## Environment Variables
- No specific environment variables are currently required for this project.

## Tests
- **TODO**: Automated tests are currently not implemented. (The `src/test/java` directory is present but empty).

