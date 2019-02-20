package com.listener.animations.view

import android.animation.*
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.*
import android.animation.Animator

/**
 * View that creates a Segmented color bar
 */
class SegmentedColorBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), ValueAnimator.AnimatorUpdateListener {
    //key for ValueAnimator
    private val PROPERTY_RIGHT: String = "right"

    //set of animations that builds the colorbar
    private var introAnimation : AnimatorSet? = null

    //Should we animate view in on first draw?
    private var isAnimated: Boolean = false

    //list of given segments
    private var segments : ArrayList<Pair<Double, String>> = ArrayList()

    //pre-computed list of Rectangles to drawn, derived from segments
    private var renderQueue : ArrayList<Pair<RectF, Paint>> = ArrayList()

    //rolling total of segment weight
    private var segmentWeight = 0.0

    //flag to indicate if we have had a render step yet
    private var hasDrawn = false

    //The Interpolator we will be using if we animate
    private var animationStyle:BaseInterpolator = AccelerateDecelerateInterpolator()

    /**
     * Init the view based on the arguments provided
     * Two values can be preset in XML
     * isAnimated - boolean flag
     * e.g. app:animate="true"
     * segments - comma separated list of segments w/colors
     * e.g. app:segments="3:#2E804A,20:#F8CA42,3:#3c3c3c"
     */
    init {
        //did we get attributes
        attrs?.apply {
            //apply our defined styles to attribute set if included to make a TypedArray
            context.obtainStyledAttributes(this, com.listener.animations.R.styleable.SegmentedColorBar)?.apply {
                //check to see if we got an animate String
                isAnimated = this.getBoolean(com.listener.animations.R.styleable.SegmentedColorBar_animate, false)

                //Preferred animation style is passed by magic number
                val animateStyle = this.getInteger(com.listener.animations.R.styleable.SegmentedColorBar_animateStyle, 0)
                animationStyle = when(animateStyle) {
                    1 -> AccelerateDecelerateInterpolator()
                    2 -> AccelerateInterpolator()
                    3 -> AnticipateInterpolator()
                    4 -> AnticipateOvershootInterpolator()
                    5 -> BounceInterpolator()
                    6 -> DecelerateInterpolator()
                    7 -> LinearInterpolator()
                    8 -> OvershootInterpolator()
                    else -> AccelerateDecelerateInterpolator()
                }

                //check to see if we got a segment string, we we got one, split on commas
                val rawCol : List<String>? = this.getString(com.listener.animations.R.styleable.SegmentedColorBar_segments)?.split(",")

                //if our string split into a list, iterate on list for raw values
                rawCol?.forEach { rawSegments ->
                    //split the raw Value into size of segment, and color
                    val rawEntry :List<String>? = rawSegments.split(":")

                    //if the raw value split into a list, grab each part, and add the segment
                    rawEntry?.let {
                        addSegment(amount = rawEntry[0].toDouble(), color = rawEntry[1])
                    }
                }

            }?.recycle() //recycle the TypedArray, it is expensive
        }
    }

    /**
     * Adds a segment to the list of segments, and increments total weight
     *
     * returns self for liquid assignments
     * e.g. foo.addSegment(20, #000000).addSegment(40, #111111).addSegment etc....
     */
    fun addSegment(amount:Double, color:String ) : SegmentedColorBar {
        //add pair to segment list
        segments.add(Pair(amount, color))

        //update weight total
        segmentWeight += amount

        //update the shapes we have to draw
        generateRenderQueue()

        //if we are already painted on screen, invalidate to force redraw
        if (hasDrawn) {
            invalidate()
        }

        return this
    }

    /**
     * Colorbar segments are computed into a queue of rectangles and paints ASAP
     * for speedy drawing
     */
    private fun generateRenderQueue() {
        //clear any existing render data
        renderQueue.clear()

        //current left point in the colorbar
        var currentLeft = 0.0

        //for each segment
        segments.forEach { pair ->
            //create paint
            val segPaint =  Paint(Paint.ANTI_ALIAS_FLAG).apply {
                this.style = Paint.Style.FILL
                this.color = Color.parseColor(pair.second)
            }

            //compute shape
            //get the segments proportion of weight
            val segProportion =  pair.first / segmentWeight

            //apply proportion to width
            val segWidth = segProportion * width

            //add width to current left position to get right position
            var right = currentLeft + segWidth

            //build rectangle
            val nRect = RectF(currentLeft.toFloat(), 0F, right.toFloat(), height.toFloat())

            //update the current leftmost position of the color bar, to the end of the last segment
            currentLeft = right

            //add render info to queue
            val segPair  = Pair(nRect, segPaint)
            renderQueue.add(segPair)
        }

        //if we are animating the view, build the animations
        if (isAnimated) {
            generateAnimationQueue()
        }
    }


    /**
     * Generate a sequenced list of Animators
     */
    private fun generateAnimationQueue() {
        //start list
        introAnimation = AnimatorSet()

        val animationQueue: ArrayList<Animator> = ArrayList()

        //for each render segment, create an associated animator
        renderQueue.forEach { pair ->

            //animate the width of the segment by growing the right pos, from the left to it's original value
            val growRight: PropertyValuesHolder = PropertyValuesHolder.ofFloat(PROPERTY_RIGHT, pair.first.left, pair.first.right)

            //set Animator properties
            var segAnimator = ValueAnimator()
            segAnimator.apply {
                duration = 2000
                interpolator = animationStyle
                setValues(growRight) //set the properties to animate

            }.addUpdateListener(this) //listen for updates

            //to prepare for the animation, set the current(full) right, to the value of the left - effectively making
            //the segment invisible
            pair.first.right = pair.first.left

            //add the animation to the queue
            animationQueue.add(segAnimator)
        }

        //add the animation sequence to the set and play
        introAnimation?.playSequentially(animationQueue)
        introAnimation?.start()
    }

    /**
     * When given a new view size, recompute render Queue
     */
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        generateRenderQueue()
    }

    fun reset() {
        generateRenderQueue()
    }

    /**
     * When on a draw step, iterate over render queue and paint to canvas
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //do we have items to render?
        if (renderQueue.size > 0) {
            renderQueue.forEach { pair ->
                canvas?.drawRect(pair.first, pair.second)
            }
        }

        //set the flag that we have rendered once, all segment additions must trigger a redraw
        hasDrawn = true
    }

    /**
     * segment animation listener
     */
    override fun onAnimationUpdate(animation: ValueAnimator?) {
        //get the update of the animating value
        val newRight = animation?.getAnimatedValue(PROPERTY_RIGHT) as Float


        val position = getRunningAnimationPosition()
        if (position > -1) {
            //set the new value of right to the segment currently animating
            renderQueue[position].first.right = newRight

            //invalidate the view to trigger a redraw
            invalidate()
        }
    }


    private fun getRunningAnimationPosition(): Int {
        val childAnimations = introAnimation?.childAnimations
        childAnimations?.let {
            for (i in 0 until childAnimations.size) {
                val animator = childAnimations[i]
                if (animator.isRunning) {
                    return i
                }
            }
        }

        return -1
    }

}