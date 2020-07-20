package com.trials.sample001.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.trials.sample001.R
import com.trials.sample001.databinding.ActivityDetailBinding
import com.trials.sample001.db.ContentEntity
import com.trials.sample001.record.Record
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityDetailBinding>(
                this,
                R.layout.activity_detail
            )
        val viewModel = ViewModelProvider(this).get(DetailViewModel::class.java).apply {
            intent.extras?.let {
                val content = (it[ARG_CONTENT_SELECTED] as Record.Content)
                Picasso.get().load(content.imageUrl).into(image_book_selected)
                text_book_name.text = content.nameBook
                text_author.text = String.format(getString(R.string.text_author), content.author)
                text_publisher.text = String.format(getString(R.string.text_publisher, content.publisher))
                currentContentEntity = ContentEntity().apply {
                    idBook = content.idBook
                    nameBook = content.nameBook
                    hasContents = content.hasContents
                    isUnlimited = content.isUnlimited
                    hasPurchased = content.hasPurchased
                    hasRegistered = false
                    author = content.author
                    publisher = content.publisher
                    createAt = content.createAt
                    imageUrl = content.imageUrl
                }
            }
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        lifecycle.addObserver(viewModel)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.title_detail)
        }
    }

    companion object {
        const val ARG_CONTENT_SELECTED = "ARG_CONTENT_SELECTED"
    }
}
