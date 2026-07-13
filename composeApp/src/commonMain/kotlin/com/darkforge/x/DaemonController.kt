package com.darkforge.x

interface DaemonController {
    fun start()
    fun stop()
}

expect fun createDaemonController(): DaemonController
