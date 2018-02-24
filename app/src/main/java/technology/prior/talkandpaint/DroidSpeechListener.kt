package technology.prior.talkandpaint

import com.vikramezhil.droidspeech.OnDSListener
import android.util.Log

/**
 * Created by stevop on 02/02/18.
 */
class DroidSpeechListener : OnDSListener {
    fun e(msg:String) = Log.e("DroidSpeechListener",msg)
    fun d(msg:String) = Log.d("DroidSpeechListener",msg)

    var sendIntent = fun(intent : PaintIntent)
    {
        d("sending $intent")
        textFeedback("Setting color to ${intent.toString()}")
    }
    var textFeedback = fun(text : String?)
    {
        if (text==null) return

        println(text)
    }

    /**
     * The droid speech rms changed result
     *
     * @param rmsChangedValue The rms changed result
     */
    override fun onDroidSpeechRmsChanged(rmsChangedValue: Float) {
        d("rmsChangedValue = $rmsChangedValue")
    }

    /**
     * The droid speech supported languages
     *
     * @param currentSpeechLanguage The current speech language
     *
     * @param supportedSpeechLanguages The supported speech languages
     */
    override fun onDroidSpeechSupportedLanguages(currentSpeechLanguage: String?, supportedSpeechLanguages: MutableList<String>?) {
        d("onDroidSpeechSupportedLanguages $currentSpeechLanguage")

    }

    /**
     * The droid speech recognizer error update
     *
     * @param errorMsg The error message
     */
    override fun onDroidSpeechError(errorMsg: String?) {
        e("ERROR: $errorMsg")
        textFeedback("ERROR: $errorMsg")

    }

    /**
     * The droid speech recognition was closed by user
     */
    override fun onDroidSpeechClosedByUser() {
        textFeedback("onDroidSpeechClosedByUser") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * The droid speech recognizer live result
     *
     * @param liveSpeechResult The live speech result
     */
    override fun onDroidSpeechLiveResult(liveSpeechResult: String?) {
        if (liveSpeechResult == null) return
        d("liveSpeechResult $liveSpeechResult")
        textFeedback(liveSpeechResult)
    }

    /**
     * The droid speech recognizer final result
     *
     * @param finalSpeechResult The final speech result
     */
    override fun onDroidSpeechFinalResult(finalSpeechResult: String?) {
        if (finalSpeechResult==null) return
        d("onDroidSpeechFinalResult $finalSpeechResult") //To change body of created functions use File | Settings | File Templates.
        val recognised = recogniseColor(finalSpeechResult)
        if (recognised!=null) {
            sendIntent(ChangeColor(recognised))
        }
    }
}