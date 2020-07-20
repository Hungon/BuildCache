package com.trials.sample001.record

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Record(
    @SerializedName("top_category_list")
    val topCategoryList: List<CategoryList>
) {
    data class CategoryList(
        @SerializedName("id_top_category")
        val idTopCategory: String,
        @SerializedName("name_category")
        val nameCategory: String,
        @SerializedName("sub_category_list")
        val subCategoryList: List<SubCategory>
    )

    data class SubCategory(
        @SerializedName("book_list")
        val bookList: List<Content>,
        @SerializedName("id_category")
        val idCategory: String,
        @SerializedName("is_ranking")
        val isRanking: Boolean,
        @SerializedName("name_category")
        val nameCategory: String,
        @SerializedName("need_load_more")
        val needLoadMore: Boolean
    )

    data class Content(
        val author: String,
        @SerializedName("create_at")
        val createAt: String,
        @SerializedName("has_contents")
        val hasContents: Int,
        @SerializedName("has_purchased")
        val hasPurchased: Boolean,
        @SerializedName("id_book")
        val idBook: String,
        @SerializedName("img_url")
        val imageUrl: String,
        @SerializedName("is_unlimited")
        val isUnlimited: Int,
        @SerializedName("name_book")
        val nameBook: String,
        val publisher: String
    ) : Serializable
}
