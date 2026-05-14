package com.heistcorp.heistcraft

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests UI mínimos alineados con la rúbrica del Hito 3 (objetivos claros por pantalla).
 */
@RunWith(AndroidJUnit4::class)
class RubricInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun bottomBar_muestra_faq() {
        composeRule.onNodeWithText("FAQ").assertIsDisplayed()
    }

    @Test
    fun inicio_muestra_acceso_y_tagline() {
        composeRule.onNodeWithText("Iniciar sesión").assertIsDisplayed()
        composeRule.onNodeWithText("Expertos en robos", substring = true).assertIsDisplayed()
    }

    @Test
    fun bottomBar_muestra_inicio_bancos_utensilios() {
        composeRule.onNodeWithText("Inicio").assertIsDisplayed()
        composeRule.onNodeWithText("Bancos").assertIsDisplayed()
        composeRule.onNodeWithText("Utensilios").assertIsDisplayed()
    }
}
