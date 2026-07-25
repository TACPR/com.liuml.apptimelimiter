package com.liuml.apptimelimiter.core

object RuleSnapshotSelectionPolicy {
    fun shouldUseShared(
        sharedGeneration: Long,
        sharedVersion: Long,
        cachedGeneration: Long?,
        cachedVersion: Long?,
    ): Boolean {
        if (cachedGeneration == null || cachedVersion == null) return true
        return sharedGeneration > cachedGeneration ||
            (sharedGeneration == cachedGeneration && sharedVersion >= cachedVersion)
    }
}
