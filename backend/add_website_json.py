import json
try:
    with open('ai_finder_colleges.json', 'r', encoding='utf-8') as f:
        d = json.load(f)
    for c in d:
        c['website'] = 'https://www.' + c.get('name', '').replace(' ', '').lower() + '.edu.in'
    with open('ai_finder_colleges.json', 'w', encoding='utf-8') as f:
        json.dump(d, f, indent=2)
    print("Updated JSON")
except Exception as e:
    print(str(e))
