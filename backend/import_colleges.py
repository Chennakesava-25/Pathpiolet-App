import csv
import json
import os
import mysql.connector
from db_config import DB_CONFIG

CSV_FILE = r"C:\Users\DELL\Downloads\AI_College_Dataset_ALL_1399.csv"
JSON_FILE = r"c:\Users\DELL\AndroidStudioProjects\PathPiolet\backend\ai_finder_colleges.json"

def normalize_specializations(spec_str):
    if not spec_str:
        return ["All Branches"]
    specs = [s.strip() for s in spec_str.split(',')]
    return specs

def parse_csv():
    colleges = []
    with open(CSV_FILE, mode='r', encoding='utf-8') as f:
        reader = csv.DictReader(f)
        for row in reader:
            college = {
                "name": row['Name'],
                "location": f"{row['District']}, {row['State']}",
                "city": row['District'],
                "state": row['State'],
                "region": row['Region'],
                "budget_range": row['Budget Range'],
                "fees": f"₹ {row['Approx Fees']}" if row['Approx Fees'] else "Contact for details",
                "type": row['College Type'],
                "hostel": row['Hostel Availability'],
                "placement_priority": row['Placement Priority'],
                "average_package": row['Average Salary'],
                "campus_size": row['Campus Size'],
                "cutoff_exam": row['Entrance Exam'],
                "specializations": normalize_specializations(row['Specializations']),
                "description": f"{row['Name']} is a {row['College Type']} institution located in {row['District']}, {row['State']}. It offers specializations in {row['Specializations']}.",
                "website": row['Website']
            }
            colleges.append(college)
    return colleges

def update_json(colleges):
    print(f"Updating JSON file with {len(colleges)} colleges...")
    # We overwrite the JSON with the new data as requested (it's the 'ALL' dataset)
    with open(JSON_FILE, 'w', encoding='utf-8') as f:
        json.dump(colleges, f, indent=2)
    print("JSON updated successfully.")

def update_db(colleges):
    print(f"Updating MySQL database with {len(colleges)} colleges...")
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor(buffered=True)
        
        # Ensure 'website' column exists
        cursor.execute("SHOW COLUMNS FROM colleges LIKE 'website'")
        if not cursor.fetchone():
            print("Adding 'website' column to 'colleges' table...")
            cursor.execute("ALTER TABLE colleges ADD COLUMN website VARCHAR(255)")
            conn.commit()

        # Ensure 'description' column exists
        cursor.execute("SHOW COLUMNS FROM colleges LIKE 'description'")
        if not cursor.fetchone():
            print("Adding 'description' column to 'colleges' table...")
            cursor.execute("ALTER TABLE colleges ADD COLUMN description TEXT")
            conn.commit()

        for college in colleges:
            tags = f"{college['region']}, {college['type']}, Hostel: {college['hostel']}, {', '.join(college['specializations'])}"
            
            # Check if exists
            cursor.execute("SELECT id FROM colleges WHERE name = %s", (college['name'],))
            existing = cursor.fetchone()
            
            if existing:
                # Update
                cursor.execute("""
                    UPDATE colleges 
                    SET city=%s, state=%s, fees=%s, avg_package=%s, description=%s, tags=%s, website=%s
                    WHERE name=%s
                """, (college['city'], college['state'], college['fees'], college['average_package'], college['description'], tags, college['website'], college['name']))
            else:
                # Insert
                cursor.execute("""
                    INSERT INTO colleges (name, city, state, score, nirf_rank, fees, avg_package, description, tags, website)
                    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
                """, (college['name'], college['city'], college['state'], 70.0, 0, college['fees'], college['average_package'], college['description'], tags, college['website']))
        
        conn.commit()
        print("Database updated successfully.")
        conn.close()
    except Exception as e:
        print(f"Error updating database: {e}")

if __name__ == "__main__":
    if not os.path.exists(CSV_FILE):
        print(f"Error: CSV file not found at {CSV_FILE}")
    else:
        data = parse_csv()
        update_json(data)
        update_db(data)
