package hu.eqn34f.retroquiz.utils

import android.content.Intent

//This was an accepted answer from StackOverflow
//Allows you to directly store Enum types in intent extras
inline fun <reified T : Enum<T>> Intent.putExtra(victim: T): Intent =
    putExtra(T::class.java.name, victim.ordinal)

inline fun <reified T: Enum<T>> Intent.getEnumExtra(): T? =
    getIntExtra(T::class.java.name, -1)
        .takeUnless { it == -1 }
        ?.let { T::class.java.enumConstants[it] }
