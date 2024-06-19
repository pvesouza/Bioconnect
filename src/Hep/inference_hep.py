# Imports for inference module
from sklearn.preprocessing import StandardScaler
import matplotlib.pyplot as plt
from joblib import load
import numpy as np
import pandas as pd
from scipy.fft import fft
import os
import json

class Hep:
    
    # Loads The model and Json
    def __init__(self):
        #path_scaler = 'src\Hep\std_scaler.bin'
        #path_model = 'src\Hep\Models\logistic.pkl'
        path_model = 'C:\\Users\\pveso\\Documents\\heart_attack_analysis\\src\\Hep\\Models\\logistic.pkl'
        path_scaler = 'C:\\Users\\pveso\\Documents\\heart_attack_analysis\\src\\Hep\\std_scaler.bin'
        self.scaler = load(path_scaler)
        self.classifier = load(path_model)
        print("Hepatitis module Initialized")
        
    def getJsonFromFile(self, path):
        with open(path, 'r') as f:
            json_data = json.load(f)
            return json_data
        
    def calc_iqr(self, current):
        return np.percentile(current, 75, axis = 0) - np.percentile(current, 25, axis = 0)
        
    def scale_data(self, data):
        
        if (len(data) == 0):
            return None
        else:
              
           X = self.scaler.transform(data)
           return X
       
    def transformFourier(self, current, potential):
        # Calculando o tempo
        Ei = potential[0]
        Ef = potential[len(potential) - 1]
        Sr = 0.01      
        # Number of sample points
        N = len(potential)
        T = (Ef - Ei) / Sr
        t = np.linspace(0, T, N)
        dt = np.diff(t)[0]
        #print(f'Tempo total = {T} s')
        #print(f'Number of Samples = {N} s')
        #print(f'E inicial = {Ei} s')
        #print(f'E final = {Ef} s')
        #print(f'dt = {dt}')
        fft_signal = fft(current)
        return ((2.0/N) * np.abs(fft_signal))
    
    def calc_signal_energy(self,x):
        return (np.sum(np.abs(x)**2)/len(x))
        
    def getFeatures(self, current_fft, potential):
        
        if (len(current_fft) == 0 or len(potential) == 0):
            return None
        else:
            
            # Normalizando o sinal pela linha de base
            norm_current = (current_fft - np.min(current_fft, axis=0))
            # Caculando a área sob a curva
            area = np.trapz(norm_current, axis = 0)
            fft_signal = self.transformFourier(norm_current, potential)
            
            # Extração de features baseada no domínio do tempo
            mean = norm_current.mean(axis=0)
            std = norm_current.std(axis = 0)
            max_ = norm_current.max(axis = 0)
            energy = self.calc_signal_energy(norm_current)
            freq_pos_mean = fft_signal.mean(axis = 0)
            dc_ = fft_signal[0] 
            iqr = self.calc_iqr(norm_current)         
            return self.scale_data(np.array([mean, std, max_, energy, iqr, freq_pos_mean, area, dc_]).reshape(1, -1))
        
    def predict_data(self, current, potential):
        if (len(current) == 0):
            return None
        else:
            features = self.getFeatures(current, potential)
            result = float(self.classifier.predict(features))
            return result
                