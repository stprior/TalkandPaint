package technology.prior.talkandpaint

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * PaintView - responsible for displaying some part of a
 */
class PaintView : View {
    val mPainter: Painter = Painter()
    private var mHasTouch = false
    private val mPointerLookup = HashMap<Int, Line>()

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.PaintView, defStyle, 0)

        mPainter.initCanvas(100, 100)
        a.recycle()

    }


    fun paintFromTouch(actionId: Int, motionEvent: MotionEvent) {
        mPainter.paint(motionEvent.getX(actionId), motionEvent.getY(actionId))
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (changed) mPainter.resize(width, height)
    }
    fun startLine(start : MotionEvent.PointerCoords) =        mPainter.startLine(start.x, start.y)
    override fun onTouchEvent(event: MotionEvent): Boolean {

        val action = event.action

        fun startLine(index: Int)
        {
            val pointerCoords = MotionEvent.PointerCoords()
            val id = event.getPointerId(index)
            event.getPointerCoords(index, pointerCoords)
            mPointerLookup.set(id,mPainter.startLine(pointerCoords.x, pointerCoords.y))
        }
        /*
         * Switch on the action. The action is extracted from the event by
         * applying the MotionEvent.ACTION_MASK. Alternatively a call to
         * event.getActionMasked() would yield in the action as well.
         */
        when (action and MotionEvent.ACTION_MASK) {

            MotionEvent.ACTION_DOWN -> {
                // first pressed gesture has started
                mHasTouch = true

                mPointerLookup.clear()
                for (i in 0 until event.pointerCount) {
                    startLine(i)
                }
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                val index = event.actionIndex
                startLine(index)
            }

            MotionEvent.ACTION_UP -> {
                /*
                 * Final pointer has gone up and has ended the last pressed
                 * gesture.
                 */

                mHasTouch = false
            }

            MotionEvent.ACTION_MOVE -> {
                /*
                 * A change event happened during a pressed gesture. (Between
                 * ACTION_DOWN and ACTION_UP or ACTION_POINTER_DOWN and
                 * ACTION_POINTER_UP)
                 */

                /*
                 * Loop through all active pointers contained within this event.
                 * Data for each pointer is stored in a MotionEvent at an index
                 * (starting from 0 up to the number of active pointers). This
                 * loop goes through each of these active pointers, extracts its
                 * data (position and pressure) and updates its stored data. A
                 * pointer is identified by its pointer number which stays
                 * constant across touch events as long as it remains active.
                 * This identifier is used to keep track of a pointer across
                 * events.
                 */
                for (index in 0 until event.pointerCount) {
                    val id = event.getPointerId(index)
                    val line = mPointerLookup[id]
                    for(pos in 0 until event.historySize)
                    {
                        val x= event.getHistoricalX(index, pos)
                        val y= event.getHistoricalY(index, pos)

                        if (line==null) {
                            mPointerLookup[id] = mPainter.startLine(x,y)
                        }
                        else
                        {
                            mPainter.continueLine(mPointerLookup.getValue(id),x,y)
                        }
                    }
                }
            }
        }

        // trigger redraw on UI thread
        this.postInvalidate()

        return true
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val bitMap = mPainter.getBitmap()
        canvas.drawBitmap(bitMap, 0F, 0F, null)
    }
}
