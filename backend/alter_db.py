import mysql.connector
from app import DB_CONFIG

def add_profile_picture_column():
    conn = mysql.connector.connect(**DB_CONFIG)
    cursor = conn.cursor()
    try:
        # Check if the column exists to prevent duplicate column error
        cursor.execute("SHOW COLUMNS FROM users LIKE 'profile_picture'")
        result = cursor.fetchone()
        if not result:
            cursor.execute("ALTER TABLE users ADD COLUMN profile_picture VARCHAR(255);")
            conn.commit()
            print("Successfully added profile_picture column.")
        else:
            print("Column profile_picture already exists.")
    except Exception as e:
        print(f"Error: {e}")
    finally:
        cursor.close()
        conn.close()

if __name__ == "__main__":
    add_profile_picture_column()
