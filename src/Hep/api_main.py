from flask import Flask, make_response, jsonify, request
from inference_hep import Hep
from inference_rhod import Rhod
from scipy.signal  import resample
import numpy as np

app = Flask("hepatitis")
hep_analysis = Hep()

#Test Api
@app.route('/', methods=['GET'])
def test_api():
    return make_response(jsonify("Hepatitis test"))

@app.route("/hep", methods=['POST'])
def analyse_hep():
    
    try:
        json = request.json
        #print(json)
        json_readings = json["readings"]
        potential = []
        current = []
        
        for i in range(len(json_readings)):
            sample = json_readings[i]
            v = float(sample["V"])
            c = float(sample["I"])
            potential.append(v)
            current.append(c)
            #print(v,c)
        
        del potential[0:11]
        del current[0:11]
               
        result = hep_analysis.predict_data(np.array(current, dtype=float), np.array(potential, dtype=float))
        
        if (result != None):
            return make_response(jsonify(result), 200)
        else:
            print("Error!!!")
            return make_response(jsonify("Error"), 400)
    except:
        return make_response(jsonify("Error"), 500)

@app.route("/rhod", methods=['POST'])
def analyse_rhod():
    try:
        
        json = request.json
        json_readings = json["readings"]
        #print(len(json_readings))
        potential = []
        current = []
        inference_module = Rhod("C:\\Users\\pveso\\Documents\\heart_attack_analysis\\src\\Hep\\Models\\Rhod\\cnn_rod.h5")
        
        for i in range(len(json_readings)):
            sample = json_readings[i]
            v = float(sample["V"])
            c = float(sample["I"]) 
            potential.append(v)
            current.append(c)
            #print(v,c)
        
        # 1115 is the shape that cnn was trained
        if (len(current) < 1115):
            
            return make_response(jsonify("Less Samples"), 500)
        elif (len(current) == 1115):           
           
            data = np.array(current).reshape(-1, 1)
            #print(data.shape)
            result = inference_module.predict_sample(data)
        else:
   
            data = np.array(current).reshape(-1, 1)
            #print(f"Old Shape: {data.shape}")
            diff = abs(data.shape[0] - 1115)
            n_of_samples = data.shape[0] - diff
            data =  resample(data, n_of_samples, domain='time')
            #print(f"New Shape: {data.shape}")
            
            result = inference_module.predict_sample(data) 
        
        return make_response(jsonify(result), 200)  
    except:
        
        return make_response(jsonify("Error"), 500)
    
#Start Api
#app.run(host="192.168.0.122")
if __name__ == '__main__':
    app.run("0.0.0.0", port=5000)
       