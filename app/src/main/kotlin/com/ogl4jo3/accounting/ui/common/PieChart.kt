package com.ogl4jo3.accounting.ui.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.ogl4jo3.accounting.R
import com.ogl4jo3.accounting.common.dpToPx
import com.ogl4jo3.accounting.common.spToPx


class PieChart @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attributeSet, defStyleAttr) {

    private var colorArray = context.resources.getIntArray(R.array.pie_chart_color_array)

    private var circleRangeSideLen = 0
    private var circlePadding = 0
    private var circleDiameter = 0
    private var circleStartX = 0
    private var circleStartY = 0
    private var circleOval = RectF()

    private var textStartX = 0
    private var textStartY = 0
    private var rectSideLen = 0

    private var pieChartDataList: List<PieChartData> = emptyList()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        circleRangeSideLen = width * 2 / 3
        circlePadding = circleRangeSideLen * 1 / 10
        circleDiameter = circleRangeSideLen - 2 * circlePadding
        circleStartX = circlePadding
        circleStartY = circlePadding
        circleOval = RectF(
            circleStartX.toFloat(),
            circleStartY.toFloat(),
            (circleStartX + circleDiameter).toFloat(),
            (circleStartY + circleDiameter).toFloat()
        )

        textStartX = circleRangeSideLen + ((width - circleRangeSideLen) * 1 / 4)
        textStartY = height / 10
        rectSideLen = (16).dpToPx()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawPieChart(canvas)
        drawText(canvas)
    }

    fun setData(data: List<PieChartData>) {
        val sumOfValue = data.sumOf { it.value }.toFloat()
        pieChartDataList = data
            .sortedByDescending { it.value }.apply {
                forEachIndexed { index, pieChartData ->
                    pieChartData.sweepAngle = pieChartData.value.div(sumOfValue) * 360
                    pieChartData.paint.color = if (index >= colorArray.size) {
                        colorArray.last()
                    } else {
                        colorArray[index]
                    }
                }
            }
        postInvalidate()
    }

    private fun drawPieChart(canvas: Canvas) {
        var curAngle = 0.0f
        pieChartDataList.forEach {
            canvas.drawArc(circleOval, curAngle, it.sweepAngle, true, it.paint)
            curAngle += it.sweepAngle
        }
    }

    private fun drawText(canvas: Canvas) {
        var curHeight = textStartY
        pieChartDataList.forEach { pieChartData ->
            canvas.drawRect(
                textStartX.toFloat(),
                curHeight.toFloat(),
                textStartX.toFloat() + rectSideLen,
                curHeight.toFloat() + rectSideLen,
                pieChartData.paint
            )
            canvas.drawText(
                pieChartData.name,//TODO: fix , when pieChart size > colorArray size, use "others" instead
                textStartX.toFloat() + rectSideLen + (6).dpToPx(),
                curHeight.toFloat() + rectSideLen,
                pieChartData.paint
            )
            curHeight += height / 12
        }
    }

}

data class PieChartData(
    val name: String,
    val value: Int,
    var sweepAngle: Float = 0.0f,
    var paint: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        textSize = (17).spToPx().toFloat()
    }
)