import mysql.connector
from db_config import DB_CONFIG

def check_status():
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor(dictionary=True)
        
        print("\n=== Registered Users ===")
        cursor.execute("SELECT id, username, email FROM users")
        for user in cursor.fetchall():
            print(f"ID: {user['id']}, Name: {user['username']}, Email: {user['email']}")

        print("\n=== Recent OTPs ===")
        cursor.execute("SELECT * FROM user_otps ORDER BY created_at DESC LIMIT 10")
        otps = cursor.fetchall()
        if not otps:
            print("No OTPs found.")
        else:
            for otp in otps:
                print(f"Email: {otp['email']}, Code: {otp['otp_code']}, Verified: {otp['verified']}, Expires: {otp['expires_at']}")

    except mysql.connector.Error as err:
        print(f"Error: {err}")
    finally:
        if 'cursor' in locals():
            cursor.close()
        if 'conn' in locals():
            conn.close()

if __name__ == "__main__":
    check_status()
