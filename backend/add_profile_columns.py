import mysql.connector
from db_config import DB_CONFIG

def add_columns():
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor()
        
        # education_level
        try:
            cursor.execute("ALTER TABLE users ADD COLUMN education_level VARCHAR(100)")
            print("Added education_level column")
        except mysql.connector.Error as err:
            if err.errno == 1060: # Duplicate column name
                print("education_level column already exists")
            else:
                print(f"Error adding education_level: {err}")

        # interested_field
        try:
            cursor.execute("ALTER TABLE users ADD COLUMN interested_field VARCHAR(100)")
            print("Added interested_field column")
        except mysql.connector.Error as err:
            if err.errno == 1060: # Duplicate column name
                print("interested_field column already exists")
            else:
                print(f"Error adding interested_field: {err}")

        conn.commit()
    except mysql.connector.Error as err:
        print(f"Main Error: {err}")
    finally:
        if 'cursor' in locals():
            cursor.close()
        if 'conn' in locals():
            conn.close()

if __name__ == "__main__":
    add_columns()
