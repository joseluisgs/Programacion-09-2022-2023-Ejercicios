package es.sergiomisas.concesionario.errors

sealed class CocheError(val message: String) {
    class LoadJson(message: String) : CocheError(message)
    class SaveJson(message: String) : CocheError(message)
    class LoadImage(message: String) : CocheError(message)
    class SaveImage(message: String) : CocheError(message)
    class DeleteImage(message: String) : CocheError(message)
    class DeleteById(message: String) : CocheError(message)
    class ValidationProblem(message: String) : CocheError(message)
    class NotFound(message: String) : CocheError(message)
    class ExportZip(message: String) : CocheError(message)
    class ImportZip(message: String) : CocheError(message)
}
