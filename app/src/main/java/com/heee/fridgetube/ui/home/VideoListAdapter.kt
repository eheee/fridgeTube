package com.heee.fridgetube.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import com.heee.fridgetube.R
import com.heee.fridgetube.data.RecipeCard
import com.heee.fridgetube.databinding.RecyclerViewRecipeBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class VideoListAdapter(
    private val lifecycle: Lifecycle,
    val itemClickListener: OnItemClickListener,
) :
    RecyclerView.Adapter<VideoListAdapter.ViewHolder>() {
    private var recipeCards = listOf<RecipeCard>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            RecyclerViewRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        lifecycle.addObserver(binding.youtubePlayerView)

        return ViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipeCard = recipeCards[position]
        holder.binding.root.setOnClickListener {
            itemClickListener.onItemClicked(recipeCard.recipe.videoId)
        }
        holder.readyVideo(recipeCard.recipe.videoId)

        val inFridge = recipeCard.inFridge.map { it.name }
        val notInFridge = recipeCard.notInFridge.map { it.name }

        holder.binding.tvVideoTitle.text = "${recipeCard.recipe.name}"
        holder.binding.tvItemInFridge.text = "있는 재료 : $inFridge"
        holder.binding.tvItemNotInFridge.text = "없는 재료 : $notInFridge"
    }

    override fun getItemCount(): Int = recipeCards.size

    fun setRecipes(recipeCards: List<RecipeCard>) {
        this.recipeCards = recipeCards
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClicked(videoId: String)
    }

    class ViewHolder(
        val binding: RecyclerViewRecipeBinding,
        onItemClickListener: OnItemClickListener,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private var youtubePlayer: YouTubePlayer? = null
        private var currentVideoId: String? = null

        init {
            val customPlayerUI =
                binding.youtubePlayerView.inflateCustomPlayerUi(R.layout.custom_youtube_player_ui)
            val panel = customPlayerUI.findViewById<View>(R.id.panel)
            // To Apply the click event on WebView.
            panel.setOnClickListener {
                onItemClickListener.onItemClicked(currentVideoId!!)
            }

            binding.youtubePlayerView.addYouTubePlayerListener(object :
                AbstractYouTubePlayerListener() {
                override fun onReady(initializedYouTubePlayer: YouTubePlayer) { // Called when the player is ready to play video
                    youtubePlayer = initializedYouTubePlayer
                    youtubePlayer?.cueVideo(currentVideoId!!, 0F)
                }
            })
        }

        fun readyVideo(videoId: String) {
            currentVideoId = videoId
        }
    }

}