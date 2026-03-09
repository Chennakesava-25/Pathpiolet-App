
import requests
import json

BASE_URL = "http://10.224.155.67:5000"

def test_recommendations():
    payload = {
        "location": "Bengaluru",
        "budget": 300000,
        "collegeTypes": ["Private"],
        "hostel": True,
        "placementPriority": "Medium",
        "campusSize": "Large",
        "specializations": ["Computer Science"]
    }
    
    try:
        response = requests.post(f"{BASE_URL}/api/recommendations", json=payload)
        print("\nTesting General Recommendations (Bengaluru)")
        print(f"Status: {response.status_code}")
        if response.status_code == 200:
            results = response.json()
            print(f"Found {len(results)} results.")
            for i, college in enumerate(results[:3]):
                print(f"{i+1}. {college['name']} - Location: {college['location']}")
        else:
            print(f"Error: {response.text}")
    except Exception as e:
        print(f"Connection failed: {e}")

def test_strict_location():
    location = "Chennai"
    payload = {
        "location": location,
        "budget": 500000,
        "collegeTypes": [],
        "hostel": False,
        "placementPriority": "Medium",
        "campusSize": "",
        "specializations": []
    }
    
    try:
        response = requests.post(f"{BASE_URL}/api/recommendations", json=payload)
        print(f"\nTesting Strict Location: {location}")
        print(f"Status: {response.status_code}")
        if response.status_code == 200:
            results = response.json()
            print(f"Found {len(results)} results.")
            mismatches = [c['name'] for c in results if location.lower() not in c['location'].lower()]
            if not mismatches:
                print("PASSED: All results match the requested location.")
            else:
                print(f"FAILED: Found mismatches: {mismatches}")
        else:
            print(f"Error: {response.text}")
    except Exception as e:
        print(f"Connection failed: {e}")

if __name__ == "__main__":
    test_recommendations()
    test_strict_location()
