CREATE DATABASE IF NOT EXISTS pathpilot_db;
USE pathpilot_db;

-- Disable foreign key checks to allow dropping tables in any order
SET FOREIGN_KEY_CHECKS = 0;

-- Standardize environment by dropping existing tables
DROP TABLE IF EXISTS user_saved_colleges;
DROP TABLE IF EXISTS user_otps;
DROP TABLE IF EXISTS user_events;
DROP TABLE IF EXISTS login_logs;
DROP TABLE IF EXISTS roadmap_milestones;
DROP TABLE IF EXISTS career_options;
DROP TABLE IF EXISTS colleges;
DROP TABLE IF EXISTS users;

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    age INT,
    education_level VARCHAR(100),
    interested_field VARCHAR(100),
    profile_picture VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Login Logs Table
CREATE TABLE IF NOT EXISTS login_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    email VARCHAR(100),
    login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Colleges Table
CREATE TABLE IF NOT EXISTS colleges (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    city VARCHAR(100),
    state VARCHAR(100),
    score DOUBLE,
    nirf_rank INT,
    fees VARCHAR(100),
    avg_package VARCHAR(100),
    description TEXT,
    tags TEXT
);

-- Career Options Table
CREATE TABLE IF NOT EXISTS career_options (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    average_salary VARCHAR(50)
);

-- Roadmap Milestones Table
CREATE TABLE IF NOT EXISTS roadmap_milestones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    step_number INT,
    title VARCHAR(100),
    description TEXT,
    status ENUM('locked', 'active', 'completed') DEFAULT 'locked'
);

