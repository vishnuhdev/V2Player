package com.player.v2player.models

data class VideoDetails(
    val folderName: String,
    val videoPaths: List<String>
) {
    companion object {
        fun fromMap(map: MutableMap<String, MutableList<String>>): List<VideoDetails> {
            return map.entries.map { (folderName, videoPaths) ->
                VideoDetails(folderName, videoPaths)
            }
        }
    }
}

