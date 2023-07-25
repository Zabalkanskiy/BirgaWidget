package com.example.newfinamwidget.Recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.compose.ui.text.toLowerCase
import androidx.recyclerview.widget.RecyclerView
import com.example.newfinamwidget.FinamApplication
import com.example.newfinamwidget.R
import com.example.newfinamwidget.deletListPaper
import com.example.newfinamwidget.helper.Securite
import com.example.newfinamwidget.saveListPaper

class RecyclerViewSecurite(var securites: List<Securite>) : RecyclerView.Adapter<SecuriteViewHolder>(), Filterable{

    var filterList = mutableListOf<Securite>()
init {
      filterList = securites.toMutableList()
}




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecuriteViewHolder {
        val view = LayoutInflater.from(FinamApplication.getAppContext()).inflate(R.layout.list_item_securite, parent, false)
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
                saveListPaper(FinamApplication.getAppContext(), paper.secid)
                //   }
            } else{
                deletListPaper(FinamApplication.getAppContext(), paper.secid)
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
                        if(sec.secid.lowercase().contains(charString) ){

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