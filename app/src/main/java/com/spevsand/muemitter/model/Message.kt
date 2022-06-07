package com.spevsand.muemitter.model

import com.google.firebase.firestore.Exclude

class Message(
    nameIn: String = "",
    bodyIn: String = "",
    unixTimeIn: Long = 0,
    senderUidIn: String = ""
) {
    var name = nameIn
    var body = bodyIn
    var senderUid = senderUidIn
    var unixTime = unixTimeIn

    @Exclude
    @get: Exclude
    var id = ""
}