import json
import os
import mysql.connector
from db_config import DB_CONFIG

NEW_JSON = r"c:\Users\DELL\AndroidStudioProjects\PathPiolet\backend\ai_finder_colleges.json"
OLD_JSON = r"c:\Users\DELL\AndroidStudioProjects\PathPiolet\backend\colleges_data.json"

def normalize_name(name):
    return " ".join(name.strip().lower().split())

def merge_datasets():
    print("Merging datasets...")
    
    if not os.path.exists(NEW_JSON) or not os.path.exists(OLD_JSON):
        print("Missing JSON files.")
        return

    with open(NEW_JSON, 'r', encoding='utf-8') as f:
        new_colleges = json.load(f)
    
    with open(OLD_JSON, 'r', encoding='utf-8') as f:
        old_colleges = json.load(f)

    seen_names = {} # normalized_name -> college_object
    
    # Process NEW colleges first (they have more data)
    for col in new_colleges:
        norm_name = normalize_name(col['name'])
        seen_names[norm_name] = col

    # Process OLD colleges, add only if not seen
    added_count = 0
    for col in old_colleges:
        norm_name = normalize_name(col['name'])
        if norm_name not in seen_names:
            # Normalize to new schema
            loc_parts = col.get('location', '').split(',')
            city = loc_parts[0].strip() if len(loc_parts) > 0 else "N/A"
            state = loc_parts[1].strip() if len(loc_parts) > 1 else "India"
            
            normalized_col = {
                "name": col['name'],
                "location": col.get('location', f"{city}, {state}"),
                "city": city,
                "state": state,
                "region": "Unknown", # Default for old entries
                "budget_range": "Medium", # Default
                "fees": col.get('fees', "N/A"),
                "type": col.get('type', "N/A"),
                "hostel": col.get('hostel', "N/A"),
                "placement_priority": "Medium",
                "average_package": col.get('average_package', "N/A"),
                "campus_size": "Medium",
                "cutoff_exam": col.get('cutoff_exam', "N/A"),
                "specializations": col.get('specializations', ["All Branches"]),
                "description": f"{col['name']} is a {col.get('type', 'private')} institution located in {city}, {state}.",
                "website": ""
            }
            seen_names[norm_name] = normalized_col
            added_count += 1
    
    master_list = list(seen_names.values())
    print(f"Master dataset contains {len(master_list)} unique colleges.")
    print(f"Added {added_count} unique colleges from the old dataset.")
    
    with open(NEW_JSON, 'w', encoding='utf-8') as f:
        json.dump(master_list, f, indent=2)
    print(f"Updated {NEW_JSON} with merged data.")
    return master_list

def sync_with_db(master_list):
    print("Syncing master list with database...")
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor(buffered=True)
        
        # We will iterate and ensure each college in master_list exists and is updated.
        # Then we will delete anything in DB that is NOT in master_list (to keep it clean as requested)
        
        master_names = {normalize_name(c['name']) for c in master_list}
        
        for college in master_list:
            tags = f"{college['region']}, {college['type']}, Hostel: {college['hostel']}, {', '.join(college['specializations'])}"
            
            cursor.execute("SELECT id FROM colleges WHERE name = %s", (college['name'],))
            existing = cursor.fetchone()
            
            if existing:
                cursor.execute("""
                    UPDATE colleges 
                    SET city=%s, state=%s, fees=%s, avg_package=%s, description=%s, tags=%s, website=%s
                    WHERE name=%s
                """, (college['city'], college['state'], college['fees'], college['average_package'], college['description'], tags, college.get('website', ''), college['name']))
            else:
                cursor.execute("""
                    INSERT INTO colleges (name, city, state, score, nirf_rank, fees, avg_package, description, tags, website)
                    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
                """, (college['name'], college['city'], college['state'], 70.0, 0, college['fees'], college['average_package'], college['description'], tags, college.get('website', '')))
        
        # Cleanup: remove any extra entries NOT in the master list
        cursor.execute("SELECT name FROM colleges")
        db_count = 0
        deleted_count = 0
        for (db_name,) in cursor.fetchall():
            if normalize_name(db_name) not in master_names:
                cursor.execute("DELETE FROM colleges WHERE name = %s", (db_name,))
                deleted_count += 1
            else:
                db_count += 1
        
        conn.commit()
        print(f"Database synced. Total active colleges: {db_count}. Removed {deleted_count} stale/duplicate entries.")
        conn.close()
    except Exception as e:
        print(f"Error syncing database: {e}")

if __name__ == "__main__":
    final_list = merge_datasets()
    if final_list:
        sync_with_db(final_list)
