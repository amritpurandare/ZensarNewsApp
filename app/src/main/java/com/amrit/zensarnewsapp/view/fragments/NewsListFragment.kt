package com.amrit.zensarnewsapp.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.amrit.zensarnewsapp.R
import com.amrit.zensarnewsapp.constants.NetworkAPIConstants
import com.amrit.zensarnewsapp.databinding.FragmentNewsListBinding
import com.amrit.zensarnewsapp.modal.data.Article
import com.amrit.zensarnewsapp.network.NewsApiResponse
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
        viewModel = ViewModelProvider(requireActivity())[NewsViewModal::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _newsListBinding = FragmentNewsListBinding.inflate(inflater, container, false)
        initViews()
        initObservers()
        return newsListBinding.root
    }

    private fun initObservers() {
        viewModel.newsApiResponse.observe(viewLifecycleOwner) {
            it.let { response ->
                newsListBinding.progressBar.isVisible = false
                when (it) {
                    is NewsApiResponse.Success -> {
                        it.data?.articles?.let { articles -> handleApiSuccess(articles) }
                    }
                    is NewsApiResponse.Error -> {
                        handleApiFailure(response.errorMessage)
                    }
                    else -> {
                        newsListBinding.progressBar.isVisible = true
                    }
                }
            }
        }
    }

    private fun handleApiFailure(
        message: String?
    ) {
        Log.d("NewsListFragment", message.toString())
        Toast.makeText(
            requireActivity(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun handleApiSuccess(
        response: ArrayList<Article>
    ) {
        newsListAdapter.setAdapterData(response)
        newsListBinding.headlinesRecyclerView.adapter = newsListAdapter
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
        handleMenu()
        viewModel.callNewsApi()
    }

    private fun handleMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_items, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                viewModel.handleMenuItemClick(menuItem.itemId)
                requireActivity().invalidateOptionsMenu()
                return true
            }

            override fun onPrepareMenu(menu: Menu) {
                if (viewModel.getUserCountry() == NetworkAPIConstants.COUNTRY_USA) {
                    menu.findItem(R.id.country).title = getString(R.string.us)
                } else {
                    menu.findItem(R.id.country).title = getString(R.string.canada)
                }
                super.onPrepareMenu(menu)
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onNewsArticleClick(article: Article) {
        findNavController().navigate(R.id.action_newsListFragment2_to_newsDetailsFragment3)
        viewModel.article.postValue(article)
        logArticleClickEvent(article.title ?: getString(R.string.unknown))
    }

    override fun onRefresh() {
        viewModel.callNewsApi()
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