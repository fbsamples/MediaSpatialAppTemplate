// Copyright (c) Meta Platforms, Inc. and affiliates.

// This source code is licensed under the MIT license found in the
// LICENSE file in the root directory of this source tree.

package com.meta.media.template

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class VideoFragment : Fragment() {
  val viewModel by activityViewModels<AppViewModel>()
  private var player: ExoPlayer? = null
  private lateinit var playerView: PlayerView

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    val view = inflater.inflate(R.layout.fragment_video, container, false)
    playerView = view.findViewById(R.id.player_view)
    initializePlayer()
    viewModel.currentMedia.observe(
        viewLifecycleOwner,
        Observer {
          playVideo(
              it.mediaUrl
                  ?: "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
        })
    val backButton = view.findViewById<ImageButton>(R.id.back_button)
    backButton.setOnClickListener { activity?.onBackPressed() }
    return view
  }

  private fun initializePlayer() {
    player = ExoPlayer.Builder(requireContext()).build()
    playerView.player = player
  }

  fun playVideo(mediaUrl: String) {
    val mediaItem = MediaItem.fromUri(mediaUrl)
    player?.setMediaItem(mediaItem)
    player?.prepare()
    player?.play()
  }

  override fun onStop() {
    super.onStop()
    player?.release()
    player = null
  }
}
