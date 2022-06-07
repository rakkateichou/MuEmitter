package com.spevsand.muemitter.model

import com.google.firebase.database.Exclude

@Suppress("PropertyName")
class AudioData(
    audioTitleIn: String = "",
    audioOwnerIn: String = "",
    audioMaxLengthIn: Int = 0,
    audioUrlIn: String = "",
    audioTimeIn: Int = 0
) {
    var audio_title = audioTitleIn
    var audio_owner = audioOwnerIn
    var audio_max_length = audioMaxLengthIn
    var audio_url = audioUrlIn
    var audio_time = audioTimeIn

    @Exclude
    @get: Exclude
    var id = ""
}