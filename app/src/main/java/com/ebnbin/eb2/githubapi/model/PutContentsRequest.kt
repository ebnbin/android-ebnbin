package com.ebnbin.eb2.githubapi.model

import com.ebnbin.eb2.util.EBModel

class PutContentsRequest : EBModel {
    lateinit var message: String
    lateinit var content: String
    var sha: String? = null
}
