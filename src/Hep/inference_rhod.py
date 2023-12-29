import numpy as np
import joblib
import tensorflow as tf
import os

class Rhod():
    
    def __init__(self, file):
        self.model = tf.keras.models.load_model(file)
        
    def normalize(self, reshaped_sample):
        # normalization min-max
        copy = reshaped_sample.copy()
        max_ = np.max(copy)
        min_ = np.min(copy)
        copy[:,0] = ((copy[:,0] - min_) / (max_ - min_))
        return copy
    
    def predict_sample(self, sample_data):
        normalized_data = self.normalize(sample_data)
        # Reshape normalized data
        normalized_data = normalized_data.reshape(1, -1, 1)
        result = self.model.predict(normalized_data)
        
        if result > 0.5:
            return 1
        else:
            return 0      