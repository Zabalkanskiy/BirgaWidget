package com.example.newfinamwidget.Recycler

import android.view.TextureView
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newfinamwidget.R

class SecuriteViewHolder(view: View) :RecyclerView.ViewHolder(view) {
    val tikerView =view.findViewById<TextView>(R.id.securite_tiker)
    val shortNameView = view.findViewById<TextView>(R.id.securite_short_name)
    val chekcBox = view.findViewById<CheckBox>(R.id.securite_check_box)

}
