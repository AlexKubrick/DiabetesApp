package ru.alexkubrick.android.diabetesapp.presentation.main

import java.util.UUID

sealed class State {
    object Main : State()
    class Edit(val dataId: UUID) : State()
}
