package com.rorycd.bowerbird.utils

import android.net.Uri

fun getFolderNameFromUri(uri: Uri?): String {
    return uri?.lastPathSegment?.substringAfter(":") ?: ""
}
