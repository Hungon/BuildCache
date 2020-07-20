package com.trials.sample001.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.trials.sample001.ui.detail.DetailActivity
import com.trials.sample001.R
import kotlinx.android.synthetic.main.fragment_main.view.*

class SectionsFragment : Fragment() {

    private lateinit var sectionsPagerViewModel: SectionsPagerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            sectionsPagerViewModel =
                ViewModelProvider(it).get(SectionsPagerViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        root.list_categories.apply {
            layoutManager = LinearLayoutManager(this@SectionsFragment.context)
            setHasFixedSize(true)
        }
        val snapListAdapter = SectionsListAdapter(listener = {
            startActivity(
                Intent(activity, DetailActivity::class.java).apply {
                    putExtra(DetailActivity.ARG_CONTENT_SELECTED, it)
                })
        })
        root.list_categories.adapter = snapListAdapter
        sectionsPagerViewModel.getCategoryList.observe(viewLifecycleOwner, Observer {
            for (sub in it) {
                snapListAdapter.setRecords(sub.subCategoryList)
            }
        })
        return root
    }

    companion object {
        private const val ARG_SECTION_NAME = "section_name"

        @JvmStatic
        fun newInstance(sectionName: String): SectionsFragment {
            return SectionsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SECTION_NAME, sectionName)
                }
            }
        }
    }
}