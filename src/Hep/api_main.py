from flask import Flask, make_response, jsonify, request
from inference_hep import Hep
import numpy as np

app = Flask("hepatitis")

#Test Api
@app.route('/test', methods=['GET'])
def test_api():
    return make_response(jsonify("Hepatitis test"))

@app.route("/hep", methods=['POST'])
def analyse_hep():
    try:
        json = request.json
        json_readings = json["readings"]
        potential = []
        current = []
        
        for i in range(len(json_readings)):
            sample = json_readings[i]
            v = np.float32(sample["V"])
            c = np.float32(sample["I"])
            potential.append(v)
            current.append(c)
        
        hep_analysis = Hep()
        result = hep_analysis.predict_data(np.array(current))
        
        print(result)
        return make_response(jsonify(result), 200)
    
    except:
        return make_response(jsonify("Error"), 500)

@app.route("/rhod", methods=['POST'])
def analyse_rhod():
    try:
        
        json = request.json
        json_readings = json["readings"]
        potential = []
        current = []
        
        for i in range(len(json_readings)):
            sample = json_readings[i]
            v = float(sample["V"])
            c = float(sample["I"]) 
            potential.append(v)
            current.append(c)
        
        #print(len(json_readings))
        return make_response(jsonify(f"Rhodamine: {min(potential)}, {min(current)}, {max(potential)}, {max(current)}, {len(json_readings)}"), 200)
    
    except:
        return make_response(jsonify("Error"), 500)
    
#Start Api
app.run()