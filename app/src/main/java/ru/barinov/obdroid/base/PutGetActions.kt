package ru.barinov.obdroid.base

interface PutGetActions<T> {

    fun getValue(): T

    fun saveValue(value: T)
}
