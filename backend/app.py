import pickle
import pandas as pd
from datetime import datetime, timedelta
from flask import Flask, request, jsonify
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

with open('artifacts/maintenance.pickle', 'rb') as f:
    maintenance_model = pickle.load(f)

with open('artifacts/fault_detection.pickle', 'rb') as f:
    fault_model = pickle.load(f)

with open('artifacts/quality_control.pickle', 'rb') as f:
    quality_model = pickle.load(f)

def inference_maintenance(
                        json_data,
                        class_dict = {
                                    "Solar Panel": 0,
                                    "Charge Controller": 1,
                                    "Inverter": 2,
                                    "Battery": 3
                                }
                        ):
    df = pd.DataFrame([json_data])
    df['fixed_date'] = pd.to_datetime(df['fixed_date'])
    df['component_type'] = df['component_type'].apply(lambda x: class_dict[x])
    df = df.dropna()
    df = df.drop('fixed_date', axis=1)
    label = maintenance_model.predict(df)
    label = int(label) + 1
    return f"{label} Services"

def inference_fault(
                    json_data,
                    class_dict = {
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

def inference_quality(
                    json_data,
                    class_dict = {
                                "Solar Panel output": 0,
                                "Charge Controller to battery": 1,
                                "Charge Controller to inverter": 2,
                                "Battery output": 3
                                },
                    power_range_dict = {
                                    "components": {
                                        "Solar Panel output": {
                                            "lower_bound": 250,
                                            "upper_bound": 400
                                        },
                                        "Charge Controller to battery": {
                                            "lower_bound": 150,
                                            "upper_bound": 300
                                        },
                                        "Charge Controller to inverter": {
                                            "lower_bound": 200,
                                            "upper_bound": 350
                                        },
                                        "Battery output": {
                                            "lower_bound": 200,
                                            "upper_bound": 400
                                        }
                                    }
                                }
                    ):
    df = pd.DataFrame([json_data])
    details = power_range_dict['components'][json_data['power_output_type']]
    df['power_output_type'] = df['power_output_type'].apply(lambda x: class_dict[x])
    pred = quality_model.predict(df)
    power = int(pred.squeeze())
    lower_bound = details['lower_bound']
    upper_bound = details['upper_bound']

    if power < lower_bound:
        status = "Power level is too low"
    elif power > upper_bound:
        status = "Power level is too high"
    else:
        status = "Power level is within range"
    power = f"{power} W"
    return {
        "power": power,
        "status": status
        }

@app.route('/api/maintenance', methods=['POST'])
def maintenance():
    data = request.get_json()
    result = inference_maintenance(data)
    return jsonify({"result": result})

@app.route('/api/fault', methods=['POST'])
def fault():
    data = request.get_json()
    result = inference_fault(data)
    return jsonify({"result": result})

@app.route('/api/quality', methods=['POST'])
def quality():
    data = request.get_json()
    result = inference_quality(data)
    return jsonify({"result": result})

if __name__ == '__main__':
    app.run(
            host='0.0.0.0',
            port=5000
            )