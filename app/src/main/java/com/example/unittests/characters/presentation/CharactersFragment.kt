package com.example.unittests.characters.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unittests.databinding.FragmentCharacterListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.example.unittests.characters.presentation.adapter.CharactersAdapter

@AndroidEntryPoint
class CharactersFragment : Fragment() {

    private var _binding: FragmentCharacterListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CharacterListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = CharactersAdapter(
            onAddToFavorites = { favoriteId -> viewModel.addToFavorites(favoriteId) },
            onRemoveFromFavorites = { favoriteId -> viewModel.removeFromFavorites(favoriteId) },
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }

        subscribeUI()

        viewModel.requestCharacters()
    }

    private fun subscribeUI() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.state.collect { state ->
                        when {
                            state.isLoading -> showLoading()
                            state.hasError -> {
                                Toast.makeText(
                                    requireContext(),
                                    "Error wile loading data",
                                    Toast.LENGTH_SHORT
                                ).show()

                                viewModel.errorHasShown()
                            }

                            else -> showCharacterList(characterStateList = state.characterListState)
                        }
                    }
                }
            }
        }
    }

    private fun showCharacterList(characterStateList: List<CharacterState>) {
        binding.progress.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.swipeRefreshLayout.isRefreshing = false
        (binding.recyclerView.adapter as CharactersAdapter).submitList(characterStateList)
    }

    private fun showLoading() {
        binding.progress.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
