package com.example.hazi.ui.animation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.TransformOrigin
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration

@OptIn(DelicateCoroutinesApi::class)
class AnimatedTextState() {
    val paused: StateFlow<Boolean> get() = _paused

    val stopped: StateFlow<Boolean> get() = _stopped

    val playing get() = paused.map { !it }

    val ongoing get() = stopped.map { !it }

    internal var attached = false

    internal val layout = CompletableDeferred<Any>()

    internal var current by mutableStateOf(-1)

    private var job: Job? = null

    private var jobs: MutableSet<Job> = mutableSetOf()

    private var _paused = MutableStateFlow(true)
    private var _stopped = MutableStateFlow(true)

    private var _visibility: List<MutableState<Boolean>>? = null
    private var _lineHeight: MutableList<Float>? = null
    private var _transformOrigin: MutableList<TransformOrigin>? = null

    private var _animationDuration: Duration? = null
    private var _intermediateDuration: Duration? = null

    internal var visibility: List<MutableState<Boolean>>
        get() = _visibility!!
        set(value) {
            _visibility = value
        }
    internal var lineHeight: MutableList<Float>
        get() = _lineHeight!!
        set(value) {
            _lineHeight = value
        }
    internal var transformOrigin: MutableList<TransformOrigin>
        get() = _transformOrigin!!
        set(value) {
            _transformOrigin = value
        }
    internal var animationDuration: Duration
        get() = _animationDuration!!
        set(value) {
            _animationDuration = value
        }
    internal var intermediateDuration: Duration
        get() = _intermediateDuration!!
        set(value) {
            _intermediateDuration = value
        }

    fun start() {
        stop()
        resume()
        _stopped.update { false }
    }

    fun stop() {
        pause()
        current = -1
        for (i in visibility.indices) {
            visibility[i].value = false
        }
        _stopped.update { true }
    }

    fun resume() {
        if (paused.value) {
            job = GlobalScope.launch(Dispatchers.IO) {
                layout.await()
                for (i in current.coerceIn(0, 1 shl 16) until visibility.size) {
                    if (coroutineContext.isActive) {
                        delay(intermediateDuration)
                        visibility[i].value = true
                        jobs.add(
                            async {
                                delay(animationDuration)
                                current = i
                                delay(100)
                                visibility[i].value = false

                                if (current == visibility.size - 1) {
                                    _stopped.update { true }
                                }
                            }
                        )
                    }
                }
            }
            _paused.update { false }
        }
    }

    fun pause() {
        if (!paused.value) {
            job?.cancel()
            job = null
            jobs.forEach { it.cancel() }
            jobs.clear()
            _paused.update { true }
        }
    }
}

@Composable
fun rememberAnimatedTextState() = remember { AnimatedTextState() }