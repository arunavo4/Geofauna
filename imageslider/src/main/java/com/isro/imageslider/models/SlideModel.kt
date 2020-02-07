package com.isro.imageslider.models

import java.io.File

class SlideModel {

    var imageUrl: String? = null
    var imagePath: File? = null
    var title: String? = null
    var centerCrop = false

    constructor (imageUrl: String) {
        this.imageUrl = imageUrl
    }

    constructor (imagePath: File) {
        this.imagePath = imagePath
    }

    constructor (imageUrl: String, title: String?) {
        this.imageUrl = imageUrl
        this.title = title
    }

    constructor (imagePath: File, title: String?) {
        this.imagePath = imagePath
        this.title = title
    }

    constructor (imageUrl: String, centerCrop: Boolean) {
        this.imageUrl = imageUrl
        this.centerCrop = centerCrop
    }

    constructor (imagePath: File, centerCrop: Boolean) {
        this.imagePath = imagePath
        this.centerCrop = centerCrop
    }

    constructor (imagePath: File, title: String?, centerCrop: Boolean) {
        this.imagePath = imagePath
        this.title = title
        this.centerCrop = centerCrop
    }

    constructor (imageUrl: String, title: String?, centerCrop: Boolean) {
        this.imageUrl = imageUrl
        this.title = title
        this.centerCrop = centerCrop
    }

}