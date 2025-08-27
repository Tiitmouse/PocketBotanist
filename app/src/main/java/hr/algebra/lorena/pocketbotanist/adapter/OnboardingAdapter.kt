package hr.algebra.lorena.pocketbotanist.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import hr.algebra.lorena.pocketbotanist.ui.onboarding.OnboardingPageFragment

class OnboardingAdapter(fragment: Fragment, private val pages: List<OnboardingPageFragment>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = pages.size

    override fun createFragment(position: Int): Fragment = pages[position]
}