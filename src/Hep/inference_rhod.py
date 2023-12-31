import numpy as np
import tensorflow as tf

class Rhod():
    
    def __init__(self, file):
        self.model = tf.keras.models.load_model(file)
        
    def normalize(self, reshaped_sample):
        #print("Normalization")
        # normalization min-max
        if (len (reshaped_sample) == 0):
            return None
        copy = reshaped_sample.copy()
        max_ = np.max(copy)
        min_ = np.min(copy)
        print(max_, min_)
        copy[:,0] = ((copy[:,0] - min_) / (max_ - min_))
        #print(np.max( copy[:,0]), np.min( copy[:,0]))
        return copy
    
    def predict_sample(self, sample_data):
        
        #print("Predict")
        if (len(sample_data) == 0):
            return 0
        
        normalized_data = self.normalize(sample_data)
        #print("Pos Normal")
        
        if (normalized_data.shape[0] > 0):
            # Reshape normalized data
            #print(normalized_data.shape)
            normalized_data = normalized_data.reshape(1, -1, 1)
            result = self.model.predict(normalized_data)
            
            if result > 0.5:
                return 1
            else:
                return 0
        else:
            print("Erro na normalização")
        return 0