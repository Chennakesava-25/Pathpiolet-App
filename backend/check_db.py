import mysql.connector
from db_config import DB_CONFIG

def check_vijayawada():
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor(dictionary=True)
        cursor.execute("SELECT * FROM colleges WHERE city LIKE '%Vijayawada%'")
        rows = cursor.fetchall()
        print(f"Found {len(rows)} colleges in Vijayawada:")
        for row in rows:
            print(f"- {row['name']} ({row['city']})")
        conn.close()
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    check_vijayawada()
