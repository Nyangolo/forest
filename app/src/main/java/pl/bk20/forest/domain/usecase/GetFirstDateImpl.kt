package pl.bk20.forest.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import pl.bk20.forest.domain.repository.DayRepository
import java.time.LocalDate

class GetFirstDateImpl(
    private val dayRepository: DayRepository
) : GetFirstDate {

    override fun invoke(): Flow<LocalDate?> {
        return dayRepository.getFirstDay().transform { it?.date }
    }
}