package com.liuml.apptimelimiter.core

import org.junit.Assert.assertEquals
import org.junit.Test

class RuleStorageBootstrapPolicyTest {
    @Test
    fun `first upgraded run adopts existing rules without clearing them`() {
        assertEquals(
            RuleStorageBootstrapAction.ADOPT_EXISTING,
            RuleStorageBootstrapPolicy.action(
                privateMarkerPresent = false,
                sharedMarkerPresent = false,
            ),
        )
    }

    @Test
    fun `missing private marker with retained shared marker means app data was cleared`() {
        assertEquals(
            RuleStorageBootstrapAction.RESET_AFTER_DATA_CLEAR,
            RuleStorageBootstrapPolicy.action(
                privateMarkerPresent = false,
                sharedMarkerPresent = true,
            ),
        )
    }

    @Test
    fun `matching markers keep the current rules`() {
        assertEquals(
            RuleStorageBootstrapAction.KEEP,
            RuleStorageBootstrapPolicy.action(
                privateMarkerPresent = true,
                sharedMarkerPresent = true,
            ),
        )
    }
}
