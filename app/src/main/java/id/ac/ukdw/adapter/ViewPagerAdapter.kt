package id.ac.ukdw.adapter

import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import id.ac.ukdw.drones_isai.ui.EmisiKarbonFragment
import id.ac.ukdw.drones_isai.ui.KarbonTerserapFragment
import id.ac.ukdw.drones_isai.ui.NilaiAgregatFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val instantiatedFragments: SparseArray<Fragment> = SparseArray()

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> KarbonTerserapFragment()
            1 -> EmisiKarbonFragment()
            2 -> NilaiAgregatFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment
        instantiatedFragments.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        instantiatedFragments.remove(position)
        super.destroyItem(container, position, obj)
    }

    fun getFragment(position: Int): Fragment? {
        return instantiatedFragments[position]
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Karbon Terserap"
            1 -> "Emisi Karbon"
            2 -> "Nilai Agregat"
            else -> null
        }
    }
}

