package com.ebnbin.eb.githubapi.model

import com.ebnbin.eb.util.EBModel

class Content : EBModel {
    lateinit var name: String
    lateinit var content: String
    lateinit var sha: String
}
