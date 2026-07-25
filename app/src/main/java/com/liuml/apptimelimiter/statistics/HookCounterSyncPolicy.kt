package com.liuml.apptimelimiter.statistics

object HookCounterSyncPolicy {
    /**
     * System UsageStats can produce duration and launch counts without any Hook report. That is
     * not a synchronization failure. Only warn when this app has actually reported from an older
     * module build and therefore needs its target process restarted.
     */
    fun shouldShowReloadWarning(
        lastHookEventAtMillis: Long,
        hookVersionCode: Int,
        currentVersionCode: Int,
    ): Boolean =
        lastHookEventAtMillis > 0L &&
            hookVersionCode > 0 &&
            currentVersionCode > 0 &&
            hookVersionCode < currentVersionCode
}
