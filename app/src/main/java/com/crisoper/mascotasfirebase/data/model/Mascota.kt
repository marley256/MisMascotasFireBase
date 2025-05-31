package com.crisoper.mascotasfirebase.data.model

import java.io.Serializable

data class Mascota(
    var id: String = "",
    var nombre: String = "",
    var historia: String = "",
    var edad: Int = 0,
    var fotoUrl: String? = null
) : Serializable