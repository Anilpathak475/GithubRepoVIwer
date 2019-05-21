package com.codechallenge.authenteq.ui.user.search


import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.codechallenge.authenteq.R
import com.codechallenge.authenteq.api.NetworkState
import com.codechallenge.authenteq.base.BaseFragment
import com.codechallenge.authenteq.extensions.onQueryTextChange
import com.codechallenge.authenteq.ui.user.search.views.SearchUserAdapter
import org.koin.android.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.fragment_search_user.fragment_search_user_empty_list_button as emptyListButton
import kotlinx.android.synthetic.main.fragment_search_user.fragment_search_user_empty_list_image as emptyListImage
import kotlinx.android.synthetic.main.fragment_search_user.fragment_search_user_empty_list_title as emptyListTitle
import kotlinx.android.synthetic.main.fragment_search_user.fragment_search_user_progress as progressBar
import kotlinx.android.synthetic.main.fragment_search_user.fragment_search_user_rv as recyclerView


class SearchUserFragment : BaseFragment(), SearchUserAdapter.OnClickListener {

    // FOR DATA ---
    private val viewModel: SearchUserViewModel by viewModel()
    private lateinit var adapter: SearchUserAdapter

    // OVERRIDE ---
    override fun getLayoutId(): Int = R.layout.fragment_search_user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureRecyclerView()
        configureObservables()
        configureOnClick()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        configureMenu(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // ACTION ---
    override fun onClickRetry() {
        viewModel.refreshFailedRequest()
    }

    override fun whenListIsUpdated(size: Int, networkState: NetworkState?) {
        updateUIWhenLoading(size, networkState)
        updateUIWhenEmptyList(size, networkState)
    }

    private fun configureOnClick() {
        emptyListButton.setOnClickListener { viewModel.refreshAllList() }
    }

    private fun configureMenu(menu: Menu) {
        val searchMenuItem = menu.findItem(R.id.action_search)
        val possibleExistingQuery = viewModel.getCurrentQuery()
        val searchView = searchMenuItem.actionView as SearchView
        if (possibleExistingQuery.isNotEmpty()) {
            searchMenuItem.expandActionView()
            searchView.setQuery(possibleExistingQuery, false)
            searchView.clearFocus()
        }
        searchView.onQueryTextChange {
            viewModel.fetchUsersByName(it)
        }
    }

    private fun configureRecyclerView() {
        adapter = SearchUserAdapter(this)
        recyclerView.adapter = adapter
    }

    private fun configureObservables() {
        viewModel.networkState?.observe(this, Observer { adapter.updateNetworkState(it) })
        viewModel.users.observe(this, Observer { adapter.submitList(it) })
    }

    // UPDATE UI ----
    private fun updateUIWhenEmptyList(size: Int, networkState: NetworkState?) {
        emptyListImage.visibility = View.GONE
        emptyListButton.visibility = View.GONE
        emptyListTitle.visibility = View.GONE
        if (size == 0) {
            when (networkState) {
                NetworkState.SUCCESS -> {
                    Glide.with(this).load(R.drawable.ic_directions_run_black_24dp).into(emptyListImage)
                    emptyListTitle.text = getString(R.string.no_result_found)
                    emptyListImage.visibility = View.VISIBLE
                    emptyListTitle.visibility = View.VISIBLE
                    emptyListButton.visibility = View.GONE
                }
                NetworkState.FAILED -> {
                    Glide.with(this).load(R.drawable.ic_healing_black_24dp).into(emptyListImage)
                    emptyListTitle.text = getString(R.string.technical_error)
                    emptyListImage.visibility = View.VISIBLE
                    emptyListTitle.visibility = View.VISIBLE
                    emptyListButton.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun updateUIWhenLoading(size: Int, networkState: NetworkState?) {
        progressBar.visibility = if (size == 0 && networkState == NetworkState.RUNNING) View.VISIBLE else View.GONE
    }
}
