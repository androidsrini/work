package com.codesense.driverapp.ui.history

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codesense.driverapp.R

class HistoryRecyclerAdapter(onHistorySelection: OnHistorySelection) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onHistorySelection: OnHistorySelection

    companion object {
        private val SUCCESS = 0x0001;
        private val FAILED = 0x0002;
    }

    init {
        this@HistoryRecyclerAdapter.onHistorySelection = onHistorySelection
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder{
        val viewHolder: RecyclerView.ViewHolder
        if  (FAILED == p1) {
            viewHolder = FailedViewHolder(LayoutInflater.from(p0.context)
                    .inflate(R.layout.cancel_history_list_screen, null))
        }else {
            viewHolder = SuccessViewHolder(LayoutInflater.from(p0.context)
                    .inflate((R.layout.success_history_list_screen), null))
        }
        return viewHolder

    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun getItemViewType(position: Int): Int {
        return if (0 == position) return FAILED else SUCCESS
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
    }

    inner class FailedViewHolder(root: View): RecyclerView.ViewHolder(root) {
        init {

        }
    }

    inner class SuccessViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        init {
            root.setOnClickListener({
                onHistorySelection.onItemClick(adapterPosition)
            })
        }
    }

    interface OnHistorySelection {
        fun onItemClick(position: Int)
    }

}
