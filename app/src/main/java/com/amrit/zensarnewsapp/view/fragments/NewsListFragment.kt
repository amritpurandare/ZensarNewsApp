package com.amrit.zensarnewsapp.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.amrit.zensarnewsapp.R
import com.amrit.zensarnewsapp.constants.NetworkAPIConstants
import com.amrit.zensarnewsapp.modal.Articles
import com.amrit.zensarnewsapp.network.ApiStatus
import com.amrit.zensarnewsapp.utils.ConnectivityManager
import com.amrit.zensarnewsapp.utils.SharedPrefHelper
import com.amrit.zensarnewsapp.view.NewsListAdapter
import com.amrit.zensarnewsapp.view.OnNewsRowClick
import com.amrit.zensarnewsapp.viewmodal.NewsViewModal

class NewsListFragment : Fragment(), OnNewsRowClick, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var country: String
    private lateinit var viewModel: NewsViewModal
    private lateinit var sharedPrefHelper: SharedPrefHelper
    private lateinit var newsListAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        sharedPrefHelper = SharedPrefHelper(requireActivity())
        viewModel = ViewModelProvider(requireActivity())[NewsViewModal::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_news_list, container, false)
        val (recyclerView, progressBar) = initViews(view)

        viewModel.newsHeadLines.observe(viewLifecycleOwner, Observer {
            it.let { response ->
                when (response.status) {
                    ApiStatus.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        response.data?.let { it1 ->
                            newsListAdapter.setAdapterData(it1)
                            recyclerView.adapter = newsListAdapter
                        }
                    }
                    ApiStatus.ERROR -> {
                        Log.d("NewsListFragment", response.message.toString())
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.error_message),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
        return view
    }

    private fun initViews(view: View): Pair<RecyclerView, ProgressBar> {
        newsListAdapter = NewsListAdapter(this@NewsListFragment)
        val layoutManager = LinearLayoutManager(requireActivity())
        val recyclerView = view.findViewById<RecyclerView>(R.id.headlinesRecyclerView)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeLayout)
        swipeRefreshLayout.setOnRefreshListener(this@NewsListFragment)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        return Pair(recyclerView, progressBar)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
    }

    private fun getData() {
        country = sharedPrefHelper.getUserCountry()
        if (ConnectivityManager.isNetworkAvailable(requireActivity()) && this::viewModel.isInitialized)
            viewModel.getNewsHeadline(country)
    }

    override fun onNewsArticleClick(article: Articles) {
        viewModel.showNewsDetailsFragment.postValue(true)
        viewModel.articles.postValue(article)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_items, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        if (sharedPrefHelper.getUserCountry() == NetworkAPIConstants.COUNTRY_USA) {
            menu.findItem(R.id.country).title = getString(R.string.us)
        } else {
            menu.findItem(R.id.country).title = getString(R.string.canada)
        }
        return super.onPrepareOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var isCountryChanged = false
        when (item.itemId) {
            R.id.usa -> {
                if (country != NetworkAPIConstants.COUNTRY_USA) {
                    isCountryChanged = true
                    sharedPrefHelper.saveUserChoiceCountry(NetworkAPIConstants.COUNTRY_USA)
                }
            }
            R.id.canada -> {
                if (country != NetworkAPIConstants.COUNTRY_CANADA) {
                    isCountryChanged = true
                    sharedPrefHelper.saveUserChoiceCountry(NetworkAPIConstants.COUNTRY_CANADA)
                }
            }
        }
        if (isCountryChanged) {
            getData()
        }
        requireActivity().invalidateOptionsMenu()
        return super.onOptionsItemSelected(item)
    }

    override fun onRefresh() {
        getData()
        swipeRefreshLayout.isRefreshing = false
    }
}