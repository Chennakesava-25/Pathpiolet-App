from flask import Flask, jsonify, request, render_template_string, send_from_directory
from flask_mail import Mail, Message
from werkzeug.security import generate_password_hash, check_password_hash
import os
import json
import uuid
from werkzeug.utils import secure_filename
import mysql.connector
from db_config import DB_CONFIG
from flask_cors import CORS
import time
from ai_service import get_ai_recommendations
from dotenv import load_dotenv

load_dotenv()

app = Flask(__name__)
CORS(app) # Enable CORS for all routes

# --- Upload Configuration ---
UPLOAD_FOLDER = os.path.join(os.getcwd(), 'uploads', 'profile_pictures')
os.makedirs(UPLOAD_FOLDER, exist_ok=True)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
app.config['MAX_CONTENT_LENGTH'] = 16 * 1024 * 1024 # 16 MB limit

# --- Flask-Mail Configuration ---
# TODO: Replace with your actual SMTP credentials or use environment variables
app.config['MAIL_SERVER'] = 'smtp.gmail.com'
app.config['MAIL_PORT'] = 587
app.config['MAIL_USE_TLS'] = True
app.config['MAIL_USERNAME'] = 'pathpiolet@gmail.com'
app.config['MAIL_PASSWORD'] = 'oawa oukf xatv jpbi'
app.config['MAIL_DEFAULT_SENDER'] = ('PathPilot Support', 'pathpiolet@gmail.com')

mail = Mail(app)

def get_db_connection():
    try:
        connection = mysql.connector.connect(**DB_CONFIG)
        return connection
    except mysql.connector.Error as err:
        print(f"Error connecting to database: {err}")
        return None

@app.route('/')
def home():
    return jsonify({"message": "Welcome to PathPilot Backend API"})

# --- Authentication Routes ---

@app.route('/api/auth/register', methods=['POST'])
def register():
    data = request.json
    username = data.get('username')
    email = data.get('email')
    password = data.get('password')
    phone = data.get('phone')
    age = data.get('age')

    if not username or not email or not password or not age:
        return jsonify({"error": "Missing required fields include age"}), 400

    try:
        age_int = int(age)
        if age_int <= 15:
            return jsonify({"error": "Age must be greater than 15"}), 400
    except ValueError:
        return jsonify({"error": "Invalid age format"}), 400

    # Strict Validations
    if not email.endswith('@gmail.com'):
        return jsonify({"error": "Only @gmail.com addresses are allowed"}), 400
    
    if phone and (len(phone) != 10 or not phone.isdigit()):
        return jsonify({"error": "Phone number must be exactly 10 digits"}), 400
    
    if len(password) < 8:
        return jsonify({"error": "Password must be at least 8 characters long"}), 400
    
    if not any(char.isupper() for char in password):
        return jsonify({"error": "Password must contain at least one uppercase letter"}), 400
    
    if not any(char.isdigit() for char in password):
        return jsonify({"error": "Password must contain at least one number"}), 400

    conn = get_db_connection()
    if not conn:
        return jsonify({"error": "Database connection failed"}), 500

    cursor = conn.cursor(dictionary=True)
    try:
        # Check if user already exists in users table
        cursor.execute("SELECT id FROM users WHERE email = %s", (email,))
        if cursor.fetchone():
            return jsonify({"error": "Email already registered"}), 400

        # Generate 6-digit OTP
        import random
        otp = str(random.randint(100000, 999999))

        # Hash the password before storing
        hashed_password = generate_password_hash(password)

        # Save to temp_registrations
        cursor.execute(
            "INSERT INTO temp_registrations (username, email, password_hash, phone, age, otp_code) VALUES (%s, %s, %s, %s, %s, %s)",
            (username, email, hashed_password, phone, age, otp)
        )
        conn.commit()

        # Send OTP Email
        try:
            msg = Message(
                "Verify Your PathPilot Account",
                recipients=[email]
            )
            msg.body = f"""Hello {username},

Thank you for choosing PathPilot! To complete your registration, please use the following One-Time Password (OTP):

OTP: {otp}

This code is valid for 15 minutes. If you did not initiate this request, please ignore this email.

Best regards,
The PathPilot Team"""
            mail.send(msg)
            print(f"INFO: Signup OTP {otp} sent successfully to {email}")
            return jsonify({"message": "OTP sent successfully to your email"}), 200
        except Exception as e:
            print(f"ERROR Sending Signup Email: {str(e)}")
            return jsonify({
                "message": "OTP generated but email failed to send. Check backend console.",
                "debug_otp": otp,
                "error": str(e)
            }), 200 # Return 200 for simulated dev environment
    except mysql.connector.Error as err:
        return jsonify({"error": str(err)}), 400
    finally:
        cursor.close()
        conn.close()

