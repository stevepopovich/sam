package com.stevenpopovich.talktothat

import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentationTest {
    @Test
    fun testOnCreate() {
        val scenario = launch(MainActivity::class.java)

        scenario.moveToState(Lifecycle.State.CREATED)

        scenario.onActivity {
            val mainText = it.findViewById<TextView>(R.id.main_text)

            assert(mainText.text == "Hello World!")
        }
    }
}
