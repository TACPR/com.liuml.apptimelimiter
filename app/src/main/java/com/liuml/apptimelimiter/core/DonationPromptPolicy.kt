package com.liuml.apptimelimiter.core

data class DonationPromptLaunchDecision(
    val shouldPrompt: Boolean,
    val firstUseEpochDay: Long,
    val lastPromptEpochDay: Long?,
    val launchesSincePrompt: Int,
)

object DonationPromptPolicy {
    const val REPEAT_INTERVAL_DAYS = 3L
    const val REPEAT_LAUNCH_COUNT = 5
    private const val MAX_STORED_LAUNCH_COUNT = 10_000

    fun firstUseDay(storedEpochDay: Long?, todayEpochDay: Long): Long =
        storedEpochDay ?: todayEpochDay

    fun onLaunch(
        storedFirstUseEpochDay: Long?,
        storedLastPromptEpochDay: Long?,
        storedLaunchesSincePrompt: Int,
        todayEpochDay: Long,
        disabled: Boolean,
    ): DonationPromptLaunchDecision {
        val firstUseEpochDay = firstUseDay(storedFirstUseEpochDay, todayEpochDay)
        if (disabled) {
            return DonationPromptLaunchDecision(
                shouldPrompt = false,
                firstUseEpochDay = firstUseEpochDay,
                lastPromptEpochDay = storedLastPromptEpochDay,
                launchesSincePrompt = storedLaunchesSincePrompt.coerceIn(
                    0,
                    MAX_STORED_LAUNCH_COUNT,
                ),
            )
        }
        val launches = (storedLaunchesSincePrompt.coerceIn(
            0,
            MAX_STORED_LAUNCH_COUNT - 1,
        ) + 1)
        val firstPromptDue = storedLastPromptEpochDay == null
        val dayDistance = storedLastPromptEpochDay?.let { todayEpochDay - it }
        val repeatPromptDue =
            dayDistance != null &&
                dayDistance > 0L &&
                (
                    dayDistance >= REPEAT_INTERVAL_DAYS ||
                        launches >= REPEAT_LAUNCH_COUNT
                    )
        val shouldPrompt = firstPromptDue || repeatPromptDue
        return DonationPromptLaunchDecision(
            shouldPrompt = shouldPrompt,
            firstUseEpochDay = firstUseEpochDay,
            lastPromptEpochDay = if (shouldPrompt) {
                todayEpochDay
            } else {
                storedLastPromptEpochDay
            },
            launchesSincePrompt = if (shouldPrompt) 0 else launches,
        )
    }
}
