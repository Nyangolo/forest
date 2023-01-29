package pl.bk20.forest.domain.usecase

import pl.bk20.forest.domain.repository.DayRepository
import pl.bk20.forest.domain.repository.SettingsRepository

class DayUseCases(
    dayRepository: DayRepository,
    settingsRepository: SettingsRepository
) {

    val getDay: GetDay = GetDayImpl(dayRepository, settingsRepository)
    val incrementStepCount: IncrementStepCount = IncrementStepCountImpl(dayRepository, getDay)
    val updateDayParameters: UpdateDayParameters = UpdateDayParametersImpl(dayRepository)
}