-- User Events Table
CREATE TABLE IF NOT EXISTS user_events (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    event_date DATE NOT NULL,
    time VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- User OTPs Table
CREATE TABLE IF NOT EXISTS user_otps (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    otp_code VARCHAR(6) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP DEFAULT (CURRENT_TIMESTAMP + INTERVAL 10 MINUTE),
    verified BOOLEAN DEFAULT FALSE
);

-- User Saved Colleges Table
CREATE TABLE IF NOT EXISTS user_saved_colleges (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    college_id INT NOT NULL,
    saved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (college_id) REFERENCES colleges(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_college (user_id, college_id)
);

-- Temporary Registrations Table
CREATE TABLE IF NOT EXISTS temp_registrations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    age INT,
    otp_code VARCHAR(6) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP DEFAULT (CURRENT_TIMESTAMP + INTERVAL 15 MINUTE)
);

-- User Feedback Table
CREATE TABLE IF NOT EXISTS user_feedback (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- User Ratings Table
CREATE TABLE IF NOT EXISTS user_ratings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    rating INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Contact Requests Table
CREATE TABLE IF NOT EXISTS contact_requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    subject VARCHAR(255),
    message TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert 100 Colleges
INSERT INTO colleges (name, city, state, score, nirf_rank, fees, avg_package, description, tags) VALUES 
('Indian Institute of Science', 'Bengaluru', 'Karnataka', 85.05, 1, '₹ 3L/yr', '₹ 25 LPA', 'The Indian Institute of Science (IISc) is a public, deemed, research university for higher education and research in science, engineering, design, and management. It is located in Bengaluru, in the Indian city of Karnataka.', 'Government, Research'),
('Jawaharlal Nehru University', 'New Delhi', 'Delhi', 71.00, 2, '₹ 20K/yr', '₹ 12 LPA', 'Jawaharlal Nehru University is a public research university located in New Delhi, India. It was established in 1969 and named after Jawaharlal Nehru, India s first Prime Minister.', 'Government, Arts'),
('Manipal Academy of Higher Education', 'Manipal', 'Karnataka', 69.25, 3, '₹ 4.5L/yr', '₹ 8 LPA', 'Manipal Academy of Higher Education (MAHE) is a private deemed university located in Manipal, India. The university also has campuses in Mangalore, Bangalore, Jamshedpur, Malacca and Dubai.', 'Private, Engineering'),
('Jamia Millia Islamia', 'New Delhi', 'Delhi', 69.10, 4, '₹ 35K/yr', '₹ 9 LPA', 'Jamia Millia Islamia is a central university located in New Delhi, India. Originally established at Aligarh, United Provinces, India during the British Raj in 1920.', 'Government, Minority'),
('University of Delhi', 'Delhi', 'Delhi', 67.38, 5, '₹ 25K/yr', '₹ 10 LPA', 'The University of Delhi, informally known as Delhi University (DU), is a collegiate central university located in New Delhi, India. It was founded in 1922 by an Act of the Central Legislative Assembly.', 'Government, Central'),
('Banaras Hindu University', 'Varanasi', 'Uttar Pradesh', 67.28, 6, '₹ 30K/yr', '₹ 11 LPA', 'Banaras Hindu University (BHU), formerly Central Hindu College, is a central university located in Varanasi, Uttar Pradesh. It was established in 1916.', 'Government, Central'),
('BITS Pilani', 'Pilani', 'Rajasthan', 67.24, 7, '₹ 5.5L/yr', '₹ 18 LPA', 'Private, Engineering'),
('Amrita Vishwa Vidyapeetham', 'Coimbatore', 'Tamil Nadu', 67.05, 8, '₹ 3.5L/yr', '₹ 7.5 LPA', 'Private, Engineering'),
('Jadavpur University', 'Kolkata', 'West Bengal', 65.42, 9, '₹ 15K/yr', '₹ 14 LPA', 'Government, State'),
('Aligarh Muslim University', 'Aligarh', 'Uttar Pradesh', 65.35, 10, '₹ 2.5L/yr', '₹ 6.5 LPA', 'Government, Central'),
('SRM Institute of Science and Technology', 'Chennai', 'Tamil Nadu', 65.26, 11, '₹ 4L/yr', '₹ 7 LPA', 'Private, Deemed'),
('Homi Bhabha National Institute', 'Mumbai', 'Maharashtra', 65.08, 12, '₹ 50K/yr', '₹ 10 LPA', 'Government, Research'),
('Saveetha Institute', 'Chennai', 'Tamil Nadu', 65.04, 13, '₹ 3.2L/yr', '₹ 5.5 LPA', 'Private, Deemed'),
('Vellore Institute of Technology', 'Vellore', 'Tamil Nadu', 64.64, 14, '₹ 2L/yr', '₹ 9 LPA', 'Private, Engineering'),
('Siksha O Anusandhan', 'Bhubaneswar', 'Odisha', 63.14, 15, '₹ 2.8L/yr', '₹ 6 LPA', 'Private, Deemed'),
('Indian Agricultural Research Institute', 'New Delhi', 'Delhi', 62.89, 16, '₹ 40K/yr', '₹ 7 LPA', 'Government, Agriculture'),
('KIIT University', 'Bhubaneswar', 'Odisha', 62.87, 17, '₹ 3.5L/yr', '₹ 6.5 LPA', 'Private, Deemed'),
('University of Hyderabad', 'Hyderabad', 'Telangana', 61.83, 18, '₹ 45K/yr', '₹ 9.5 LPA', 'Government, Central'),
('Chandigarh University', 'Mohali', 'Punjab', 61.27, 19, '₹ 1.8L/yr', '₹ 8 LPA', 'Private, Engineering'),
('Anna University', 'Chennai', 'Tamil Nadu', 61.22, 20, '₹ 55K/yr', '₹ 7.5 LPA', 'Government, State'),
('JSS Academy', 'Mysuru', 'Karnataka', 60.00, 21, '₹ 2.2L/yr', '₹ 5 LPA', 'Private, Deemed'),
('Amity University', 'Noida', 'Uttar Pradesh', 59.68, 22, '₹ 3.8L/yr', '₹ 6 LPA', 'Private, University'),
('Andhra University', 'Visakhapatnam', 'Andhra Pradesh', 59.20, 23, '₹ 40K/yr', '₹ 4.5 LPA', 'Government, State'),
('Symbiosis International', 'Pune', 'Maharashtra', 59.16, 24, '₹ 4.2L/yr', '₹ 11 LPA', 'Private, Deemed'),
('Kerala University', 'Thiruvananthapuram', 'Kerala', 59.05, 25, '₹ 35K/yr', '₹ 5 LPA', 'Government, State'),
('Thapar Institute', 'Patiala', 'Punjab', 58.87, 26, '₹ 4L/yr', '₹ 10 LPA', 'Private, Deemed'),
('Koneru Lakshmaiah', 'Vaddeswaram', 'Andhra Pradesh', 58.87, 26, '₹ 3L/yr', '₹ 7 LPA', 'Private, Deemed'),
('Kalasalingam Academy', 'Krishnan Koil', 'Tamil Nadu', 58.37, 28, '₹ 1.5L/yr', '₹ 5 LPA', 'Private, Deemed'),
('SASTRA', 'Thanjavur', 'Tamil Nadu', 58.06, 29, '₹ 2.5L/yr', '₹ 6.5 LPA', 'Private, Deemed'),
('Osmania University', 'Hyderabad', 'Telangana', 57.94, 30, '₹ 30K/yr', '₹ 6 LPA', 'Government, State'),
('Lovely Professional University', 'Phagwara', 'Punjab', 57.77, 31, '₹ 2.4L/yr', '₹ 6 LPA', 'Private, University'),
('Cochin University', 'Cochin', 'Kerala', 57.55, 32, '₹ 60K/yr', '₹ 5.5 LPA', 'Government, State'),
('Gauhati University', 'Guwahati', 'Assam', 57.37, 33, '₹ 40K/yr', '₹ 4.5 LPA', 'Government, State'),
('University of Kashmir', 'Srinagar', 'J&K', 57.13, 34, '₹ 50K/yr', '₹ 5 LPA', 'Government, State'),
('Panjab University', 'Chandigarh', 'Chandigarh', 56.88, 35, '₹ 45K/yr', '₹ 7 LPA', 'Government, State'),
('Bharathidasan University', 'Tiruchirappalli', 'Tamil Nadu', 55.85, 36, '₹ 35K/yr', '₹ 4 LPA', 'Government, State'),
('BBAU', 'Lucknow', 'Uttar Pradesh', 55.55, 37, '₹ 40K/yr', '₹ 5 LPA', 'Government, Central'),
('University of Madras', 'Chennai', 'Tamil Nadu', 55.23, 38, '₹ 50K/yr', '₹ 6 LPA', 'Government, State'),
('Calcutta University', 'Kolkata', 'West Bengal', 55.17, 39, '₹ 20K/yr', '₹ 5.5 LPA', 'Government, State'),
('Institute of Chemical Technology', 'Mumbai', 'Maharashtra', 55.13, 40, '₹ 1.2L/yr', '₹ 9 LPA', 'Government, Deemed'),
('Dr. D. Y. Patil Vidyapeeth', 'Pune', 'Maharashtra', 54.90, 41, '₹ 3.5L/yr', '₹ 6 LPA', 'Private, Deemed'),
('Delhi Technological University', 'New Delhi', 'Delhi', 54.88, 42, '₹ 2.1L/yr', '₹ 15 LPA', 'Government, State'),
('Mahatma Gandhi University', 'Kottayam', 'Kerala', 54.61, 43, '₹ 45K/yr', '₹ 5 LPA', 'Government, State'),
('Alagappa University', 'Karaikudi', 'Tamil Nadu', 54.45, 44, '₹ 30K/yr', '₹ 4.5 LPA', 'Government, State'),
('UPES', 'Dehradun', 'Uttarakhand', 54.44, 45, '₹ 4.5L/yr', '₹ 7 LPA', 'Private, University'),
('Bharathiar University', 'Coimbatore', 'Tamil Nadu', 54.42, 46, '₹ 40K/yr', '₹ 5 LPA', 'Government, State'),
('Jamia Hamdard', 'New Delhi', 'Delhi', 54.10, 47, '₹ 1.5L/yr', '₹ 6 LPA', 'Private, Deemed'),
('Graphic Era University', 'Dehradun', 'Uttarakhand', 53.92, 48, '₹ 3L/yr', '₹ 6.5 LPA', 'Private, Deemed'),
('Datta Meghe Institute', 'Wardha', 'Maharashtra', 53.91, 49, '₹ 2.5L/yr', '₹ 5 LPA', 'Private, Deemed'),
('King Georges Medical University', 'Lucknow', 'Uttar Pradesh', 53.13, 50, '₹ 1.5L/yr', '₹ 8 LPA', 'Government, State'),
('University of Jammu', 'Jammu', 'J&K', 53.01, 51, '₹ 45K/yr', '₹ 4.5 LPA', 'Government, State'),
('Narsee Monjee Institute', 'Mumbai', 'Maharashtra', 52.54, 52, '₹ 4.5L/yr', '₹ 12 LPA', 'Private, Deemed'),
('Sathyabama Institute', 'Chennai', 'Tamil Nadu', 52.52, 53, '₹ 3.5L/yr', '₹ 5 LPA', 'Private, Deemed'),
('Mumbai University', 'Mumbai', 'Maharashtra', 52.48, 54, '₹ 40K/yr', '₹ 7 LPA', 'Government, State'),
('IIIT Hyderabad', 'Hyderabad', 'Telangana', 52.34, 55, '₹ 3.6L/yr', '₹ 30 LPA', 'Private, Engineering'),
('Savitribai Phule Pune University', 'Pune', 'Maharashtra', 52.33, 56, '₹ 50K/yr', '₹ 8 LPA', 'Government, State'),
('Shiv Nadar University', 'Gautam Buddha Nagar', 'Uttar Pradesh', 52.18, 57, '₹ 4L/yr', '₹ 9 LPA', 'Private, University'),
('Manipal University Jaipur', 'Jaipur', 'Rajasthan', 51.57, 58, '₹ 3.8L/yr', '₹ 7 LPA', 'Private, University'),
('Bharati Vidyapeeth', 'Pune', 'Maharashtra', 51.37, 59, '₹ 2.5L/yr', '₹ 6 LPA', 'Private, Deemed'),
('Sri Ramachandra Institute', 'Chennai', 'Tamil Nadu', 51.35, 60, '₹ 4.5L/yr', '₹ 6 LPA', 'Private, Deemed'),
('Chettinad Academy', 'Kelambakkam', 'Tamil Nadu', 51.06, 61, '₹ 3.5L/yr', '₹ 5.5 LPA', 'Private, Deemed'),
('Jain University', 'Bengaluru', 'Karnataka', 50.95, 62, '₹ 2.5L/yr', '₹ 7 LPA', 'Private, Deemed'),
('Christ University', 'Bengaluru', 'Karnataka', 50.92, 63, '₹ 2.5L/yr', '₹ 8 LPA', 'Private, Deemed'),
('Punjab Agricultural University', 'Ludhiana', 'Punjab', 50.84, 64, '₹ 60K/yr', '₹ 6 LPA', 'Government, Agriculture'),
('Bangalore University', 'Bengaluru', 'Karnataka', 50.72, 65, '₹ 45K/yr', '₹ 6 LPA', 'Government, State'),
('Banasthali Vidyapith', 'Banasthali', 'Rajasthan', 50.65, 66, '₹ 2.8L/yr', '₹ 5 LPA', 'Private, Women'),
('Sri Balaji Vidyapeeth', 'Puducherry', 'Pondicherry', 50.46, 67, '₹ 4L/yr', '₹ 6 LPA', 'Private, Deemed'),
('Madan Mohan Malaviya University', 'Gorakhpur', 'Uttar Pradesh', 50.35, 68, '₹ 1.2L/yr', '₹ 5.5 LPA', 'Government, State'),
('Shoolini University', 'Solan', 'Himachal Pradesh', 50.16, 69, '₹ 2.5L/yr', '₹ 6 LPA', 'Private, University'),
('Vignans Foundation', 'Guntur', 'Andhra Pradesh', 50.06, 70, '₹ 2.2L/yr', '₹ 5 LPA', 'Private, Deemed'),
('Mysore University', 'Mysuru', 'Karnataka', 49.98, 71, '₹ 40K/yr', '₹ 5.5 LPA', 'Government, State'),
('Tata Institute', 'Mumbai', 'Maharashtra', 49.89, 72, '₹ 1.5L/yr', '₹ 10 LPA', 'Government, Deemed'),
('Tamil Nadu Agricultural University', 'Coimbatore', 'Tamil Nadu', 49.80, 73, '₹ 50K/yr', '₹ 5 LPA', 'Government, Agriculture'),
('Gujarat University', 'Ahmedabad', 'Gujarat', 49.74, 74, '₹ 35K/yr', '₹ 5.5 LPA', 'Government, State'),
('Sher-e-Kashmir University', 'Srinagar', 'J&K', 49.70, 75, '₹ 45K/yr', '₹ 4.5 LPA', 'Government, Agriculture'),
('Bharath Institute', 'Chennai', 'Tamil Nadu', 49.59, 76, '₹ 2.5L/yr', '₹ 4.5 LPA', 'Private, Deemed'),
('Central University of Punjab', 'Bathinda', 'Punjab', 49.53, 77, '₹ 35K/yr', '₹ 5 LPA', 'Government, Central'),
('Chitkara University', 'Rajpura', 'Punjab', 49.32, 78, '₹ 2L/yr', '₹ 6.5 LPA', 'Private, University'),
('Tezpur University', 'Tezpur', 'Assam', 49.31, 79, '₹ 45K/yr', '₹ 5 LPA', 'Government, Central'),
('NITTE', 'Mangaluru', 'Karnataka', 49.26, 80, '₹ 3L/yr', '₹ 5 LPA', 'Private, Deemed'),
('JNTU', 'Hyderabad', 'Telangana', 49.22, 81, '₹ 60K/yr', '₹ 6.5 LPA', 'Government, State'),
('Mizoram University', 'Aizawl', 'Mizoram', 49.21, 82, '₹ 40K/yr', '₹ 4 LPA', 'Government, Central'),
('Central University of Tamil Nadu', 'Tiruvarur', 'Tamil Nadu', 49.13, 83, '₹ 35K/yr', '₹ 4.5 LPA', 'Government, Central'),
('Acharya Nagarjuna University', 'Guntur', 'Andhra Pradesh', 49.08, 84, '₹ 30K/yr', '₹ 4 LPA', 'Government, State'),
('Madurai Kamaraj University', 'Madurai', 'Tamil Nadu', 49.07, 85, '₹ 35K/yr', '₹ 5 LPA', 'Government, State'),
('Maharishi Markandeshwar', 'Ambala', 'Haryana', 49.07, 85, '₹ 2.5L/yr', '₹ 5 LPA', 'Private, Deemed'),
('Sharda University', 'Greater Noida', 'Uttar Pradesh', 49.00, 87, '₹ 2.2L/yr', '₹ 5 LPA', 'Private, University'),
('GITAM', 'Visakhapatnam', 'Andhra Pradesh', 48.79, 88, '₹ 3.5L/yr', '₹ 6 LPA', 'Private, Deemed'),
('Central University of Rajasthan', 'Kishangarh', 'Rajasthan', 48.47, 89, '₹ 40K/yr', '₹ 5 LPA', 'Government, Central'),
('Sant Longowal Institute', 'Longowal', 'Punjab', 48.43, 90, '₹ 80K/yr', '₹ 5.5 LPA', 'Government, Deemed'),
('Padmashree Dr. D. Y. Patil', 'Mumbai', 'Maharashtra', 48.41, 91, '₹ 3.2L/yr', '₹ 6 LPA', 'Private, Deemed'),
('Birla Institute of Technology', 'Ranchi', 'Jharkhand', 48.25, 92, '₹ 3L/yr', '₹ 10 LPA', 'Private, Deemed'),
('Guru Gobind Singh Indraprastha University', 'New Delhi', 'Delhi', 48.01, 93, '₹ 1.2L/yr', '₹ 10 LPA', 'Government, State'),
('Periyar University', 'Salem', 'Tamil Nadu', 47.98, 94, '₹ 30K/yr', '₹ 4 LPA', 'Government, State'),
('University of Agricultural Sciences', 'Bengaluru', 'Karnataka', 47.95, 95, '₹ 50K/yr', '₹ 5 LPA', 'Government, Agriculture'),
('Manav Rachna Institute', 'Faridabad', 'Haryana', 47.30, 96, '₹ 2.5L/yr', '₹ 5 LPA', 'Private, Deemed'),
('Assam University', 'Silchar', 'Assam', 47.28, 97, '₹ 40K/yr', '₹ 4.5 LPA', 'Government, Central'),
('University of Lucknow', 'Lucknow', 'Uttar Pradesh', 47.26, 98, '₹ 35K/yr', '₹ 5 LPA', 'Government, State'),
('Avinashilingam Institute', 'Coimbatore', 'Tamil Nadu', 47.09, 99, '₹ 30K/yr', '₹ 4 LPA', 'Government, Deemed'),
('IIIT Delhi', 'New Delhi', 'Delhi', 47.07, 100, '₹ 4L/yr', '₹ 20 LPA', 'Government, State'),
('KL University', 'Vijayawada', 'Andhra Pradesh', 62.50, 44, '₹ 2.5L/yr', '₹ 8 LPA', 'Private, Deemed'),
('SRM University AP', 'Vijayawada', 'Andhra Pradesh', 61.20, 48, '₹ 2.8L/yr', '₹ 7.5 LPA', 'Private, Engineering'),
('VIT-AP University', 'Vijayawada', 'Andhra Pradesh', 60.80, 52, '₹ 1.9L/yr', '₹ 9 LPA', 'Private, Engineering'),
('IIT Bombay', 'Mumbai', 'Maharashtra', 88.50, 2, '₹ 2.2L/yr', '₹ 28 LPA', 'Government, Engineering'),
('COEP Technological University', 'Pune', 'Maharashtra', 65.00, 75, '₹ 1.2L/yr', '₹ 10 LPA', 'Government, Engineering'),
('VJTI', 'Mumbai', 'Maharashtra', 63.50, 82, '₹ 90K/yr', '₹ 12 LPA', 'Government, State'),
('NITK Surathkal', 'Mangaluru', 'Karnataka', 72.00, 12, '₹ 1.5L/yr', '₹ 18 LPA', 'Government, Engineering'),
('RV College of Engineering', 'Bengaluru', 'Karnataka', 64.00, 85, '₹ 2.4L/yr', '₹ 12 LPA', 'Private, Engineering'),
('MS Ramaiah Institute', 'Bengaluru', 'Karnataka', 62.00, 95, '₹ 2.5L/yr', '₹ 9 LPA', 'Private, Engineering'),
('IIT Gandhinagar', 'Gandhinagar', 'Gujarat', 68.00, 23, '₹ 2.3L/yr', '₹ 15 LPA', 'Government, Engineering'),
('DAIICT', 'Gandhinagar', 'Gujarat', 61.50, 105, '₹ 2.2L/yr', '₹ 14 LPA', 'Private, Engineering'),
('Nirma University', 'Ahmedabad', 'Gujarat', 58.00, 120, '₹ 2.1L/yr', '₹ 8 LPA', 'Private, University'),
('IIT Delhi', 'New Delhi', 'Delhi', 87.00, 3, '₹ 2.2L/yr', '₹ 26 LPA', 'Government, Engineering'),
('NSUT', 'New Delhi', 'Delhi', 62.00, 90, '₹ 2.1L/yr', '₹ 16 LPA', 'Government, State'),
('Shiv Nadar University', 'Greater Noida', 'Uttar Pradesh', 55.00, 130, '₹ 4L/yr', '₹ 9 LPA', 'Private, University'),
('IIT Kanpur', 'Kanpur', 'Uttar Pradesh', 86.00, 4, '₹ 2.2L/yr', '₹ 24 LPA', 'Government, Engineering'),
('HBTI Kanpur', 'Kanpur', 'Uttar Pradesh', 58.00, 140, '₹ 1.3L/yr', '₹ 8 LPA', 'Government, State'),
('Jaypee Institute', 'Noida', 'Uttar Pradesh', 56.50, 145, '₹ 2.8L/yr', '₹ 7.5 LPA', 'Private, Deemed'),
('IIT Jodhpur', 'Jodhpur', 'Rajasthan', 64.00, 30, '₹ 2.2L/yr', '₹ 16 LPA', 'Government, Engineering'),
('MNIT Jaipur', 'Jaipur', 'Rajasthan', 62.50, 37, '₹ 1.5L/yr', '₹ 12 LPA', 'Government, Engineering'),
('LNMIIT', 'Jaipur', 'Rajasthan', 59.50, 150, '₹ 3.5L/yr', '₹ 14 LPA', 'Private, Deemed'),
('IIT Indore', 'Indore', 'Madhya Pradesh', 69.50, 14, '₹ 2.2L/yr', '₹ 22 LPA', 'Government, Engineering'),
('MANIT Bhopal', 'Bhopal', 'Madhya Pradesh', 61.00, 80, '₹ 1.5L/yr', '₹ 11 LPA', 'Government, Engineering'),
('SGSITS', 'Indore', 'Madhya Pradesh', 57.50, 160, '₹ 95K/yr', '₹ 7 LPA', 'Government, State'),
('IIT Kharagpur', 'Kharagpur', 'West Bengal', 85.50, 5, '₹ 2.2L/yr', '₹ 20 LPA', 'Government, Engineering'),
('IIEST Shibpur', 'Howrah', 'West Bengal', 63.00, 66, '₹ 1.4L/yr', '₹ 8.5 LPA', 'Government, Engineering'),
('Heritage Institute', 'Kolkata', 'West Bengal', 54.00, 170, '₹ 1.1L/yr', '₹ 5 LPA', 'Private, Engineering'),
('IIT Patna', 'Patna', 'Bihar', 65.50, 41, '₹ 2.2L/yr', '₹ 15 LPA', 'Government, Engineering'),
('NIT Patna', 'Patna', 'Bihar', 59.00, 101, '₹ 1.6L/yr', '₹ 9 LPA', 'Government, Engineering'),
('BIT Mesra (Patna)', 'Patna', 'Bihar', 56.00, 180, '₹ 2.8L/yr', '₹ 7 LPA', 'Private, Engineering'),
('IIT Bhubaneswar', 'Bhubaneswar', 'Odisha', 63.50, 47, '₹ 2.2L/yr', '₹ 16 LPA', 'Government, Engineering'),
('NIT Rourkela', 'Rourkela', 'Odisha', 68.50, 16, '₹ 1.6L/yr', '₹ 12 LPA', 'Government, Engineering'),
('ITER Bhubaneswar', 'Bhubaneswar', 'Odisha', 55.00, 190, '₹ 2.5L/yr', '₹ 6 LPA', 'Private, Deemed'),
('IIT Palakkad', 'Palakkad', 'Kerala', 58.00, 69, '₹ 2.2L/yr', '₹ 14 LPA', 'Government, Engineering'),
('NIT Calicut', 'Calicut', 'Kerala', 66.50, 23, '₹ 1.6L/yr', '₹ 13 LPA', 'Government, Engineering'),
('MEC Kochi', 'Kochi', 'Kerala', 55.50, 200, '₹ 40K/yr', '₹ 8 LPA', 'Government, State'),
('IIT Ropar', 'Ropar', 'Punjab', 64.50, 33, '₹ 2.2L/yr', '₹ 18 LPA', 'Government, Engineering'),
('PEC Chandigarh', 'Chandigarh', 'Chandigarh', 62.00, 87, '₹ 1.8L/yr', '₹ 12 LPA', 'Government, Deemed'),
('GNDU', 'Amritsar', 'Punjab', 56.00, 210, '₹ 1.2L/yr', '₹ 6 LPA', 'Government, State'),
('IIT Guwahati', 'Guwahati', 'Assam', 80.50, 7, '₹ 2.2L/yr', '₹ 25 LPA', 'Government, Engineering'),
('NIT Silchar', 'Silchar', 'Assam', 62.00, 40, '₹ 1.5L/yr', '₹ 11 LPA', 'Government, Engineering'),
('IIT Roorkee', 'Roorkee', 'Uttarakhand', 83.00, 6, '₹ 2.2L/yr', '₹ 23 LPA', 'Government, Engineering'),
('IIT (BHU) Varanasi', 'Varanasi', 'Uttar Pradesh', 72.50, 15, '₹ 2.2L/yr', '₹ 22 LPA', 'Government, Engineering'),
('ISM Dhanbad', 'Dhanbad', 'Jharkhand', 69.00, 17, '₹ 2.2L/yr', '₹ 19 LPA', 'Government, Engineering'),
('IIT Mandi', 'Mandi', 'Himachal Pradesh', 63.50, 35, '₹ 2.2L/yr', '₹ 16 LPA', 'Government, Engineering'),
('IIIT Allahabad', 'Prayagraj', 'Uttar Pradesh', 66.00, 89, '₹ 1.8L/yr', '₹ 25 LPA', 'Government, Engineering'),
('IIIT Gwalior', 'Gwalior', 'Madhya Pradesh', 62.50, 95, '₹ 1.7L/yr', '₹ 20 LPA', 'Government, Deemed'),
('IIIT Bangalore', 'Bengaluru', 'Karnataka', 68.00, 81, '₹ 3.5L/yr', '₹ 32 LPA', 'Private, Deemed'),
('BIT Pilani (Goa)', 'Goa', 'Goa', 66.00, 25, '₹ 5L/yr', '₹ 19 LPA', 'Private, Engineering'),
('BIT Pilani (Hyderabad)', 'Hyderabad', 'Telangana', 65.50, 25, '₹ 5L/yr', '₹ 18 LPA', 'Private, Engineering');

-- Insert Dummy Data for Career Options
INSERT INTO career_options (title, description, average_salary) VALUES
('Software Engineer', 'Develops software solutions.', '8-12 LPA'),
('Data Scientist', 'Analyzes complex data.', '10-15 LPA'),
('AI Researcher', 'Researches artificial intelligence.', '12-20 LPA');

-- Insert Dummy Data for Roadmap
INSERT INTO roadmap_milestones (step_number, title, description, status) VALUES
(1, '10th Standard', 'Complete secondary education.', 'completed'),
(2, '12th Standard (MPC)', 'Focus on Maths, Physics, Chemistry.', 'active'),
(3, 'B.Tech / B.Sc', 'Undergraduate degree.', 'locked'),
(4, 'Masters', 'Postgraduate specialization.', 'locked'),
(5, 'PhD', 'Doctorate research.', 'locked');

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;
