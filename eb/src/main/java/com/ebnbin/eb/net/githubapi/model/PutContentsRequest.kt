package com.ebnbin.eb.net.githubapi.model

import com.ebnbin.eb.util.EBModel

class PutContentsRequest : EBModel {
    lateinit var message: String
    lateinit var content: String
    var sha: String? = null
}
