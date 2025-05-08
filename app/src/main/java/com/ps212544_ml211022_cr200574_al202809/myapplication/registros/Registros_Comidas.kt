package com.ps212544_ml211022_cr200574_al202809.myapplication.registros

data class Registros_Comidas (
    var id :String = "", // <- Campo para ID
    var url_foto: String? = "",
    var nombre: String = "",
    var descripcion: String = "",
    var precio: String = "",
    var dia : String = "",
    var tiempoDia : String = ""
) {

    //Constructor
    constructor() : this("", "", "", "", "", "", "")

    fun toMap() : Map<String, Any?> {
        return mapOf(
            "id" to id,
            "url_foto" to url_foto,
            "nombre" to nombre,
            "descripcion" to descripcion,
            "precio" to precio,
            "dia" to dia,
            "tiempoDia" to tiempoDia
        )
    }
}