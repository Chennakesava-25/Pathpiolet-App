
import json
import os

data = """
 Indian Institute of Science Bengalur Karnatak South High ₹2,50,000 Private/D Yes Medium 6 Large JEE/Stat All Branc
 Jawaharlal Nehru University New Delh Delhi North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Manipal Academy of Higher Education Manipal Karnatak South High ₹2,50,000 Private Yes Medium 6 Large JEE/Stat All Branc
 Jamia Millia Islamia New Delh Delhi North High ₹2,50,000 Private Yes Medium 6 Large JEE/Stat All Branc
 University of Delhi Delhi Delhi North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Banaras Hindu University Varanasi Uttar Pra North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 BITS Pilani Pilani Rajastha North High ₹2,50,000 Private Yes Medium 6 Large JEE/Stat All Branc
 Amrita Vishwa Vidyapeetham Coimbato Tamil Na South High ₹2,50,000 Private Yes Medium 6 Large JEE/Stat All Branc
 Jadavpur University Kolkata West Ben East Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Aligarh Muslim University Aligarh Uttar Pra North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 SRM Institute of Science and Technology Chennai Tamil Na South High ₹2,50,000 Private/D Yes Medium 6 Large JEE/Stat All Branc
 Vellore Institute of Technology Vellore Tamil Na South High ₹2,50,000 Private/D Yes Medium 6 Large JEE/Stat All Branc
 KIIT University Bhubane Odisha East Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 University of Hyderabad Hyderaba Telangan South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Chandigarh University Mohali Punjab North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Anna University Chennai Tamil Na South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Amity University Noida Uttar Pra North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Andhra University Visakhap Andhra P South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Symbiosis International Pune Maharas West High ₹2,50,000 Private Yes Medium 6 Large JEE/Stat All Branc
 Kerala University Thiruvana Kerala South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Thapar Institute of Engineering and Technology Patiala Punjab North High ₹2,50,000 Private/D Yes Medium 6 Large JEE/Stat All Branc
 Lovely Professional University Phagwar Punjab North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Osmania University Hyderaba Telangan South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Panjab University Chandiga Chandiga Central Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Delhi Technological University New Delh Delhi North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 UPES Dehradun Uttarakha North High ₹2,50,000 Private Yes Medium 6 Large JEE/Stat All Branc
 Graphic Era University Dehradun Uttarakha North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Savitribai Phule Pune University Pune Maharas West Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 IIIT Hyderabad Hyderaba Telangan South High ₹2,50,000 Private Yes Medium 6 Large JEE/Stat All Branc
 Shiv Nadar University Greater N Uttar Pra North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Manipal University Jaipur Jaipur Rajastha North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Jain University Bengalur Karnatak South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Christ University Bengalur Karnatak South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Bangalore University Bengalur Karnatak South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Banasthali Vidyapith Banastha Rajastha North High ₹2,50,000 Private Yes Medium 6 Large JEE/Stat All Branc
 Shoolini University Solan Himacha North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Tezpur University Tezpur Assam East Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Acharya Nagarjuna University Guntur Andhra P South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Annamalai University Annamal Tamil Na South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Ashoka University Sonepat Haryana North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 CV Raman Global University Bhubane Odisha East Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Calicut University Malappur Kerala South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Central University of Kerala Kasarago Kerala South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Dibrugarh University Dibrugarh Assam East Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 GLA University Mathura Uttar Pra North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Galgotias University Greater N Uttar Pra North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Guru Jambheshwar University Hisar Haryana North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Hindustan Institute of Technology and Science Chennai Tamil Na South High ₹2,50,000 Private/D Yes Medium 6 Large JEE/Stat All Branc
 Integral University Lucknow Uttar Pra North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Kalinga University Raipur Chhattisg Central Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Karunya Institute of Technology and Sciences Coimbato Tamil Na South High ₹2,50,000 Private/D Yes Medium 6 Large JEE/Stat All Branc
 Kurukshetra University Kurukshe Haryana North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 MS Ramaiah University Bengalur Karnatak South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Maharshi Dayanand University Rohtak Haryana North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Marwadi University Rajkot Gujarat West Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Maulana Azad National Urdu University Hyderaba Telangan South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Netaji Subhas University of Technology Delhi Delhi North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Nirma University Ahmedab Gujarat West Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Parul University Vadodara Gujarat West Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Pondicherry University Puduche Puduche Central Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 SR University Waranga Telangan South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Sri Venkateswara University Tirupati Andhra P South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Suresh Gyan Vihar University Jaipur Rajastha North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Swami Rama Himalayan University Dehradun Uttarakha North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Utkal University Bhubane Odisha East Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Vel Tech University Chennai Tamil Na South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Vels University Chennai Tamil Na South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Visvesvaraya Technological University Belgaum Karnatak South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Alliance University Bengalur Karnatak South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Anurag University Hyderaba Telangan South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Centurion University Paralakh Odisha East Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 COEP Technological University Pune Maharas West Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Devi Ahilya Vishwavidyalaya Indore Madhya  Central High ₹2,50,000 Private Yes Medium 6 Large JEE/Stat All Branc
 MIT World Peace University Pune Maharas West Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 LNCT University Bhopal Madhya  Central Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Pandit Deendayal Energy University Gandhina Gujarat West Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 PES University Bengalur Karnatak South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Presidency University Kolkata West Ben East Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Punjabi University Patiala Punjab North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Reva University Bengalur Karnatak South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Shivaji University Kolhapur Maharas West Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Sikkim Manipal University Gangtok Sikkim Central Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Sumandeep Vidyapeeth Vadodara Gujarat West High ₹2,50,000 Private Yes Medium 6 Large JEE/Stat All Branc
 Tamil Nadu Veterinary and Animal Sciences Unive Chennai Tamil Na South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 University of Agricultural Sciences Dharwad Karnatak South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Veer Surendra Sai University of Technology Sambalp Odisha East Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Vidyasagar University Midnapor West Ben East Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Visva Bharati Santinike West Ben East High ₹2,50,000 Private Yes Medium 6 Large JEE/Stat All Branc
 Yenepoya University Mangalur Karnatak South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Central University of Karnataka Gulbarga Karnatak South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Central University of South Bihar Gaya Bihar Central Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Chaudhary Charan Singh University Meerut Uttar Pra North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Dr Babasaheb Ambedkar Marathwada University Aurangab Maharas West Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Glocal University Saharanp Uttar Pra North Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Indira Gandhi Krishi Vishwavidyalaya Raipur Chhattisg Central High ₹2,50,000 Private Yes Medium 6 Large JEE/Stat All Branc
 Islamic University of Science and Technology Pulwama Jammu a Central Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Mangalore University Mangalor Karnatak South Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Nagaland University Zunhebo Nagaland Central Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Navsari Agricultural University Navsari Gujarat West Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 North Eastern Hill University Shillong Meghalay Central Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Rabindranath Tagore University Raisen Madhya  Central Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Rajiv Gandhi University Itanagar Arunacha Central Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Sambalpur University Sambalp Odisha East Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Sher-e-Kashmir University of Agricultural Sciences Jammu Jammu a Central Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 Sri Padmavathi Mahila Visvavidyalayam Tirupati Andhra P South High ₹2,50,000 Private Yes Medium 6 Large JEE/Stat All Branc
 Sri Sathya Sai Institute of Higher Learning Prasanth Andhra P South High ₹2,50,000 Private/D Yes Medium 6 Large JEE/Stat All Branc
 The University of Burdwan Bardham West Ben East Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
 University of Science and Technology Ribhoi Meghalay Central Low ₹50,000 Governm Yes Medium 6 Large JEE/Stat All Branc
"""

