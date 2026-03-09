package com.simats.pathpiolet.ui

import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import com.simats.pathpiolet.data.College
import com.simats.pathpiolet.databinding.ActivityCollegesBinding
import com.simats.pathpiolet.ui.adapter.CollegeAdapter
import com.simats.pathpiolet.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun CollegesScreen(
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val binding = ActivityCollegesBinding.inflate(LayoutInflater.from(context))
            
            // Back button should probably be hidden or handled differently in tab
            binding.btnBack.root.visibility = android.view.View.GONE
            
            setupCollegesView(context, binding)
            
            binding.root
        }
    )
}

private fun setupCollegesView(context: Context, binding: ActivityCollegesBinding) {
    val collegeList = mutableListOf<College>()
    val adapter = CollegeAdapter(collegeList) { college ->
        val intent = Intent(context, CollegeDetailsActivity::class.java)
        intent.putExtra("college_data", college)
        context.startActivity(intent)
    }
    
    binding.rvColleges.layoutManager = LinearLayoutManager(context)
    binding.rvColleges.adapter = adapter
    
    // Search
    binding.etSearch.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            adapter.filter(s.toString())
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
    
    // Fetch Data
    binding.rvColleges.alpha = 0.5f
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
                Toast.makeText(context, "Failed to load colleges", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<List<College>>, t: Throwable) {
            binding.rvColleges.alpha = 1.0f
            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}
