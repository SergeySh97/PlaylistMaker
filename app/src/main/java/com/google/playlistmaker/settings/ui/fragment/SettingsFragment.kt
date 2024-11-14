package com.google.playlistmaker.settings.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.playlistmaker.databinding.FragmentSettingsBinding
import com.google.playlistmaker.settings.ui.viewmodel.SettingsVM
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding get() = requireNotNull(_binding) { "Binding wasn't initiliazed!" }
    private val viewModel: SettingsVM by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeUI() {

        with(binding) {
            scAppTheme.isChecked = viewModel.getTheme()
            scAppTheme.setOnCheckedChangeListener { _, isChecked ->
                viewModel.switchTheme(isChecked)
            }
            tvShare.setOnClickListener {
                viewModel.shareAppLink()
            }
            tvSupport.setOnClickListener {
                viewModel.sendSupport()
            }
            tvUserAgreement.setOnClickListener {
                viewModel.openUserAgreement()
            }
        }
    }
}