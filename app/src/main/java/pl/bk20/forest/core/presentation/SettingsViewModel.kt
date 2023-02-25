package pl.bk20.forest.core.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import pl.bk20.forest.ForestApplication
import pl.bk20.forest.core.data.repository.DayRepositoryImpl
import pl.bk20.forest.core.data.repository.SettingsRepositoryImpl
import pl.bk20.forest.core.domain.model.DayParameters
import pl.bk20.forest.core.domain.usecase.DayUseCases
import pl.bk20.forest.core.domain.usecase.SettingsUseCases
import java.time.LocalDate

class SettingsViewModel(
    private val dayUseCases: DayUseCases,
    private val settingsUseCases: SettingsUseCases
) : ViewModel() {

    private var observeSettingsChangesJob: Job? = null

    fun observeSettingsChanges() {
        observeSettingsChangesJob?.cancel()
        observeSettingsChangesJob = settingsUseCases.getSettings().onEach {
            dayUseCases.updateDayParameters(
                DayParameters(
                    date = LocalDate.now(),
                    goal = it.dailyGoal,
                    height = it.height,
                    weight = it.weight,
                    stepLength = it.stepLength,
                    pace = it.pace
                )
            )
        }.launchIn(viewModelScope)
    }

    companion object Factory : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            Log.d("SettingsViewModel", "Creating")
            val application = checkNotNull(extras[APPLICATION_KEY]) as ForestApplication

            val settingsStore = application.settingsStore
            val settingsRepository = SettingsRepositoryImpl(settingsStore)
            val settingsUseCases = SettingsUseCases(settingsRepository)

            val dayDatabase = application.forestDatabase
            val dayRepository = DayRepositoryImpl(dayDatabase.dayDao)
            val dayUseCases = DayUseCases(dayRepository, settingsRepository)

            return SettingsViewModel(dayUseCases, settingsUseCases) as T
        }
    }
}