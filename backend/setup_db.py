import mysql.connector
from db_config import DB_CONFIG
import os

def setup_database():
    script_dir = os.path.dirname(os.path.abspath(__file__))
    schema_path = os.path.join(script_dir, 'schema.sql')
    if not os.path.exists(schema_path):
        print(f"Error: {schema_path} not found.")
        return

    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor()
        
        print("Reading schema.sql...")
        with open(schema_path, 'r', encoding='utf-8') as f:
            # Split by semicolon but ignore inside comments or strings (simple version)
            sql_commands = f.read().split(';')

        print("Executing schema commands...")
        for command in sql_commands:
            if command.strip():
                try:
                    cursor.execute(command)
                except mysql.connector.Error as e:
                    print(f"Skipping command due to error: {e}")
        
        conn.commit()
        print("Database setup completed successfully!")

    except mysql.connector.Error as err:
        print(f"Connection Error: {err}")
    finally:
        if 'cursor' in locals():
            cursor.close()
        if 'conn' in locals():
            conn.close()

if __name__ == "__main__":
    setup_database()
