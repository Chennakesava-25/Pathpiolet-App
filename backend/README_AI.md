# PathPilot AI College Recommendation Backend

This module provides AI-powered college recommendations using the Google Gemini model (gemini-1.5-flash).

## Setup Instructions

1. **Install Dependencies**
   ```bash
   pip install -r requirements.txt
   ```

2. **Configure API Key**
   - Create a `.env` file in the `backend` directory (copy from `.env.example`).
   - Add your Google Gemini API key:
     ```
     GEMINI_API_KEY=your_actual_api_key_here
     ```
   - You can get a free API key from [Google AI Studio](https://aistudio.google.com/).

3. **Run the Server**
   ```bash
   python app.py
   ```

## API Usage

- **Endpoint**: `POST /api/ai/recommend`
- **Body**:
  ```json
  {
    "location": "Tamil Nadu",
    "budget": "2L-5L",
    "college_type": "Private",
    "hostel": "Yes",
    "placement_priority": "High",
    "campus_size": "Large",
    "specialization": "Computer Science",
    "exam_score": "JEE 95 percentile"
  }
  ```

## Features

- **Gemini 1.5 Flash**: Fast and accurate recommendations.
- **JSON Response**: Structured data ready for the Android app.
- **Fallback Logic**: Provides top-tier sample colleges if the AI service is unavailable.
- **CORS Enabled**: Accessible from the Android environment.
- **Response Logging**: Tracks processing time for performance monitoring.
