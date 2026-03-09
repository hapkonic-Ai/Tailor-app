package com.hapkonic.tailorapp.utils

import platform.Foundation.NSUUID

actual fun generateId(): String = NSUUID().UUIDString()
