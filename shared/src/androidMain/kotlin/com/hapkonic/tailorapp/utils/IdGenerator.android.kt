package com.hapkonic.tailorapp.utils

import java.util.UUID

actual fun generateId(): String = UUID.randomUUID().toString()
