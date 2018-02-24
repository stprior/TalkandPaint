package technology.prior.talkandpaint


/**
 * Created by Stephen on 01/11/2017.
 */

fun recogniseColor(spokenText: String) : RecognisedColor?
{
    val mColourMap = hashMapOf<String,Int>(
            "Red" to 0xffe6194b.toInt(),
            "Green" to 0xff3cb44b.toInt(),
            "Yellow" to 0xffffe119.toInt(),
            "Blue" to 0xff0082c8.toInt(),
            "Orange" to 0xfff58231.toInt(),
            "Purple" to 0xff911eb4.toInt(),
            "Cyan" to 0xff46f0f0.toInt(),
            "Magenta" to 0xfff032e6.toInt(),
            "Lime" to 0xffd2f53c.toInt(),
            "Pink" to 0xfffabebe.toInt(),
            "Teal" to 0xff008080.toInt(),
            "Lavender" to 0xffe6beff.toInt(),
            "Brown" to 0xffaa6e28.toInt(),
            "Beige" to 0xfffffac8.toInt(),
            "Maroon" to 0xff800000.toInt(),
            "Mint" to 0xffaaffc3.toInt(),
            "Olive" to 0xff808000.toInt(),
            "Coral" to 0xffffd8b1.toInt(),
            "Navy" to 0xff000080.toInt(),
            "Grey" to 0xff808080.toInt(),
            "White" to 0xffFFFFFF.toInt(),
            "Black" to 0xff000000.toInt())
    for (colour in mColourMap.keys)
    {
        if (spokenText.contains(colour, ignoreCase = true))
        {
            return RecognisedColor(
                    mColourMap[colour]!!,
                    colour)
        }
    }
    return null
}
class RecognisedColor(argb:Int, name:String) {
    //TODO: Annotate with @android.support.annotation.ColorInt
    var mFoundColour =argb
    var mFoundColourName =name
}