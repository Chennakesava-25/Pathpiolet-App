import mysql.connector
from db_config import DB_CONFIG

def insert_data():
    colleges = [
        ('KL University', 'Vijayawada', 'Andhra Pradesh', 62.50, 44, '₹ 2.5L/yr', '₹ 8 LPA', 'Private, Deemed'),
        ('SRM University AP', 'Vijayawada', 'Andhra Pradesh', 61.20, 48, '₹ 2.8L/yr', '₹ 7.5 LPA', 'Private, Engineering'),
        ('VIT-AP University', 'Vijayawada', 'Andhra Pradesh', 60.80, 52, '₹ 1.9L/yr', '₹ 9 LPA', 'Private, Engineering')
    ]
    
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor()
        for college in colleges:
            cursor.execute(
                "INSERT INTO colleges (name, city, state, score, nirf_rank, fees, avg_package, tags) VALUES (%s, %s, %s, %s, %s, %s, %s, %s)",
                college
            )
        conn.commit()
        print(f"Successfully inserted {len(colleges)} colleges.")
        conn.close()
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    insert_data()
