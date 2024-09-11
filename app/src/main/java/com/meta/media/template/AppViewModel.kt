// Copyright (c) Meta Platforms, Inc. and affiliates.

// This source code is licensed under the MIT license found in the
// LICENSE file in the root directory of this source tree.

package com.meta.media.template

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {
  private val mutableMediaItem = MutableLiveData<MediaData>()
  val currentMedia: LiveData<MediaData>
    get() = mutableMediaItem

  fun updateMedia(mediaItem: MediaData) {
    mutableMediaItem.value = mediaItem
  }
}

data class MediaData(
    val mediaUrl: String,
)
