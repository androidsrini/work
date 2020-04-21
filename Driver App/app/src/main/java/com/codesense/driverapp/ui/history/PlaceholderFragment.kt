package com.codesense.driverapp.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.codesense.driverapp.R
import com.codesense.driverapp.ui.receipt.ReceiptActivity

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    //private lateinit var pageViewModel: PageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_ride_history, container, false)
        var historyRecyclerView = root.findViewById<RecyclerView>(R.id.historyRecyclerView);
        with(historyRecyclerView) {
            adapter = HistoryRecyclerAdapter(object : HistoryRecyclerAdapter.OnHistorySelection{
                override fun onItemClick(position: Int) {
                    ReceiptActivity.start(context)
                }
            })
        }
        return root
    }

    companion object {

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(): PlaceholderFragment {
            return PlaceholderFragment()
        }
    }
}