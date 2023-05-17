package id.ac.ukdw.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.ac.ukdw.drones_isai.EmisiKarbonFragment
import id.ac.ukdw.drones_isai.KarbonTerserapFragment
import id.ac.ukdw.drones_isai.NilaiHSTFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> KarbonTerserapFragment()
            1 -> EmisiKarbonFragment()
            2 -> NilaiHSTFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Karbon Terserap"
            1 -> "Emisi Karbon"
            2 -> "Nilai HST"
            else -> null
        }
    }
}