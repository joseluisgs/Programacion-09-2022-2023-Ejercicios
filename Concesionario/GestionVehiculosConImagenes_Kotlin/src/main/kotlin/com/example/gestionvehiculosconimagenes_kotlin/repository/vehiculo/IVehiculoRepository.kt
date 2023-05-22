package com.example.gestionvehiculosconimagenes_kotlin.repository.vehiculo

import com.example.gestionvehiculosconimagenes_kotlin.model.TipoMotor
import com.example.gestionvehiculosconimagenes_kotlin.model.Vehiculo
import com.example.gestionvehiculosconimagenes_kotlin.repository.base.CrudRepository

interface IVehiculoRepository: CrudRepository<Vehiculo, Long> {
    fun getByMarcaModelo(marcaModelo: String): List<Vehiculo>
    fun getByTipoMotor(tipoMotor: TipoMotor): List<Vehiculo>
}