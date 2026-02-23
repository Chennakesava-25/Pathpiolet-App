import mysql.connector
from db_config import DB_CONFIG

def view_all_users():
    """
    Diagnostic script to view all registered users in the PathPilot database.
    This helps you see details like email, phone number, and password (if plain text).
    """
    
    print("-" * 50)
    print("PATHPILOT - REGISTERED USERS LIST")
    print("-" * 50)
    
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor(dictionary=True)
        
        # Select all users
        cursor.execute("SELECT id, username, email, phone, password_hash, created_at FROM users ORDER BY id DESC")
        users = cursor.fetchall()
        
        if not users:
            print("No users found in the database.")
        else:
            print(f"{'ID':<4} | {'Username':<15} | {'Email':<25} | {'Phone':<15}")
            print("-" * 75)
            for user in users:
                # Note: Currently 'password_hash' column stores the plain password from the app
                print(f"{user['id']:<4} | {user['username']:<15} | {user['email']:<25} | {user['phone'] or 'N/A':<15}")
                print(f"     -> Password: {user['password_hash']}")
                print(f"     -> Joined:   {user['created_at']}")
                print("-" * 75)
                
        cursor.close()
        conn.close()
        
    except mysql.connector.Error as err:
        print(f"Database Error: {err}")
    except Exception as e:
        print(f"An error occurred: {e}")

if __name__ == "__main__":
    view_all_users()
