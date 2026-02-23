import mysql.connector
from db_config import DB_CONFIG

def view_login_logs():
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor(dictionary=True)
        
        print("\n" + "="*50)
        print("          REAL-TIME LOGIN ACTIVITY")
        print("="*50)
        print(f"{'Log ID':<10} {'User ID':<10} {'Email':<30} {'Login Time'}")
        print("-" * 75)

        cursor.execute("SELECT * FROM login_logs ORDER BY login_time DESC LIMIT 20")
        logs = cursor.fetchall()

        if not logs:
            print("No login activity found.")
        else:
            for log in logs:
                print(f"{log['id']:<10} {log['user_id']:<10} {log['email']:<30} {log['login_time']}")

        print("="*50 + "\n")

    except mysql.connector.Error as err:
        print(f"Error: {err}")
    finally:
        if 'cursor' in locals():
            cursor.close()
        if 'conn' in locals():
            conn.close()

if __name__ == "__main__":
    view_login_logs()
