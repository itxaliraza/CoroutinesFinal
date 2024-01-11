package com.practice.coroutines.ui.gallery

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.practice.coroutines.databinding.ActivityGalleryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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



            lifecycleScope.launch {
                galleryViewModel.counterPhotoVideos.flowWithLifecycle(
                    lifecycle,
                    Lifecycle.State.STARTED
                ).collectLatest {
                    if (it.first != null) {
                        tabLayout.getTabAt(0)?.text = "Photo (${it.first})"
                    } else
                        tabLayout.getTabAt(0)?.text = tabsArray[0]


                    if (it.second != null) {
                        tabLayout.getTabAt(1)?.text = "Video (${it.second})"
                    } else
                        tabLayout.getTabAt(1)?.text = tabsArray[1]

                }
            }

        }


        binding.ivRefresh.setOnClickListener {
            galleryViewModel.fetchGalleryItemsGoodPractice()
        }
    }
}