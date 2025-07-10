package com.plugin.demo.inspection

import com.intellij.DynamicBundle
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey
import java.util.function.Supplier

object DataClassBundle : DynamicBundle(DataClassBundle.BUNDLE) {
    const val BUNDLE: @NonNls String = "messages.DataClass"

    fun message(key: @PropertyKey(resourceBundle = BUNDLE) String, vararg params: Any): String {
        return getMessage(key, *params)
    }

    fun partialMessage(
        key: @PropertyKey(resourceBundle = BUNDLE) String,
        unassignedParams: Int,
        vararg params: Any
    ): String {
        return getPartialMessage(key, unassignedParams, *params)
    }

    fun messagePointer(key: @PropertyKey(resourceBundle = BUNDLE) String, vararg params: Any): Supplier<String> {
        return getLazyMessage(key, *params)
    }
}