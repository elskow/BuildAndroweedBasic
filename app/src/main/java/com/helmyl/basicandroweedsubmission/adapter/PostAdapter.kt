package com.helmyl.basicandroweedsubmission.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.chip.Chip
import com.helmyl.basicandroweedsubmission.R
import com.helmyl.basicandroweedsubmission.databinding.ItemPostBinding
import com.helmyl.basicandroweedsubmission.model.Post

class PostAdapter(
    private var posts: List<Post>, private val onItemClick: (Post, ImageView) -> Unit
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    fun updatePosts(newPosts: List<Post>) {
        val diffResult = DiffUtil.calculateDiff(PostDiffCallback(posts, newPosts))
        posts = newPosts
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.apply {
                postImage.transitionName = "image_${post.id}"
                postTitle.text = post.title
                postDescription.text = post.body

                postImage.load(post.getImageUrl()) {
                    crossfade(true)
                    placeholder(R.drawable.placeholder_image)
                }

                likesCount.text = post.reactions.likes.toString()
                dislikesCount.text = post.reactions.dislikes.toString()
                viewsCount.text = post.views.toString()

                postTags.removeAllViews()
                post.tags.forEach { tag ->
                    val chip = Chip(root.context).apply {
                        text = tag
                        isCheckable = false
                        setChipBackgroundColorResource(R.color.chip_background)
                        setTextColor(ContextCompat.getColor(context, R.color.chip_text))
                        setTextAppearance(R.style.ChipTextStyle)
                        chipStrokeWidth = 0f
                        chipStartPadding = 12f
                        chipEndPadding = 12f
                    }
                    postTags.addView(chip)
                }

                root.setOnClickListener { onItemClick(post, postImage) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount() = posts.size
}


class PostDiffCallback(
    private val oldList: List<Post>, private val newList: List<Post>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}
