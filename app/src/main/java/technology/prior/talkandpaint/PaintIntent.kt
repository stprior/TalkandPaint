package technology.prior.talkandpaint

import android.graphics.Color

/**
 * Created by stevop on 02/02/18.
 */
interface PaintIntent {
    override fun toString(): String
}

class ChangeColor(color: RecognisedColor) : PaintIntent
{
    val targetColour = color
    override fun toString(): String {
        return "change color to ${targetColour.mFoundColourName}"
    }
}