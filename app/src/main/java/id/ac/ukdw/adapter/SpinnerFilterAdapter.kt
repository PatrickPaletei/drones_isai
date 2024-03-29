package id.ac.ukdw.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import id.ac.ukdw.drones_isai.ui.tren.SpinnerItem

class SpinnerFilterAdapter(context: Context, resource: Int, items: List<SpinnerItem>) :
    ArrayAdapter<SpinnerItem>(context, resource, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_item, parent, false)
        val item = getItem(position)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = item?.name
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_spinner_dropdown_item, parent, false)
        val item = getItem(position)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = item?.name
        return view
    }
}