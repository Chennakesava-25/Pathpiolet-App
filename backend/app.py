from flask import Flask, jsonify, request
from flask_mail import Mail, Message
import mysql.connector
from db_config import DB_CONFIG
from flask_cors import CORS

app = Flask(__name__)
CORS(app) # Enable CORS for all routes

# --- Flask-Mail Configuration ---
# TODO: Replace with your actual SMTP credentials or use environment variables
app.config['MAIL_SERVER'] = 'smtp.gmail.com'
app.config['MAIL_PORT'] = 587
app.config['MAIL_USE_TLS'] = True
app.config['MAIL_USERNAME'] = 'pathpiolet@gmail.com'
app.config['MAIL_PASSWORD'] = 'oawa oukf xatv jpbi'
app.config['MAIL_DEFAULT_SENDER'] = ('PathPilot Support', 'your-email@gmail.com')

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

    if not username or not email or not password:
        return jsonify({"error": "Missing required fields"}), 400

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
        cursor.execute(
            "INSERT INTO users (username, email, password_hash, phone, age) VALUES (%s, %s, %s, %s, %s)",
            (username, email, password, phone, age)
        )
        user_id = cursor.lastrowid
        conn.commit()
        
        return jsonify({
            "message": "User registered successfully",
            "user": {
                "id": user_id,
                "username": username,
                "email": email,
                "phone": phone,
                "age": age
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
        cursor.execute("SELECT * FROM users WHERE email = %s AND password_hash = %s", (email, password))
        user = cursor.fetchone()
        if user:
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
                    "interested_field": user['interested_field']
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
        # Verify current password
        cursor.execute("SELECT * FROM users WHERE id = %s AND password_hash = %s", (user_id, current_password))
        user = cursor.fetchone()
        if not user:
            return jsonify({"error": "Incorrect current password"}), 401

        # Update to new password
        cursor.execute("UPDATE users SET password_hash = %s WHERE id = %s", (new_password, user_id))
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
                "PathPilot: Your Password Reset OTP",
                recipients=[email]
            )
            msg.body = f"Hello,\n\nYou requested to reset your password. Your 6-digit OTP is: {otp}\n\nThis OTP will expire in 10 minutes.\n\nIf you did not request this, please ignore this email."
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

        # Update password
        cursor.execute("UPDATE users SET password_hash = %s WHERE email = %s", (new_password, email))
        
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
    cursor.execute("SELECT * FROM colleges")
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
    data = request.json
    raw_location = data.get('location', '').strip()
    pref_location = normalize_str(raw_location)
    pref_budget = data.get('budget', 500000)
    # Standardized types: 'government', 'private', 'deemed'
    pref_types = [t.lower() for t in data.get('collegeTypes', [])]
    pref_hostel = data.get('hostel', False)
    pref_specializations = [s.lower() for s in data.get('specializations', [])]

    conn = get_db_connection()
    if not conn:
        return jsonify({"error": "Database connection failed"}), 500
    
    cursor = conn.cursor(dictionary=True)
    cursor.execute("SELECT * FROM colleges")
    colleges = cursor.fetchall()
    
    results = []
    
    # Handle global keywords for "all locations"
    global_keywords = ["any", "all", "every", "everywhere", "anywhere"]
    if pref_location in global_keywords:
        pref_location = ""

    # Handle common aliases
    if "bangal" in pref_location: pref_location = "bengaluru"
    if "bombay" in pref_location: pref_location = "mumbai"
    if "madras" in pref_location: pref_location = "chennai"
    if "vijawada" in pref_location or "vijayawada" in pref_location: pref_location = "vijayawada"
    if "vijya" in pref_location: pref_location = "vijayawada"

    for college in colleges:
        score = 0
        norm_city = normalize_str(college.get('city', ''))
        norm_state = normalize_str(college.get('state', ''))
        norm_name = normalize_str(college.get('name', ''))
        college_tags = (college.get('tags', '') or '').lower()
        norm_tags = normalize_str(college_tags)
        
        # 1. Location Matching (Aggressive Substring + Name/Tag Match)
        if pref_location:
            if pref_location in norm_city or pref_location in norm_state or pref_location in norm_name or pref_location in norm_tags:
                score += 50
            else:
                score -= 10
        else:
            score += 10

        # 2. Budget Match (Max 30 points)
        college_fees = parse_fees(college.get('fees', '0'))
        if college_fees <= pref_budget:
            score += 30
        elif college_fees <= pref_budget * 1.5:
            score += 10
            
        # 3. College Type Match (Strict)
        if pref_types:
            type_match = False
            for pt in pref_types:
                # Check tags primarily, then infer from name
                if pt in college_tags or pt in college_name:
                    type_match = True
                    break
            if type_match:
                score += 30
        else:
            score += 10

        # 4. Specialization Match
        if pref_specializations:
            matched_spec = False
            for ps in pref_specializations:
                if ps in college_tags or ps in college_name:
                    matched_spec = True
                    break
            if matched_spec:
                score += 20

        # Standardize for Android robustness
        college['matchScore'] = int(score)
        college['tags'] = (college.get('tags') or '').split(',') if college.get('tags') else []
        college['city'] = college.get('city') or 'Unknown'
        college['state'] = college.get('state') or ''
        college['fees'] = college.get('fees') or '₹ 0'
        college['avg_package'] = college.get('avg_package') or '₹ 0 LPA'
        
        # Filtering: Strictly follow location if provided
        if pref_location:
            if pref_location in norm_city or pref_location in norm_state or pref_location in norm_name or pref_location in norm_tags:
                results.append(college)
        else:
            if score > 40: # General threshold
                 results.append(college)

    # Fallback: if no results found for specific city/state, try high scores or partial name matches
    if pref_location and not results:
        for college in colleges:
            if college['matchScore'] > 60:
                 results.append(college)

    # Sort by score descending, then by nirf_rank
    results.sort(key=lambda x: (-x['matchScore'], x.get('nirf_rank', 999)))
    
    cursor.close()
    conn.close()
    return jsonify(results[:15]) # Return top 15 matches

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
        cursor.execute("SELECT id, username, email, phone, age, education_level, interested_field FROM users WHERE id = %s", (user_id,))
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
    cursor = conn.cursor()
    try:
        cursor.execute("INSERT INTO contact_requests (user_id, subject, message) VALUES (%s, %s, %s)", (user_id, subject, message))
        conn.commit()
        return jsonify({"message": "Contact request saved successfully"}), 201
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    finally:
        cursor.close()
        conn.close()

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
