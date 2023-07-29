package com.example.widgetbirga.Recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.widgetbirga.WidgetApplication
import com.example.widgetbirga.R
import com.example.widgetbirga.deletListPaper
import com.example.widgetbirga.helper.Securite
import com.example.widgetbirga.saveListPaper

class RecyclerViewSecurite(var securites: List<Securite>) : RecyclerView.Adapter<SecuriteViewHolder>(), Filterable{

    var filterList = mutableListOf<Securite>()
init {
      filterList = securites.toMutableList()
}




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecuriteViewHolder {
        val view = LayoutInflater.from(WidgetApplication.getAppContext()).inflate(R.layout.list_item_securite, parent, false)
        return SecuriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: SecuriteViewHolder, position: Int) {
        val paper = filterList[position]
        holder.tikerView.setText(paper.secid)
        holder.shortNameView.setText(paper.shortname)
        holder.chekcBox.setOnClickListener { view ->
            // saveListPaper(FinamAppApplications.getAppContext(), paper.secid)
            // holder.chekcBox.isChecked = !holder.chekcBox.isChecked
             filterList.get(position).checked = !filterList.get(position).checked
            if (holder.chekcBox.isChecked) {
                saveListPaper(WidgetApplication.getAppContext(), paper.secid)
                //   }
            } else{
                deletListPaper(WidgetApplication.getAppContext(), paper.secid)
            }

        }
        holder.chekcBox.isChecked = filterList.get(position).checked
    }


    override fun getItemCount(): Int {
     return   filterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?):FilterResults {
                val charString = constraint.toString().lowercase() ?:""
                var list = mutableListOf<Securite>()
                if (charString.isEmpty()){
                    filterList = securites.toMutableList()

                } else {
                    val resultList = mutableListOf<Securite>()
          //          securites.filter { (it.secid.contains(charString) ) || (it.shortname.contains(charString)) }.forEach {
          //              resultList.add(it)
           //         }
                    for (sec in securites){
                        val shortname: String = sec.shortname ?: ""
                        if((sec.secid.lowercase().contains(charString)) || shortname.lowercase().contains(charString, ignoreCase = true)){

                                                    resultList.add(sec)
                        }
                    }




                    filterList = resultList
                }
                val filter = FilterResults()
                filter.values = filterList
                return filter
         //      return FilterResults().apply { values = filterList }

            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filterList = p1?.values as MutableList<Securite>// ?: securites.toMutableList()
                notifyDataSetChanged()
            }
        }


    }
}