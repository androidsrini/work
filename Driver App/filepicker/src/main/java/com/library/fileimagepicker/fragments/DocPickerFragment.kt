package com.library.fileimagepicker.filepicker.fragments

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ProgressBar
import com.library.fileimagepicker.R
import com.library.fileimagepicker.adapters.SectionsPagerAdapter
import com.library.fileimagepicker.cursors.loadercallbacks.FileMapResultCallback
import com.library.fileimagepicker.filepicker.PickerManager
import com.library.fileimagepicker.filepicker.utils.MediaStoreHelper
import com.library.fileimagepicker.models.Document
import com.library.fileimagepicker.models.FileType
import com.library.fileimagepicker.utils.TabLayoutHelper

class DocPickerFragment : BaseFragment() {

    private var currentPagePosition = 0
    lateinit var tabLayout: TabLayout

    lateinit var viewPager: ViewPager
    private var progressBar: ProgressBar? = null
    private var mListener: DocPickerFragmentListener? = null

    interface DocPickerFragmentListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doc_picker, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DocPickerFragmentListener) {
            mListener = context
        } else {
            throw RuntimeException(context?.toString() + " must implement DocPickerFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews(view)
        initView()
    }

    private fun initView() {
        setUpViewPager()
        setData()
    }

    private fun setViews(view: View) {
        tabLayout = view.findViewById(R.id.tabs)
        viewPager = view.findViewById(R.id.viewPager)
        progressBar = view.findViewById(R.id.progress_bar)

        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        if (PickerManager.hasOnlyPdfFile) {
            tabLayout.visibility = GONE
        }
    }

    private fun setData() {
        context?.let {
            MediaStoreHelper.getDocs(it.contentResolver,
                    PickerManager.getFileTypes(),
                    PickerManager.sortingType.comparator,
                    object : FileMapResultCallback {
                        override fun onResultCallback(files: Map<FileType, List<Document>>) {
                            if(isAdded) {
                                progressBar?.visibility = View.GONE
                                setDataOnFragments(files)
                            }
                        }
                    }
            )
        }
    }

    private fun setDataOnFragments(filesMap: Map<FileType, List<Document>>) {
        view.let {
            val sectionsPagerAdapter = viewPager.adapter as SectionsPagerAdapter?
            if (sectionsPagerAdapter != null) {
                for (index in 0 until sectionsPagerAdapter.count) {
                    val docFragment = childFragmentManager
                            .findFragmentByTag(
                                    "android:switcher:" + R.id.viewPager + ":" + index) as DocFragment
                    val fileType = docFragment.fileType
                    if (fileType != null) {
                        val filesFilteredByType = filesMap[fileType]
                        if (filesFilteredByType != null)
                            docFragment.updateList(filesFilteredByType)
                    }
                }
            }
        }
    }

    private fun setUpViewPager() {
        val adapter = SectionsPagerAdapter(childFragmentManager)
        val supportedTypes = PickerManager.getFileTypes()
        for (index in supportedTypes.indices) {
            adapter.addFragment(DocFragment.newInstance(supportedTypes.get(index)), supportedTypes.get(index).title)
        }

        viewPager.offscreenPageLimit = supportedTypes.size
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        val mTabLayoutHelper = TabLayoutHelper(tabLayout, viewPager)
        mTabLayoutHelper.isAutoAdjustTabModeEnabled = true
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                currentPagePosition = p0;
            }

            override fun onPageSelected(p0: Int) {
                currentPagePosition = p0;
            }

        })
    }

    companion object {

        private val TAG = DocPickerFragment::class.java.simpleName

        fun newInstance(): DocPickerFragment {
            return DocPickerFragment()
        }
    }
}// Required empty public constructor
