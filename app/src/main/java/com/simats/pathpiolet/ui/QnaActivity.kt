package com.simats.pathpiolet.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.simats.pathpiolet.databinding.ActivityQnaBinding
import com.simats.pathpiolet.databinding.ItemQnaBinding

class QnaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQnaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityQnaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }
        binding.fabAsk.setOnClickListener {
            startActivity(Intent(this, AskQuestionActivity::class.java))
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvQna.layoutManager = LinearLayoutManager(this)
        binding.rvQna.adapter = QnaAdapter()
    }

    class QnaAdapter : RecyclerView.Adapter<QnaAdapter.ViewHolder>() {
        class ViewHolder(val binding: ItemQnaBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemQnaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // Static content for design matching
            holder.binding.tvQuestionTitle.text = "Which specialization is best for AI in B.Tech?"
            holder.binding.tvQuestionDesc.text = "I am interested in Artificial Intelligence and want to know which specialization would be beneficial for my future career..."
        }

        override fun getItemCount(): Int = 1
    }
}
