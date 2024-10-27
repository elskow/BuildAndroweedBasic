package com.helmyl.basicandroweedsubmission

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.lifecycleScope
import coil.load
import com.google.android.material.chip.Chip
import com.helmyl.basicandroweedsubmission.databinding.ActivityDetailBinding
import com.helmyl.basicandroweedsubmission.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var post: Post? = null
    private var shareJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        post = intent.getParcelableExtra(EXTRA_POST)

        binding.fragmentDetail.shareButton.setOnClickListener {
            sharePost()
        }

        binding.fragmentDetail.detailImage.transitionName = "image_${post?.id}"

        displayPostDetails(post)

        binding.fragmentDetail.detailImage.doOnPreDraw {
            startPostponedEnterTransition()
        }

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun displayPostDetails(post: Post?) {
        post?.let {
            binding.apply {
                val detailContent = fragmentDetail
                detailContent.detailTitle.text = it.title
                detailContent.detailImage.load(it.getImageUrl()) {
                    crossfade(true)
                    placeholder(R.drawable.placeholder_image)
                }
                detailContent.detailBody.text = it.body
                detailContent.detailLikesCount.text = it.reactions.likes.toString()
                detailContent.detailDislikesCount.text = it.reactions.dislikes.toString()
                detailContent.detailViewsCount.text = it.views.toString()

                detailContent.backButton.setOnClickListener { onBackPressed() }

                it.tags.forEach { tag ->
                    val chip = Chip(this@DetailActivity).apply {
                        text = tag
                        isCheckable = false
                    }
                    detailContent.detailTags.addView(chip)
                }

                fragmentDetail.detailTitle.alpha = 0f
                fragmentDetail.detailBody.alpha = 0f
                fragmentDetail.detailTags.alpha = 0f

                fragmentDetail.detailTitle.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(300)
                    .setStartDelay(300)
                    .start()

                fragmentDetail.detailBody.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(300)
                    .setStartDelay(400)
                    .start()

                fragmentDetail.detailTags.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(300)
                    .setStartDelay(500)
                    .start()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sharePost() {
        shareJob?.cancel()

        shareJob = lifecycleScope.launch(Dispatchers.IO) {
            post?.let { post ->
                val shareText = buildString {
                    append(post.title)
                    append("\n\n")
                    append(post.body)
                    append("\n\n")
                    append("Tags: ")
                    append(post.tags.joinToString(", "))
                    append("\n\n")
                    append("Likes: ${post.reactions.likes} | Views: ${post.views}")
                }

                withContext(Dispatchers.Main) {
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TITLE, post.title)
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }

                    try {
                        startActivity(Intent.createChooser(shareIntent, "Share Post"))
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@DetailActivity,
                            "Unable to share at this moment",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        shareJob?.cancel()
    }

    companion object {
        const val EXTRA_POST = "extra_post"
    }
}
