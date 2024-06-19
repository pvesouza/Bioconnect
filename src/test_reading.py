import json
import joblib
import Hep.inference_hep as inf
import os
import numpy as np


# Reading Json Data
json_base_path = 'data\JSON\Hep\\a'
list_jsonFiles = os.listdir(json_base_path)
inference_module = inf.Hep()
current_array = []
potential_array = []

for file in list_jsonFiles:
    total_path = os.path.join(json_base_path, file)
    f = open(total_path, 'r')
    data = json.load(f)
    readings = data['readings']
    
    for read in readings:
        read = json.loads(read)
        current = float(read['I'])
        potential = float(read['V'])
        potential_array.append(potential)
        current_array.append(current)
    
    print(total_path)  
    print(inference_module.predict_data(np.array(current_array, dtype=float), np.array(potential_array, dtype=float)))
    current_array.clear()
    potential_array.clear()
        
    
    
    