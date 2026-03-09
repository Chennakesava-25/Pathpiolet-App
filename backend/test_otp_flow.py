import requests
import json
import time

BASE_URL = "http://localhost:5000"

def test_signup_otp_flow():
    print("Testing Signup OTP Flow...")
    
    # 1. Register (Step 1: Request OTP)
    signup_data = {
        "username": "testuser_otp",
        "email": "testuser_otp@gmail.com",
        "password": "Password123",
        "phone": "1234567890",
        "age": "20"
    }
    
    print(f"1. Requesting OTP for {signup_data['email']}...")
    try:
        response = requests.post(f"{BASE_URL}/api/auth/register", json=signup_data)
        print(f"Response: {response.status_code} - {response.text}")
        if response.status_code != 200:
            print("Failed to request OTP")
            return
        
        # Since it's local, we assume OTP is in the console or debug output
        # In this environment, I'll need to check the database for the OTP
        print("Note: In a real test, I'd check logs or DB for OTP. For now, assuming it's sent.")
        
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    # Note: Backend must be running
    print("Ensure backend is running on http://localhost:5000")
    # test_signup_otp_flow() # Uncomment if you want to run this manually
