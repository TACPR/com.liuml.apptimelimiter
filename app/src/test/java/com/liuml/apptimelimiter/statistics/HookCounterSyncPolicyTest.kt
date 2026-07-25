package com.liuml.apptimelimiter.statistics

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HookCounterSyncPolicyTest {
    @Test
    fun `system-only usage is not reported as a hook synchronization failure`() {
        assertFalse(
            HookCounterSyncPolicy.shouldShowReloadWarning(
                lastHookEventAtMillis = 0L,
                hookVersionCode = 0,
                currentVersionCode = 21,
            ),
        )
    }

    @Test
    fun `current or newer hook report does not request a restart`() {
        assertFalse(HookCounterSyncPolicy.shouldShowReloadWarning(1L, 21, 21))
        assertFalse(HookCounterSyncPolicy.shouldShowReloadWarning(1L, 22, 21))
    }

    @Test
    fun `older hook report requests a target-process restart`() {
        assertTrue(HookCounterSyncPolicy.shouldShowReloadWarning(1L, 20, 21))
    }
}
