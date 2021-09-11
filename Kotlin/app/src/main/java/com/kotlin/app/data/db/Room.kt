package com.kotlin.app.data.db

import com.kotlin.app.app
import com.kotlin.app.shared.appId

val room: RoomDB by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { RoomDB.getInstance(app, appId) }