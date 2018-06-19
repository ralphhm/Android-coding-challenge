package de.rhm.cityweather.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import io.reactivex.Observable
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class Speech(private val speechRecognizer: SpeechRecognizer): KoinComponent {

    private val context by inject<Context>()

    val observable: Observable<SpeechState> = Observable.create { emitter ->
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) = emitter.onNext(SpeechState.Listening())

            override fun onRmsChanged(rmsdB: Float) = Unit

            override fun onBufferReceived(buffer: ByteArray?) = Unit

            override fun onPartialResults(partialResults: Bundle?) = Unit

            override fun onEvent(eventType: Int, params: Bundle?) = Unit

            override fun onBeginningOfSpeech() = Unit

            override fun onEndOfSpeech() = Unit

            override fun onError(error: Int) = with(emitter) {
                when (error) {
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> onNext(SpeechState.NeedsPermission)
                    else -> onNext(SpeechState.Failure(getErrorMessage(error)))
                }
                onComplete()
            }

            override fun onResults(results: Bundle) {
                emitter.onNext(SpeechState.Result(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).first()))
                emitter.onComplete()
            }
        })
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        speechRecognizer.startListening(intent)
    }

    fun getErrorMessage(code: Int) = when(code) {
        SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network"
        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Timeout"
        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Busy"
        SpeechRecognizer.ERROR_NO_MATCH -> "No match"
        else -> "Unknown"
    }
}


sealed class SpeechState {
    class Listening : SpeechState()
    object NeedsPermission : SpeechState()
    class Failure(val message: String) : SpeechState()
    class Result(val text: String) : SpeechState()
}
