package com.example.omok

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.solver.widgets.Rectangle
import androidx.core.graphics.createBitmap
import com.example.omok.BaseApplication.Companion.blackStone
import com.example.omok.BaseApplication.Companion.whiteStone
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var gridView: GridView

    private var turn = blackStone
    private var cellWidth = 55
    private var emptySize = 88

    private var omokValue = Array(19) { Array(19) { 0 } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        var x = displayMetrics.widthPixels
        var y = displayMetrics.heightPixels

        Log.d("동환", "x : $x , y : $y")

        var bitmap = Bitmap.createBitmap(x - emptySize, x - emptySize, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bitmap)
        var paint = Paint()
        canvas.drawColor(getColor(R.color.omok_background))
        paint.color = getColor(R.color.black)
        paint.strokeWidth = 1.0f
        paint.style = Paint.Style.STROKE
        for (num1 in 0..18) {
            canvas.drawLine(
                0.0f,
                (cellWidth * num1).toFloat(),
                x.toFloat(),
                (cellWidth * num1).toFloat(),
                paint
            )
            canvas.drawLine(
                (cellWidth * num1).toFloat(),
                0.0f,
                (cellWidth * num1).toFloat(),
                x.toFloat(),
                paint
            )
        }

        omok.setImageBitmap(bitmap)

        omok.setOnTouchListener { v: View?, event: MotionEvent? ->
            Log.d("동환", " 클릭 좌표 X : ${event?.x} / Y : ${event?.y} ")
            var x = event!!.x
            var y = event.y

            var xCount = Math.round(x / cellWidth)
            var yCount = Math.round(y / cellWidth)

            if (omokValue[xCount][yCount] != 0) {
                Toast.makeText(applicationContext, "이미 돌이 존재합니다.", Toast.LENGTH_SHORT).show()
            } else {
                var circlePaint = Paint()
                when (turn) {
                    blackStone -> {
                        circlePaint.color = getColor(R.color.black)
                        omokValue[xCount][yCount] = blackStone
                    }
                    whiteStone -> {
                        circlePaint.color = getColor(R.color.white)
                        omokValue[xCount][yCount] = whiteStone
                    }
                }
                canvas.drawCircle(
                    xCount * cellWidth.toFloat(),
                    yCount * cellWidth.toFloat(),
                    cellWidth / 2.0f,
                    circlePaint
                )
                omok.setImageBitmap(bitmap)
                if (horizonCheck(xCount, yCount, turn) || verticalCheck(
                        xCount,
                        yCount,
                        turn
                    ) || rightDownCheck(xCount, yCount, turn) || rightUpCheck(xCount, yCount, turn)
                ) {
                    Toast.makeText(applicationContext, "${turn}이 이겼습니다.", Toast.LENGTH_SHORT).show()

                }
                turn = if (turn == blackStone) whiteStone else blackStone
            }
            return@setOnTouchListener false
        }

    }

    fun horizonCheck(x: Int, y: Int, turn: Int): Boolean {
        return (currentLeftCheck(x, y, turn) + currentRightCheck(x, y, turn) == 4)
    }

    fun verticalCheck(x: Int, y: Int, turn: Int): Boolean {
        return (currentUpCheck(x, y, turn) + currentDownCheck(x, y, turn) == 4)
    }

    fun rightUpCheck(x: Int, y: Int, turn: Int): Boolean { // 우상향
        return (currentLeftDownCheck(x, y, turn) + currentRightUpCheck(x, y, turn) == 4)
    }

    fun rightDownCheck(x: Int, y: Int, turn: Int): Boolean { // 우하향
        return (currentLeftUpCheck(x, y, turn) + currentRightDownCheck(x, y, turn) == 4)
    }


    fun currentLeftCheck(x: Int, y: Int, turn: Int): Int {
        var count = 0
        while (omokValue[x - 1 - count][y] == turn) {
            count++
        }
        return count
    }

    fun currentRightCheck(x: Int, y: Int, turn: Int): Int {
        var count = 0
        while (omokValue[x + 1 + count][y] == turn) {
            count++
        }
        return count
    }

    fun currentUpCheck(x: Int, y: Int, turn: Int): Int {
        var count = 0
        while (omokValue[x][y + 1 + count] == turn) {
            count++
        }
        return count
    }

    fun currentDownCheck(x: Int, y: Int, turn: Int): Int {
        var count = 0
        while (omokValue[x][y - 1 - count] == turn) {
            count++
        }
        return count
    }

    fun currentLeftDownCheck(x: Int, y: Int, turn: Int): Int {
        var count = 0
        while (omokValue[x - 1 - count][y - 1 - count] == turn) {
            count++
        }
        return count
    }

    fun currentRightUpCheck(x: Int, y: Int, turn: Int): Int {
        var count = 0
        while (omokValue[x + 1 + count][y + 1 + count] == turn) {
            count++
        }
        return count
    }

    fun currentLeftUpCheck(x: Int, y: Int, turn: Int): Int {
        var count = 0
        while (omokValue[x - 1 - count][y + 1 + count] == turn) {
            count++
        }
        return count
    }

    fun currentRightDownCheck(x: Int, y: Int, turn: Int): Int {
        var count = 0
        while (omokValue[x + 1 + count][y - 1 - count] == turn) {
            count++
        }
        return count
    }

}
