package com.practice.coroutines.ui.gallery.photoFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.practice.coroutines.databinding.FragmentMediaBinding
import com.practice.coroutines.model.MyResult
import com.practice.coroutines.ui.gallery.GalleryViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PhotoFragment : Fragment() {
    val galleryViewmodel: GalleryViewmodel by activityViewModels()

    @Inject
    lateinit var photoAdapter: PhotoAdapter

    @Inject
    lateinit var binding: FragmentMediaBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            rvMedia.apply {
                layoutManager = GridLayoutManager(context, 2)
                adapter = photoAdapter
            }

             lifecycleScope.launch {
                 galleryViewmodel.photosState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                     .collectLatest {
                         when (it) {
                             is MyResult.Failure -> {

                             }

                             MyResult.Idle -> {

                             }

                             MyResult.Loading -> {
                                 pbMain.visibility = View.VISIBLE
                             }

                             is MyResult.Success -> {
                                 pbMain.visibility = View.GONE
                                 photoAdapter.submitList(it.data)
                             }
                         }
                     }

             }

            /*galleryViewmodel.fetchPhotosBadPractice {
                CoroutineScope(Dispatchers.Main).launch {
                    binding.pbMain.visibility = View.GONE
                    photoAdapter.submitList(it)
                }
            }*/
        }
    }


}