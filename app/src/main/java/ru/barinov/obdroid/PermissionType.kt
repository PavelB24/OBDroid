package ru.barinov.obdroid

sealed class PermissionType(open val granted: Boolean){

    data class RuntimeLocation(override val granted : Boolean) : PermissionType(granted)

    data class BackGroundLocation(override val granted : Boolean) : PermissionType(granted)

    data class Doze(override val granted : Boolean) : PermissionType(granted)

    data class BluetoothPermission(override val granted : Boolean) : PermissionType(granted)

    data class WiFiPermission(override val granted : Boolean) : PermissionType(granted)

    data class FileSystemPermission(override val granted : Boolean) : PermissionType(granted)

}
