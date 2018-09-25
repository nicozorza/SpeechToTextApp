package com.example.nicozorza.speechtotextapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Button
import android.widget.TextView
import java.io.IOException

private const val LOG_TAG = "AudioRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class AudioRecord: AppCompatActivity() {

    private var mFileName: String = ""

    private lateinit var recordButton: Button
    private var mediaRecorder: MediaRecorder? = null

    private lateinit var playButton: Button
    private var mediaPlayer: MediaPlayer? = null

    // Requesting permission to RECORD_AUDIO
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    private var isRecording: Boolean = false
    private var isPlaying: Boolean = false

    private lateinit var decodeButton: Button

    private lateinit var textView : TextView

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
        if (!permissionToRecordAccepted) finish()
    }

    private fun onRecord(start: Boolean) = if (start) {
        startRecording()
    } else {
        stopRecording()
    }

    private fun onPlay(start: Boolean) = if (start) {
        startPlaying()
    } else {
        stopPlaying()
    }

    private fun startPlaying() {
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(mFileName)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }
        }
    }

    private fun stopPlaying() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun startRecording() {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(mFileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }

            start()
        }
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
    }

    private fun onRecordClick() {
        isRecording = !isRecording
        onRecord(isRecording)
        recordButton.text = when (isRecording) {
            true -> "Stop recording"
            false -> "Start recording"
        }
    }

    private fun onPlayClick() {
        isPlaying = !isPlaying
        onPlay(isPlaying)
        playButton.text = when (isPlaying) {
            true -> "Stop playing"
            false -> "Start playing"
        }
    }

    private fun onDecodeClick() {

    }

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

        setContentView(R.layout.activity_audio_record)

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        // Record to the external cache directory for visibility
        mFileName = "${externalCacheDir.absolutePath}/audiorecordtest.3gp"


        recordButton = findViewById(R.id.recordButton)
        playButton = findViewById(R.id.playButton)

        recordButton.text = "Start recording"
        recordButton.setOnClickListener {
            onRecordClick()
        }

        playButton.text = "Start playing"
        playButton.setOnClickListener {
            onPlayClick()
        }

        textView = findViewById(R.id.textView)
        textView.text = ""

        decodeButton = findViewById(R.id.decodeButton)
        decodeButton.setOnClickListener {
            onDecodeClick()
        }


    }

    override fun onStop() {
        super.onStop()
        mediaRecorder?.release()
        mediaRecorder = null
        mediaPlayer?.release()
        mediaPlayer = null
    }
}