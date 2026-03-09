
import json
import os

json_path = 'c:/Users/DELL/AndroidStudioProjects/Pathpiolet/backend/ai_finder_colleges.json'

if os.path.exists(json_path):
    with open(json_path, 'r', encoding='utf-8') as f:
        colleges = json.load(f)
    
    for college in colleges:
        college['average_package'] = '6 LPA'
    
    with open(json_path, 'w', encoding='utf-8') as f:
        json.dump(colleges, f, indent=2)
    
    print(f"Successfully updated {len(colleges)} colleges with average package 6 LPA.")
else:
    print("Dataset not found.")
