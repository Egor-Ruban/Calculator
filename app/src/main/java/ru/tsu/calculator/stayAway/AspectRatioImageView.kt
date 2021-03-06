package ru.tsu.calculator.stayAway

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import ru.tsu.calculator.R

class AspectRatioImageView @JvmOverloads constructor(
    context:Context,
    attrs:AttributeSet? = null,
    defStyleAttrs:Int=0
):ImageView(context, attrs, defStyleAttrs){

    companion object{
        private const val DEFAULT_ASPECT_RATIO = 1.78f
    }

    private var aspectRatio = DEFAULT_ASPECT_RATIO

    init{
        if(attrs!=null){
            val a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView)
            aspectRatio = a.getFloat(R.styleable.AspectRatioImageView_aspectRatio, DEFAULT_ASPECT_RATIO)
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val newHeight = (measuredWidth/aspectRatio).toInt()
        setMeasuredDimension(measuredWidth,newHeight)
    }
}
