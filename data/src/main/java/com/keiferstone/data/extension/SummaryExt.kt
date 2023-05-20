package com.keiferstone.data.extension

import com.keiferstone.data.db.Summary
import com.keiferstone.data.model.Ttls


fun Summary.isStale(ttl: Long = Ttls.ONE_DAY): Boolean {
    return System.currentTimeMillis().let { currentTime ->
        currentTime - (last_fetched_at ?: currentTime) > ttl
    }
}