package com.ebnbin.eb.net.githubapi.model

import com.ebnbin.eb.net.EBModel

class Content : EBModel {
    lateinit var name: String
    lateinit var content: String
    lateinit var sha: String
}
