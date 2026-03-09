import os
import json
import logging
import google.generativeai as genai
from dotenv import load_dotenv

load_dotenv()

# Configure logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Configure Gemini
api_key = os.getenv("GEMINI_API_KEY")
if api_key:
    genai.configure(api_key=api_key)
else:
    logger.warning("GEMINI_API_KEY not found in environment variables.")

def get_ai_recommendations(prefs):
    """
    Generates college recommendations using Gemini.
    Strictly analyzes ALL provided preferences.
    """
    if not api_key:
        logger.error("AI recommendations failed: Missing API Key")
        return get_fallback_recommendations(prefs)

    try:
        model = genai.GenerativeModel('gemini-1.5-flash')
        
        # Enhanced prompt that forces AI to acknowledge all parameters
        prompt = f"""
        You are an expert Indian education consultant. Analyze the user's detailed preferences for engineering/CS colleges:
        
        CRITICAL PARAMETERS TO MATCH:
        1. Location Preference: {prefs.get('location', 'Anywhere in India')}
        2. Yearly Budget: {prefs.get('budget', 'Any')} (Compare strictly against college fees)
        3. College Type: {prefs.get('college_type', 'Any')} (Private, Government, Autonomous)
        4. Hostel Requirement: {prefs.get('hostel', 'Not specified')}
        5. Placement Priority: {prefs.get('placement_priority', 'Medium')} (Focus on average_package)
        6. Campus Size: {prefs.get('campus_size', 'Any')}
        7. Specialization: {prefs.get('specialization', 'Computer Science')}
        8. Entrance Exam & Score: {prefs.get('exam_score', 'Not provided')} (Verify eligibility)

        TASK: Suggest exactly 5 best-matching real colleges in India.
        For each college, you MUST explain in the "reason" field how it satisfies at least 3 of the specific parameters above (e.g., "Matches budget of <2L and offers your preferred CS specialization in <Location>").

        REQUIRED JSON FORMAT (Raw array only):
        [
          {{
            "name": "College Name",
            "location": "City, State",
            "fees": "Approx yearly fees (e.g. ₹ 2L)",
            "average_package": "e.g. ₹ 12 LPA",
            "match_score": "e.g. 95%",
            "website": "Official website URL",
            "reason": "Specific detailed reason matching at least 3 user criteria."
          }}
        ]
        Do NOT wrap in markdown.
        """

        response = model.generate_content(prompt)
        
        if not response.text:
            logger.error("Gemini returned empty response")
            return get_fallback_recommendations(prefs)

        text = response.text.strip()
        # Remove potential markdown formatting
        if text.startswith("```"):
            text = '\n'.join(text.split('\n')[1:-1])
        text = text.strip()

        colleges = json.loads(text)
        return colleges

    except Exception as e:
        logger.error(f"Gemini API Error: {str(e)}")
        return get_fallback_recommendations(prefs)

def get_fallback_recommendations(prefs=None):
    """
    Returns recommendations from the local dataset if AI fails.
    If prefs are provided, it does a simple filtering.
    """
    logger.info("Providing fallback recommendations from local dataset.")
    
    try:
        # Load local dataset
        data_path = os.path.join(os.path.dirname(__file__), 'colleges_data.json')
        with open(data_path, 'r', encoding='utf-8') as f:
            local_dataset = json.load(f)
        
        if not prefs:
            return local_dataset[:5]

        # Simple manual filtering for basic smart fallback
        results = []
        loc_pref = prefs.get('location', '').lower()
        spec_pref = prefs.get('specialization', '').lower()

        for col in local_dataset:
            score = 70 # Base score
            if loc_pref in col['location'].lower(): score += 20
            if any(spec_pref in s.lower() for s in col.get('specializations', [])): score += 10
            
            results.append({
                "name": col['name'],
                "location": col['location'],
                "fees": col['fees'],
                "average_package": col['average_package'],
                "match_score": f"{min(score, 99)}%",
                "website": col.get('website', ''),
                "reason": f"From our verified dataset. Matches your focus on {col['specializations'][0]}."
            })

        # Sort by score and return top 5
        results.sort(key=lambda x: int(x['match_score'].replace('%','')), reverse=True)
        return results[:5]

    except Exception as e:
        logger.error(f"Fallback Error: {str(e)}")
        # Ultimate static fallback
        return [
            {"name": "IIT Madras", "location": "Chennai", "fees": "₹ 2.2L", "average_package": "₹ 21 LPA", "match_score": "95%", "website": "https://www.iitm.ac.in/", "reason": "Top Government Institute."}
        ]
