package com.adedom.teg.request.image

import io.ktor.locations.Location

@Location("/api/image/{imageFile}")
data class ImageRequest(val imageFile: String)
