# Predictive maintenance system for solar PV systems

## Project Overview
The Predictive Maintenance System for Solar PV Systems improves reliability and efficiency by addressing potential issues before they lead to failures. It ensures consistent, high-quality power output through predictive quality control and condition-based maintenance (CBM), reducing maintenance costs and operational disruptions. The system uses advanced IoT and machine learning, including CNN-based failure detection in PV strings, to enhance fault prediction accuracy. Root cause analysis enables precise identification and resolution of issues, preventing recurring faults and improving overall system reliability. By optimizing performance and extending the lifespan of components, this solution ensures sustainable and cost-effective solar energy operations.

## Project ID
24-25J-245

## Team Members
- Navindi R.L.S (IT21166174)
- Nayanamudu R.M.H (IT21199080)
- Perera U.V.S.A (IT21164958)
- Perera M.A.V.D (IT21322662)

### Supervisors
- Ms. Sanjeevi Chandrasiri (Supervisor)
- Mr. Nelum Amarasena (Co-Supervisor)
- Mr. Udara Dissanayake (External-Supervisor)

## System Overall Diagram

![System Diagram](diagram.png)

### Key Modules and Features

#### 1. Predictive Quality Control for Power Output
- Description:  
  This function analyzes historical and real-time data from solar PV panels to identify factors affecting power quality and output. By integrating advanced data acquisition systems and leveraging machine learning, it forecasts potential quality issues and predicts whether power output meets standard levels. Preventive actions are triggered to control breakdowns, enhance panel efficiency, and ensure reliability. The system supports proactive maintenance to maintain consistent power output, reduce faults, and optimize panel durability and performance.

- Functionality:  
  - Real-Time Data Collection  
  - Historical Data Integration  
  - Data Filtering and Preprocessing  

- Benefits:  
  - Improved Power Output Consistency  
  - Enhanced Panel Durability and Reliability  

---

#### 2. Root Cause Analysis for Fault Detection and Prediction
- Description:  
  This system leverages real-time IoT data and historical maintenance records to predict and analyze faults in solar PV panels. It integrates environmental and operational data, using machine learning models to enhance fault detection accuracy while considering varying conditions. Advanced preprocessing ensures scalable, reliable data storage and retrieval. AI models with improved explainability offer transparent predictions, identifying root causes via anomaly detection and integrated data insights. A customizable, user-friendly interface displays real-time visualizations, analysis results, and automated reports. Continuous learning mechanisms refine fault predictions and analysis over time, enabling proactive, targeted maintenance to optimize panel performance and reduce operational downtime.

- Functionality:  
  - Real-time data collection from IoT sensors with seamless protocol integration.  
  - Scalable database for efficient data preprocessing, storage, and retrieval.  
  - Machine learning-based fault detection and root cause analysis using real-time and historical data.  
  - User-friendly interface with real-time data visualizations, reports, and maintenance recommendations.  

- Benefits:  
  - Enhanced Fault Detection and Root Cause Analysis  
  - Transparent Insights and Proactive Maintenance  

---

#### 3. Condition-Based Maintenance (CBM) Implementation
- Description:  
  This project involves implementing a Condition-Based Maintenance (CBM) strategy for solar PV panels. It includes real-time monitoring of panel conditions such as voltage, current, and environmental factors, with historical data used to develop machine learning models for predicting the Remaining Useful Life (RUL). A dynamic maintenance scheduling system ensures maintenance is triggered based on RUL predictions. Continuous model updates and performance evaluations optimize the strategy for cost reduction and improved panel lifespan.

- Functionality:  
  - Real-Time Data Monitoring and Collection  
  - Predictive Analytics and Remaining Useful Life (RUL) Estimation  
  - Dynamic Maintenance Scheduling  

- Benefits:  
  - Cost Efficiency: Reduces unnecessary maintenance activities, minimizing labor and resource expenses.  
  - Enhanced Panel Lifespan: Addresses issues proactively, preventing severe degradation and extending the operational life of panels.  

---

#### 4. Failures (IR) Detection in PV Strings Using Convolutional Neural Network (CNN)
- Description:  
  This functionality employs a CNN-based model integrated with normal and infrared camera data captured by a Raspberry Pi fixed on PV panels. The system transmits data via Wi-Fi to a server, where machine learning algorithms analyze it to detect faults like overheating or micro-cracks. The model provides interpretable, actionable insights, offering precise fault detection and early interventions. By leveraging diverse datasets and IR imagery, it ensures enhanced efficiency, durability, and optimized performance of PV systems.

- Functionality:  
  - Fault Detection Using Multi-Sensor Data Integration  
  - Real-Time Fault Analysis with Actionable Insights  
  - Data Transmission and Remote Monitoring  
  - Model Accuracy Optimization with Large & Diverse Datasets  

- Benefits:  
  - Early Fault Detection and Prevention  
  - Enhanced Efficiency and Longevity  

## Technologies Used
### Core Technologies
Mongo DB
VS code
Jupyter Notebook
Anaconda environment
IOT
Arduino
Flask
Flutter

### Hardware Components
- ESP32 Wi-Fi Module
- Raspberry Pi
- Irradiance Sensors
- Temperature Sensor
- Humidity Sensor
- Invertor
- Battery
- Charge Controller
- Solar Panel
- IoT devices for data collection

### Techniques
- Context-aware weighting 
- Reliability Verification
- Cross Validation
- IR-CNN Fusion for Fault Detection
- Time Series Analysis
- Normalization

## System Requirements
### Software Requirements
- Jupyter Notebook
- Python 3.x
- Flask
- Firebase account
- Mongo DB
- Weather API access

### Hardware Requirements
- Infrared (IR) and Visible Cameras
- Wi-Fi Modules
- IoT devices and sensors
- Wiring components
- Battery and related parts
- Mobile device for app testing

## Future Work
- Integration of IoT devices for real-time data collection
- Mobile application development
- System deployment and testing

GitHub Link - https://github.com/HarendraRathnayaka/Predictive-maintenance-system-for-solar-systems