@app.route('/api/auth/verify-signup', methods=['POST'])
def verify_signup():
    data = request.json
    email = data.get('email')
    otp = data.get('otp')

    if not email or not otp:
        return jsonify({"error": "Email and OTP are required"}), 400

    conn = get_db_connection()
    if not conn:
        return jsonify({"error": "Database connection failed"}), 500
    
    cursor = conn.cursor(dictionary=True)
    try:
        # Check for valid, non-expired OTP in temp_registrations
        cursor.execute(
            "SELECT * FROM temp_registrations WHERE email = %s AND otp_code = %s AND expires_at > CURRENT_TIMESTAMP ORDER BY created_at DESC LIMIT 1",
            (email, otp)
        )
        temp_user = cursor.fetchone()

        if not temp_user:
            return jsonify({"error": "Invalid or expired OTP"}), 400

        # Create actual user
        cursor.execute(
            "INSERT INTO users (username, email, password_hash, phone, age) VALUES (%s, %s, %s, %s, %s)",
            (temp_user['username'], temp_user['email'], temp_user['password_hash'], temp_user['phone'], temp_user['age'])
        )
        user_id = cursor.lastrowid
        
        # Clean up temp registration
        cursor.execute("DELETE FROM temp_registrations WHERE email = %s", (email,))
        
        conn.commit()

        return jsonify({
            "message": "Registration successful",
            "user": {
                "id": user_id,
                "username": temp_user['username'],
                "email": temp_user['email'],
                "phone": temp_user['phone'],
                "age": temp_user['age']
            }
        }), 201
    except mysql.connector.Error as err:
        return jsonify({"error": str(err)}), 400
    finally:
        cursor.close()
        conn.close()

@app.route('/api/auth/login', methods=['POST'])
def login():
    data = request.json
    email = data.get('email')
    password = data.get('password')

    conn = get_db_connection()
    if not conn:
        return jsonify({"error": "Database connection failed"}), 500

    cursor = conn.cursor(dictionary=True)
    try:
        cursor.execute("SELECT * FROM users WHERE email = %s", (email,))
        user = cursor.fetchone()
        if user and check_password_hash(user['password_hash'], password):
            # Record the login in the logs table
            cursor.execute(
                "INSERT INTO login_logs (user_id, email) VALUES (%s, %s)",
                (user['id'], user['email'])
            )
            conn.commit()

            return jsonify({
                "message": "Login successful",
                "user": {
                    "id": user['id'],
                    "username": user['username'],
                    "email": user['email'],
                    "phone": user['phone'],
                    "age": user['age'],
                    "education_level": user['education_level'],
                    "interested_field": user['interested_field'],
                    "profile_picture": user.get('profile_picture')
                }
            }), 200
        else:
            return jsonify({"error": "Invalid credentials"}), 401
    finally:
        cursor.close()
        conn.close()

@app.route('/api/auth/change-password', methods=['POST'])
def change_password():
    data = request.json
    user_id = data.get('user_id')
    current_password = data.get('current_password')
    new_password = data.get('new_password')

    if not user_id or not current_password or not new_password:
        return jsonify({"error": "Missing required fields"}), 400

    conn = get_db_connection()
    if not conn:
        return jsonify({"error": "Database connection failed"}), 500

    cursor = conn.cursor(dictionary=True)
    try:
        # Verify current password using hash comparison
        cursor.execute("SELECT * FROM users WHERE id = %s", (user_id,))
        user = cursor.fetchone()
        if not user or not check_password_hash(user['password_hash'], current_password):
            return jsonify({"error": "Incorrect current password"}), 401

        # Hash and update new password
        cursor.execute("UPDATE users SET password_hash = %s WHERE id = %s", (generate_password_hash(new_password), user_id))
        conn.commit()

        return jsonify({"message": "Password changed successfully"}), 200
    finally:
        cursor.close()
        conn.close()

