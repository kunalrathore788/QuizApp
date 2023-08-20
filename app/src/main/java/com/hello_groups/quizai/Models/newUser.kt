package com.hello_groups.quizai.Models

class newUser {
    private var name: String? = null
    private var email: String? = null
    private var pass: String? = null
    private var referCode: String? = null
    private var profile: String? = null
    private var coins: Long? = null

    constructor() {}

    constructor(name: String?, email: String?, pass: String?, referCode: String?, profile: String?, coins: Long) {
        this.name = name
        this.email = email
        this.pass = pass
        this.referCode = referCode
        this.profile = profile
        this.coins = coins
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun getPass(): String? {
        return pass
    }

    fun setPass(pass: String?) {
        this.pass = pass
    }

    fun getReferCode(): String? {
        return referCode
    }

    fun setReferCode(referCode: String?) {
        this.referCode = referCode
    }

    fun getProfile(): String? {
        return profile
    }

    fun setProfile(profile: String?) {
        this.profile = profile
    }

    fun getCoins(): Long? {
        return coins
    }

    fun setCoins(coins: Long) {
        this.coins = coins
    }

}