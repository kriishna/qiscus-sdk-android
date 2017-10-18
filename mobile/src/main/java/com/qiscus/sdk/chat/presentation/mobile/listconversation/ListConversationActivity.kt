package com.qiscus.sdk.chat.presentation.mobile.listconversation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.qiscus.sdk.chat.presentation.listconversation.ListConversationContract
import com.qiscus.sdk.chat.presentation.mobile.R
import com.qiscus.sdk.chat.presentation.model.ConversationViewModel
import kotlinx.android.synthetic.main.activity_list_conversation.*

/**
 * Created on : October 04, 2017
 * Author     : zetbaitsu
 * Name       : Zetra
 * GitHub     : https://github.com/zetbaitsu
 */
class ListConversationActivity : AppCompatActivity(), ListConversationContract.View {
    private lateinit var listConversationPresenter: ListConversationContract.Presenter

    private val adapter = ConversationAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_conversation)

        conversationRecyclerView.adapter = adapter
        conversationRecyclerView.layoutManager = LinearLayoutManager(this)
        conversationRecyclerView.setHasFixedSize(true)
    }

    private fun init() {
        val activityComponent = ListConversationActivityComponent(this)
        listConversationPresenter = activityComponent.listConversationPresenter
        listConversationPresenter.start()
    }

    override fun onStart() {
        super.onStart()
        init()
    }

    override fun addOrUpdateConversation(conversationViewModel: ConversationViewModel) {
        adapter.addOrUpdate(conversationViewModel)
    }

    override fun removeConversation(conversationViewModel: ConversationViewModel) {
        adapter.removeConversation(conversationViewModel)
    }

    override fun onStop() {
        super.onStop()
        listConversationPresenter.stop()
    }
}