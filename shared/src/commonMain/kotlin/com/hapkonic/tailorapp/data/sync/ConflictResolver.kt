package com.hapkonic.tailorapp.data.sync

import com.hapkonic.tailorapp.domain.model.HasTimestamp

/**
 * Last-write-wins conflict resolution strategy.
 * Compares [updatedAt] timestamps — the most recently modified version is kept.
 */
class ConflictResolver {

    /**
     * Returns whichever entity has the higher [HasTimestamp.updatedAt].
     * In case of a tie, the [remote] version is preferred.
     */
    fun <T : HasTimestamp> resolve(local: T, remote: T): T =
        if (local.updatedAt > remote.updatedAt) local else remote
}