@app.route('/api/auth/forgot-password', methods=['POST'])
def forgot_password():
    data = request.json
    email = data.get('email')

    if not email:
        return jsonify({"error": "Email is required"}), 400

    conn = get_db_connection()
    if not conn:
        return jsonify({"error": "Database connection failed"}), 500
    
    cursor = conn.cursor(dictionary=True)
    try:
        # Check if user exists
        cursor.execute("SELECT id FROM users WHERE email = %s", (email,))
        user = cursor.fetchone()
        if not user:
            return jsonify({"error": "No account found with this email"}), 404

        # Generate 6-digit OTP
        import random
        otp = str(random.randint(100000, 999999))

        # Save OTP to database
        cursor.execute(
            "INSERT INTO user_otps (email, otp_code) VALUES (%s, %s)",
            (email, otp)
        )
        conn.commit()

        # Send Real Email
        try:
            # Check if credentials are still placeholders
            if app.config['MAIL_USERNAME'] == 'your-email@gmail.com' or app.config['MAIL_PASSWORD'] == 'your-app-password':
                print(f"DEBUG: Mock sending OTP {otp} to {email} (SMTP not configured)")
                return jsonify({
                    "message": "OTP generated (Simulated). Check backend console.",
                    "debug_otp": otp
                }), 200
            
            msg = Message(
                "Reset Your PathPilot Password",
                recipients=[email]
            )
            msg.body = f"""Hello,

You requested to reset your password for PathPilot. Please use the following One-Time Password (OTP) to proceed:

OTP: {otp}

This code is valid for 10 minutes. If you did not request a password reset, please ignore this email.

Best regards,
The PathPilot Team"""
            mail.send(msg)
            print(f"INFO: OTP {otp} sent successfully to {email}")
            return jsonify({"message": "OTP sent successfully to your email"}), 200
        except Exception as e:
            # Fallback for debugging if SMTP fails
            print(f"ERROR Sending Email: {str(e)}")
            return jsonify({
                "message": "OTP generated but email failed to send. Check console.",
                "debug_otp": otp,
                "error": str(e)
            }), 500
    finally:
        cursor.close()
        conn.close()

@app.route('/api/auth/verify-otp', methods=['POST'])
def verify_otp():
    data = request.json
    email = data.get('email')
    otp = data.get('otp')

    if not email or not otp:
        return jsonify({"error": "Email and OTP are required"}), 400

    conn = get_db_connection()
    if not conn:
        return jsonify({"error": "Database connection failed"}), 500
    
    cursor = conn.cursor(dictionary=True)
    try:
        # Check for valid, non-expired, and unverified OTP
        cursor.execute(
            "SELECT * FROM user_otps WHERE email = %s AND otp_code = %s AND verified = FALSE AND expires_at > CURRENT_TIMESTAMP ORDER BY created_at DESC LIMIT 1",
            (email, otp)
        )
        otp_record = cursor.fetchone()

        if otp_record:
            # Mark as verified
            cursor.execute("UPDATE user_otps SET verified = TRUE WHERE id = %s", (otp_record['id'],))
            conn.commit()
            return jsonify({"message": "OTP verified successfully"}), 200
        else:
            return jsonify({"error": "Invalid or expired OTP"}), 400
    finally:
        cursor.close()
        conn.close()

@app.route('/api/auth/reset-password', methods=['POST'])
def reset_password():
    data = request.json
    email = data.get('email')
    new_password = data.get('password')

    if not email or not new_password:
        return jsonify({"error": "Email and new password are required"}), 400

    if len(new_password) < 8:
        return jsonify({"error": "Password must be at least 8 characters long"}), 400
    
    if not any(char.isupper() for char in new_password):
        return jsonify({"error": "Password must contain at least one uppercase letter"}), 400
    
    if not any(char.isdigit() for char in new_password):
        return jsonify({"error": "Password must contain at least one number"}), 400

    conn = get_db_connection()
    if not conn:
        return jsonify({"error": "Database connection failed"}), 500
    
    cursor = conn.cursor(dictionary=True)
    try:
        # Check if email has a verified OTP
        cursor.execute(
            "SELECT * FROM user_otps WHERE email = %s AND verified = TRUE ORDER BY created_at DESC LIMIT 1",
            (email,)
        )
        verified_otp = cursor.fetchone()

        if not verified_otp:
            return jsonify({"error": "OTP verification required"}), 403

        # Hash new password before saving
        cursor.execute("UPDATE users SET password_hash = %s WHERE email = %s", (generate_password_hash(new_password), email))
        
        # Clean up verified OTPs for this email after use
        cursor.execute("DELETE FROM user_otps WHERE email = %s", (email,))
        
        conn.commit()
        return jsonify({"message": "Password reset successful"}), 200
    finally:
        cursor.close()
        conn.close()

# --- Content Routes ---

@app.route('/api/colleges', methods=['GET'])
def get_colleges():
    conn = get_db_connection()
    if not conn:
        return jsonify({"error": "Database connection failed"}), 500
    
    cursor = conn.cursor(dictionary=True)
    # Sort by NIRF Rank (non-zero first, then ascending), then by score
    cursor.execute("""
        SELECT * FROM colleges 
        ORDER BY (CASE WHEN nirf_rank IS NULL OR nirf_rank = 0 THEN 999999 ELSE nirf_rank END) ASC, 
                 score DESC, 
                 name ASC
    """)
    colleges = cursor.fetchall()
    
    # Format tags for Android robustness (expecting List<String>)
    for college in colleges:
        tags_raw = college.get('tags')
        if tags_raw:
            # Split comma-separated string into a trimmed list
            college['tags'] = [tag.strip() for tag in tags_raw.split(',') if tag.strip()]
        else:
            college['tags'] = []

    cursor.close()
    conn.close()
    return jsonify(colleges)

