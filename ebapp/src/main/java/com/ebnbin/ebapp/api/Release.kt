package com.ebnbin.ebapp.api

class Release(
    var id: Int,
    var tag_name: String,
    var assets: List<Asset>
) {
    class Asset(
        var browser_download_url: String,
        var id: Int,
        var content_type: String
    )
}
