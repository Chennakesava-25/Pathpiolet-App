import mysql.connector
from db_config import DB_CONFIG

def insert_more_data():
    colleges = [
        # Maharashtra
        ('IIT Bombay', 'Mumbai', 'Maharashtra', 88.50, 2, '₹ 2.2L/yr', '₹ 28 LPA', 'Government, Engineering'),
        ('COEP Technological University', 'Pune', 'Maharashtra', 65.00, 75, '₹ 1.2L/yr', '₹ 10 LPA', 'Government, Engineering'),
        ('VJTI', 'Mumbai', 'Maharashtra', 63.50, 82, '₹ 90K/yr', '₹ 12 LPA', 'Government, State'),
        
        # Karnataka
        ('NITK Surathkal', 'Mangaluru', 'Karnataka', 72.00, 12, '₹ 1.5L/yr', '₹ 18 LPA', 'Government, Engineering'),
        ('RV College of Engineering', 'Bengaluru', 'Karnataka', 64.00, 85, '₹ 2.4L/yr', '₹ 12 LPA', 'Private, Engineering'),
        ('MS Ramaiah Institute', 'Bengaluru', 'Karnataka', 62.00, 95, '₹ 2.5L/yr', '₹ 9 LPA', 'Private, Engineering'),
        
        # Gujarat
        ('IIT Gandhinagar', 'Gandhinagar', 'Gujarat', 68.00, 23, '₹ 2.3L/yr', '₹ 15 LPA', 'Government, Engineering'),
        ('DAIICT', 'Gandhinagar', 'Gujarat', 61.50, 105, '₹ 2.2L/yr', '₹ 14 LPA', 'Private, Engineering'),
        ('Nirma University', 'Ahmedabad', 'Gujarat', 58.00, 120, '₹ 2.1L/yr', '₹ 8 LPA', 'Private, University'),
        
        # Delhi / NCR
        ('IIT Delhi', 'New Delhi', 'Delhi', 87.00, 3, '₹ 2.2L/yr', '₹ 26 LPA', 'Government, Engineering'),
        ('NSUT', 'New Delhi', 'Delhi', 62.00, 90, '₹ 2.1L/yr', '₹ 16 LPA', 'Government, State'),
        ('Shiv Nadar University', 'Greater Noida', 'Uttar Pradesh', 55.00, 130, '₹ 4L/yr', '₹ 9 LPA', 'Private, University'),
        
        # Uttar Pradesh
        ('IIT Kanpur', 'Kanpur', 'Uttar Pradesh', 86.00, 4, '₹ 2.2L/yr', '₹ 24 LPA', 'Government, Engineering'),
        ('HBTI Kanpur', 'Kanpur', 'Uttar Pradesh', 58.00, 140, '₹ 1.3L/yr', '₹ 8 LPA', 'Government, State'),
        ('Jaypee Institute', 'Noida', 'Uttar Pradesh', 56.50, 145, '₹ 2.8L/yr', '₹ 7.5 LPA', 'Private, Deemed'),
        
        # Rajasthan
        ('IIT Jodhpur', 'Jodhpur', 'Rajasthan', 64.00, 30, '₹ 2.2L/yr', '₹ 16 LPA', 'Government, Engineering'),
        ('MNIT Jaipur', 'Jaipur', 'Rajasthan', 62.50, 37, '₹ 1.5L/yr', '₹ 12 LPA', 'Government, Engineering'),
        ('LNMIIT', 'Jaipur', 'Rajasthan', 59.50, 150, '₹ 3.5L/yr', '₹ 14 LPA', 'Private, Deemed'),
        
        # Madhya Pradesh
        ('IIT Indore', 'Indore', 'Madhya Pradesh', 69.50, 14, '₹ 2.2L/yr', '₹ 22 LPA', 'Government, Engineering'),
        ('MANIT Bhopal', 'Bhopal', 'Madhya Pradesh', 61.00, 80, '₹ 1.5L/yr', '₹ 11 LPA', 'Government, Engineering'),
        ('SGSITS', 'Indore', 'Madhya Pradesh', 57.50, 160, '₹ 95K/yr', '₹ 7 LPA', 'Government, State'),
        
        # West Bengal
        ('IIT Kharagpur', 'Kharagpur', 'West Bengal', 85.50, 5, '₹ 2.2L/yr', '₹ 20 LPA', 'Government, Engineering'),
        ('IIEST Shibpur', 'Howrah', 'West Bengal', 63.00, 66, '₹ 1.4L/yr', '₹ 8.5 LPA', 'Government, Engineering'),
        ('Heritage Institute', 'Kolkata', 'West Bengal', 54.00, 170, '₹ 1.1L/yr', '₹ 5 LPA', 'Private, Engineering'),
        
        # Bihar
        ('IIT Patna', 'Patna', 'Bihar', 65.50, 41, '₹ 2.2L/yr', '₹ 15 LPA', 'Government, Engineering'),
        ('NIT Patna', 'Patna', 'Bihar', 59.00, 101, '₹ 1.6L/yr', '₹ 9 LPA', 'Government, Engineering'),
        ('BIT Mesra (Patna)', 'Patna', 'Bihar', 56.00, 180, '₹ 2.8L/yr', '₹ 7 LPA', 'Private, Engineering'),
        
        # Odisha
        ('IIT Bhubaneswar', 'Bhubaneswar', 'Odisha', 63.50, 47, '₹ 2.2L/yr', '₹ 16 LPA', 'Government, Engineering'),
        ('NIT Rourkela', 'Rourkela', 'Odisha', 68.50, 16, '₹ 1.6L/yr', '₹ 12 LPA', 'Government, Engineering'),
        ('ITER Bhubaneswar', 'Bhubaneswar', 'Odisha', 55.00, 190, '₹ 2.5L/yr', '₹ 6 LPA', 'Private, Deemed'),
        
        # Kerala
        ('IIT Palakkad', 'Palakkad', 'Kerala', 58.00, 69, '₹ 2.2L/yr', '₹ 14 LPA', 'Government, Engineering'),
        ('NIT Calicut', 'Calicut', 'Kerala', 66.50, 23, '₹ 1.6L/yr', '₹ 13 LPA', 'Government, Engineering'),
        ('MEC Kochi', 'Kochi', 'Kerala', 55.50, 200, '₹ 40K/yr', '₹ 8 LPA', 'Government, State'),
        
        # Punjab / Chandigarh
        ('IIT Ropar', 'Ropar', 'Punjab', 64.50, 33, '₹ 2.2L/yr', '₹ 18 LPA', 'Government, Engineering'),
        ('PEC Chandigarh', 'Chandigarh', 'Chandigarh', 62.00, 87, '₹ 1.8L/yr', '₹ 12 LPA', 'Government, Deemed'),
        ('GNDU', 'Amritsar', 'Punjab', 56.00, 210, '₹ 1.2L/yr', '₹ 6 LPA', 'Government, State'),
        
        # Assam / Northeast
        ('IIT Guwahati', 'Guwahati', 'Assam', 80.50, 7, '₹ 2.2L/yr', '₹ 25 LPA', 'Government, Engineering'),
        ('NIT Silchar', 'Silchar', 'Assam', 62.00, 40, '₹ 1.5L/yr', '₹ 11 LPA', 'Government, Engineering'),
        ('Tezpur University', 'Tezpur', 'Assam', 58.00, 93, '₹ 45K/yr', '₹ 6 LPA', 'Government, Central'),
        
        # Others
        ('IIT Roorkee', 'Roorkee', 'Uttarakhand', 83.00, 6, '₹ 2.2L/yr', '₹ 23 LPA', 'Government, Engineering'),
        ('IIT Hyderabad', 'Sangareddy', 'Telangana', 75.00, 8, '₹ 2.2L/yr', '₹ 24 LPA', 'Government, Engineering'),
        ('NIT Trichy', 'Tiruchirappalli', 'Tamil Nadu', 78.50, 9, '₹ 1.6L/yr', '₹ 17 LPA', 'Government, Engineering'),
        ('IIT (BHU) Varanasi', 'Varanasi', 'Uttar Pradesh', 72.50, 15, '₹ 2.2L/yr', '₹ 22 LPA', 'Government, Engineering'),
        ('ISM Dhanbad', 'Dhanbad', 'Jharkhand', 69.00, 17, '₹ 2.2L/yr', '₹ 19 LPA', 'Government, Engineering'),
        ('IIT Mandi', 'Mandi', 'Himachal Pradesh', 63.50, 35, '₹ 2.2L/yr', '₹ 16 LPA', 'Government, Engineering'),
        ('IIIT Allahabad', 'Prayagraj', 'Uttar Pradesh', 66.00, 89, '₹ 1.8L/yr', '₹ 25 LPA', 'Government, Engineering'),
        ('IIIT Gwalior', 'Gwalior', 'Madhya Pradesh', 62.50, 95, '₹ 1.7L/yr', '₹ 20 LPA', 'Government, Deemed'),
        ('IIIT Bangalore', 'Bengaluru', 'Karnataka', 68.00, 81, '₹ 3.5L/yr', '₹ 32 LPA', 'Private, Deemed'),
        ('BIT Pilani (Goa)', 'Goa', 'Goa', 66.00, 25, '₹ 5L/yr', '₹ 19 LPA', 'Private, Engineering'),
        ('BIT Pilani (Hyderabad)', 'Hyderabad', 'Telangana', 65.50, 25, '₹ 5L/yr', '₹ 18 LPA', 'Private, Engineering')
    ]
    
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor()
        for college in colleges:
            # Check if college exists (simple name check)
            cursor.execute("SELECT id FROM colleges WHERE name = %s", (college[0],))
            if cursor.fetchone():
                continue
            cursor.execute(
                "INSERT INTO colleges (name, city, state, score, nirf_rank, fees, avg_package, tags) VALUES (%s, %s, %s, %s, %s, %s, %s, %s)",
                college
            )
        conn.commit()
        print(f"Successfully processed {len(colleges)} colleges.")
        conn.close()
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    insert_more_data()
