package com.example.newfinamwidget.Recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newfinamwidget.FinamApplication
import com.example.newfinamwidget.R
import com.example.newfinamwidget.helper.Securite
import com.example.newfinamwidget.saveListPaper

class RecyclerViewSecurite(var securites: List<Securite>) : RecyclerView.Adapter<SecuriteViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecuriteViewHolder {
        val view = LayoutInflater.from(FinamApplication.getAppContext()).inflate(R.layout.list_item_securite, parent, false)
        return SecuriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: SecuriteViewHolder, position: Int) {
       val paper =  securites[position]
        holder.tikerView.setText(paper.secid)
        holder.shortNameView.setText(paper.shortname)
        holder.chekcBox.setOnClickListener { view ->
            // saveListPaper(FinamAppApplications.getAppContext(), paper.secid)
            // holder.chekcBox.isChecked = !holder.chekcBox.isChecked
            if (holder.chekcBox.isChecked) {
                saveListPaper(FinamApplication.getAppContext(), paper.secid)
            }
        }
    }

    override fun getItemCount(): Int {
     return   securites.size
    }
}