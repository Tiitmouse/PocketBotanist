package hr.algebra.lorena.pocketbotanist.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import hr.algebra.lorena.pocketbotanist.MainActivity
import hr.algebra.lorena.pocketbotanist.R
import hr.algebra.lorena.pocketbotanist.adapter.OnboardingAdapter
import hr.algebra.lorena.pocketbotanist.databinding.FragmentOnboardingBinding
import hr.algebra.lorena.pocketbotanist.utils.getOnboardingPrefs
import hr.algebra.lorena.pocketbotanist.utils.setOnboardingFinished

class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pages = listOf(
            OnboardingPageFragment.newInstance(
                getString(R.string.onboarding_welcome_title),
                getString(R.string.onboarding_welcome_desc)
            ),
            OnboardingPageFragment.newInstance(
                getString(R.string.onboarding_track_title),
                getString(R.string.onboarding_track_desc)
            ),
            OnboardingPageFragment.newInstance(
                getString(R.string.onboarding_terms_title),
                getString(R.string.onboarding_terms_desc)
            )
        )

        val adapter = OnboardingAdapter(this, pages)
        binding.viewPager.adapter = adapter
        binding.viewPager.setPageTransformer(DepthPageTransformer())

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == adapter.itemCount - 1) {
                    binding.btnNext.text = getString(R.string.onboarding_start_button)
                    val pulseAnimation = AnimationUtils.loadAnimation(context, R.anim.pulse)
                    binding.btnNext.startAnimation(pulseAnimation)
                } else {
                    binding.btnNext.text = getString(R.string.onboarding_next_button)
                    binding.btnNext.clearAnimation()
                }
            }
        })

        binding.btnNext.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            if (currentItem < adapter.itemCount - 1) {
                binding.viewPager.currentItem = currentItem + 1
            } else {
                onOnboardingFinished()
                findNavController().navigate(R.id.action_onboardingFragment_to_nav_plant_list)
            }
        }
    }

    private fun onOnboardingFinished() {
        requireActivity().getOnboardingPrefs().edit().setOnboardingFinished(true)
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.hideFab()
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }

    override fun onPause() {
        super.onPause()
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}