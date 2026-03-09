import json
import urllib.request
import urllib.parse
import re
import mysql.connector
from db_config import DB_CONFIG
import time
import os

def get_real_url(query):
    try:
        url = 'https://html.duckduckgo.com/html/?q=' + urllib.parse.quote(query + ' official website')
        req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'})
        html = urllib.request.urlopen(req, timeout=10).read().decode('utf-8')
        matches = re.findall(r'class=\"result__url\" href=\"([^\"]+)\"', html)
        for m in matches:
            if 'http' in m and 'wikipedia' not in m and 'shiksha' not in m and 'collegedunia' not in m and 'careers360' not in m and 'facebook' not in m and 'twitter' not in m:
                if 'uddg=' in m:
                    actual = urllib.parse.unquote(m.split('uddg=')[1].split('&')[0])
                    return actual
                return m
    except Exception as e:
        print(f"Error fetching for {query}: {e}")
    
    # fallback to google search url
    return f'https://www.google.com/search?q={urllib.parse.quote(query)}+official+website'

def repair_websites():
    # 1. Update JSON
    json_path = 'ai_finder_colleges.json'
    if not os.path.exists(json_path):
        print(f"Error: {json_path} not found")
        return

    with open(json_path, 'r', encoding='utf-8') as f:
        colleges = json.load(f)

    print(f"Auditing {len(colleges)} colleges in JSON...")
    updated_count = 0
    
    # Define some manual fixes for prominent ones to be safe
    manual_fixes = {
        "IIT Madras": "https://www.iitm.ac.in/",
        "IIT Bombay": "https://www.iitb.ac.in/",
        "BITS Pilani": "https://www.bits-pilani.ac.in/",
        "NIT Trichy": "https://www.nitt.edu/",
        "VIT Vellore": "https://vit.ac.in/",
        "SRM University": "https://www.srmist.edu.in/",
        "CoEP Pune": "https://www.coep.org.in/",
        "RV College of Engineering": "https://www.rvce.edu.in/",
        "PSG College of Technology": "https://www.psgtech.edu/",
        "Delhi Technological University (DTU)": "http://www.dtu.ac.in/",
        "Manipal Institute of Technology": "https://manipal.edu/mit.html",
        "Amrita Vishwa Vidyapeetham": "https://www.amrita.edu/",
        "VJTI Mumbai": "https://vjti.ac.in/"
    }

    for college in colleges:
        name = college['name']
        current_url = college.get('website', '')
        
        if not current_url or current_url.startswith('https://www.google.com'):
            if name in manual_fixes:
                new_url = manual_fixes[name]
                print(f"Manual Fix: {name} -> {new_url}")
            else:
                print(f"Searching for {name}...")
                new_url = get_real_url(name)
                print(f"Found: {new_url}")
                time.sleep(1) # rate limiting
            
            college['website'] = new_url
            updated_count += 1

    with open(json_path, 'w', encoding='utf-8') as f:
        json.dump(colleges, f, indent=2)
    
    print(f"JSON Update Complete: {updated_count} colleges updated.")

    # 2. Update Database
    print("Connecting to DB...")
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor()
        
        for college in colleges:
            cursor.execute('UPDATE colleges SET website = %s WHERE name = %s', (college['website'], college['name']))
        
        conn.commit()
        print("Database Update Complete!")
    except Exception as e:
        print(f"Database update failed: {e}")
    finally:
        if 'cursor' in locals(): cursor.close()
        if 'conn' in locals() and conn.is_connected(): conn.close()

if __name__ == "__main__":
    repair_websites()
