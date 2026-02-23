package com.simats.pathpiolet.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.simats.pathpiolet.databinding.ActivityQnaBinding
import com.simats.pathpiolet.databinding.ItemQnaBinding

class QnaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQnaBinding
    private lateinit var adapter: QnaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQnaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolBar()
        setupRecyclerView()
        setupFab()
    }

    private fun setupToolBar() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        val dummyData = listOf(
            QnaItem(
                "Which specialization is best for AI in B.Tech?",
                "I am interested in Artificial Intelligence and want to know which specialization would be beneficial...",
                "Feb 10, 2026",
                "Career Guidance",
                "Answered"
            ),
            QnaItem(
                "How to apply for IIT Bombay?",
                "Can you guide me through the application process for IIT Bombay after JEE Advanced?",
                "Feb 12, 2026",
                "Admission",
                "Answered"
            ),
            QnaItem(
                "Best colleges for Data Science in Bangalore?",
                "Looking for top-rated colleges for Data Science based on placement records.",
                "Feb 15, 2026",
                "College Search",
                "Answered"
            )
        )

        adapter = QnaAdapter(dummyData)
        binding.rvQna.layoutManager = LinearLayoutManager(this)
        binding.rvQna.adapter = adapter
    }

    private fun setupFab() {
        binding.fabAsk.setOnClickListener {
            // Placeholder for asking a new question
            android.widget.Toast.makeText(this, "Ask Feature Coming Soon!", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    data class QnaItem(
        val title: String,
        val description: String,
        val date: String,
        val tag: String,
        val status: String
    )

    class QnaAdapter(private val items: List<QnaItem>) : RecyclerView.Adapter<QnaAdapter.ViewHolder>() {

        class ViewHolder(val binding: ItemQnaBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemQnaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.binding.tvQuestionTitle.text = item.title
            holder.binding.tvQuestionDesc.text = item.description
            holder.binding.tvDate.text = "${item.date}  • ${item.tag}"
            // For status, usually we'd have a color or text change. Layout shows static "Answered" for now.
        }

        override fun getItemCount(): Int = items.size
    }
}
