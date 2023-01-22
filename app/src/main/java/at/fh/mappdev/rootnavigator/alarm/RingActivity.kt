package at.fh.mappdev.rootnavigator.alarm

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import at.fh.mappdev.rootnavigator.R

// activity to dismiss the alarm
class RingActivity : AppCompatActivity(){
    private var dismiss:Button? = null
    private var clock : ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ring)
        dismiss = findViewById<Button>(R.id.activity_ring_dismiss)
        clock = findViewById<ImageView>(R.id.activity_ring_clock)
        dismiss?.setOnClickListener {
                val intentService = Intent(applicationContext, AlarmService::class.java)
                applicationContext.stopService(intentService)
                finish()
        }

        animateClock()
    }

    private fun animateClock() {
        val rotateAnimation = ObjectAnimator.ofFloat(clock, "rotation", 0f, 20f, 0f, -20f, 0f)
        rotateAnimation.repeatCount = ValueAnimator.INFINITE
        rotateAnimation.duration = 800
        rotateAnimation.start()
    }

}