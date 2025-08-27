package hr.algebra.lorena.pocketbotanist.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import hr.algebra.lorena.pocketbotanist.ui.onboarding.OnboardingPageFragment

class OnboardingAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val pages = listOf(
        OnboardingPageFragment.newInstance(
            "Welcome to PocketBotanist",
            "Your digital companion to help you never forget to water your plants again. Soon adding more features!"
        ),
        OnboardingPageFragment.newInstance(
            "Track Your Plants",
            "Add your plants, set watering schedules, and keep notes on their progress."
        ),
        OnboardingPageFragment.newInstance(
            "Terms of Use",
            "By using this app, you agree to take great care of your plants. This is a very serious matter."
        )
    )

    override fun getItemCount(): Int = pages.size

    override fun createFragment(position: Int): Fragment = pages[position]
}