package com.keiferstone.owlplayerstats.extension


import com.keiferstone.owlplayerstats.model.Filter

fun MutableList<Filter>.select(filter: Filter) {
    if (contains(filter)) remove(filter)
    else if (filter is Filter.PlaysRole && any { it is Filter.PlaysRole }) {
        removeAll { it is Filter.PlaysRole }
        add(filter)
    }
    else if (filter is Filter.TimePlayed) {
        removeAll { it is Filter.TimePlayed }
        add(filter)
    }
    else add(filter)
}