import json

regions = {"North", "South", "East", "West", "Central"}
multi_word_states = {"Tamil Na", "West Ben", "Uttar Pra", "Andhra P", "Jammu a", "Madhya ", "Himacha", "Arunacha"}
multi_word_cities = {"New Delh", "Greater N"}

def parse_line(line):
    parts = line.strip().split()
    if not parts: return None
    
    # Find region index
    region_idx = -1
    for i, p in enumerate(parts):
        if p in regions:
            region_idx = i
            break
            
    if region_idx == -1: return None
    
    # Region and fields after it are fixed offsets
    region = parts[region_idx]
    budget_range = parts[region_idx+1]
    fees = parts[region_idx+2]
    college_type = parts[region_idx+3]
    hostel = parts[region_idx+4]
    placement_priority = parts[region_idx+5]
    avg_salary = parts[region_idx+6]
    campus_size = parts[region_idx+7]
    entrance_exam = parts[region_idx+8]
    specializations = [" ".join(parts[region_idx+9:])] # Everything else

    # Parse State (backwards from Region)
    state_parts = []
    # Try 2 words first
    potential_state_2 = " ".join(parts[region_idx-2 : region_idx])
    if potential_state_2 in multi_word_states:
        state = potential_state_2
        city_start_idx = region_idx - 2
    else:
        state = parts[region_idx-1]
        city_start_idx = region_idx - 1
        
    # Parse City (backwards from State)
    potential_city_2 = " ".join(parts[city_start_idx-2 : city_start_idx])
    if potential_city_2 in multi_word_cities:
        city = potential_city_2
        name_end_idx = city_start_idx - 2
    else:
        city = parts[city_start_idx-1]
        name_end_idx = city_start_idx - 1
        
    # Name is everything before City
    name = " ".join(parts[:name_end_idx])
    
    return {
        "name": name,
        "location": f"{city}, {state}",
        "city": city,
        "state": state,
        "region": region,
        "budget_range": budget_range,
        "fees": fees,
        "type": college_type,
        "hostel": hostel,
        "placement_priority": placement_priority,
        "average_package": f"{avg_salary} LPA",
        "campus_size": campus_size,
        "cutoff_exam": entrance_exam,
        "specializations": specializations
    }

lines = data.strip().split('\n')
colleges = []
seen = set()

for line in lines:
    college = parse_line(line)
    if college:
        # Deduplicate
        key = (college['name'], college['location'])
        if key not in seen:
            colleges.append(college)
            seen.add(key)

# Complete with previous knowledge of college names if missing in this set
# Actually, the user provided a lot of them.

with open('c:/Users/DELL/AndroidStudioProjects/Pathpiolet/backend/ai_finder_colleges.json', 'w', encoding='utf-8') as f:
    json.dump(colleges, f, indent=2)

print(f"Successfully updated with {len(colleges)} colleges with actual names.")
