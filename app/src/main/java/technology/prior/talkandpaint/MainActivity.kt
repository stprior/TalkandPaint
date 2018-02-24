package technology.prior.talkandpaint

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import com.vikramezhil.droidspeech.DroidSpeech
import com.vikramezhil.droidspeech.OnDSPermissionsListener
import android.util.Log
import android.widget.ToggleButton

class MainActivity : Activity(), OnDSPermissionsListener  {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()


        setContentView(R.layout.activity_main)
        val dsListener = DroidSpeechListener()
        dsListener.textFeedback = this::tellUser



        val button = findViewById<ToggleButton>(R.id.toggleButton)
        val droidSpeech = DroidSpeech(this, getFragmentManager())
        droidSpeech.setOnDroidSpeechListener(dsListener)
        if (button == null)
        {
            Log.e("MainActivity", "found no toggleButton")
        }else {
            button.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) droidSpeech.startDroidSpeechRecognition() else droidSpeech.closeDroidSpeechOperations()
            }
        }

        dsListener.sendIntent = fun(intent : PaintIntent)
        {

            when (intent) {
                is ChangeColor -> {
                    val paintView = findViewById<PaintView>(R.id.paint_view)
                    paintView?.mPainter?.setColor(intent.targetColour.mFoundColour)
                }
            }

        }
    }

    fun deactivateVoice() {
        val button = findViewById<ToggleButton>(R.id.toggleButton)
        if (button == null) return
        button.isChecked = false

    }

    override fun onDroidSpeechAudioPermissionStatus(audioPermissionGiven: Boolean, errorMsgIfAny: String) {
        // Triggered if the audio permission was provided or denied by the user
        if (!audioPermissionGiven)
        {
            tellUser("Unable to hear mic")
            deactivateVoice()

        }
    }

    fun tellUser(str: String?)
    {

        val textView = findViewById<TextView>(R.id.text_box)
        if (textView != null) {
            textView.text = str
            textView.invalidate()
        }else
        {
            Log.w("MainActivity.tellUser", "text_box missing, can't display $str")
        }
        Log.d("MainActivity", "telling $str")

    }
}
