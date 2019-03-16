package ru.wearemad.fadeview

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler = findViewById<RecyclerView>(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = MyAdapter(getData(), this)
    }

    private fun getData(): List<String> {
        return mutableListOf<String>().apply {
            for (i in 0..20) {
                add("some comment for recycler view item $i")
            }
        }
    }

    class MyAdapter(private val mData: List<String>,
                    context: Context) : RecyclerView.Adapter<MyAdapter.MyHolder>() {

        private val mInflater = LayoutInflater.from(context)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
            return MyHolder(mInflater.inflate(R.layout.item_recycler, parent, false))
        }

        override fun getItemCount() = mData.size

        override fun onBindViewHolder(holder: MyHolder, position: Int) {
            holder.bind(mData[position])
        }

        inner class MyHolder(view: View) : RecyclerView.ViewHolder(view) {

            private val tv1 = view.findViewById<TextView>(R.id.tv1)

            fun bind(item: String) {
                tv1.text = item
            }
        }
    }
}
