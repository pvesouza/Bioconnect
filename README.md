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
6. [Refences](#References)

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

ALHADRAMI, H. A. Biosensors: Classifications, medical applications, and future prospective. Biotechnology and Applied Biochemistry, v. 65, n. 3, p. 497–508, maio 2018.
ALI, J. et al. Biosensors: Their Fundamentals, Designs, Types and Most Recent Impactful Applications: A Review. Journal of Biosensors & Bioelectronics, v. 08, n. 01, 2017. Disponível em: <https://www.omicsonline.org/open-access/biosensors-their-fundamentals-designs-types-and-most-recent-impactful-applications-a-review-2155-6210-1000235.php?aid=85357>. Acesso em: 10 ago. 2024.
ALIYAR VELLAMEERAN, F.; BRINDHA, T. A new variant of deep belief network assisted with optimal feature selection for heart disease diagnosis using IoT wearable medical devices. Computer Methods in Biomechanics and Biomedical Engineering, v. 25, n. 4, p. 387–411, 12 mar. 2022.
ANTIPCHIK, M. et al. An electrochemical biosensor for direct detection of hepatitis C virus. Analytical Biochemistry, v. 624, p. 114196, jul. 2021.
ARYA, S. K.; DATTA, M.; MALHOTRA, B. D. Recent advances in cholesterol biosensor. Biosensors and Bioelectronics, v. 23, n. 7, p. 1083–1100, fev. 2008.
BAHADIR, E. B.; SEZGINTÜRK, M. K. A review on impedimetric biosensors. Artificial Cells, Nanomedicine, and Biotechnology, v. 44, n. 1, p. 248–262, 2 jan. 2016.
BARANWAL, J. et al. Electrochemical Sensors and Their Applications: A Review. Chemosensors, v. 10, n. 9, p. 363, 9 set. 2022.
BONNEY, G. E. Logistic Regression for Dependent Binary Observations. Biometrics, v. 43, n. 4, p. 951, dez. 1987.
BOONKAEW, S. et al. An automated fast-flow/delayed paper-based platform for the simultaneous electrochemical detection of hepatitis B virus and hepatitis C virus core antigen. Biosensors and Bioelectronics, v. 193, p. 113543, dez. 2021.
BREIMAN, L. [No title found]. Machine Learning, v. 45, n. 1, p. 5–32, 2001.
C A PADMANABHA REDDY, Y.; VISWANATH, P.; ESWARA REDDY, B. Semi-supervised learning: a brief review. International Journal of Engineering & Technology, v. 7, n. 1.8, p. 81, 9 fev. 2018.
CABRAL, D. G. A. et al. A label-free electrochemical immunosensor for hepatitis B based on hyaluronic acid–carbon nanotube hybrid film. Talanta, v. 148, p. 209–215, fev. 2016.
CAI, G. et al. A point-to-point “cap” strategy to construct a highly selective dual-function molecularly-imprinted sensor for the simultaneous detection of HAV and HBV. Biosensors and Bioelectronics, v. 219, p. 114794, jan. 2023.
CHAN, K. Y. et al. Deep neural networks in the cloud: Review, applications, challenges and research directions. Neurocomputing, v. 545, p. 126327, ago. 2023.
CHEN, H.; KÄTELHÖN, E.; COMPTON, R. G. Machine learning in fundamental electrochemistry: Recent advances and future opportunities. Current Opinion in Electrochemistry, v. 38, p. 101214, 1 abr. 2023.
CHEN, L.-Y. et al. Hepatitis D: challenges in the estimation of true prevalence and laboratory diagnosis. Gut Pathogens, v. 13, n. 1, p. 66, dez. 2021.
CHO, I.-H.; KIM, D. H.; PARK, S. Electrochemical biosensors: perspective on functional nanomaterials for on-site analysis. Biomaterials Research, v. 24, n. 1, p. 6, 4 fev. 2020.
CHOWDHURY, A. D. et al. Electrical pulse-induced electrochemical biosensor for hepatitis E virus detection. Nature Communications, v. 10, n. 1, p. 3737, 19 ago. 2019.
COSTA, M. A. Seleção e caracterização de biomarcadores por phage display e desenvolvimento de plataformas imunoenzimática e biofotônica para detecção da doença hepatite delta. 2023. Universidade Federal de Uberlândia, Minas Gerai, 2023. 
DAMBORSKÝ, P.; ŠVITEL, J.; KATRLÍK, J. Optical biosensors. Essays in Biochemistry, v. 60, n. 1, p. 91–100, 30 jun. 2016.
DE CASTRO, A. C. H. et al. Electrochemical Biosensor for Sensitive Detection of Hepatitis B in Human Plasma. Applied Biochemistry and Biotechnology, v. 194, n. 6, p. 2604–2619, jun. 2022.
DEAN, S. N. et al. Machine Learning Techniques for Chemical Identification Using Cyclic Square Wave Voltammetry. Sensors, v. 19, n. 10, p. 2392, jan. 2019.
ELSAYED, M.; EROL-KANTARCI, M. AI-Enabled Future Wireless Networks: Challenges, Opportunities, and Open Issues. IEEE Vehicular Technology Magazine, v. 14, n. 3, p. 70–77, set. 2019.
ETEZADI, D. et al. Nanoplasmonic mid-infrared biosensor for in vitro protein secondary structure detection. Light: Science & Applications, v. 6, n. 8, p. e17029–e17029, 27 fev. 2017.
FARCI, P. Diagnostic and Prognostic Significance of the IgM Antibody to the Hepatitis Delta Virus. JAMA: The Journal of the American Medical Association, v. 255, n. 11, p. 1443, 21 mar. 1986.
FATTOVICH, G. Influence of hepatitis delta virus infection on morbidity and mortality in compensated cirrhosis type B. Gut, v. 46, n. 3, p. 420–426, 1 mar. 2000.
GRIESHABER, D. et al. Electrochemical Biosensors - Sensor Principles and Architectures. Sensors, v. 8, n. 3, p. 1400–1458, 7 mar. 2008.
HAMMOND, J. L. et al. Electrochemical biosensors and nanobiosensors. Essays in Biochemistry, v. 60, n. 1, p. 69–80, 30 jun. 2016.
HAYASHI, T. et al. The global hepatitis delta virus (HDV) epidemic: what gaps to address in order to mount a public health response? Archives of Public Health, v. 79, n. 1, p. 180, 19 out. 2021.
HEARTY, J. Advanced Machine Learning with Python. Birmingham, UK: Sonail Vernekar, 2016. 
JAFFREZIC-RENAULT, N.; DZYADEVYCH, S. V. Conductometric Microbiosensors for Environmental Monitoring. Sensors, v. 8, n. 4, p. 2569–2588, 11 abr. 2008.
KOTSIANTIS, S. B. Decision trees: a recent overview. Artificial Intelligence Review, v. 39, n. 4, p. 261–283, abr. 2013.
KOTSIANTIS, S. B.; ZAHARAKIS, I. D.; PINTELAS, P. E. Machine learning: a review of classification and combining techniques. Artificial Intelligence Review, v. 26, n. 3, p. 159–190, nov. 2006.
KOZAN, M. Supervised and Unsupervised Learning (an Intuitive Approach). . [S.l: s.n.]. Disponível em: <https://medium.com/@metehankozan/supervised-and-unsupervised-learning-an-intuitive-approach-cd8f8f64b644>. Acesso em: 26 ago. 2024. , 2021
LAMMERS, F.; SCHEPER, T. Thermal Biosensors in Biotechnology. In: BHATIA, P. K. et al. (Org.). . Thermal Biosensors, Bioactivity, Bioaffinitty. Advances in Biochemical Engineering/Biotechnology. Berlin, Heidelberg: Springer Berlin Heidelberg, 1999. v. 64. p. 35–67. Disponível em: <https://link.springer.com/10.1007/3-540-49811-7_2>. Acesso em: 19 ago. 2024. 
LI, X. et al. A novel electrochemical immunosensor for hepatitis B surface antigen based on Fe3O4 nanoflowers and heterogeneous chain reaction signal amplification strategy. Talanta, v. 221, p. 121459, jan. 2021.
LU, H.; MA, X. Hybrid decision tree-based machine learning models for short-term water quality prediction. Chemosphere, v. 249, p. 126169, jun. 2020.
MAMMONE, A.; TURCHI, M.; CRISTIANINI, N. Support vector machines. WIREs Computational Statistics, v. 1, n. 3, p. 283–289, nov. 2009.
MANZANO, M. et al. Rapid and label-free electrochemical DNA biosensor for detecting hepatitis A virus. Biosensors and Bioelectronics, v. 100, p. 89–95, fev. 2018.
MOLINARA, M. et al. A Deep Learning Approach to Organic Pollutants Classification Using Voltammetry. Sensors, v. 22, n. 20, p. 8032, jan. 2022.
MURTAGH, F. Multilayer perceptrons for classification and regression. Neurocomputing, v. 2, n. 5–6, p. 183–197, jul. 1991.
NABAEI, V.; CHANDRAWATI, R.; HEIDARI, H. Magnetic biosensors: Modelling and simulation. Biosensors and Bioelectronics, v. 103, p. 69–86, abr. 2018.
NANDY, A.; BISWAS, M. Reinforcement Learning with Open AI, TensorFlow and Keras Using Python. India: Todd Green, 2018. 
PUSOMJIT, P. et al. Electrochemical immunoassay for detection of hepatitis C virus core antigen using electrode modified with Pt-decorated single-walled carbon nanotubes. Microchimica Acta, v. 189, n. 9, p. 339, set. 2022.
QUINLAN, J. R. Induction of decision trees. Machine Learning, v. 1, n. 1, p. 81–106, mar. 1986.
RAMANATHAN, K. The development and applications of thermal biosensors for bioprocess monitoring. Trends in Biotechnology, v. 17, n. 12, p. 499–505, 1 dez. 1999.
RAMANATHAN, KUMARAN; DANIELSSON, B. Principles and applications of thermal biosensors. Biosensors and Bioelectronics, v. 16, n. 6, p. 417–423, ago. 2001.
RIZZETTO, M et al. Immunofluorescence detection of new antigen-antibody system (delta/anti-delta) associated to hepatitis B virus in liver and in serum of HBsAg carriers. Gut, v. 18, n. 12, p. 997–1003, 1 dez. 1977.
RIZZETTO, MARIO. Hepatitis D Virus: Introduction and Epidemiology. Cold Spring Harbor Perspectives in Medicine, v. 5, n. 7, p. a021576, jul. 2015a.
RIZZETTO, MARIO. Hepatitis D Virus: Introduction and Epidemiology. Cold Spring Harbor Perspectives in Medicine, v. 5, n. 7, p. a021576, 1 jul. 2015b.
ROSINA, F. et al. Changing pattern of chronic hepatitis D in Southern Europe. Gastroenterology, v. 117, n. 1, p. 161–166, jul. 1999.
SAFAVIAN, S. R.; LANDGREBE, D. A survey of decision tree classifier methodology. IEEE Transactions on Systems, Man, and Cybernetics, v. 21, n. 3, p. 660–674, jun. 1991.
SAGNELLI, C. et al. HBV/HDV Co-Infection: Epidemiological and Clinical Changes, Recent Knowledge and Future Challenges. Life, v. 11, n. 2, p. 169, 22 fev. 2021.
SARKER, I. H. Machine Learning: Algorithms, Real-World Applications and Research Directions. SN Computer Science, v. 2, n. 3, p. 160, maio 2021.
SAUSEN, D. G. et al. Hepatitis B and Hepatitis D Viruses: A Comprehensive Update with an Immunological Focus. International Journal of Molecular Sciences, v. 23, n. 24, p. 15973, 15 dez. 2022.
SCARPONI, C. F. DE O. et al. Hepatitis Delta Prevalence in South America: A Systematic Review and Meta-Analysis. Revista da Sociedade Brasileira de Medicina Tropical, v. 52, p. e20180289, 24 jan. 2019.
SHAKOORI, Z. et al. Electrochemical DNA biosensor based on gold nanorods for detecting hepatitis B virus. Analytical and Bioanalytical Chemistry, v. 407, n. 2, p. 455–461, jan. 2015.
SHARIATI, M.; SADEGHI, M. Ultrasensitive DNA biosensor for hepatitis B virus detection based on tin-doped WO3/In2O3 heterojunction nanowire photoelectrode under laser amplification. Analytical and Bioanalytical Chemistry, v. 412, n. 22, p. 5367–5377, set. 2020.
STULP, F.; SIGAUD, O. Many regression algorithms, one unified model: A review. Neural Networks, v. 69, p. 60–79, set. 2015.
TEENGAM, P. et al. An innovative wireless electrochemical card sensor for field-deployable diagnostics of Hepatitis B surface antigen. Scientific Reports, v. 13, n. 1, p. 3523, 2 mar. 2023.
ULIANA, C. V. et al. Optimization of an amperometric biosensor for the detection of hepatitis C virus using fractional factorial designs. Journal of the Brazilian Chemical Society, v. 19, n. 4, p. 782–787, 2008.
WALKER, N. L. et al. Recent advances in potentiometric biosensing. Current Opinion in Electrochemistry, v. 28, p. 100735, ago. 2021.
WANG, JOSEPH. Analytical Electrochemistry. 2. ed. New York: John Wiley & Sons, Inc., 2001. 
WANG, Y. et al. Ultrasensitive electrochemical detection of hepatitis C virus core antigen using terminal deoxynucleotidyl transferase amplification coupled with DNA nanowires. Microchimica Acta, v. 188, n. 8, p. 285, ago. 2021.
WATT, J.; BORHANI, R.; KATSAGGELOS, A. Machine Learning Refined: Foundations, Algorithms, and Applications. 1. ed. [S.l.]: Cambridge University Press, 2016. Disponível em: <https://www.cambridge.org/core/product/identifier/9781316402276/type/book>. Acesso em: 26 ago. 2024. 
WHO. Hepatitis D. Disponível em: <https://www.who.int/news-room/fact-sheets/detail/hepatitis-d>. Acesso em: 23 ago. 2024. 
WRANKE, A. et al. Clinical long-term outcome of hepatitis D compared to hepatitis B monoinfection. Hepatology International, v. 17, n. 6, p. 1359–1367, dez. 2023.
ZHANG, T. et al. GCN-GENE: A novel method for prediction of coronary heart disease-related genes. Computers in Biology and Medicine, v. 150, p. 105918, nov. 2022.
ZHANG, Y. et al. Online Learning of Wearable Sensing for Human Activity Recognition. IEEE Internet of Things Journal, v. 9, n. 23, p. 24315–24327, 1 dez. 2022.
ZHANG, ZHENWEI; SEJDIĆ, E. Radiological images and machine learning: Trends, perspectives, and prospects. Computers in Biology and Medicine, v. 108, p. 354–370, maio 2019.
ZHANG, ZHONGHENG. Introduction to machine learning: k-nearest neighbors. Annals of Translational Medicine, v. 4, n. 11, p. 218–218, jun. 2016.
