import json
import urllib.request
import urllib.parse
import re
import mysql.connector
from db_config import DB_CONFIG
import time

def get_real_url(query):
    try:
        url = 'https://html.duckduckgo.com/html/?q=' + urllib.parse.quote(query + ' official website')
        req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0'})
        html = urllib.request.urlopen(req).read().decode('utf-8')
        matches = re.findall(r'class=\"result__url\" href=\"([^\"]+)\"', html)
        for m in matches:
            if 'http' in m and 'wikipedia' not in m and 'shiksha' not in m and 'collegedunia' not in m and 'careers360' not in m:
                if 'uddg=' in m:
                    actual = urllib.parse.unquote(m.split('uddg=')[1].split('&')[0])
                    return actual
                return m
    except:
        pass
    
    # fallback to google search url
    return f'https://www.google.com/search?q={urllib.parse.quote(query)}+official+website'

print('Connecting to DB...')
try:
    conn = mysql.connector.connect(**DB_CONFIG)
    cursor = conn.cursor(dictionary=True)
    
    cursor.execute('SELECT id, name FROM colleges')
    colleges = cursor.fetchall()
    
    print(f'Found {len(colleges)} colleges. Updating URLs (this will take a minute)...')
    
    for i, c in enumerate(colleges):
        url = get_real_url(c['name'])
        print(f'{i+1}/{len(colleges)}: {c["name"]} -> {url}')
        cursor.execute('UPDATE colleges SET website = %s WHERE id = %s', (url, c['id']))
        conn.commit()
        time.sleep(1) # prevent rate limiting
        
    print('DB Update Complete!')
        
except Exception as e:
    print('Failed:', e)
finally:
    if 'cursor' in locals(): cursor.close()
    if 'conn' in locals() and conn.is_connected(): conn.close()
