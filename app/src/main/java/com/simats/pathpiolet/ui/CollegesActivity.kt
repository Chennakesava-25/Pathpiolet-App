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
import com.simats.pathpiolet.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast

class CollegesActivity : BaseActivity() {

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
        
        binding.btnBack.root.setOnClickListener {
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
        binding.rvColleges.alpha = 0.5f // Visual feedback for loading
        
        RetrofitClient.instance.getColleges().enqueue(object : Callback<List<College>> {
            override fun onResponse(call: Call<List<College>>, response: Response<List<College>>) {
                binding.rvColleges.alpha = 1.0f
                if (response.isSuccessful) {
                    response.body()?.let { colleges ->
                        collegeList.clear()
                        collegeList.addAll(colleges)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(this@CollegesActivity, "Failed to load colleges", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<College>>, t: Throwable) {
                binding.rvColleges.alpha = 1.0f
                Toast.makeText(this@CollegesActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
