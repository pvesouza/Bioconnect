
import torch.nn as nn
import numpy as np
from sklearn.metrics import mean_absolute_error, mean_squared_error, r2_score

class Metrics_Regression:


    def __init__(self, model, x_test, y_test):
        self.x = x_test
        self.y = y_test.detach().numpy()
        self.model = model
        if (isinstance(self.model, nn.Module)):
            self.model.eval()
            self.y_pred = self.model(self.x.float())


    def calc_mean_absolute_error(self):  
        return mean_absolute_error(self.y, self.y_pred.detach().numpy())
        
    def calc_mean_squared_error(self):
        return mean_squared_error(self.y, self.y_pred.detach().numpy())
    
    def calc_root_mean_squared_error(self):
        return np.sqrt(mean_squared_error(self.y, self.y_pred.detach().numpy()))
    
    def calc_r_squared(self):
        return r2_score(self.y, self.y_pred.detach().numpy())


