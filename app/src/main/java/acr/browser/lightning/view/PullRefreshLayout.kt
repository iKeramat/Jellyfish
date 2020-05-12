package acr.browser.lightning.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

import android.view.ViewConfiguration

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlin.math.abs

/**
 * This is working around the following issue: https://github.com/Slion/Fulguris/issues/60
 * See: https://stackoverflow.com/a/23989911/3969362
 *
 * It improves to odds of not running into the issue while not fixing it.
 * If you still force pull-to-refresh while scrolling you can trigger the bug preventing you to open new page until you restart the app.
 */
class PullRefreshLayout(context: Context, attrs: AttributeSet?) : SwipeRefreshLayout(context, attrs) {
    private val mTouchSlop: Int = ViewConfiguration.get(context).scaledTouchSlop
    private var mPrevX = 0f
    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            // Mark X position of our touchdown
            MotionEvent.ACTION_DOWN -> mPrevX = event.x
            MotionEvent.ACTION_MOVE -> {
                // Check if we think user is scrolling vertically
                val eventX = event.x
                val xDiff = abs(eventX - mPrevX)
                if (xDiff > mTouchSlop) {
                    // User is scrolling vertically do not intercept inputs
                    // Thus preventing pull-to-refresh to trigger while we scroll vertically
                    return false
                }
            }
        }
        return super.onInterceptTouchEvent(event)
    }

}