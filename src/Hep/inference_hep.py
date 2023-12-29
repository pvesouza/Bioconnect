# Imports for inference module
from sklearn.preprocessing import StandardScaler
import matplotlib.pyplot as plt
import joblib
import numpy as np
import pandas as pd
from scipy.fft import fft
import os
import json

class Hep:
    
    def __init__(self):
        self.scaler = joblib.load("C:\\Users\\pveso\\Documents\\heart_attack_analysis\\src\\Hep\\Models\\scaler.pkl")
        self.classifier = joblib.load("C:\\Users\\pveso\\Documents\\heart_attack_analysis\\src\\Hep\\Models\\hep_tree.pkl")
        
    def getJsonFromFile(self, path):
        with open(path, 'r') as f:
            json_data = json.load(f)
            return json_data
        
    def scale_data(self, data):
        
        if (len(data) == 0):
            return None
        else:
              
           X = self.scaler.transform(data)
           return X
       
    def transformFourier(self, current):
        if (len(current) == 0):
            return None
        else:
            N = current.shape[0]
            fft_signal = fft(current)
            return (2.0/N * np.abs(fft_signal[0:N//2]))
        
    def getFeatures(self, current_fft):
        
        if (len(current_fft) == 0):
            return None
        else:
            fft_signal = self.transformFourier(current_fft)
            df = pd.DataFrame(fft_signal)
            descriptive = df.describe()
            #print(descriptive)
            mean = float(descriptive.iloc[1])
            std = float(descriptive.iloc[2])
            min_ = float(descriptive.iloc[3])
            max_ = float(descriptive.iloc[7])
            percentile_25 = float(descriptive.iloc[4])
            percentile_50 = float(descriptive.iloc[5])
            percentile_75 = float(descriptive.iloc[6])
            #print("Features")
            #print([mean, std, min_, percentile_25, percentile_50, percentile_75, max_])
            #print(self.scale_data([mean, std, min_, percentile_25, percentile_50, percentile_75, max_]))
            return self.scale_data(np.array([mean, std, min_, percentile_25, percentile_50, percentile_75, max_]).reshape(1, -1))
        
    def predict_data(self, current):
        if (len(current) == 0):
            return None
        else:
            features = self.getFeatures(current)
            result = int(self.classifier.predict(features))
            #print(f"Result: {result}")
            return result
                