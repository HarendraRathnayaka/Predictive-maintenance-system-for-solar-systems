import pickle
import pandas as pd
from datetime import datetime, timedelta
from flask import Flask, request, jsonify
from flask_cors import CORS

# Initialize Flask app
app = Flask(__name__)
CORS(app)

# Load the fault detection model
with open('artifacts/fault_detection.pickle', 'rb') as f:
    fault_model = pickle.load(f)

# Fault prediction inference function
def inference_fault(
                    json_data,
                    class_dict={
                        "Solar Panel": 0,
                        "Charge Controller": 1,
                        "Inverter": 2,
                        "Battery": 3
                    }
                   ):
    df = pd.DataFrame([json_data])
    df['component_type'] = df['component_type'].apply(lambda x: class_dict[x])
    time_to_fault = fault_model.predict(df)[0]
    time_to_fault = time_to_fault + 1 if time_to_fault - int(time_to_fault) > 0.5 else int(time_to_fault)
    current_time = datetime.now()
    fault_time = current_time + timedelta(days=time_to_fault)
    fault_date = fault_time.strftime("%d-%m-%Y")
    return f"Fault predicted in {json_data['component_type']} in {time_to_fault} days, on the {fault_date}"

# API route for fault prediction
@app.route('/api/fault', methods=['POST'])
def fault():
    data = request.get_json()
    result = inference_fault(data)
    return jsonify({"result": result})

# Run the app
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
