# Bioconnect Electrochemical Platform

## Overview

This repository contains all the files related to the **Bioconnect Electrochemical Platform**, which was developed as part of a PhD thesis to create a fully integrated, low-cost, portable potentiostat capable of analyzing biofluids and performing electrochemical analyses. The platform supports **USB**, **Bluetooth**, and **Wi-Fi** communication and integrates **machine learning algorithms** through the **AWS cloud**.

The main goal of the platform is to provide real-time electrochemical analysis and support AI-based classification for applications such as **Hepatitis Delta Virus** (HDV) detection.

## Table of Contents

1. [Desktop Application](#desktop-application)
2. [Mobile Application](#mobile-application)
3. [PCB and Schematic](#pcb-and-schematic)
4. [Firmware](#firmware)
5. [Results](#results)

---

## Desktop Application

### Description

The desktop application was developed using **C++** with **Qt (v5.6)**. It serves as the main interface for electrochemical data acquisition, signal processing, and machine learning tasks. It allows the operator to:

- Acquire electrochemical signals in real-time.
- Store and label data in **JSON**, **CSV**, and **PostgreSQL** databases.
- Perform signal processing, feature extraction, and feature engineering.

### Dependencies

- **Qt v5.6**
- **PostgreSQL v16.3**

### Key Features

- Real-time electrochemical analysis.
- Data storage in multiple formats (JSON, CSV, PostgreSQL).
- Machine learning integration for classification of electrochemical signals.

## Mobile Application

### Description

The mobile application, developed for **Android API 33**, provides an end-user interface for interacting with the biosensor platform. It communicates with the **hardware** via **Bluetooth 2.1** and uploads data to an **AWS cloud** via **Wi-Fi**.

### Key Features

- Real-time data visualization on Android devices.
- Synchronization of data with the cloud in case of offline mode.
- User-friendly interface for electrochemical analysis.

### Target Device

- **Samsung Galaxy Tab 6**

## PCB and Schematic

The hardware consists of a **PCB** designed to perform multiple types of electrochemical analysis including **cyclic voltammetry**, **pulse differential voltammetry**, **amperometry**, and **impedance spectroscopy**. The PCB is powered by an **1800 mAh Li-Ion battery**, which can last for approximately 13 hours of operation.

### Hardware Components

- **EMSTAT Pico OEM chip**: Potentiostat core for electrochemical measurements.
- **Raspberry Pico W**: Communication controller with support for Wi-Fi and Bluetooth.
- **TP4056 Module**: Battery charging and management circuit.
  
### Power Consumption

- Total power consumption: **137 mA**
- Operating time: **13 hours**

## Firmware

The firmware for the hardware platform is responsible for handling communication between the sensors and external devices. It supports the following communication protocols:

- **Bluetooth 2.1**
- **Wi-Fi**
- **USB**

It also manages the data acquisition process from the biosensors and ensures that data is sent to the cloud for further processing via the **AWS API**.

### Key Firmware Features

- Real-time signal acquisition and transmission.
- Seamless communication with mobile apps and cloud services.
- Power management for extended operation time.

## Results

### Electrochemical Signal Analysis

The platform was validated using **Cyclic Voltammetry (CV)** in a potassium ferri/ferrocyanide solution with both modified and non-modified electrodes. The results demonstrated smaller **standard deviations** in both anodic and cathodic current measurements when compared to the **Emstat3+ Blue** potentiostat, indicating greater precision.

### HDV Detection

The Bioconnect platform achieved an accuracy of **87.5%** using logistic regression for HDV detection and **92%** using a machine learning algorithm hosted on **AWS**. The integration with machine learning further enhances the platform’s diagnostic capabilities.

### Performance Comparison

The Bioconnect platform was compared against other state-of-the-art systems, including **NanoStat**, **TBISTAT**, and **ACEstat**, and showed competitive or superior performance in terms of flexibility, connectivity, and precision.

---

## Repository Structure

- `/src`: Contains the source code for the desktop and mobile applications.
- `/firmware`: Contains the firmware for the hardware platform.
- `/hardware`: Contains the PCB designs and schematics.
- `/docs`: Contains additional documentation and research results.
  
---

## License

This project is licensed under the MIT License. See the `LICENSE` file for more details.

---

## References

For more detailed information, refer to the **Bioconnect PhD Thesis** and related publications.