package utils.model

data class DeviceConfig(val device: String, val apiLevel: Int, val systemImageSource: String) {
    override fun toString() = buildString {
        append(device.lowercase().replace(" ", ""))
        append("api")
        append(apiLevel.toString())
        append(systemImageSource.replace("-", ""))
    }
}
