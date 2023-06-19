package com.example.ngiu.functions.chart

import android.R.attr
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.text.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator


// 自定义View，绘制圆形图表
// 作者：SQ_孙琦
// https://www.jianshu.com/p/a5bb8c9f2596

class CircleChartView : View {
    //饼状图画笔
    private lateinit var mPiePaint: Paint

    //阴影画笔
    private var mPaintShadow: Paint? = null

    //默认第一张图半径
    private val mRadiusOne = DensityUtils.dp2px(context, 90f).toFloat()
    private val mRadiusOneCover = DensityUtils.dp2px(context, 65f).toFloat()
    private val mRadiusTwo = DensityUtils.dp2px(context, 95f).toFloat()
    private val mRadiusTwoCover = DensityUtils.dp2px(context, 60f).toFloat()
    private val mRadiusInside = DensityUtils.dp2px(context, 57f).toFloat()
    private val mRadiusInsideCover = DensityUtils.dp2px(context, 55f).toFloat()

    //圆和view边框距离
    private val toFrame = DensityUtils.dp2px(context, 40f).toFloat()

    //构成饼状图的数据集合
    private var mPieDataList: List<PieData> = ArrayList()

    //绘制弧形的sweep数组
    private var mPieSweep: FloatArray? = null

    //初始画弧所在的角度
    private val startDegree = -90
    private var animPro: Float = 0.toFloat()
    private var isStartAnim = true

    //默认圆
    private val mRectFOne = RectF()
    private val mRectFSelect = RectF()
    private val mRectFInside = RectF()
    private val mRectFCover = RectF()

    //扇形外部text起始点坐标
    private var outTextX: Float = 0.toFloat()
    private var outTextY: Float = 0.toFloat()

    private var centerMoney = ""
    private var totalText = ""

    private var mListener: OnSpecialTypeClickListener? = null

    interface OnSpecialTypeClickListener {
        fun onSpecialTypeClick(index: Int, type: String)
    }

