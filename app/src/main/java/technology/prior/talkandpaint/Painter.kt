package technology.prior.talkandpaint

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint


class Painter {
    private var mBitmap: Bitmap? = null
    private var mCanvas: Canvas? = null
    private val mBrushPaint = Paint()
    private val mBackgroundPaint = Paint()
    private var mBrushWidth: Float = 7.0F

    fun setWeight(weight: Float) {
        mBrushWidth = weight
    }

    fun setColor(color: Int) {

        mBrushPaint.color = color
    }

    init {
        mBrushPaint.color = Color.BLUE
        mBackgroundPaint.color = Color.GRAY
    }

    fun ready(): Boolean {
        return (mCanvas != null)
    }

    fun initCanvas(width: Int, height: Int) {
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(mBitmap)
        canvas.drawPaint(mBackgroundPaint)
        mCanvas = canvas
    }

    fun resize(width: Int, height: Int) {
        if (width == 0 || height == 0) return

        val bitmap = mBitmap ?: return initCanvas(width, height)
        val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val newCanvas = Canvas(newBitmap)
        newCanvas.drawBitmap(bitmap, 0F, 0F, mBackgroundPaint)
        mBitmap = newBitmap
        mCanvas = newCanvas
    }

    fun paint(x: Float, y: Float) {
        mCanvas?.drawCircle(x, y, mBrushWidth, mBrushPaint)
    }

    fun startLine(x : Float, y: Float) : Line
    {
        mCanvas?.drawLine(x,y,x,y,mBrushPaint)
        return Line(x,y)
    }

    fun continueLine(l: Line, x : Float, y: Float)
    {
        val lastPoint = l.vertices.last()
        mCanvas?.drawLine(lastPoint.x, lastPoint.y, x, y, mBrushPaint)
        l.addPoint(x,y)
    }

    fun getBitmap(): Bitmap {
        return mBitmap ?: Bitmap.createBitmap(500, 500, null)
    }
}
class Point(val x:Float,val y:Float){

}
class Line(startX:Float,startY:Float)
{
    val vertices = mutableListOf(Point(startX,startY))

    fun addPoint(x:Float, y:Float)
    {
        vertices.add(Point(x,y))
    }
}
