package id.ac.ukdw.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.ac.ukdw.data.AccordionItem
import id.ac.ukdw.drones_isai.R

class AccordionAdapter(private val items: List<AccordionItem>) : RecyclerView.Adapter<AccordionAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val accordionButton: Button = itemView.findViewById(R.id.accordion_button)
        val accordionContent: TextView = itemView.findViewById(R.id.accordion_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_accordion, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.accordionButton.text = item.title
        holder.accordionContent.text = item.content

        if (item.isExpanded) {
            holder.accordionContent.visibility = View.VISIBLE
            holder.accordionButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up, 0)
        } else {
            holder.accordionContent.visibility = View.GONE
            holder.accordionButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_down, 0)
        }

        holder.accordionButton.setOnClickListener {
            item.isExpanded = !item.isExpanded
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
