package com.example.gestionvehiculosconimagenes_kotlin.service.storage

import com.example.gestionvehiculosconimagenes_kotlin.error.VehiculoError
import com.github.michaelbull.result.Result
import java.io.File

interface IStorageImages {
    fun saveImage(fileName: File): Result<File, Unit>
    fun loadImage(fileName: String): Result<File, Unit>
    fun deteleImage(fileName: File): Result<Unit, VehiculoError>
    fun updateImage(oldImage: File, newImage: File): Result<File, Unit>
}