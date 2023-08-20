package com.hello_groups.quizai.Models

class Questions {
    private var questions: String
    private var option1: String
    private var option2: String
    private var option3: String
    private var option4: String
    private var answers: String

    constructor() {
        questions = ""
        option1 = ""
        option2 = ""
        option3 = ""
        option4 = ""
        answers = ""
    }

    constructor(
        questions: String,
        option1: String,
        option2: String,
        option3: String,
        option4: String,
        answers: String
    ) {
        this.questions = questions
        this.option1 = option1
        this.option2 = option2
        this.option3 = option3
        this.option4 = option4
        this.answers = answers
    }

    fun getQuestions(): String {
        return questions
    }

    fun setQuestions(questions: String) {
        this.questions = questions
    }

    fun getOption1(): String {
        return option1
    }

    fun setOption1(option1: String) {
        this.option1 = option1
    }

    fun getOption2(): String {
        return option2
    }

    fun setOption2(option2: String) {
        this.option2 = option2
    }

    fun getOption3(): String {
        return option3
    }

    fun setOption3(option3: String) {
        this.option3 = option3
    }

    fun getOption4(): String {
        return option4
    }

    fun setOption4(option4: String) {
        this.option4 = option4
    }

    fun getAnswers(): String {
        return answers
    }

    fun setAnswers(answers: String) {
        this.answers = answers
    }
}




