package id.ac.ukdw.drones_isai.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import id.ac.ukdw.adapter.CarouselAdapter
import id.ac.ukdw.drones_isai.R
import java.util.Timer
import java.util.TimerTask


class TentangFragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: CarouselAdapter
    private lateinit var timer: Timer
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler
    private lateinit var dotsContainer: ViewGroup
    private val dots = ArrayList<ImageView>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tentang, container, false)
        viewPager = view.findViewById(R.id.viewPager)
        dotsContainer = view.findViewById(R.id.dotsContainer)

        // Create and set the adapter for the ViewPager2
        adapter = CarouselAdapter(requireActivity())
        viewPager.adapter = adapter

        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                val newPosition = viewPager.currentItem + 1
                if (newPosition >= adapter.itemCount) {
                    viewPager.currentItem = 0 // Start from the beginning
                } else {
                    viewPager.currentItem = newPosition
                }
                handler.postDelayed(this, 6000) // Auto-scroll interval (1000ms = 1 seconds)
            }
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDots(position)
            }
        })

        setupDots(adapter.itemCount)
        startTimer()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
        handler.removeCallbacks(runnable)
    }

    private fun setupDots(count: Int) {
        for (i in 0 until count) {
            val dot = ImageView(requireContext())
            dot.setImageResource(R.drawable.dot_unselected)
            val params = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            dot.layoutParams = params
            dotsContainer.addView(dot)
            dots.add(dot)
        }

        updateDots(0) // Select the first dot initially
    }

    private fun updateDots(currentPosition: Int) {
        for (i in dots.indices) {
            val dot = dots[i]
            if (i == currentPosition) {
                dot.setImageResource(R.drawable.dot_selected)
            } else {
                dot.setImageResource(R.drawable.dot_unselected)
            }
        }
    }

    private fun startTimer() {
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(runnable)
            }
        }, 5000) // Initial delay before auto-scrolling starts (1000ms = 1 seconds)
    }
}