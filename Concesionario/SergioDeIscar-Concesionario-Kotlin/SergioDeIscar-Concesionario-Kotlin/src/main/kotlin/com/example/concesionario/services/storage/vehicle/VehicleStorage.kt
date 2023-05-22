package com.example.concesionario.services.storage.vehicle

import com.example.concesionario.errors.VehicleError
import com.example.concesionario.models.Vehicle
import services.storage.StorageService

interface VehicleStorage: StorageService<Vehicle, VehicleError>