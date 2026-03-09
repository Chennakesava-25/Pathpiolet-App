import requests
import json

def test_recommendation():
    url = "http://localhost:5000/api/ai/recommend"
    payload = {
        "location": "Tamil Nadu",
        "budget": "1.5L - 3L",
        "college_type": "Private Autonomous",
        "hostel": "Yes",
        "placement_priority": "High",
        "campus_size": "Large",
        "specialization": "AI & Data Science",
        "exam_score": "JEE Main 92 percentile"
    }

    print(f"Testing AI Recommendation Endpoint: {url}")
    try:
        response = requests.post(url, json=payload, timeout=60)
        print(f"Status Code: {response.status_code}")
        
        if response.status_code == 200:
            data = response.json()
            print("Response success:", data.get('success'))
            print("Colleges suggested:")
            for college in data.get('colleges', []):
                print(f"- {college.get('name')} ({college.get('location')}) | Match: {college.get('match_score')}")
        else:
            print("Error:", response.text)
    except Exception as e:
        print(f"Request failed: {str(e)}")
        print("\nNote: Make sure the Flask server (app.py) is running locally before running this test.")

if __name__ == "__main__":
    test_recommendation()
