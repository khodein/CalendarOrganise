package ru.calendar.core.tools.formatter

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.monthsUntil
import kotlinx.datetime.periodUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until
import kotlinx.datetime.yearsUntil

@JvmInline
value class LocalDateFormatter(
    val localDateTime: LocalDateTime
) {
    val dayOfMonth: Int
        get() = localDateTime.dayOfMonth

    val dayOfWeek: DayOfWeek
        get() = localDateTime.dayOfWeek

    val dayOfYear: Int
        get() = this.localDateTime.dayOfYear

    /**Суббота или Воскресенье*/
    val isWeekend: Boolean
        get() {
            val dayOfWeek = this.localDateTime.dayOfWeek
            return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY
        }

    /**Кол-во недель в месяце*/
    val weeksOfMonth: Int
        get() = monthDays / 7

    /**Кол-во дней в месяце*/
    val monthDays: Int
        get() {
            val year = localDateTime.year
            val month = localDateTime.month
            val start = LocalDate(year, month, 1)
            val end = start.plus(1, DateTimeUnit.MONTH)
            return start.until(end, DateTimeUnit.DAY)
        }

    inline fun update(block: LocalDateTime.() -> LocalDateTime): LocalDateFormatter {
        return LocalDateFormatter(localDateTime.block())
    }

    /**Вернет дату первого дня месяца*/
    fun startDayOfMonth(
        timeZone: TimeZone = getSystemDefault()
    ): LocalDateFormatter {
        val year = localDateTime.year
        val month = localDateTime.month
        val localDateTime = LocalDate(year = year, month = month, dayOfMonth = 1)
            .atStartOfDayIn(timeZone)
            .toLocalDateTime(timeZone)
        return LocalDateFormatter(localDateTime)
    }

    /**Вернет дату последнего дня дня месяца*/
    fun endDayOfMonth(
        timeZone: TimeZone = getSystemDefault()
    ): LocalDateFormatter {
        val year = localDateTime.year
        val month = localDateTime.month
        val localDateTime = LocalDate(
            year = year,
            month = month,
            dayOfMonth = monthDays
        ).atStartOfDayIn(timeZone).toLocalDateTime(timeZone)
        return LocalDateFormatter(localDateTime)
    }

    /**Возвращает начало дня типо 2025-01-13T00:00*/
    fun startOfTheDay(
        timeZone: TimeZone = getSystemDefault()
    ): LocalDateFormatter {
        val startOfTheDay = localDateTime
            .date
            .atStartOfDayIn(timeZone)
            .toLocalDateTime(timeZone)
        return LocalDateFormatter(startOfTheDay)
    }

    /**Возвращает конец дня типо 2025-01-12T23:59:59*/
    fun endOfTheDay(
        timeZone: TimeZone = getSystemDefault()
    ): LocalDateFormatter {
        return startOfTheDay(timeZone)
            .plusDays(1)
            .setOperation(
                quantity = 1000,
                unit = DateTimeUnit.MILLISECOND,
                timeZone = timeZone,
                operator = Operator.MINUS
            )
    }

    /**Возвращает завтрашний день прибавив ровно 24 часа*/
    fun tomorrow(
        timeZone: TimeZone = getSystemDefault(),
    ): LocalDateFormatter {
        return this.setOperation(
            quantity = 1,
            unit = DateTimeUnit.DAY,
            timeZone = timeZone,
            operator = Operator.PLUS
        )
    }

    /**Возвращает вчерашний день отнимет 24 часа*/
    fun yesterday(
        timeZone: TimeZone = getSystemDefault(),
    ): LocalDateFormatter {
        return this.setOperation(
            quantity = 1,
            unit = DateTimeUnit.DAY,
            timeZone = timeZone,
            operator = Operator.MINUS
        )
    }

    /**Возвращает дату с прибаленным кол-вом дней*/
    fun plusDays(
        quantity: Int,
        timeZone: TimeZone = getSystemDefault(),
    ): LocalDateFormatter {
        return this.setOperation(
            quantity = quantity,
            unit = DateTimeUnit.DAY,
            timeZone = timeZone,
            operator = Operator.PLUS
        )
    }

    /**Возвращает дату с вычитом кол-вом дней*/
    fun minusDays(
        quantity: Int,
        timeZone: TimeZone = getSystemDefault(),
    ): LocalDateFormatter {
        val minusDays = localDateTime
            .toInstant(timeZone)
            .minus(
                value = quantity,
                unit = DateTimeUnit.DAY,
                timeZone = timeZone
            ).toLocalDateTime(timeZone)

        return LocalDateFormatter(minusDays)
    }

    /**Возвращает дату с прибалением кол-вом месяцев*/
    fun plusMonths(
        quantity: Int,
        timeZone: TimeZone = getSystemDefault(),
    ): LocalDateFormatter {
        return this.setOperation(
            quantity = quantity,
            unit = DateTimeUnit.MONTH,
            timeZone = timeZone,
            operator = Operator.PLUS
        )
    }

    /**Возвращает дату с вычитом кол-вом месяцев*/
    fun minusMonths(
        quantity: Int,
        timeZone: TimeZone = getSystemDefault(),
    ): LocalDateFormatter {
        return this.setOperation(
            quantity = quantity,
            unit = DateTimeUnit.MONTH,
            timeZone = timeZone,
            operator = Operator.MINUS
        )
    }

    /**Возвращает дату с прибавлением кол-вом лет*/
    fun plusYears(
        quantity: Int,
        timeZone: TimeZone = getSystemDefault(),
    ): LocalDateFormatter {
        return this.setOperation(
            quantity = quantity,
            unit = DateTimeUnit.YEAR,
            timeZone = timeZone,
            operator = Operator.PLUS
        )
    }

    /**Возвращает дату с вычитом кол-вом лет*/
    fun minusYears(
        quantity: Int,
        timeZone: TimeZone = getSystemDefault()
    ): LocalDateFormatter {
        return this.setOperation(
            quantity = quantity,
            unit = DateTimeUnit.YEAR,
            timeZone = timeZone,
            operator = Operator.MINUS
        )
    }

    fun period(untilLocalDateFormatter: LocalDateFormatter): DatePeriod {
        val thisDate = localDateTime.date
        val untilDate = untilLocalDateFormatter.localDateTime.date
        return thisDate.periodUntil(untilDate)
    }

    fun between(
        untilLocalDateFormatter: LocalDateFormatter,
        dateBased: DateTimeUnit.DateBased
    ): Int {
        val thisDate = localDateTime.date
        val untilDate = untilLocalDateFormatter.localDateTime.date
        val period = thisDate.until(untilDate, dateBased)
        return period
    }

    fun betweenDays(untilLocalDateFormatter: LocalDateFormatter): Int {
        return localDateTime.date.daysUntil(untilLocalDateFormatter.localDateTime.date)
    }

    fun betweenMonths(untilLocalDateFormatter: LocalDateFormatter): Int {
        return localDateTime.date.monthsUntil(untilLocalDateFormatter.localDateTime.date)
    }

    fun betweenYeats(untilLocalDateFormatter: LocalDateFormatter): Int {
        return localDateTime.date.yearsUntil(untilLocalDateFormatter.localDateTime.date)
    }

    private enum class Operator {
        PLUS,
        MINUS
    }

    companion object {
        private fun LocalDateFormatter.setOperation(
            quantity: Int,
            unit: DateTimeUnit,
            timeZone: TimeZone,
            operator: Operator,
        ): LocalDateFormatter {
            val instant = this@setOperation.localDateTime.toInstant(timeZone)
            val newInstant = when (operator) {
                Operator.PLUS -> {
                    instant.plus(
                        value = quantity,
                        unit = unit,
                        timeZone = timeZone
                    )
                }

                Operator.MINUS -> {
                    instant.minus(
                        value = quantity,
                        unit = unit,
                        timeZone = timeZone
                    )
                }
            }
            val localDateTime = newInstant.toLocalDateTime(timeZone)
            return LocalDateFormatter(localDateTime)
        }

        private fun getInstant(): Instant {
            return Clock.System.now()
        }

        private fun getSystemDefault(): TimeZone {
            return TimeZone.currentSystemDefault()
        }

        /**Возвращает дату сегодня в начале дня*/
        fun today(timeZone: TimeZone = getSystemDefault()): LocalDateFormatter {
            return nowInSystemDefault().startOfTheDay(timeZone)
        }

        /**Возвращает дату, по UTC формату*/
        fun nowInUTC(): LocalDateFormatter {
            return LocalDateFormatter(getInstant().toLocalDateTime(TimeZone.UTC))
        }

        /**Возвращает дату, сейчас, по таймзоне телефона*/
        fun nowInSystemDefault(): LocalDateFormatter {
            return LocalDateFormatter(getInstant().toLocalDateTime(getSystemDefault()))
        }
    }
}