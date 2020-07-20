package com.trials.sample001.db

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ContentEntity : RealmObject() {

    @PrimaryKey
    var idBook: String = ""
    var author: String = ""
    var createAt: String = ""
    var hasContents: Int = 0
    var hasPurchased: Boolean = false
    var hasRegistered: Boolean = false
    var imageUrl: String = ""
    var isUnlimited: Int = 0
    var nameBook: String = ""
    var publisher: String = ""
    
    fun copy(
        idBook: String = this.idBook,
        author: String = this.author,
        createAt: String = this.createAt,
        hasContents: Int = this.hasContents,
        hasPurchased: Boolean = this.hasPurchased,
        hasRegistered: Boolean = this.hasRegistered,
        imageUrl: String = this.imageUrl,
        isUnlimited: Int = this.isUnlimited,
        nameBook: String = this.nameBook,
        publisher: String = this.publisher
    ): ContentEntity {
        return ContentEntity().apply {
            this.idBook = idBook
            this.author = author
            this.createAt = createAt
            this.hasContents = hasContents
            this.hasPurchased = hasPurchased
            this.hasRegistered = hasRegistered
            this.imageUrl = imageUrl
            this.isUnlimited = isUnlimited
            this.nameBook = nameBook
            this.publisher = publisher
        }
    }
}
