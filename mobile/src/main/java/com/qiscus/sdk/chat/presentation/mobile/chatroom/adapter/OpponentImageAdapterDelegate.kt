package com.qiscus.sdk.chat.presentation.mobile.chatroom.adapter

import android.content.Context
import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.qiscus.sdk.chat.domain.model.FileAttachmentMessage
import com.qiscus.sdk.chat.domain.model.FileAttachmentProgress
import com.qiscus.sdk.chat.presentation.mobile.R
import com.qiscus.sdk.chat.presentation.model.MessageImageViewModel
import com.qiscus.sdk.chat.presentation.model.MessageViewModel
import com.qiscus.sdk.chat.presentation.uikit.adapter.ItemClickListener
import com.qiscus.sdk.chat.presentation.uikit.adapter.ItemLongClickListener
import com.qiscus.sdk.chat.presentation.uikit.widget.CircleProgress

/**
 * Created on : December 21, 2017
 * Author     : zetbaitsu
 * Name       : Zetra
 * GitHub     : https://github.com/zetbaitsu
 */
class OpponentImageAdapterDelegate @JvmOverloads constructor(private val context: Context,
                                                             private val itemClickListener: ItemClickListener? = null,
                                                             private val itemLongClickListener: ItemLongClickListener? = null)
    : MessageAdapterDelegate() {

    override fun isForViewType(data: SortedList<MessageViewModel>, position: Int): Boolean {
        val messageViewModel = data[position]
        return messageViewModel is MessageImageViewModel && messageViewModel.message.sender != account.user
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_qiscus_message_img, parent, false)
        return OpponentImageViewHolder(view, itemClickListener, itemLongClickListener)
    }
}

open class OpponentImageViewHolder @JvmOverloads constructor(view: View,
                                                             itemClickListener: ItemClickListener? = null,
                                                             itemLongClickListener: ItemLongClickListener? = null)
    : OpponentMessageViewHolder(view, itemClickListener, itemLongClickListener) {

    private val thumbnailView: ImageView = itemView.findViewById(R.id.thumbnail)
    private val captionView: TextView = itemView.findViewById(R.id.caption)
    private val downloadIconView: ImageView = itemView.findViewById(R.id.iv_download)
    private val progressView: CircleProgress = itemView.findViewById(R.id.progress)

    override fun determineDateView(): TextView {
        return itemView.findViewById(R.id.date)
    }

    override fun determineFirstMessageBubbleIndicatorView(): ImageView {
        return itemView.findViewById(R.id.bubble)
    }

    override fun determineBubbleView(): ViewGroup {
        return itemView.findViewById(R.id.message)
    }

    override fun determineTimeView(): TextView {
        return itemView.findViewById(R.id.time)
    }

    override fun determineSenderNameView(): TextView {
        return itemView.findViewById(R.id.name)
    }

    override fun determineSenderAvatarView(): ImageView {
        return itemView.findViewById(R.id.avatar)
    }

    override fun renderMessageContents(messageViewModel: MessageViewModel) {
        renderImage(messageViewModel as MessageImageViewModel)
        renderCaption(messageViewModel)
        renderDownloadIcon(messageViewModel)
    }

    open protected fun renderImage(messageViewModel: MessageImageViewModel) {
        if ((messageViewModel.message as FileAttachmentMessage).file != null) {
            Glide.with(thumbnailView)
                    .load((messageViewModel.message as FileAttachmentMessage).file)
                    .apply(RequestOptions().placeholder(R.drawable.qiscus_image_place_holder)
                            .error(R.drawable.qiscus_image_place_holder)
                    ).into(thumbnailView)
        } else {
            Glide.with(thumbnailView)
                    .load(messageViewModel.blurryThumbnail)
                    .apply(RequestOptions().placeholder(R.drawable.qiscus_image_place_holder)
                            .error(R.drawable.qiscus_image_place_holder)
                    ).into(thumbnailView)
        }
    }

    open protected fun renderCaption(messageViewModel: MessageImageViewModel) {
        if (messageViewModel.spannableCaption.isBlank()) {
            captionView.visibility = View.GONE
        } else {
            captionView.text = messageViewModel.spannableCaption
            captionView.visibility = View.VISIBLE
        }
    }

    open protected fun renderDownloadIcon(messageViewModel: MessageImageViewModel) {
        if ((messageViewModel.message as FileAttachmentMessage).file != null) {
            downloadIconView.visibility = View.GONE
            progressView.visibility = View.GONE
        } else {
            val attachmentProgress: FileAttachmentProgress? = messageViewModel.progress
            if (attachmentProgress != null && attachmentProgress.progress in 0..99) {
                progressView.progress = attachmentProgress.progress
                progressView.visibility = View.VISIBLE
                downloadIconView.visibility = View.GONE
            } else {
                progressView.visibility = View.GONE
                downloadIconView.visibility = View.VISIBLE
            }
        }
    }
}