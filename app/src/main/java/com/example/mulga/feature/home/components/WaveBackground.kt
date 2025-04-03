import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.sin
import com.example.mulga.ui.theme.MulGaTheme


@Composable
fun WaveBackground(
    modifier: Modifier = Modifier,
    waveHeight: Dp = 100.dp,
    // 고정된 진폭 값 (픽셀 단위)
    wave1Amplitude: Float = 15f,
    wave1Frequency: Float = 1.9f,
    wave1Speed: Float = 0.8f,
    wave2Amplitude: Float = 35f,
    wave2Frequency: Float = 2.5f,
    wave2Speed: Float = 0.7f,
    // baselineFraction: 전체 높이 중 어느 위치에서 파도가 시작될지를 결정 (0.0~1.0)
    // 기본값 0.9f: 90% 지점에서 시작. 만약 1.0f가 입력되면, 파도의 기준선은 canvasHeight가 되지만
    // 그라디언트는 canvasHeight의 90%부터 시작하도록 처리하여 UI가 달라지게 합니다.
    baselineFraction: Float = 0.9f,
    wave1Colors: List<Color> = listOf(MulGaTheme.colors.gradient2, Color.Transparent),
    wave2Colors: List<Color> = listOf(MulGaTheme.colors.gradient1, Color.Transparent)
) {
    val infiniteTransition = rememberInfiniteTransition()

    val wave1Offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = (3000 / wave1Speed).toInt(),
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )
    val wave2Offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = (4000 / wave2Speed).toInt(),
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(waveHeight)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        // baseline: 전체 캔버스 높이의 baselineFraction 비율 위치.
        // baselineFraction이 1.0이면 baseline은 canvasHeight가 됩니다.
        val baseline = canvasHeight * 0.9f * baselineFraction

        // 브러시 설정: baselineFraction 값에 따라 색상 전환 영역을 다르게 적용합니다.
        val brush1 = if (baselineFraction == 1.0f) {
            // baseline이 canvasHeight인 경우, 그라디언트를 캔버스 하단의 약 90% 위치부터 시작하도록 설정
            Brush.verticalGradient(
                colorStops = arrayOf(
                    0.0f to wave1Colors[0],
                    1.0f to wave1Colors[1]
                ),
                startY = canvasHeight * 0.9f,
                endY = canvasHeight
            )
        } else {
            Brush.verticalGradient(
                colorStops = arrayOf(
                    0.0f to wave1Colors[0],
                    1.0f to wave1Colors[1]
                ),
                startY = baseline,
                endY = canvasHeight
            )
        }

        val brush2 = if (baselineFraction == 1.0f) {
            Brush.verticalGradient(
                colorStops = arrayOf(
                    0.0f to wave2Colors[0],
                    1.0f to wave2Colors[1]
                ),
                startY = canvasHeight * 0.9f,
                endY = canvasHeight
            )
        } else {
            Brush.verticalGradient(
                colorStops = arrayOf(
                    0.0f to wave2Colors[0],
                    1.0f to wave2Colors[1]
                ),
                startY = baseline,
                endY = canvasHeight
            )
        }

        fun Path.drawSineWave(
            amplitude: Float,
            frequency: Float,
            offset: Float
        ) {
            moveTo(0f, baseline)
            val step = 10f
            for (x in 0..canvasWidth.toInt() step step.toInt()) {
                val fx = x.toFloat()
                val y = baseline + amplitude * sin(frequency * fx + offset)
                lineTo(fx, y)
            }
            lineTo(canvasWidth, canvasHeight)
            lineTo(0f, canvasHeight)
            close()
        }

        val path1 = Path().apply {
            drawSineWave(
                amplitude = wave1Amplitude,
                frequency = wave1Frequency,
                offset = wave1Offset
            )
        }
        val path2 = Path().apply {
            drawSineWave(
                amplitude = wave2Amplitude,
                frequency = wave2Frequency,
                offset = wave2Offset
            )
        }

        drawPath(path1, brush = brush1, alpha = 0.7f)
        drawPath(path2, brush = brush2, alpha = 0.55f)
    }
}

@Preview(showBackground = true)
@Composable
fun WaveBackgroundPreview() {
    Column {
        // 예시: baselineFraction = 0.9f와 1.0f의 UI 비교
        WaveBackground(
            waveHeight = 100.dp,
            baselineFraction = 0.9f
        )
        WaveBackground(
            waveHeight = 100.dp,
            baselineFraction = 1.0f
        )
    }
}
