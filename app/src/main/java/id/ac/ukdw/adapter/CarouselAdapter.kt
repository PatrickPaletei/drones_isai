package id.ac.ukdw.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import id.ac.ukdw.drones_isai.ui.tentang.Tentang1Fragment
import id.ac.ukdw.drones_isai.ui.tentang.Tentang2Fragment
import id.ac.ukdw.drones_isai.ui.tentang.Tentang3Fragment
import id.ac.ukdw.drones_isai.ui.tentang.Tentang4Fragment
import id.ac.ukdw.drones_isai.ui.tentang.Tentang5Fragment

class CarouselAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        Tentang1Fragment(),
        Tentang2Fragment(),
        Tentang3Fragment(),
        Tentang4Fragment(),
        Tentang5Fragment()

    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}