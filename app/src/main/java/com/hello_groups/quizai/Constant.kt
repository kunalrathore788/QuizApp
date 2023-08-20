package com.hello_groups.quizai

import com.hello_groups.quizai.Models.Rv_Create_Model

object Constant {
    private lateinit var dataList:ArrayList<Rv_Create_Model>
    fun getData():ArrayList<Rv_Create_Model>{
        dataList = ArrayList<Rv_Create_Model>()
        dataList.add(Rv_Create_Model(R.drawable.bccc,"Create","Call Room"))
        dataList.add(Rv_Create_Model(R.drawable.bc,"Create","Quiz"))
        dataList.add(Rv_Create_Model(R.drawable.bcc,"Join","Quiz"))
        return dataList
    }
}