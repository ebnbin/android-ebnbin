package com.ebnbin.eb2.githubapi.model

import com.ebnbin.eb2.util.EBModel

class Content : EBModel {
    lateinit var name: String
    lateinit var content: String
    lateinit var sha: String
}
