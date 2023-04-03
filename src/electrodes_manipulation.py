import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import scipy as sp
from scipy import signal
import scipy.stats as stats
from findpeaks import findpeaks

class Eletrode:

    def convert_df_to_float_df(self, df):
        out_df = pd.DataFrame()
        for col in df.columns:
            vals = np.zeros(shape=df[col].shape)
            for i, value in enumerate(df[col]):
                if type(value) == float:
                    vals[i] = value
                else:
                    vals[i] = float(value.replace(',', '.'))
            out_df[col] = vals
        return out_df
    
    # Função que retorma o vetor do tempo dado o potencial
    def getTime(self, scan_rate, potential):
        time = np.zeros(shape=potential.shape)
        delta_t = (potential[1] - potential[0]) / scan_rate
    
        for i in range(1, len(potential)):
            time[i] = delta_t + time[i - 1]
    
        return time
    
    # Função que retorna o tempo total da série de voltametria
    def get_total_time(self, scan_rate, potential):
        time = self.getTime(scan_rate, potential)
        return time[len(time)-1] 
    
    # Função de Filtragem do sinal
    def filter_low_pass_butterworth(self, fs, fc, order, y):
        # Frequência de corte normalizada
        w = fc / (fs / 2)
        print(f"Critical frequency {w}")
        b, a = signal.butter(order, w, 'low')
        out = signal.lfilter(b, a, y)
        #print(f'Shape Saida: {len(out)}')
        return out
    
    # Função que calcula o sample rate do processo
    def get_sampleRate(self, totalTime, lenght):
        return lenght / totalTime
    

    
    # Função que calcula a primeira derivada
    def derivate(self, x_axis, y_axis, time = False):
    
        #print(f'X Shape {len(x_axis)}')
        #print(f'Y Shape {len(y_axis)}')
        sample_rate = 0
        h = abs(x_axis[1] - x_axis[0])          # Calculating h difference
        # Padding to not losing data between derivate
        y = np.zeros(shape = (len(y_axis) + 2))
        # Coping the y array into y
        y[1:-1] = y_axis
        # Duplicating the last and the first elements
        y[0] = y_axis[0]
        y[len(y) - 1] = y_axis[len(y_axis) - 1]
    
        for k in range(len(y) - 1):
            y[k] = (y[k + 1] - y[k])/h
    
        # Filtragem do sinal da derivada
        if (not(time)):
            total_time = self.get_total_time(0.75, x_axis)
            sample_rate = self.get_sampleRate(total_time, len(x_axis))
        else:
            sample_rate = 1 / h
        
        out = self.filter_low_pass_butterworth(sample_rate, 5, 10, y[1:-1])
        #print(len(out))
        return (y[1:-1], out)
    
    # Retorna a FFT e as frequências 
    def getFFTAndFrequencies(self, current, sample_period):
        fft = np.fft.fft(current)
        freqs = np.fft.fftfreq(len(current), sample_period)
        abs_fft = abs(fft)
        return (abs_fft, freqs)
    
    # Mostra estatísticas de um vetor de medidas
    def showStatistics(self, array):
        print(f"Mean: {np.mean(array)}")
        print(f"Median: {np.median(array)}")
        print(f"Std: {np.std(array)}")
        print(f"Maximum: {np.max(array)}")
        print(f"Minumum: {np.min(array)}")

