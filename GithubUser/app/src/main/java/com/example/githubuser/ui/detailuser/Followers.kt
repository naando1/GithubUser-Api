package com.example.githubuser.ui.detailuser

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.ui.UserAdapter

class Followers : Fragment(R.layout.fragment_follow) {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: UserAdapter
    private lateinit var username: String
    private lateinit var viewModel: FollowersViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        username = args?.getString(UserDetailActivity.EXTRA_USERNAME).toString()
        _binding = FragmentFollowBinding.bind(view)


        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvFollower.setHasFixedSize(true)
            rvFollower.layoutManager = LinearLayoutManager(activity)
            rvFollower.adapter = adapter
        }
        showLoading(true)
        viewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        ).get(FollowersViewModel::class.java)
        viewModel.setFollowersList(username)
        viewModel.getFollowersList().observe(viewLifecycleOwner, {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}