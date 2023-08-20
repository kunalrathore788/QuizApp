package com.hello_groups.quizai.Models

data class Categories_Model(private var icon: String, private var title: String, private var categoryId: String) {
    // Empty constructor
    constructor() : this("", "", "")
    // Initialized constructor
    constructor(icon: String, title: String) : this(icon, title, "")

    // Getter and Setter for icon
    fun getIcon(): String {
        return icon
    }

    fun setIcon(icon: String) {
        this.icon = icon
    }

    // Getter and Setter for title
    fun getTitle(): String {
        return title
    }

    fun setTitle(title: String) {
        this.title = title
    }

    // Getter and Setter for categoryId
    fun getCategoryId(): String {
        return categoryId
    }

    fun setCategoryId(categoryId: String) {
        this.categoryId = categoryId
    }
}
