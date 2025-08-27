package hr.algebra.lorena.pocketbotanist.ui.internet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import hr.algebra.lorena.pocketbotanist.MainActivity
import hr.algebra.lorena.pocketbotanist.R
import hr.algebra.lorena.pocketbotanist.SplashActivity
import hr.algebra.lorena.pocketbotanist.databinding.FragmentNoInternetBinding
import hr.algebra.lorena.pocketbotanist.utils.getOnboardingPrefs
import hr.algebra.lorena.pocketbotanist.utils.isOnboardingFinished

class NoInternetFragment : Fragment() {

    private var _binding: FragmentNoInternetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoInternetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnProceed.setOnClickListener {
            navigateToApp()
        }

        binding.btnRetry.setOnClickListener {
            activity?.let {
                val intent = Intent(it, SplashActivity::class.java)
                it.startActivity(intent)
                it.finish()
            }
        }
    }

    private fun navigateToApp() {
        val onboardingFinished = requireContext().getOnboardingPrefs().isOnboardingFinished()
        val intent = Intent(requireContext(), MainActivity::class.java)

        if (onboardingFinished) {
            intent.putExtra("NAVIGATE_TO", R.id.nav_plant_list)
        }

        startActivity(intent)
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}