package com.liuml.apptimelimiter.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DonationPromptPolicyTest {
    @Test
    fun `first launch records today and prompts immediately`() {
        val decision = DonationPromptPolicy.onLaunch(
            storedFirstUseEpochDay = null,
            storedLastPromptEpochDay = null,
            storedLaunchesSincePrompt = 0,
            todayEpochDay = 100L,
            disabled = false,
        )

        assertEquals(100L, decision.firstUseEpochDay)
        assertEquals(0, decision.launchesSincePrompt)
        assertEquals(100L, decision.lastPromptEpochDay)
        assertTrue(decision.shouldPrompt)
    }

    @Test
    fun `existing first-use record without a prompt shows immediately after update`() {
        val decision = DonationPromptPolicy.onLaunch(
            storedFirstUseEpochDay = 100L,
            storedLastPromptEpochDay = null,
            storedLaunchesSincePrompt = 3,
            todayEpochDay = 101L,
            disabled = false,
        )

        assertTrue(decision.shouldPrompt)
        assertEquals(101L, decision.lastPromptEpochDay)
        assertEquals(0, decision.launchesSincePrompt)
    }

    @Test
    fun `repeat prompt is due after three days`() {
        val decision = DonationPromptPolicy.onLaunch(
            storedFirstUseEpochDay = 100L,
            storedLastPromptEpochDay = 101L,
            storedLaunchesSincePrompt = 1,
            todayEpochDay = 104L,
            disabled = false,
        )

        assertTrue(decision.shouldPrompt)
    }

    @Test
    fun `repeat prompt is due after five launches but never twice in one day`() {
        val nextDay = DonationPromptPolicy.onLaunch(
            storedFirstUseEpochDay = 100L,
            storedLastPromptEpochDay = 101L,
            storedLaunchesSincePrompt = 4,
            todayEpochDay = 102L,
            disabled = false,
        )
        val sameDay = DonationPromptPolicy.onLaunch(
            storedFirstUseEpochDay = 100L,
            storedLastPromptEpochDay = 101L,
            storedLaunchesSincePrompt = 8,
            todayEpochDay = 101L,
            disabled = false,
        )

        assertTrue(nextDay.shouldPrompt)
        assertFalse(sameDay.shouldPrompt)
    }

    @Test
    fun `disabled or rolled-back clock never prompts`() {
        val disabled = DonationPromptPolicy.onLaunch(
            storedFirstUseEpochDay = 100L,
            storedLastPromptEpochDay = null,
            storedLaunchesSincePrompt = 0,
            todayEpochDay = 101L,
            disabled = true,
        )
        val rolledBack = DonationPromptPolicy.onLaunch(
            storedFirstUseEpochDay = 100L,
            storedLastPromptEpochDay = 101L,
            storedLaunchesSincePrompt = 8,
            todayEpochDay = 99L,
            disabled = false,
        )

        assertFalse(disabled.shouldPrompt)
        assertFalse(rolledBack.shouldPrompt)
    }
}
