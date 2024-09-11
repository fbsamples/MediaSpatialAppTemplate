// Copyright (c) Meta Platforms, Inc. and affiliates.

// This source code is licensed under the MIT license found in the
// LICENSE file in the root directory of this source tree.

package com.meta.media.template

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.google.gson.Gson
import java.io.InputStream

class MainFragment : Fragment() {
  private var movieList: ArrayList<MediaTitle> = ArrayList()
  private val viewModel by activityViewModels<AppViewModel>()
  private lateinit var currentView: View

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View {
    currentView = inflater.inflate(R.layout.fragment_main, container, false)
    populateMediaShelves(arrayOf("Popular", "Trending", "Evergreen"))
    return currentView
  }

  private fun populateMediaShelves(shelves: Array<String>) {
    val jsonString = readJsonFromAsset("open-movies.json")
    Gson().fromJson(jsonString, Array<MediaTitle>::class.java).forEach { movieList.add(it) }
    shelves.forEach { shelfName -> addMediaShelf(shelfName, movieList!!) }
  }

  private fun addMovieTitle(title: MediaTitle, shelfView: LinearLayout) {
    val movieTitleView = LayoutInflater.from(requireContext()).inflate(R.layout.media_title, null)
    val imageButton = movieTitleView.findViewById<ImageButton>(R.id.media_title_button)
    Glide.with(requireContext()).load(title.thumbnailUrl).into(imageButton)

    imageButton.setOnClickListener {
      // Executed when clicked on a media title
      switchToMediaScreen(title.videoUrl)
    }

    val titleTextView = movieTitleView.findViewById<TextView>(R.id.media_title_text)
    titleTextView.text = title.title
    shelfView.addView(movieTitleView)
  }

  private fun switchToMediaScreen(mediaUrl: String) {
    parentFragmentManager.commit {
      addToBackStack("video")
      viewModel.updateMedia(MediaData(mediaUrl))
      replace(R.id.container, VideoFragment())
    }
  }

  private fun addMediaShelf(shelfTitle: String, contents: ArrayList<MediaTitle>) {
    val shelfView = LayoutInflater.from(requireContext()).inflate(R.layout.media_shelf, null)
    val titleStack = shelfView.findViewById<LinearLayout>(R.id.titles_stack)
    val titleTextView = shelfView.findViewById<TextView>(R.id.shelf_title)
    titleTextView.text = shelfTitle

    // Shuffle the movie array from json and take the top 3, populate those into the shelf
    contents.shuffle()
    for (i in 0..2) {
      val content = contents.get(i)
      addMovieTitle(content, titleStack)
    }

    // Add movie titles dynamically to titles_stack here using the addMovieTitle()
    val rootConstraintLayout = currentView?.findViewById<LinearLayout>(R.id.media_shelves_view)
    rootConstraintLayout?.addView(shelfView)
  }

  private fun readJsonFromAsset(fileName: String): String {
    val inputStream: InputStream = resources.assets.open(fileName)
    val size = inputStream.available()
    val buffer = ByteArray(size)
    inputStream.read(buffer, 0, size)
    inputStream.close()
    return String(buffer)
  }

  companion object {
    fun newInstance() = MainFragment()
  }
}