    fun setOnSpecialTypeClickListener(listener: OnSpecialTypeClickListener?) {
        this.mListener = listener
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    //初始化画笔
    private fun init() {
        mPiePaint = Paint()
        mPiePaint.isAntiAlias = true
        mPiePaint.style = Paint.Style.FILL

        mPaintShadow = Paint()
        mPaintShadow?.color = Color.WHITE
        mPaintShadow?.style = Paint.Style.FILL
        mPaintShadow?.maskFilter = BlurMaskFilter(2f, BlurMaskFilter.Blur.NORMAL)

        initRectF()
        initSelectRectF()
        initInsideSelectRectF()
        initRectFCover()
    }

    /**
     * 初始化绘制弧形所在矩形的四点坐标
     */
    private fun initRectFCover() {
        mRectFCover.left = toFrame - 1
        mRectFCover.top = toFrame - 1
        mRectFCover.right = 2 * mRadiusOne + toFrame + 1f
        mRectFCover.bottom = 2 * mRadiusOne + toFrame + 1f
    }

    /**
     * 初始化绘制弧形所在矩形的四点坐标
     */
    private fun initRectF() {
        mRectFOne.left = toFrame
        mRectFOne.top = toFrame
        mRectFOne.right = 2 * mRadiusOne + toFrame
        mRectFOne.bottom = 2 * mRadiusOne + toFrame
    }


    /**
     * 选中圆
     */
    private fun initSelectRectF() {
        mRectFSelect.left = toFrame - DensityUtils.dp2px(context, 5f)
        mRectFSelect.top = toFrame - DensityUtils.dp2px(context, 5f)
        mRectFSelect.right = 2 * mRadiusTwo + toFrame - DensityUtils.dp2px(context, 5f)
        mRectFSelect.bottom = 2 * mRadiusTwo + toFrame - DensityUtils.dp2px(context, 5f)
    }

    /**
     * 内部圆
     */
    private fun initInsideSelectRectF() {
        mRectFInside.left = toFrame + DensityUtils.dp2px(context, 33f)
        mRectFInside.top = toFrame + DensityUtils.dp2px(context, 33f)
        mRectFInside.right =
            2 * mRadiusInside + toFrame + DensityUtils.dp2px(context, 33f).toFloat()
        mRectFInside.bottom =
            2 * mRadiusInside + toFrame + DensityUtils.dp2px(context, 33f).toFloat()
    }

    fun setTotalText(totalText: String) {
        this.totalText = totalText
    }

    fun setTextMoney(centerMoney: String) {
        this.centerMoney = centerMoney
    }

    fun startAnimation() {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        //从0到1 意思是从没有到原本设置的那个值
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.addUpdateListener { animation ->
            animPro = animation.animatedValue as Float
            //原来值的完成度
            invalidate()
            //重绘
        }
        valueAnimator.duration = 2000
        valueAnimator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        if (!mPieDataList.isEmpty()) {
            //起始是从-90°位置开始画
            var pieStartOne = startDegree.toFloat()
            var pieStartTwo = startDegree.toFloat()
            var pieStartInside = startDegree.toFloat()
            if (mPieSweep == null) {
                mPieSweep = FloatArray(mPieDataList.size)
            }

            //底部圆
            for (i in mPieDataList.indices) {
                //设置弧形颜色
                mPiePaint.color = Color.parseColor(mPieDataList[i].color)
                //绘制弧形区域，以构成饼状图
                val pieSweep = getProportion(i) * 360
                canvas.drawArc(mRectFOne, pieStartOne, mPieSweep!![i], true, mPiePaint)
                //获取下一个弧形的起点
                pieStartOne += pieSweep
            }
            if (isStartAnim) {
                isStartAnim = false
                startAnimation()
            }

            mPiePaint.color = Color.parseColor("#ffffff")//白色
            canvas.drawArc(mRectFCover, -90f, -360 * (1 - animPro), true, mPiePaint)

            mPiePaint.color = Color.parseColor("#ffffff")//白色
            canvas.drawCircle(
                mRadiusOne + toFrame,
                mRadiusOne + toFrame,
                mRadiusOneCover,
                mPiePaint
            )

            //选中圆
            for (i in mPieDataList.indices) {
                val pieSweep = getProportion(i) * 360
                //设置弧形颜色
                if (mPieDataList[i].isSelected) {
                    //获取外部位置
                    drawText(pieStartTwo, pieSweep)
                    mPiePaint.color = Color.parseColor(mPieDataList[i].color)
                } else {
                    mPiePaint.color = Color.parseColor(mPieDataList[i].on_select_color)
                }
                //绘制弧形区域，以构成饼状图
                canvas.drawArc(mRectFSelect, pieStartTwo, mPieSweep!![i], true, mPiePaint)
                //获取下一个弧形的起点
                pieStartTwo += pieSweep
            }

            mPiePaint.color = Color.parseColor("#ffffff")//白色
            canvas.drawCircle(
                mRadiusOne + toFrame,
                mRadiusOne + toFrame,
                mRadiusTwoCover,
                mPiePaint
            )

            //内部圆
            for (i in mPieDataList.indices) {
                val pieSweep = getProportion(i) * 360
                //设置弧形颜色
                mPiePaint.color = Color.parseColor(mPieDataList[i].color)
                //绘制弧形区域，以构成饼状图
                canvas.drawArc(mRectFInside, pieStartInside, mPieSweep!![i], true, mPiePaint)
                //获取下一个弧形的起点
                pieStartInside += pieSweep
            }

            mPiePaint.color = Color.parseColor("#ffffff")//白色
            canvas.drawCircle(
                mRadiusOne + toFrame,
                mRadiusOne + toFrame,
                mRadiusInsideCover,
                mPiePaint
            )

            //画一个矩形
            mPiePaint.color = Color.TRANSPARENT
            mPiePaint.style = Paint.Style.FILL
            canvas.drawRect(mRectFInside, mPiePaint)


            //绘制文字
            mPiePaint.isAntiAlias = true
            mPiePaint.color = Color.BLUE
            mPiePaint.style = Paint.Style.FILL
            //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
            mPiePaint.textAlign = Paint.Align.CENTER
            val fontMetrics = mPiePaint.fontMetrics
            val top = fontMetrics?.top//为基线到字体上边框的距离,即上图中的top
            val bottom = fontMetrics?.bottom//为基线到字体下边框的距离,即上图中的bottom
            val baseLineY =
                (mRectFInside.centerY() - top!! / 2 - bottom!! / 2).toInt()//基线中间点的y轴计算公式
            mPiePaint.textSize = 36f
            mPiePaint.color = Color.parseColor("#868686")
            canvas.drawText(
                totalText,
                mRectFInside.centerX(),
                (baseLineY - 40).toFloat(),
                mPiePaint
            )

            mPiePaint.textSize = 56f
            mPiePaint.color = Color.parseColor("#555555")
            mPiePaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            canvas.drawText(
                centerMoney,
                mRectFInside.centerX(),
                (baseLineY + 20).toFloat(),
                mPiePaint
            )

            //绘制外围选中
            for (i in mPieDataList.indices) {
                if (mPieDataList[i].isSelected) {
                    //绘制圆形
                    mPaintShadow?.let {
                        canvas.drawCircle(
                            outTextX,
                            outTextY,
                            DensityUtils.dp2px(context, 27.5f).toFloat(),
                            it
                        )
                    } //阴影
                    mPiePaint.color = Color.parseColor(mPieDataList[i].color)
                    canvas.drawCircle(
                        outTextX,
                        outTextY,
                        DensityUtils.dp2px(context, 26.5f).toFloat(),
                        mPiePaint
                    )
                    mPiePaint.color = Color.parseColor("#ffffff")
                    canvas.drawCircle(
                        outTextX,
                        outTextY,
                        DensityUtils.dp2px(context, 23.5f).toFloat(),
                        mPiePaint
                    )
                    //绘制提示内容
                    mPiePaint.textSize = 28f
                    mPiePaint.color = Color.parseColor("#868686")
                    mPiePaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
                    if (mPieDataList[i].type.contains("\n")) {
                        //多行文本绘制，如果有换行符
                        val texts = mPieDataList[i].type.split("\n")
                        var y = outTextY + 0
                        for (txt in texts){
                            canvas.drawText(txt, outTextX, y, mPiePaint)
                            y += mPiePaint.textSize
                        }
                    } else {
                        //单行文本绘制
                        canvas.drawText(mPieDataList[i].type, outTextX, outTextY + 5, mPiePaint)
                    }


                }
            }
        } else {
            //无数据时，显示灰色圆环
            mPiePaint.color = Color.parseColor("#dadada")//灰色
            canvas.drawCircle(mRadiusOne, mRadiusOne, mRadiusOne, mPiePaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val length = (2 * (mRadiusOne + toFrame)).toInt()
        setMeasuredDimension(length, length)
    }

    /**
     * 获取圆弧外位置
     */
    private fun drawText(pieStart: Float, pieSweep: Float) {
        //        //弧形区域角平分线距离-90角度
        val a = pieStart + pieSweep / 2 + 90f
        if (a < 90) {
            //右上部
            outTextX =
                mRadiusTwo + toFrame + (mRadiusTwo * Math.sin(java.lang.Double.parseDouble(((a / 360).toDouble() * 2.0 * Math.PI).toString()))).toFloat()
            outTextY =
                mRadiusTwo + toFrame - (mRadiusTwo * Math.cos(java.lang.Double.parseDouble(((a / 360).toDouble() * 2.0 * Math.PI).toString()))).toFloat() - 40f
        } else if (a == 90f) {
            outTextX =
                mRadiusTwo + toFrame + (mRadiusTwo * Math.sin(java.lang.Double.parseDouble(((a / 360).toDouble() * 2.0 * Math.PI).toString()))).toFloat() + 10f
            outTextY =
                mRadiusTwo + toFrame - (mRadiusTwo * Math.cos(java.lang.Double.parseDouble(((a / 360).toDouble() * 2.0 * Math.PI).toString()))).toFloat()
        } else if (a < 180) {
            //右下
            outTextX =
                mRadiusTwo + toFrame + (mRadiusTwo * Math.sin(java.lang.Double.parseDouble(((a / 360).toDouble() * 2.0 * Math.PI).toString()))).toFloat() + 10f
            outTextY =
                mRadiusTwo + toFrame - (mRadiusTwo * Math.cos(java.lang.Double.parseDouble(((a / 360).toDouble() * 2.0 * Math.PI).toString()))).toFloat() + 10
        } else if (a == 180f) {
            outTextX =
                mRadiusTwo + toFrame + (mRadiusTwo * Math.sin(java.lang.Double.parseDouble(((a / 360).toDouble() * 2.0 * Math.PI).toString()))).toFloat() - 20
            outTextY =
                mRadiusTwo + toFrame - (mRadiusTwo * Math.cos(java.lang.Double.parseDouble(((a / 360).toDouble() * 2.0 * Math.PI).toString()))).toFloat() + 10
        } else if (a < 270) {
            //左下
            outTextX =
                mRadiusTwo + toFrame + (mRadiusTwo * Math.sin(java.lang.Double.parseDouble(((a / 360).toDouble() * 2.0 * Math.PI).toString()))).toFloat() - 30
            outTextY =
                mRadiusTwo + toFrame - (mRadiusTwo * Math.cos(java.lang.Double.parseDouble(((a / 360).toDouble() * 2.0 * Math.PI).toString()))).toFloat() + 20
        } else if (a == 270f) {
            outTextX =
                mRadiusTwo + toFrame + (mRadiusTwo * Math.sin(java.lang.Double.parseDouble(((a / 360).toDouble() * 2.0 * Math.PI).toString()))).toFloat() - 60
            outTextY =
                mRadiusTwo + toFrame - (mRadiusTwo * Math.cos(java.lang.Double.parseDouble(((a / 360).toDouble() * 2.0 * Math.PI).toString()))).toFloat()
        } else if (a < 360) {
            //左上
            outTextX =
                mRadiusTwo + toFrame + (mRadiusTwo * Math.sin(java.lang.Double.parseDouble(((a / 360).toDouble() * 2.0 * Math.PI).toString()))).toFloat() - 40
            outTextY =
                mRadiusTwo + toFrame - (mRadiusTwo * Math.cos(java.lang.Double.parseDouble(((a / 360).toDouble() * 2.0 * Math.PI).toString()))).toFloat() + 10
        }
    }


    /**
     * 所在区域占总区域比例
     */
    fun getProportion(i: Int): Float {
        return mPieDataList[i].value / getSumData(mPieDataList)
    }

    /**
     * 获取各区域数值总和
     */
    fun getSumData(mPieDataList: List<PieData>?): Float {
        if (mPieDataList == null || mPieDataList.isEmpty()) {
            return 0f
        }
        var mSum = 0f
        for (i in mPieDataList.indices) {
            mSum += mPieDataList[i].value
        }
        return mSum
    }


    /**
     * 设置需要绘制的数据集合
     */
    fun setPieDataList(pieDataList: List<PieData>) {
        this.mPieDataList = pieDataList
//        if (mPieSweep == null) {
//            mPieSweep = FloatArray(mPieDataList!!.size)
//        }
        mPieSweep = FloatArray(mPieDataList!!.size)
        for (i in pieDataList.indices) {
            mPieSweep!![i] = getProportion(i) * 360
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> doOnSpecialTypeClick(event)
        }
        return super.onTouchEvent(event)
    }

    private fun doOnSpecialTypeClick(event: MotionEvent) {
        if (mPieDataList.isEmpty()) {
            return
        }
        val which = touchOnWhichPart(event)
        if (mListener != null && which != 1000) {
            for (i in mPieDataList.indices) {
                mPieDataList[i].isSelected = i == which
            }
            mListener!!.onSpecialTypeClick(which, mPieDataList[which].type)
        }
    }

    /**
     * 点击位置
     */
    private fun touchOnWhichPart(event: MotionEvent): Int {
        val x = event.x
        val y = event.y
        var mWhich = 1000//错误码
        var a = 0.0//与-90的夹角
        var sum = 0f//所占比例和
        val newRadius = mRadiusOne + toFrame//用来参与计算的半径+间距
        //在圆内
        if (Math.pow((x - newRadius).toDouble(), 2.0) + Math.pow(
                (y - newRadius).toDouble(),
                2.0
            ) <= Math.pow(mRadiusOne.toDouble(), 2.0)
        ) {
            if (event.x > newRadius) {
                //圆的右半部
                if (event.y > newRadius) {
                    //圆的下半部(综上:右下  0-90)
                    a =
                        Math.PI / 2 + Math.atan(java.lang.Double.parseDouble(((y - newRadius) / (x - newRadius)).toString()))
                } else {
                    //右上(-90-0)
                    a =
                        Math.atan(java.lang.Double.parseDouble(((x - newRadius) / (newRadius - y)).toString()))
                }
            } else {
                //圆的左半部
                if (event.y > newRadius) {
                    //圆的下半部(综上:左下 90-180)
                    a =
                        Math.PI + Math.atan(java.lang.Double.parseDouble(((newRadius - x) / (y - newRadius)).toString()))
                } else {
                    //左上  180-270
                    a =
                        2 * Math.PI - Math.atan(java.lang.Double.parseDouble(((newRadius - x) / (newRadius - y)).toString()))
                }
            }

            for (i in mPieDataList.indices) {
                if (i < mPieDataList.size - 1) {
                    sum += getProportion(i)
                } else {
                    sum = 1f
                }

                if (a / (2 * Math.PI) <= sum) {
                    mWhich = i
                    break
                }
            }
        }
        return mWhich
    }


}