import json
import os
import mysql.connector
from db_config import DB_CONFIG

JSON_FILE = r"c:\Users\DELL\AndroidStudioProjects\PathPiolet\backend\ai_finder_colleges.json"

def deduplicate_json():
    print(f"Deduplicating JSON file: {JSON_FILE}")
    if not os.path.exists(JSON_FILE):
        print("JSON file not found.")
        return

    with open(JSON_FILE, 'r', encoding='utf-8') as f:
        colleges = json.load(f)

    seen_names = set()
    unique_colleges = []
    
    for college in colleges:
        name_lower = college['name'].strip().lower()
        if name_lower not in seen_names:
            seen_names.add(name_lower)
            unique_colleges.append(college)
    
    print(f"JSON: Reduced from {len(colleges)} to {len(unique_colleges)} colleges.")
    
    with open(JSON_FILE, 'w', encoding='utf-8') as f:
        json.dump(unique_colleges, f, indent=2)
    print("JSON updated successfully.")

def deduplicate_db():
    print("Deduplicating MySQL database...")
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor(buffered=True)
        
        # Find duplicate names
        cursor.execute("""
            SELECT name, COUNT(*) 
            FROM colleges 
            GROUP BY name 
            HAVING COUNT(*) > 1
        """)
        duplicates = cursor.fetchall()
        
        if not duplicates:
            print("No duplicates found in database.")
        else:
            total_removed = 0
            for name, count in duplicates:
                print(f"Found {count} entries for '{name}'. Keeping only the latest one.")
                
                # Get all IDs for this name
                cursor.execute("SELECT id FROM colleges WHERE name = %s ORDER BY id DESC", (name,))
                ids = [row[0] for row in cursor.fetchall()]
                
                # Keep the first one (latest), delete the rest
                ids_to_delete = ids[1:]
                for del_id in ids_to_delete:
                    cursor.execute("DELETE FROM colleges WHERE id = %s", (del_id,))
                    total_removed += 1
            
            conn.commit()
            print(f"Database: Removed {total_removed} duplicate entries.")
        
        conn.close()
    except Exception as e:
        print(f"Error deduplicating database: {e}")

if __name__ == "__main__":
    deduplicate_json()
    deduplicate_db()
