import mysql.connector
from db_config import DB_CONFIG

try:
    conn = mysql.connector.connect(**DB_CONFIG)
    cursor = conn.cursor()
    cursor.execute("SHOW COLUMNS FROM colleges LIKE 'website'")
    if not cursor.fetchone():
        cursor.execute("ALTER TABLE colleges ADD COLUMN website VARCHAR(255)")
        print("added website col")
    
    cursor.execute("UPDATE colleges SET website = CONCAT('https://www.', REPLACE(LOWER(name), ' ', ''), '.edu.in') WHERE website IS NULL")
    conn.commit()
    print("updated websites")
except Exception as e:
    print(str(e))
finally:
    if 'cursor' in locals():
        cursor.close()
    if 'conn' in locals() and conn.is_connected():
        conn.close()
