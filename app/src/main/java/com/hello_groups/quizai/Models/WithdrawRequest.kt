package com.hello_groups.quizai.Models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class WithdrawRequest {
    private var userId: String? = null
    private var emailAddress: String? = null
    private var requestedBy: String? = null

    constructor() {}
    constructor(userId: String?, emailAddress: String?, requestedBy: String?) {
        this.userId = userId
        this.emailAddress = emailAddress
        this.requestedBy = requestedBy
    }

    fun getUserId(): String? {
        return userId
    }

    fun setUserId(userId: String?) {
        this.userId = userId
    }

    fun getEmailAddress(): String? {
        return emailAddress
    }

    fun setEmailAddress(emailAddress: String?) {
        this.emailAddress = emailAddress
    }

    fun getRequestedBy(): String? {
        return requestedBy
    }

    fun setRequestedBy(requestedBy: String?) {
        this.requestedBy = requestedBy
    }

    @ServerTimestamp
    private var createdAt: Date? = null

    fun getCreatedAt(): Date? {
        return createdAt
    }

    fun setCreatedAt(createdAt: Date?) {
        this.createdAt = createdAt
    }
}