package com.jakger.core_designsystem.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Path
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import com.jakger.core_designsystem.ui.theme.Cyan600
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/***
 *  inspired by https://github.com/zainkhalid91/compose-loaders
 */

@Composable
fun InfinityTrail(
    modifier: Modifier = Modifier,
    color: Color = Cyan600,
    size: Dp = 128.dp,
    durationMillis: Int = 1500,
) {
    val progress by rememberLoopProgress(durationMillis)
    val track = remember { Path() }
    Canvas(
        modifier.loader(size, size / 2)
    ) {
        val dot = this.size.height * 0.1f
        val a = this.size.width / 2f - dot
        val scaleY = (this.size.height / 2f - dot) / (a * 0.36f)
        fun point(t: Float): Offset {
            val s = sin(t)
            val denominator = 1f + s * s
            return Offset(
                center.x + a * cos(t) / denominator,
                center.y + scaleY * a * s * cos(t) / denominator
            )
        }
        track.reset()
        for (i in 0..96) {
            val p = point(i / 96f * TwoPi)
            if (i == 0) track.moveTo(p.x, p.y) else track.lineTo(p.x, p.y)
        }
        drawPath(track, color.copy(alpha = color.alpha * 0.15f), style = Stroke(dot * 0.5f))
        val trail = 22
        for (j in trail - 1 downTo 0) {
            val fade = 1f - j / trail.toFloat()
            drawCircle(
                color = color.copy(alpha = color.alpha * fade),
                radius = dot * (0.35f + 0.65f * fade),
                center = point(TwoPi * (progress - j * 0.011f)),
            )
        }
    }
}

internal fun Modifier.loader(width: Dp, height: Dp): Modifier = progressSemantics().size(width, height)
internal const val TwoPi: Float = (2 * PI).toFloat()

/**
 * A value that runs from 0 to 1 every [durationMillis] and then wraps.
 *
 * Loaders read it only inside draw or graphicsLayer lambdas, so each frame
 * re-draws without recomposing.
 */
@Composable
internal fun rememberLoopProgress(durationMillis: Int, label: String = "LoaderLoop"): State<Float> {
    require(durationMillis > 0) { "durationMillis must be positive, was $durationMillis" }
    val transition = rememberInfiniteTransition(label)
    return transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(durationMillis, easing = LinearEasing)),
        label = label,
    )
}
