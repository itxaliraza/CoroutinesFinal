package com.practice.coroutines.ui.gallery

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practice.coroutines.databinding.ActivityGalleryBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GalleryActivity : AppCompatActivity() {
    @Inject
    lateinit var binding: ActivityGalleryBinding

    val galleryViewModel: GalleryViewmodel by viewModels()

    val tabsArray = arrayOf(
        "Photo",
        "Video",
    )
    val adapter = GalleryPagerAdapter(supportFragmentManager, lifecycle)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        with(binding) {
            binding.galleryViewPager.adapter = adapter

            TabLayoutMediator(tabLayout, binding.galleryViewPager) { tab, position ->
                tab.text = tabsArray[position]
            }.attach()
        }

        binding.ivRefresh.setOnClickListener {
            galleryViewModel.fetchGalleryItemsGoodPractice()
        }
    }
}