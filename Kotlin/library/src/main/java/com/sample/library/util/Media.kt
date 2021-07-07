package com.sample.library.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import com.sample.library.app
import com.sample.library.extension.post

object Media {

    val manager: AudioManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        (app.getSystemService(Context.AUDIO_SERVICE) as AudioManager).also {
            it.setStreamVolume(AudioManager.STREAM_MUSIC, 11, 0)
        }
    }

    var isSilent: Boolean = false

    private var soundIndex: Int = -1

    private val soundPool: SoundPool by lazy {
        val attrs = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        return@lazy SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(attrs)
                .build()
    }

    fun play(raw: Int) {
        if (isSilent) return
        if (soundIndex == -1) {
            soundIndex = soundPool.load(app, raw, 1)
        }
        post(100) {
            if (soundIndex != -1) {
                soundPool.play(soundIndex, 1f, 1f, 1, 0, 1.0f)
                isSilent = true
            }
        }
    }

}

