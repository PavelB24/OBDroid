package ru.barinov.obdroid.ui.uiModels

import ru.barinov.obdroid.domain.TroubleCodeType

data class TroubleCode(
    val code: String,
    val description: String,
    val rusTranslate: String?,
    val type: TroubleCodeType,
    val detectionDate: String?
)