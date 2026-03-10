package com.hapkonic.tailorapp.data.local

import androidx.fragment.app.FragmentActivity
import java.lang.ref.WeakReference

/** Holds a weak reference to the current Activity so BiometricPrompt can use it. */
object ActivityHolder {
    private var ref: WeakReference<FragmentActivity>? = null

    fun set(activity: FragmentActivity) { ref = WeakReference(activity) }
    fun clear() { ref = null }
    fun get(): FragmentActivity? = ref?.get()
}