def parse_fees(fee_str):
    """Utility to parse fee strings like '₹ 3L/yr' or '₹ 35K/yr' into numeric values."""
    if not fee_str: return 0
    try:
        clean_str = fee_str.replace('₹', '').replace('/yr', '').strip().upper()
        if 'L' in clean_str:
            return float(clean_str.replace('L', '')) * 100000
        if 'K' in clean_str:
            return float(clean_str.replace('K', '')) * 1000
        return float(clean_str)
    except:
        return 0

def normalize_str(s):
    """Aggressive normalization for locations: lowercase and remove all non-alphanumeric chars."""
    if not s: return ""
    import re
    return re.sub(r'[^a-zA-Z0-9]', '', s.lower())

@app.route('/api/recommendations', methods=['POST'])
def get_recommendations():
    data = request.json or {}
    pref_location = normalize_str(data.get('location', ''))
    pref_budget = data.get('budget', 500000)
    pref_types = [t.lower() for t in data.get('collegeTypes', [])]
    pref_hostel = data.get('hostel', False)
    pref_placement = data.get('placementPriority', 'Medium').lower()
    pref_size = data.get('campusSize', '').lower()
    pref_exam = normalize_str(data.get('examScore', ''))
    pref_specs = [normalize_str(s) for s in data.get('specializations', [])]

    # Load dataset
    json_path = os.path.join(os.path.dirname(__file__), 'ai_finder_colleges.json')
    if not os.path.exists(json_path):
        return jsonify({"error": "AI Finder dataset missing"}), 404
        
    with open(json_path, 'r', encoding='utf-8') as f:
        colleges = json.load(f)
        
    results = []
    for college in colleges:
        score = 0
        
        # 1. Location Matching (Strict if provided)
        c_loc = normalize_str(college.get('location', ''))
        c_name = normalize_str(college.get('name', ''))
        if pref_location:
            if pref_location in c_loc or pref_location in c_name:
                score += 50
            else:
                # STRICT FILTER: Skip if location is provided but doesn't match
                continue
        else:
            score += 10

        # ... rest of scoring remains same ...
        # 2. Budget Matching
        c_fees = parse_fees(college.get('fees', '0'))
        if c_fees > 0:
            if c_fees <= pref_budget:
                score += 30
            elif c_fees <= pref_budget * 1.5:
                score += 10
        else:
            # Fallback to budget_range if fees parsing fails
            c_range = college.get('budget_range', '').lower()
            if pref_budget <= 100000 and c_range == 'low': score += 30
            elif pref_budget > 100000 and c_range == 'high': score += 30

        # 3. College Type Matching
        if pref_types:
            c_type = college.get('type', '').lower()
            if any(pt in c_type for pt in pref_types):
                score += 30
        
        # 4. Hostel Matching
        c_hostel = str(college.get('hostel', '')).lower()
        if pref_hostel and ('yes' in c_hostel or 'true' in c_hostel):
            score += 20
            
        # 5. Placement Matching
        c_placement = college.get('placement_priority', 'Medium').lower()
        if pref_placement == c_placement:
            score += 15
            
        # 6. Campus Size Matching
        if pref_size:
            c_size = college.get('campus_size', '').lower()
            if pref_size in c_size:
                score += 10
                
        # 7. Specialization Matching
        if pref_specs:
            c_specs = [normalize_str(s) for s in college.get('specializations', [])]
            if any(ps in cs or cs == 'allbranches' for ps in pref_specs for cs in c_specs):
                score += 20

        # Android compatibility mapping
        college['tags'] = college.get('specializations', [])
        college['matchScore'] = int(score)
        
        results.append(college)

    # Sort by score descending
    results.sort(key=lambda x: x['matchScore'], reverse=True)
    
    return jsonify(results[:20]) # Return top 20 matches

