import json
import os
import mysql.connector
import re
from db_config import DB_CONFIG

# Data provided by user
NIRF_DATA = """
Indian Institute of ScienceMore Details |  | 	Bengaluru	Karnataka	85.05	1
IR-O-U-0109	Jawaharlal Nehru UniversityMore Details |  | 	New Delhi	Delhi	71.00	2
IR-O-U-0234	Manipal Academy of Higher Education-ManipalMore Details |  | 	Manipal	Karnataka	69.25	3
IR-O-U-0108	Jamia Millia IslamiaMore Details |  | 	New Delhi	Delhi	69.10	4
IR-O-U-0120	University of DelhiMore Details |  | 	Delhi	Delhi	67.38	5
IR-O-U-0500	Banaras Hindu UniversityMore Details |  | 	Varanasi	Uttar Pradesh	67.28	6
IR-O-U-0391	Birla Institute of Technology & Science -PilaniMore Details |  | 	Pilani	Rajasthan	67.24	7
IR-O-U-0436	Amrita Vishwa VidyapeethamMore Details |  | 	Coimbatore	Tamil Nadu	67.05	8
IR-O-U-0575	Jadavpur UniversityMore Details |  | 	Kolkata	West Bengal	65.42	9
IR-O-U-0496	Aligarh Muslim UniversityMore Details |  | 	Aligarh	Uttar Pradesh	65.35	10
IR-O-U-0473	S.R.M. Institute of Science and TechnologyMore Details |  | 	Chennai	Tamil Nadu	65.26	11
IR-O-U-0304	Homi Bhabha National InstituteMore Details |  | 	Mumbai	Maharashtra	65.08	12
IR-O-I-1441	Saveetha Institute of Medical and Technical SciencesMore Details |  | 	Chennai	Tamil Nadu	65.04	13
IR-O-U-0490	Vellore Institute of TechnologyMore Details |  | 	Vellore	Tamil Nadu	64.64	14
IR-O-U-0363	Siksha `O` AnusandhanMore Details |  | 	Bhubaneswar	Odisha	63.14	15
IR-O-U-0101	Indian Agricultural Research InstituteMore Details |  | 	New Delhi	Delhi	62.89	16
IR-O-U-0356	Kalinga Institute of Industrial TechnologyMore Details |  | 	Bhubaneswar	Odisha	62.87	17
IR-O-U-0042	University of HyderabadMore Details |  | 	Hyderabad	Telangana	61.83	18
IR-O-U-0747	Chandigarh UniversityMore Details |  | 	Mohali	Punjab	61.27	19
IR-O-U-0439	Anna UniversityMore Details |  | 	Chennai	Tamil Nadu	61.22	20
IR-O-U-0222	JSS Academy of Higher Education and ResearchMore Details |  | 	Mysuru	Karnataka	60.00	21
IR-O-U-0497	Amity UniversityMore Details |  | 	Gautam Budh Nagar	Uttar Pradesh	59.68	22
IR-O-U-0006	Andhra University, VisakhapatnamMore Details |  | 	Visakhapatnam	Andhra Pradesh	59.20	23
IR-O-U-0329	Symbiosis InternationalMore Details |  | 	Pune	Maharashtra	59.16	24
IR-O-U-0260	Kerala UniversityMore Details |  | 	Thiruvananthapuram	Kerala	59.05	25
IR-O-I-1480	Thapar Institute of Engineering and Technology (Deemed-to-be-university)More Details |  | 	Patiala	Punjab	58.87	26
IR-O-U-0020	Koneru Lakshmaiah Education Foundation University (K L College of Engineering)More Details |  | 	Vaddeswaram	Andhra Pradesh	58.87	26
IR-O-U-0458	Kalasalingam Academy of Research and EducationMore Details |  | 	Krishnan Koil	Tamil Nadu	58.37	28
IR-O-U-0476	Shanmugha Arts Science Technology & Research AcademyMore Details |  | 	Thanjavur	Tamil Nadu	58.06	29
IR-O-U-0027	Osmania UniversityMore Details |  | 	Hyderabad	Telangana	57.94	30
IR-O-U-0379	Lovely Professional UniversityMore Details |  | 	Phagwara	Punjab	57.77	31
IR-O-U-0253	Cochin University of Science and TechnologyMore Details |  | 	Cochin	Kerala	57.55	32
IR-O-U-0052	Gauhati UniversityMore Details |  | 	Guwahati	Assam	57.37	33
IR-O-U-0196	University of KashmirMore Details |  | 	Srinagar	Jammu and Kashmir	57.13	34
IR-O-U-0078	Panjab UniversityMore Details |  | 	Chandigarh	Chandigarh	56.88	35
IR-O-U-0448	Bharathidasan UniversityMore Details |  | 	Tiruchirappalli	Tamil Nadu	55.85	36
IR-O-U-0498	Babasheb Bhimrao Ambedkar UniversityMore Details |  | 	Lucknow	Uttar Pradesh	55.55	37
IR-O-I-1357	University of MadrasMore Details |  | 	Chennai	Tamil Nadu	55.23	38
IR-O-U-0570	Calcutta UniversityMore Details |  | 	Kolkata	West Bengal	55.17	39
IR-O-U-0308	Institute of Chemical TechnologyMore Details |  | 	Mumbai	Maharashtra	55.13	40
IR-O-I-1110	Dr. D. Y. Patil VidyapeethMore Details |  | 	Pune	Maharashtra	54.90	41
IR-O-U-0098	Delhi Technological UniversityMore Details |  | 	New Delhi	Delhi	54.88	42
IR-O-U-0262	Mahatma Gandhi University, KottayamMore Details |  | 	Kottayam	Kerala	54.61	43
IR-O-U-0435	Alagappa UniversityMore Details |  | 	Karaikudi	Tamil Nadu	54.45	44
IR-O-U-0564	UPESMore Details |  | 	Dehradun	Uttarakhand	54.44	45
IR-O-U-0447	Bharathiar UniversityMore Details |  | 	Coimbatore	Tamil Nadu	54.42	46
IR-O-U-0107	Jamia HamdardMore Details |  | 	New Delhi	Delhi	54.10	47
IR-O-U-0555	Graphic Era UniversityMore Details |  | 	Dehradun	Uttarakhand	53.92	48
IR-O-U-0295	Datta Meghe Institute of Higher Education and ResearchMore Details |  | 	Wardha	Maharashtra	53.91	49
IR-O-U-0523	King George`s Medical UniversityMore Details |  | 	Lucknow	Uttar Pradesh	53.13	50
IR-O-U-0195	University of JammuMore Details |  | 	Jammu	Jammu and Kashmir	53.01	51
IR-O-N-10	SVKM`s Narsee Monjee Institute of Management StudiesMore Details |  | 	Mumbai	Maharashtra	52.54	52
IR-O-U-0474	Sathyabama Institute of Science and TechnologyMore Details |  | 	Chennai	Tamil Nadu	52.52	53
IR-O-U-0318	Mumbai UniversityMore Details |  | 	Mumbai	Maharashtra	52.48	54
IR-O-U-0014	International Institute of Information Technology HyderabadMore Details |  | 	Hyderabad	Telangana	52.34	55
IR-O-U-0323	Savitribai Phule Pune UniversityMore Details |  | 	Pune	Maharashtra	52.33	56
IR-O-U-0642	Shiv Nadar UniversityMore Details |  | 	Gautam Buddha Nagar	Uttar Pradesh	52.18	57
IR-O-U-0749	Manipal University JaipurMore Details |  | 	Jaipur	Rajasthan	51.57	58
IR-O-I-1361	Bharati VidyapeethMore Details |  | 	Pune	Maharashtra	51.37	59
IR-O-I-1486	Sri Ramachandra Institute of Higher Education and ResearchMore Details |  | 	Chennai	Tamil Nadu	51.35	60
IR-O-U-0451	Chettinad Academy of Research and EducationMore Details |  | 	Kelambakkam, Chengalpattu District	Tamil Nadu	51.06	61
IR-O-U-0223	Jain university,BangaloreMore Details |  | 	Bengaluru	Karnataka	50.95	62
IR-O-U-0217	Christ UniversityMore Details |  | 	Bengaluru	Karnataka	50.92	63
IR-O-U-0381	Punjab Agricultural UniversityMore Details |  | 	Ludhiana	Punjab	50.84	64
IR-O-U-0215	Bangalore UniversityMore Details |  | 	Bengaluru	Karnataka	50.72	65
IR-O-U-0389	Banasthali VidyapithMore Details |  | 	Banasthali	Rajasthan	50.65	66
IR-O-U-0370	Sri Balaji Vidyapeeth Mahatma Gandhi Medical College CampusMore Details |  | 	Puducherry	Pondicherry	50.46	67
IR-O-U-0739	Madan Mohan Malaviya University of TechnologyMore Details |  | 	Gorakhpur	Uttar Pradesh	50.35	68
IR-O-U-0190	Shoolini University of Biotechnology and Management SciencesMore Details |  | 	Solan	Himachal Pradesh	50.16	69
IR-O-U-0043	Vignan's Foundation for Science, Technology and ResearchMore Details |  | 	Guntur	Andhra Pradesh	50.06	70
IR-O-U-0235	Mysore UniversityMore Details |  | 	Mysuru	Karnataka	49.98	71
IR-O-U-0331	Tata Institute of Social SciencesMore Details |  | 	Mumbai	Maharashtra	49.89	72
IR-O-U-0485	Tamil Nadu Agricultural UniversityMore Details |  | 	Coimbatore	Tamil Nadu	49.80	73
IR-O-U-0136	Gujarat UniversityMore Details |  | 	Ahmedabad	Gujarat	49.74	74
IR-O-U-0200	Sher-e-Kashmir University of Agricultural Science and Technology of KashmirMore Details |  | 	Srinagar	Jammu and Kashmir	49.70	75
IR-O-U-0446	Bharath Institute of Higher Education & ResearchMore Details |  | 	Chennai	Tamil Nadu	49.59	76
IR-O-U-0372	Central University of PunjabMore Details |  | 	Bathinda	Punjab	49.53	77
IR-O-U-0373	Chitkara UniversityMore Details |  | 	Rajpura	Punjab	49.32	78
IR-O-U-0056	Tezpur UniversityMore Details |  | 	Tezpur	Assam	49.31	79
IR-O-U-0239	NITTEMore Details |  | 	Mangaluru	Karnataka	49.26	80
IR-O-U-0017	Jawaharlal Nehru Technological UniversityMore Details |  | 	Hyderabad	Telangana	49.22	81
IR-O-U-0345	Mizoram UniversityMore Details |  | 	Aizawl	Mizoram	49.21	82
IR-O-U-0449	Central University of Tamil NaduMore Details |  | 	Tiruvarur	Tamil Nadu	49.13	83
IR-O-U-0003	Acharya Nagarjuna UniversityMore Details |  | 	Guntur	Andhra Pradesh	49.08	84
IR-O-U-0463	Madurai Kamaraj UniversityMore Details |  | 	Madurai	Tamil Nadu	49.07	85
IR-O-U-0168	Maharishi MarkandeshwarMore Details |  | 	Ambala	Haryana	49.07	85
IR-O-U-0541	Sharda UniversityMore Details |  | 	Greater Noida	Uttar Pradesh	49.00	87
IR-O-U-0011	Gandhi Institute of Technology And Management (GITAM)More Details |  | 	Visakhapatnam	Andhra Pradesh	48.79	88
IR-O-U-0392	Central University of RajasthanMore Details |  | 	Kishangarh	Rajasthan	48.47	89
IR-O-U-0384	Sant Longowal Institute of Engineering & TechnologyMore Details |  | 	Longowal	Punjab	48.43	90
IR-O-U-0321	Padmashree Dr. D. Y. Patil Vidyapeeth, MumbaiMore Details |  | 	Mumbai	Maharashtra	48.41	91
IR-O-U-0202	Birla Institute of TechnologyMore Details |  | 	Ranchi	Jharkhand	48.25	92
IR-O-U-0099	Guru Gobind Singh Indraprastha UniversityMore Details |  | 	New Delhi	Delhi	48.01	93
IR-O-U-0470	Periyar UniversityMore Details |  | 	Salem	Tamil Nadu	47.98	94
IR-O-U-0248	University of Agricultural Sciences, BangaloreMore Details |  | 	Bengaluru	Karnataka	47.95	95
IR-O-U-0169	Manav Rachna International Institute of Research & StudiesMore Details |  | 	Faridabad	Haryana	47.30	96
IR-O-U-0050	Assam University-SilcharMore Details |  | 	Silchar	Assam	47.28	97
IR-O-U-0524	University of LucknowMore Details |  | 	Lucknow	Uttar Pradesh	47.26	98
IR-O-U-0444	Avinashilingam Institute for Home Science & Higher Education for WomenMore Details |  | 	Coimbatore	Tamil Nadu	47.09	99
IR-O-U-0105	Indraprastha Institute of Information TechnologyMore Details |  | 	New Delhi	Delhi	47.07	100
"""

