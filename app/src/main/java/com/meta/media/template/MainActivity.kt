// Copyright (c) Meta Platforms, Inc. and affiliates.

// This source code is licensed under the MIT license found in the
// LICENSE file in the root directory of this source tree.

package com.meta.media.template

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // The app contains fragments for different view.
    // By default, it loads the main Fragment which displays media shelf.
    // Click each media item will switch to video fragment to play video.
    if (savedInstanceState == null) {
      supportFragmentManager
          .beginTransaction()
          .replace(R.id.container, MainFragment.newInstance())
          .commitNow()
    }
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    // Need this to not re-create activity when app is resized
    super.onConfigurationChanged(newConfig)
  }
}

data class MediaTitle(
    val id: String,
    val title: String,
    val thumbnailUrl: String,
    val duration: String,
    val videoUrl: String,
    val description: String
)