@app.route('/api/ai_finder', methods=['POST'])
def ai_finder_search():
    try:
        data = request.json or {}
        # Parse user requirements
        req_location = normalize_str(data.get('location', ''))
        req_budget_range = data.get('budgetRange', '').lower()
        req_type = data.get('collegeType', '').lower()
        req_hostel = str(data.get('hostel', '')).lower()
        try:
            req_placement = int(data.get('avgPackageLpa', 0))
        except:
            req_placement = 0
            
        req_size = data.get('campusSize', '').lower()
        req_exam = normalize_str(data.get('entranceExam', ''))
        req_specs = [normalize_str(s) for s in data.get('specializations', [])]

        # Load standard json dataset
        json_path = os.path.join(os.path.dirname(__file__), 'ai_finder_colleges.json')
        if not os.path.exists(json_path):
            return jsonify({"error": "AI Finder dataset missing"}), 404
            
        with open(json_path, 'r', encoding='utf-8') as f:
            colleges = json.load(f)
            
        results = []
        for college in colleges:
            score = 0
            
            # Match rules
            c_loc = normalize_str(college.get('location', ''))
            c_budget = college.get('budget_range', '').lower()
            c_type = college.get('type', '').lower()
            c_hostel = str(college.get('hostel', '')).lower()
            
            try:
                c_package = int(''.join(filter(str.isdigit, str(college.get('average_package', '0')))))
            except:
                c_package = 0
                
            c_size = college.get('campus_size', '').lower()
            c_exam = normalize_str(college.get('cutoff_exam', ''))
            c_specs = [normalize_str(s) for s in college.get('specializations', [])]

            # Rule Base: Score additions based on user provided constraints
            if req_location:
                if req_location in c_loc:
                    score += 20
                else:
                    # STRICT FILTER: Skip if location provided but doesn't match
                    continue
            
            if req_budget_range and req_budget_range == c_budget: score += 15
            if req_type and req_type in c_type: score += 15
            if req_hostel and (req_hostel == 'yes' or req_hostel == 'true') and 'yes' in c_hostel: score += 10
            if req_placement > 0 and c_package >= req_placement: score += 15
            if req_size and req_size == c_size: score += 5
            if req_exam and req_exam in c_exam: score += 10
            
            if req_specs:
                spec_match = False
                for rs in req_specs:
                    for cs in c_specs:
                        if rs in cs or cs == 'allbranches':
                            spec_match = True
                            break
                    if spec_match: break
                if spec_match: score += 10
            
            # NIRF Rank Boost: Give a boost to ranked colleges
            c_rank = college.get('rank', 0)
            if c_rank > 0:
                # Rank 1 gets more boost than rank 100
                boost = max(1, 20 - (c_rank // 10)) 
                score += boost
            
            # Base match constraint (e.g at least 20 score)
            if score >= 20 or not any([req_location, req_budget_range, req_type, req_hostel, req_placement > 0, req_size, req_exam, req_specs]):
                college['matchScore'] = score
                results.append(college)
                
        # Sort best matches first
        results.sort(key=lambda x: x['matchScore'], reverse=True)
        return jsonify(results[:20]) # Top 20 relevant rules
        
    except Exception as e:
        logger.error(f"AI Finder Rule Base Error: {str(e)}")
        return jsonify({"error": str(e)}), 500

@app.route('/api/careers', methods=['GET'])
def get_careers():
    conn = get_db_connection()
    if not conn:
        return jsonify({"error": "Database connection failed"}), 500
    
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM career_options")
    careers = cursor.fetchall()
    cursor.close()
    conn.close()
    return jsonify(careers)

@app.route('/api/roadmap', methods=['GET'])
def get_roadmap():
    conn = get_db_connection()
    if not conn:
        return jsonify({"error": "Database connection failed"}), 500
    
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM roadmap_milestones ORDER BY step_number")
    steps = cursor.fetchall()
    cursor.close()
    conn.close()
    return jsonify(steps)

@app.route('/api/auth/profile/<int:user_id>', methods=['GET'])
def get_profile(user_id):
    conn = get_db_connection()
    if not conn:
        return jsonify({"error": "Database connection failed"}), 500
    
    cursor = conn.cursor(dictionary=True)
    try:
        cursor.execute("SELECT id, username, email, phone, age, education_level, interested_field, profile_picture FROM users WHERE id = %s", (user_id,))
        user = cursor.fetchone()
        if user:
            return jsonify(user), 200
        else:
            return jsonify({"error": "User not found"}), 404
    finally:
        cursor.close()
        conn.close()

@app.route('/api/auth/profile/<int:user_id>', methods=['PUT'])
def update_profile(user_id):
    data = request.json
    username = data.get('username')
    phone = data.get('phone')
    age = data.get('age')
    education_level = data.get('education_level')
    interested_field = data.get('interested_field')

    conn = get_db_connection()
    if not conn:
        return jsonify({"error": "Database connection failed"}), 500
    
    cursor = conn.cursor()
    try:
        cursor.execute(
            "UPDATE users SET username = %s, phone = %s, age = %s, education_level = %s, interested_field = %s WHERE id = %s",
            (username, phone, age, education_level, interested_field, user_id)
        )
        conn.commit()
        return jsonify({"message": "Profile updated successfully"}), 200
    except mysql.connector.Error as err:
        return jsonify({"error": str(err)}), 400
    finally:
        cursor.close()
        conn.close()

@app.route('/api/auth/profile/<int:user_id>/photo', methods=['POST'])
def upload_profile_photo(user_id):
    if 'photo' not in request.files:
        return jsonify({"error": "No file part"}), 400
    file = request.files['photo']
    if file.filename == '':
        return jsonify({"error": "No selected file"}), 400
    
    if file:
        # Generate unique filename
        ext = file.filename.rsplit('.', 1)[1].lower() if '.' in file.filename else 'jpg'
        filename = secure_filename(f"{user_id}_{uuid.uuid4().hex}.{ext}")
        filepath = os.path.join(app.config['UPLOAD_FOLDER'], filename)
        
        # Determine URL
        file_url = f"/uploads/profile_pictures/{filename}"
        
        try:
            file.save(filepath)
            
            # Update database
            conn = get_db_connection()
            if not conn:
                return jsonify({"error": "Database connection failed"}), 500
            
            cursor = conn.cursor()
            cursor.execute("UPDATE users SET profile_picture = %s WHERE id = %s", (file_url, user_id))
            conn.commit()
            
            return jsonify({
                "message": "Profile picture updated successfully",
                "profile_picture": file_url
            }), 200
        except Exception as e:
            return jsonify({"error": str(e)}), 500
        finally:
            if 'cursor' in locals():
                cursor.close()
            if 'conn' in locals() and conn:
                conn.close()

@app.route('/api/auth/profile/<int:user_id>/photo', methods=['DELETE'])
def delete_profile_photo(user_id):
    conn = get_db_connection()
    if not conn:
        return jsonify({"error": "Database connection failed"}), 500
    
    cursor = conn.cursor(dictionary=True)
    try:
        # Get current photo path
        cursor.execute("SELECT profile_picture FROM users WHERE id = %s", (user_id,))
        user = cursor.fetchone()
        if not user:
            return jsonify({"error": "User not found"}), 404
        
        photo_path = user.get('profile_picture')
        if photo_path:
            # Delete file from disk
            full_path = os.path.join(app.root_path, photo_path.lstrip('/'))
            if os.path.exists(full_path):
                os.remove(full_path)
        
        # Update database
        cursor.execute("UPDATE users SET profile_picture = NULL WHERE id = %s", (user_id,))
        conn.commit()
        
        return jsonify({"message": "Profile picture removed successfully"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    finally:
        cursor.close()
        conn.close()

@app.route('/uploads/profile_pictures/<filename>')
def serve_profile_picture(filename):
    return send_from_directory(app.config['UPLOAD_FOLDER'], filename)

# --- Calendar Event Routes ---

@app.route('/api/events/<int:user_id>', methods=['GET'])
def get_events(user_id):
    conn = get_db_connection()
    if not conn:
        return jsonify({"error": "Database connection failed"}), 500
    
    cursor = conn.cursor(dictionary=True)
    try:
        cursor.execute("SELECT * FROM user_events WHERE user_id = %s ORDER BY event_date", (user_id,))
        events = cursor.fetchall()
        # Convert date objects to strings for JSON serialization
        for event in events:
            event['event_date'] = event['event_date'].strftime('%Y-%m-%d')
        return jsonify(events), 200
    finally:
        cursor.close()
        conn.close()

@app.route('/api/events', methods=['POST'])
def add_event():
    data = request.json
    user_id = data.get('user_id')
    title = data.get('title')
    description = data.get('description')
    event_date = data.get('event_date')
    time = data.get('time')

    if not user_id or not title or not event_date:
        return jsonify({"error": "Missing required fields"}), 400

    conn = get_db_connection()
    if not conn:
        return jsonify({"error": "Database connection failed"}), 500
    
    cursor = conn.cursor()
    try:
        cursor.execute(
            "INSERT INTO user_events (user_id, title, description, event_date, time) VALUES (%s, %s, %s, %s, %s)",
            (user_id, title, description, event_date, time)
        )
        conn.commit()
        return jsonify({"message": "Event added successfully", "id": cursor.lastrowid}), 201
    except mysql.connector.Error as err:
        return jsonify({"error": str(err)}), 400
    finally:
        cursor.close()
        conn.close()

@app.route('/api/events/<int:event_id>', methods=['DELETE'])
def delete_event(event_id):
    conn = get_db_connection()
    if not conn:
        return jsonify({"error": "Database connection failed"}), 500
    
    cursor = conn.cursor()
    try:
        cursor.execute("DELETE FROM user_events WHERE id = %s", (event_id,))
        conn.commit()
        return jsonify({"message": "Event deleted successfully"}), 200
    finally:
        cursor.close()
        conn.close()

# --- Saved Colleges & Activity History ---

@app.route('/api/colleges/save', methods=['POST'])
def save_college():
    data = request.json
    user_id = data.get('user_id')
    college_id = data.get('college_id')

    if not user_id or not college_id:
        return jsonify({"error": "Missing user_id or college_id"}), 400

    conn = get_db_connection()
    if not conn:
        return jsonify({"error": "Database connection failed"}), 500
    
    cursor = conn.cursor()
    try:
        cursor.execute(
            "INSERT IGNORE INTO user_saved_colleges (user_id, college_id) VALUES (%s, %s)",
            (user_id, college_id)
        )
        conn.commit()
        return jsonify({"message": "College saved successfully"}), 201
    except mysql.connector.Error as err:
        return jsonify({"error": str(err)}), 400
    finally:
        cursor.close()
        conn.close()

@app.route('/api/colleges/save/<int:user_id>/<int:college_id>', methods=['DELETE'])
def unsave_college(user_id, college_id):
    conn = get_db_connection()
    if not conn:
        return jsonify({"error": "Database connection failed"}), 500
    
    cursor = conn.cursor()
    try:
        cursor.execute(
            "DELETE FROM user_saved_colleges WHERE user_id = %s AND college_id = %s",
            (user_id, college_id)
        )
        conn.commit()
        return jsonify({"message": "College unsaved successfully"}), 200
    except mysql.connector.Error as err:
        return jsonify({"error": str(err)}), 400
    finally:
        cursor.close()
        conn.close()

@app.route('/api/activity/<int:user_id>', methods=['GET'])
def get_activity_history(user_id):
    conn = get_db_connection()
    if not conn:
        return jsonify({"error": "Database connection failed"}), 500
    
    cursor = conn.cursor(dictionary=True)
    try:
        # Get Saved Colleges
        cursor.execute("""
            SELECT c.name as title, CONCAT(c.city, ', ', c.state) as subtitle, 
                   usc.saved_at as time, 'College' as type
            FROM colleges c
            JOIN user_saved_colleges usc ON c.id = usc.college_id
            WHERE usc.user_id = %s
        """, (user_id,))
        saved_colleges = cursor.fetchall()

        # Get Calendar Events
        cursor.execute("""
            SELECT title, description as subtitle, 
                   created_at as time, 'Calendar' as type
            FROM user_events
            WHERE user_id = %s
        """, (user_id,))
        calendar_events = cursor.fetchall()

        # Combine and sort by time desc
        all_activity = saved_colleges + calendar_events
        
        # Convert times to string and humanize (simple version)
        for item in all_activity:
            item['time'] = item['time'].strftime('%Y-%m-%d %H:%M:%S')

        return jsonify(all_activity), 200
    finally:
        cursor.close()
        conn.close()

@app.route('/api/feedback', methods=['POST'])
def save_feedback():
    data = request.json
    user_id = data.get('user_id')
    message = data.get('message')
    if not user_id or not message:
        return jsonify({"error": "Missing user_id or message"}), 400
    
    conn = get_db_connection()
    if not conn: return jsonify({"error": "Database error"}), 500
    cursor = conn.cursor()
    try:
        cursor.execute("INSERT INTO user_feedback (user_id, message) VALUES (%s, %s)", (user_id, message))
        conn.commit()
        return jsonify({"message": "Feedback saved successfully"}), 201
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    finally:
        cursor.close()
        conn.close()

@app.route('/api/rate', methods=['POST'])
def save_rating():
    data = request.json
    user_id = data.get('user_id')
    rating = data.get('rating')
    if not user_id or rating is None:
        return jsonify({"error": "Missing user_id or rating"}), 400
    
    conn = get_db_connection()
    if not conn: return jsonify({"error": "Database error"}), 500
    cursor = conn.cursor()
    try:
        cursor.execute("INSERT INTO user_ratings (user_id, rating) VALUES (%s, %s)", (user_id, rating))
        conn.commit()
        return jsonify({"message": "Rating saved successfully"}), 201
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    finally:
        cursor.close()
        conn.close()

@app.route('/api/contact', methods=['POST'])
def save_contact_request():
    data = request.json
    user_id = data.get('user_id')
    subject = data.get('subject', 'General Inquiry')
    message = data.get('message')
    if not user_id or not message:
        return jsonify({"error": "Missing user_id or message"}), 400
    
    conn = get_db_connection()
    if not conn: return jsonify({"error": "Database error"}), 500
    cursor = conn.cursor(dictionary=True)
    try:
        # Fetch user details for the email
        cursor.execute("SELECT username, email FROM users WHERE id = %s", (user_id,))
        user = cursor.fetchone()
        
        # Save to database
        cursor.execute("INSERT INTO contact_requests (user_id, subject, message) VALUES (%s, %s, %s)", (user_id, subject, message))
        conn.commit()

        # Send Email Notification
        if user:
            try:
                msg = Message(
                    subject=f"Contact Us: {subject}",
                    recipients=['pathpiolet@gmail.com'],
                    body=f"""
New Contact Us Message received:

From: {user['username']} ({user['email']})
Subject: {subject}

Message:
{message}

---
This is an automated notification from PathPilot Backend.
"""
                )
                mail.send(msg)
            except Exception as mail_err:
                print(f"Error sending contact email: {mail_err}")
                # We still return 201 because the request was saved to DB

        return jsonify({"message": "Contact request saved and notification sent"}), 201
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    finally:
        cursor.close()
        conn.close()

# --- AI Recommendation Routes ---

@app.route('/api/ai/recommend', methods=['POST'])
def recommend_colleges():
    """
    Endpoint to get AI-powered college recommendations.
    """
    start_time = time.time()
    data = request.json
    
    if not data:
        return jsonify({"success": False, "error": "Missing request body"}), 400

    # Required fields for better AI results
    required_fields = ['location', 'budget', 'college_type', 'specialization']
    missing = [field for field in required_fields if not data.get(field)]
    
    if missing:
        # We don't block, but we log or potentially return error if strictness is needed
        # For now, let's just log and proceed as AI can handle some empty fields
        print(f"Notice: Missing preference fields: {missing}")

    try:
        colleges = get_ai_recommendations(data)
        response_time = round((time.time() - start_time) * 1000, 2)
        
        return jsonify({
            "success": True,
            "colleges": colleges,
            "response_time_ms": response_time
        })
    except Exception as e:
        return jsonify({
            "success": False, 
            "error": "Internal server error occurred while processing recommendations",
            "details": str(e)
        }), 500

@app.route('/api/ai/recommend/test_ui', methods=['GET'])
def test_recommendation_ui():
    """
    A simple UI to test and view recommendations in a browser.
    """
    # Sample default preferences
    prefs = {
        "location": request.args.get('location', 'Tamil Nadu'),
        "budget": request.args.get('budget', '2L-5L'),
        "college_type": request.args.get('type', 'Private'),
        "specialization": request.args.get('spec', 'Computer Science'),
        "hostel": "Yes",
        "placement_priority": "High"
    }
    
    colleges = get_ai_recommendations(prefs)
    
    html_template = """
    <!DOCTYPE html>
    <html>
    <head>
        <title>Pathpiolet AI Recommendations</title>
        <style>
            body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #F5F9FF; color: #1A3B8B; padding: 40px; }
            .container { max-width: 800px; margin: 0 auto; }
            h1 { color: #4D6FFF; margin-bottom: 30px; }
            .prefs { background: white; padding: 20px; border-radius: 12px; margin-bottom: 30px; box-shadow: 0 4px 6px rgba(0,0,0,0.05); }
            .college { background: white; padding: 25px; border-radius: 16px; margin-bottom: 20px; box-shadow: 0 4px 6px rgba(0,0,0,0.05); border-left: 6px solid #4D6FFF; }
            .college h2 { margin-top: 0; color: #1A3B8B; }
            .meta { display: flex; gap: 20px; color: #7C86A2; font-size: 0.9em; margin: 10px 0; }
            .score { color: #27AE60; font-weight: bold; }
            .reason { margin-top: 15px; font-style: italic; color: #4D6FFF; background: #E8F0FF; padding: 15px; border-radius: 8px; }
        </style>
    </head>
    <body>
        <div class="container">
            <h1>PathPilot AI Results</h1>
            <div class="prefs">
                <strong>Testing with:</strong> {{ prefs.location }} | {{ prefs.budget }} | {{ prefs.spec }}
            </div>
            
            {% for college in colleges %}
            <div class="college">
                <h2>{{ college.name }} <span class="score">({{ college.match_score }} Match)</span></h2>
                <div class="meta">
                    <span>📍 {{ college.location }}</span>
                    <span>💰 {{ college.fees }}</span>
                    <span>🚀 Avg: {{ college.average_package }}</span>
                </div>
                <div class="reason">
                    {{ college.reason }}
                </div>
            </div>
            {% endfor %}
            
            <p><small>Note: This is a testing view. For the Android app, use the JSON endpoint at <code>/api/ai/recommend</code></small></p>
        </div>
    </body>
    </html>
    """
    return render_template_string(html_template, colleges=colleges, prefs=prefs)

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5000)