def parse_nirf_data(text):
    rankings = []
    lines = text.strip().split('\n')
    for line in lines:
        # Regex to match rank at the end and score before it
        # Format: [Code]? NameMore Details | | City State Score Rank
        match = re.search(r"(.*?)(More Details \|.*?)\s+(\d+\.\d+)\s+(\d+)$", line)
        if match:
            raw_name_part = match.group(1).strip()
            # Remove potential code like IR-O-U-0109
            name = re.sub(r"^[A-Z0-9-]+\s+", "", raw_name_part).strip()
            score = float(match.group(3))
            rank = int(match.group(4))
            rankings.append({"name": name, "score": score, "rank": rank})
    return rankings

def update_nirf():
    rankings = parse_nirf_data(NIRF_DATA)
    print(f"Parsed {len(rankings)} NIRF rankings.")

    # 1. Update JSON
    JSON_FILE = r"c:\Users\DELL\AndroidStudioProjects\PathPiolet\backend\ai_finder_colleges.json"
    if os.path.exists(JSON_FILE):
        with open(JSON_FILE, 'r', encoding='utf-8') as f:
            colleges = json.load(f)
        
        updated_count = 0
        for rank_item in rankings:
            norm_rank_name = rank_item['name'].lower().strip()
            for col in colleges:
                if norm_rank_name in col['name'].lower() or col['name'].lower() in norm_rank_name:
                    col['rank'] = rank_item['rank']
                    col['score'] = rank_item['score']
                    updated_count += 1
                    break
        
        with open(JSON_FILE, 'w', encoding='utf-8') as f:
            json.dump(colleges, f, indent=2)
        print(f"Updated {updated_count} colleges in JSON.")

    # 2. Update Database
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor()
        
        db_updated_count = 0
        # Reset all nirf_rank to 0 first (or a high number) so non-ranked are last
        cursor.execute("UPDATE colleges SET nirf_rank = 0, score = 0")
        
        for rank_item in rankings:
            # Try exact match first, then fuzzy
            cursor.execute("UPDATE colleges SET nirf_rank = %s, score = %s WHERE name = %s", (rank_item['rank'], rank_item['score'], rank_item['name']))
            if cursor.rowcount == 0:
                # Fuzzy match
                cursor.execute("UPDATE colleges SET nirf_rank = %s, score = %s WHERE name LIKE %s", (rank_item['rank'], rank_item['score'], f"%{rank_item['name']}%"))
            
            db_updated_count += cursor.rowcount
        
        conn.commit()
        print(f"Updated {db_updated_count} rows in Database (includes multiple matches if any).")
        conn.close()
    except Exception as e:
        print(f"Error updating database: {e}")

if __name__ == "__main__":
    update_nirf()
