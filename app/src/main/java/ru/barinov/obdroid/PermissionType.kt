package ru.barinov.obdroid

sealed class PermissionType{

    data class RuntimeLocation(val granted : Boolean) : PermissionType()

    data class BackGroundLocation(val granted : Boolean) : PermissionType()

    data class Doze(val granted : Boolean) : PermissionType()

    data class BluetoothPermission(val granted : Boolean) : PermissionType()





}
