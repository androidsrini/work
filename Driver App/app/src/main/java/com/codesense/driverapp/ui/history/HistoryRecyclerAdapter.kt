package com.codesense.driverapp.ui.history

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codesense.driverapp.R

class HistoryRecyclerAdapter: RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder> {
    constructor() {

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder{
        return ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.history_list_screen, null))
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

    }

    class ViewHolder: RecyclerView.ViewHolder {
        constructor(root: View) : super(root) {
        }
    }

}
