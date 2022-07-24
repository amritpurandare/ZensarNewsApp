package com.amrit.zensarnewsapp.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.amrit.zensarnewsapp.R
import com.amrit.zensarnewsapp.constants.NetworkAPIConstants
import com.amrit.zensarnewsapp.databinding.FragmentNewsListBinding
import com.amrit.zensarnewsapp.modal.Articles
import com.amrit.zensarnewsapp.network.ApiStatus
import com.amrit.zensarnewsapp.network.Response
import com.amrit.zensarnewsapp.view.NewsListAdapter
import com.amrit.zensarnewsapp.view.OnNewsRowClick
import com.amrit.zensarnewsapp.viewmodal.NewsViewModal
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsListFragment : Fragment(), OnNewsRowClick, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var viewModel: NewsViewModal

    private lateinit var newsListAdapter: NewsListAdapter

    private var _newsListBinding: FragmentNewsListBinding? = null
    private val newsListBinding get() = _newsListBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(requireActivity())[NewsViewModal::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _newsListBinding = FragmentNewsListBinding.inflate(inflater, container, false)
        initViews()

        viewModel.newsHeadLines.observe(viewLifecycleOwner) {
            it.let { response ->
                when (response.status) {
                    ApiStatus.SUCCESS -> {
                        handleApiSuccess(response)
                    }
                    ApiStatus.ERROR -> {
                        handleApiFailure(response)
                    }
                    else -> {
                        newsListBinding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
        return newsListBinding.root
    }

    private fun handleApiFailure(
        response: Response<List<Articles>>
    ) {
        Log.d("NewsListFragment", response.message.toString())
        newsListBinding.progressBar.visibility = View.GONE
        Toast.makeText(
            requireActivity(),
            getString(R.string.error_message),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun handleApiSuccess(
        response: Response<List<Articles>>
    ) {
        newsListBinding.progressBar.visibility = View.GONE
        response.data?.let { it1 ->
            newsListAdapter.setAdapterData(it1)
            newsListBinding.headlinesRecyclerView.adapter = newsListAdapter
        }
    }

    private fun initViews() {
        newsListAdapter = NewsListAdapter(this@NewsListFragment)
        val layoutManager = LinearLayoutManager(requireActivity())
        newsListBinding.swipeLayout.setOnRefreshListener(this@NewsListFragment)
        newsListBinding.headlinesRecyclerView.layoutManager = layoutManager
        newsListBinding.headlinesRecyclerView.setHasFixedSize(true)
        logNewsListScreenViewEvent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getNewsHeadline()
    }

    override fun onNewsArticleClick(article: Articles) {
        findNavController().navigate(R.id.action_newsListFragment2_to_newsDetailsFragment3)
        viewModel.articles.postValue(article)
        logArticleClickEvent(article.title ?: getString(R.string.unknown))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_items, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        if (viewModel.getUserCountry() == NetworkAPIConstants.COUNTRY_USA) {
            menu.findItem(R.id.country).title = getString(R.string.us)
        } else {
            menu.findItem(R.id.country).title = getString(R.string.canada)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.handleMenuItemClick(item.itemId)
        requireActivity().invalidateOptionsMenu()
        return super.onOptionsItemSelected(item)
    }

    override fun onRefresh() {
        viewModel.getNewsHeadline()
        newsListBinding.swipeLayout.isRefreshing = false
    }

    private fun logArticleClickEvent(newsTitle: String) {
        Firebase.analytics.logEvent(getString(R.string.fb_article_click_event)) {
            param(getString(R.string.fb_article_title), newsTitle)
        }
    }

    private fun logNewsListScreenViewEvent() {
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, getString(R.string.fb_list_screen_name))
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "NewsArticleFragment")
        }
    }
}