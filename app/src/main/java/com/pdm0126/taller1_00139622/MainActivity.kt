package com.pdm0126.taller1_00139622

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                AndroidPediaApp()
            }
        }
    }
}

@Composable
fun AndroidPediaApp() {
    var currentScreen by remember { mutableStateOf("Welcome") }
    var score         by remember { mutableIntStateOf(0) }
    var currentIndex  by remember { mutableIntStateOf(0) }
    var sessionQuestions by remember {
        mutableStateOf(quizQuestions.shuffled().take(3))
    }

    when (currentScreen) {
        "Welcome" -> WelcomeScreen(
            onStart = { currentScreen = "Quiz" }
        )
        "Quiz" -> QuizScreen(
            questions       = sessionQuestions,
            currentIndex    = currentIndex,
            score           = score,
            onAnswerCorrect = { score++ },
            onNext          = {
                if (currentIndex < sessionQuestions.size - 1) {
                    currentIndex++
                } else {
                    currentScreen = "Result"
                }
            }
        )
        "Result" -> ResultScreen(
            score     = score,
            total     = sessionQuestions.size,
            onRestart = {
                sessionQuestions = quizQuestions.shuffled().take(3)
                score         = 0
                currentIndex  = 0
                currentScreen = "Welcome"
            }
        )
    }
}

@Composable
fun WelcomeScreen(onStart: () -> Unit) {
    val green = colorResource(R.color.android_green)
    val dark  = colorResource(R.color.android_dark)
    val cardBg = colorResource(R.color.card_bg)
    val textLight = colorResource(R.color.text_light)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dark),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(text = "🤖", fontSize = 72.sp)

            Text(
                text       = "AndroidPedia",
                fontSize   = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = green,
                textAlign  = TextAlign.Center
            )

            Text(
                text      = "¿Cuánto sabes de Android?",
                fontSize  = 18.sp,
                color     = textLight,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                shape    = RoundedCornerShape(16.dp),
                colors   = CardDefaults.cardColors(containerColor = cardBg),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text       = "Pamela Alexandra Gómez Tenorio",
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color      = textLight
                    )
                    Text(
                        text     = "Carnet: 00139622",
                        fontSize = 14.sp,
                        color    = green
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick  = onStart,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = green)
            ) {
                Text(
                    text       = "Comenzar Quiz",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color      = dark
                )
            }
        }
    }
}

@Composable
fun QuizScreen(
    questions       : List<Question>,
    currentIndex    : Int,
    score           : Int,
    onAnswerCorrect : () -> Unit,
    onNext          : () -> Unit
) {
    val question  = questions[currentIndex]
    val total     = questions.size

    val green          = colorResource(R.color.android_green)
    val dark           = colorResource(R.color.android_dark)
    val cardBg         = colorResource(R.color.card_bg)
    val textLight      = colorResource(R.color.text_light)
    val correctColor   = colorResource(R.color.correct_color)
    val incorrectColor = colorResource(R.color.incorrect_color)
    val neutralColor   = colorResource(R.color.neutral_color)
    val funFactCorrectBg   = colorResource(R.color.fun_fact_correct)
    val funFactIncorrectBg = colorResource(R.color.fun_fact_incorrect)
    val funFactIncorrectText = colorResource(R.color.fun_fact_incorrect_text)

    var selectedOption by remember(currentIndex) { mutableStateOf<String?>(null) }
    var hasAnswered    by remember(currentIndex) { mutableStateOf(false) }

    val isCorrect = selectedOption == question.correctAnswer

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text       = "Pregunta ${currentIndex + 1} de $total",
                    fontSize   = 14.sp,
                    color      = textLight,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text       = "Puntaje: $score / $total",
                    fontWeight = FontWeight.Bold,
                    fontSize   = 14.sp,
                    color      = green
                )
            }

            LinearProgressIndicator(
                progress   = { (currentIndex + 1).toFloat() / total },
                modifier   = Modifier.fillMaxWidth().height(6.dp),
                color      = green,
                trackColor = cardBg
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(16.dp),
                colors   = CardDefaults.cardColors(containerColor = cardBg)
            ) {
                Text(
                    text       = question.question,
                    modifier   = Modifier.padding(20.dp),
                    textAlign  = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    fontSize   = 17.sp,
                    color      = textLight,
                    lineHeight = 24.sp
                )
            }

            question.options.forEach { option ->
                val optionIsCorrect  = option == question.correctAnswer
                val optionIsSelected = option == selectedOption

                val targetColor = when {
                    hasAnswered && optionIsCorrect                       -> correctColor
                    hasAnswered && optionIsSelected && !optionIsCorrect  -> incorrectColor
                    else                                                 -> neutralColor
                }

                val animatedColor by animateColorAsState(
                    targetValue   = targetColor,
                    animationSpec = tween(durationMillis = 300),
                    label         = "buttonColor_$option"
                )

                Button(
                    onClick = {
                        if (!hasAnswered) {
                            selectedOption = option
                            hasAnswered    = true
                            if (optionIsCorrect) onAnswerCorrect()
                        }
                    },
                    enabled = !hasAnswered,
                    colors  = ButtonDefaults.buttonColors(
                        containerColor        = animatedColor,
                        disabledContainerColor = animatedColor,
                        contentColor          = textLight,
                        disabledContentColor  = textLight
                    ),
                    modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                    shape    = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text      = option,
                        fontSize  = 15.sp,
                        textAlign = TextAlign.Center,
                        modifier  = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            if (hasAnswered) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = CardDefaults.cardColors(
                        containerColor = if (isCorrect) funFactCorrectBg else funFactIncorrectBg
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text       = if (isCorrect) "✅ ¡Correcto!" else "❌ Incorrecto",
                            color      = if (isCorrect) green else funFactIncorrectText,
                            fontWeight = FontWeight.Bold,
                            fontSize   = 15.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text       = "💡 ${question.funFact}",
                            fontSize   = 14.sp,
                            fontStyle  = FontStyle.Italic,
                            color      = textLight,
                            lineHeight = 20.sp
                        )
                    }
                }

                Button(
                    onClick  = onNext,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = green)
                ) {
                    Text(
                        text       = if (currentIndex < total - 1) "Siguiente →" else "Ver Resultado",
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color      = dark
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ResultScreen(
    score     : Int,
    total     : Int,
    onRestart : () -> Unit
) {
    val green     = colorResource(R.color.android_green)
    val dark      = colorResource(R.color.android_dark)
    val cardBg    = colorResource(R.color.card_bg)
    val textLight = colorResource(R.color.text_light)

    val (emoji, message) = when (score) {
        total     -> "🏆" to "¡Perfecto! Eres todo un experto en Android."
        total - 1 -> "🥈" to "¡Muy bien! Conoces bastante sobre Android."
        1         -> "📚" to "Vas bien, pero todavía hay mucho por aprender."
        else      -> "😅" to "Necesitas estudiar más. ¡Sigue intentándolo!"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dark),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(text = emoji, fontSize = 72.sp)

            Text(
                text       = "Quiz Finalizado",
                fontSize   = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = green
            )

            Card(
                shape    = RoundedCornerShape(16.dp),
                colors   = CardDefaults.cardColors(containerColor = cardBg),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text       = "Obtuviste $score de $total",
                        fontSize   = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color      = green
                    )
                    Text(
                        text       = message,
                        fontSize   = 16.sp,
                        color      = textLight,
                        textAlign  = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                }
            }

            Button(
                onClick  = onRestart,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(12.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = green)
            ) {
                Text(
                    text       = "🔄 Reiniciar Quiz",
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color      = dark
                )
            }
        }
    }
}