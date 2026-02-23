# PathPilot Backend Setup

This folder contains the Python Flask backend for the PathPilot application.

## Prerequisites

1.  **Python 3.x** installed.
2.  **MySQL Server** installed and running (e.g., via XAMPP or MySQL Workbench).

## Setup Instructions

### 1. Database Setup
1.  Open **MySQL Workbench** or your preferred MySQL client.
2.  Open the `schema.sql` file located in this directory.
3.  Execute the script to create the `pathpilot_db` database and tables.
4.  Verify that tables `users`, `colleges`, `career_options`, and `roadmap_milestones` are created and dummy data is inserted.

### 2. Configure Database Connection
1.  Open `db_config.py`.
2.  Update the `DB_CONFIG` dictionary with your MySQL credentials:
    ```python
    DB_CONFIG = {
        'host': 'localhost',
        'user': 'YOUR_MYSQL_USERNAME', # e.g., 'root'
        'password': 'YOUR_MYSQL_PASSWORD',
        'database': 'pathpilot_db'
    }
    ```

### 3. Install Dependencies
Open a terminal in this `backend` directory and run:
```bash
pip install -r requirements.txt
```

### 4. Run the Server
Run the following command to start the Flask server:
```bash
python app.py
```
The server will start at `http://localhost:5000/`.

## API Endpoints

-   `POST /api/auth/register`
-   `POST /api/auth/login`
-   `GET /api/colleges`
-   `GET /api/careers`
-   `GET /api/roadmap`
