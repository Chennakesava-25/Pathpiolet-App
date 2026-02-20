package com.simats.pathpiolet.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.simats.pathpiolet.data.College
import com.simats.pathpiolet.databinding.ActivityCollegesBinding
import com.simats.pathpiolet.ui.adapter.CollegeAdapter
import com.simats.pathpiolet.MainActivity

class CollegesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCollegesBinding
    private lateinit var adapter: CollegeAdapter
    private var collegeList: MutableList<College> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCollegesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupData()
        setupRecyclerView()
        setupSearch()
        
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        adapter = CollegeAdapter(collegeList) { college ->
            // Navigate to Details
             val intent = Intent(this, CollegeDetailsActivity::class.java)
             intent.putExtra("college_data", college)
             startActivity(intent)
        }
        binding.rvColleges.layoutManager = LinearLayoutManager(this)
        binding.rvColleges.adapter = adapter
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adapter.filter(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }



    private fun setupData() {
        collegeList.add(College(1, "Indian Institute of Science", "Bengaluru", "Karnataka", 85.05, 1, "₹ 3L/yr", "₹ 25 LPA"))
        collegeList.add(College(2, "Jawaharlal Nehru University", "New Delhi", "Delhi", 71.00, 2, "₹ 20K/yr", "₹ 12 LPA"))
        collegeList.add(College(3, "Manipal Academy of Higher Education", "Manipal", "Karnataka", 69.25, 3, "₹ 4.5L/yr", "₹ 8 LPA"))
        collegeList.add(College(4, "Jamia Millia Islamia", "New Delhi", "Delhi", 69.10, 4, "₹ 35K/yr", "₹ 9 LPA"))
        collegeList.add(College(5, "University of Delhi", "Delhi", "Delhi", 67.38, 5, "₹ 25K/yr", "₹ 10 LPA"))
        collegeList.add(College(6, "Banaras Hindu University", "Varanasi", "Uttar Pradesh", 67.28, 6, "₹ 30K/yr", "₹ 11 LPA"))
        collegeList.add(College(7, "BITS Pilani", "Pilani", "Rajasthan", 67.24, 7, "₹ 5.5L/yr", "₹ 18 LPA"))
        collegeList.add(College(8, "Amrita Vishwa Vidyapeetham", "Coimbatore", "Tamil Nadu", 67.05, 8, "₹ 3.5L/yr", "₹ 7.5 LPA"))
        collegeList.add(College(9, "Jadavpur University", "Kolkata", "West Bengal", 65.42, 9, "₹ 15K/yr", "₹ 14 LPA"))
        collegeList.add(College(10, "Aligarh Muslim University", "Aligarh", "Uttar Pradesh", 65.35, 10, "₹ 2.5L/yr", "₹ 6.5 LPA"))
        collegeList.add(College(11, "SRM Institute of Science and Technology", "Chennai", "Tamil Nadu", 65.26, 11, "₹ 4L/yr", "₹ 7 LPA"))
        collegeList.add(College(12, "Homi Bhabha National Institute", "Mumbai", "Maharashtra", 65.08, 12, "₹ 50K/yr", "₹ 10 LPA"))
        collegeList.add(College(13, "Saveetha Institute", "Chennai", "Tamil Nadu", 65.04, 13, "₹ 3.2L/yr", "₹ 5.5 LPA"))
        collegeList.add(College(14, "Vellore Institute of Technology", "Vellore", "Tamil Nadu", 64.64, 14, "₹ 2L/yr", "₹ 9 LPA"))
        collegeList.add(College(15, "Siksha O Anusandhan", "Bhubaneswar", "Odisha", 63.14, 15, "₹ 2.8L/yr", "₹ 6 LPA"))
        collegeList.add(College(16, "Indian Agricultural Research Institute", "New Delhi", "Delhi", 62.89, 16, "₹ 40K/yr", "₹ 7 LPA"))
        collegeList.add(College(17, "KIIT University", "Bhubaneswar", "Odisha", 62.87, 17, "₹ 3.5L/yr", "₹ 6.5 LPA"))
        collegeList.add(College(18, "University of Hyderabad", "Hyderabad", "Telangana", 61.83, 18, "₹ 45K/yr", "₹ 9.5 LPA"))
        collegeList.add(College(19, "Chandigarh University", "Mohali", "Punjab", 61.27, 19, "₹ 1.8L/yr", "₹ 8 LPA"))
        collegeList.add(College(20, "Anna University", "Chennai", "Tamil Nadu", 61.22, 20, "₹ 55K/yr", "₹ 7.5 LPA"))
        collegeList.add(College(21, "JSS Academy", "Mysuru", "Karnataka", 60.00, 21, "₹ 2.2L/yr", "₹ 5 LPA"))
        collegeList.add(College(22, "Amity University", "Noida", "Uttar Pradesh", 59.68, 22, "₹ 3.8L/yr", "₹ 6 LPA"))
        collegeList.add(College(23, "Andhra University", "Visakhapatnam", "Andhra Pradesh", 59.20, 23, "₹ 40K/yr", "₹ 4.5 LPA"))
        collegeList.add(College(24, "Symbiosis International", "Pune", "Maharashtra", 59.16, 24, "₹ 4.2L/yr", "₹ 11 LPA"))
        collegeList.add(College(25, "Kerala University", "Thiruvananthapuram", "Kerala", 59.05, 25, "₹ 35K/yr", "₹ 5 LPA"))
        collegeList.add(College(26, "Thapar Institute", "Patiala", "Punjab", 58.87, 26))
        collegeList.add(College(26, "Koneru Lakshmaiah", "Vaddeswaram", "Andhra Pradesh", 58.87, 26))
        collegeList.add(College(28, "Kalasalingam Academy", "Krishnan Koil", "Tamil Nadu", 58.37, 28))
        collegeList.add(College(29, "SASTRA", "Thanjavur", "Tamil Nadu", 58.06, 29))
        collegeList.add(College(30, "Osmania University", "Hyderabad", "Telangana", 57.94, 30))
        collegeList.add(College(31, "Lovely Professional University", "Phagwara", "Punjab", 57.77, 31))
        collegeList.add(College(32, "Cochin University", "Cochin", "Kerala", 57.55, 32))
        collegeList.add(College(33, "Gauhati University", "Guwahati", "Assam", 57.37, 33))
        collegeList.add(College(34, "University of Kashmir", "Srinagar", "J&K", 57.13, 34))
        collegeList.add(College(35, "Panjab University", "Chandigarh", "Chandigarh", 56.88, 35))
        collegeList.add(College(36, "Bharathidasan University", "Tiruchirappalli", "Tamil Nadu", 55.85, 36))
        collegeList.add(College(37, "BBAU", "Lucknow", "Uttar Pradesh", 55.55, 37))
        collegeList.add(College(38, "University of Madras", "Chennai", "Tamil Nadu", 55.23, 38))
        collegeList.add(College(39, "Calcutta University", "Kolkata", "West Bengal", 55.17, 39))
        collegeList.add(College(40, "Institute of Chemical Technology", "Mumbai", "Maharashtra", 55.13, 40))
        collegeList.add(College(41, "Dr. D. Y. Patil Vidyapeeth", "Pune", "Maharashtra", 54.90, 41))
        collegeList.add(College(42, "Delhi Technological University", "New Delhi", "Delhi", 54.88, 42))
        collegeList.add(College(43, "Mahatma Gandhi University", "Kottayam", "Kerala", 54.61, 43))
        collegeList.add(College(44, "Alagappa University", "Karaikudi", "Tamil Nadu", 54.45, 44))
        collegeList.add(College(45, "UPES", "Dehradun", "Uttarakhand", 54.44, 45))
        collegeList.add(College(46, "Bharathiar University", "Coimbatore", "Tamil Nadu", 54.42, 46))
        collegeList.add(College(47, "Jamia Hamdard", "New Delhi", "Delhi", 54.10, 47))
        collegeList.add(College(48, "Graphic Era University", "Dehradun", "Uttarakhand", 53.92, 48))
        collegeList.add(College(49, "Datta Meghe Institute", "Wardha", "Maharashtra", 53.91, 49))
        collegeList.add(College(50, "King Georges Medical University", "Lucknow", "Uttar Pradesh", 53.13, 50))
        collegeList.add(College(51, "University of Jammu", "Jammu", "J&K", 53.01, 51))
        collegeList.add(College(52, "Narsee Monjee Institute", "Mumbai", "Maharashtra", 52.54, 52))
        collegeList.add(College(53, "Sathyabama Institute", "Chennai", "Tamil Nadu", 52.52, 53))
        collegeList.add(College(54, "Mumbai University", "Mumbai", "Maharashtra", 52.48, 54))
        collegeList.add(College(55, "IIIT Hyderabad", "Hyderabad", "Telangana", 52.34, 55))
        collegeList.add(College(56, "Savitribai Phule Pune University", "Pune", "Maharashtra", 52.33, 56))
        collegeList.add(College(57, "Shiv Nadar University", "Gautam Buddha Nagar", "Uttar Pradesh", 52.18, 57))
        collegeList.add(College(58, "Manipal University Jaipur", "Jaipur", "Rajasthan", 51.57, 58))
        collegeList.add(College(59, "Bharati Vidyapeeth", "Pune", "Maharashtra", 51.37, 59))
        collegeList.add(College(60, "Sri Ramachandra Institute", "Chennai", "Tamil Nadu", 51.35, 60))
        collegeList.add(College(61, "Chettinad Academy", "Kelambakkam", "Tamil Nadu", 51.06, 61))
        collegeList.add(College(62, "Jain University", "Bengaluru", "Karnataka", 50.95, 62))
        collegeList.add(College(63, "Christ University", "Bengaluru", "Karnataka", 50.92, 63))
        collegeList.add(College(64, "Punjab Agricultural University", "Ludhiana", "Punjab", 50.84, 64))
        collegeList.add(College(65, "Bangalore University", "Bengaluru", "Karnataka", 50.72, 65))
        collegeList.add(College(66, "Banasthali Vidyapith", "Banasthali", "Rajasthan", 50.65, 66))
        collegeList.add(College(67, "Sri Balaji Vidyapeeth", "Puducherry", "Pondicherry", 50.46, 67))
        collegeList.add(College(68, "Madan Mohan Malaviya University", "Gorakhpur", "Uttar Pradesh", 50.35, 68))
        collegeList.add(College(69, "Shoolini University", "Solan", "Himachal Pradesh", 50.16, 69))
        collegeList.add(College(70, "Vignans Foundation", "Guntur", "Andhra Pradesh", 50.06, 70))
        collegeList.add(College(71, "Mysore University", "Mysuru", "Karnataka", 49.98, 71))
        collegeList.add(College(72, "Tata Institute", "Mumbai", "Maharashtra", 49.89, 72))
        collegeList.add(College(73, "Tamil Nadu Agricultural University", "Coimbatore", "Tamil Nadu", 49.80, 73))
        collegeList.add(College(74, "Gujarat University", "Ahmedabad", "Gujarat", 49.74, 74))
        collegeList.add(College(75, "Sher-e-Kashmir University", "Srinagar", "J&K", 49.70, 75))
        collegeList.add(College(76, "Bharath Institute", "Chennai", "Tamil Nadu", 49.59, 76))
        collegeList.add(College(77, "Central University of Punjab", "Bathinda", "Punjab", 49.53, 77))
        collegeList.add(College(78, "Chitkara University", "Rajpura", "Punjab", 49.32, 78))
        collegeList.add(College(79, "Tezpur University", "Tezpur", "Assam", 49.31, 79))
        collegeList.add(College(80, "NITTE", "Mangaluru", "Karnataka", 49.26, 80))
        collegeList.add(College(81, "JNTU", "Hyderabad", "Telangana", 49.22, 81))
        collegeList.add(College(82, "Mizoram University", "Aizawl", "Mizoram", 49.21, 82))
        collegeList.add(College(83, "Central University of Tamil Nadu", "Tiruvarur", "Tamil Nadu", 49.13, 83))
        collegeList.add(College(84, "Acharya Nagarjuna University", "Guntur", "Andhra Pradesh", 49.08, 84))
        collegeList.add(College(85, "Madurai Kamaraj University", "Madurai", "Tamil Nadu", 49.07, 85))
        collegeList.add(College(85, "Maharishi Markandeshwar", "Ambala", "Haryana", 49.07, 85))
        collegeList.add(College(87, "Sharda University", "Greater Noida", "Uttar Pradesh", 49.00, 87))
        collegeList.add(College(88, "GITAM", "Visakhapatnam", "Andhra Pradesh", 48.79, 88))
        collegeList.add(College(89, "Central University of Rajasthan", "Kishangarh", "Rajasthan", 48.47, 89))
        collegeList.add(College(90, "Sant Longowal Institute", "Longowal", "Punjab", 48.43, 90))
        collegeList.add(College(91, "Padmashree Dr. D. Y. Patil", "Mumbai", "Maharashtra", 48.41, 91))
        collegeList.add(College(92, "Birla Institute of Technology", "Ranchi", "Jharkhand", 48.25, 92))
        collegeList.add(College(93, "Guru Gobind Singh Indraprastha University", "New Delhi", "Delhi", 48.01, 93))
        collegeList.add(College(94, "Periyar University", "Salem", "Tamil Nadu", 47.98, 94))
        collegeList.add(College(95, "University of Agricultural Sciences", "Bengaluru", "Karnataka", 47.95, 95))
        collegeList.add(College(96, "Manav Rachna Institute", "Faridabad", "Haryana", 47.30, 96))
        collegeList.add(College(97, "Assam University", "Silchar", "Assam", 47.28, 97))
        collegeList.add(College(98, "University of Lucknow", "Lucknow", "Uttar Pradesh", 47.26, 98))
        collegeList.add(College(99, "Avinashilingam Institute", "Coimbatore", "Tamil Nadu", 47.09, 99))
        collegeList.add(College(100, "IIIT Delhi", "New Delhi", "Delhi", 47.07, 100))
    }
}
