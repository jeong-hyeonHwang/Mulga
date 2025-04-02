
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.sin
import com.ilm.mulga.ui.theme.MulGaTheme

@Composable

fun WaveBackground(
    modifier: Modifier = Modifier,
    waveHeight: Dp = 100.dp,      // 전체 높이
    wave1Amplitude: Float = 15f,  // 1번 파도 진폭
    wave1Frequency: Float = 1.9f, // 1번 파도 주기
    wave1Speed: Float = 0.8f,     // 1번 파도 속도
    wave2Amplitude: Float = 35f,  // 2번 파도 진폭
    wave2Frequency: Float = 2.5f, // 2번 파도 주기
    wave2Speed: Float = 0.7f,     // 2번 파도 속도
    wave1Colors: List<Color> = listOf(MulGaTheme.colors.gradient2, Color.Transparent),  // 1번 파도 그라디언트
    wave2Colors: List<Color> = listOf(MulGaTheme.colors.gradient1, Color.Transparent),  // 2번 파도 그라디언트
) {
    // 무한 반복 애니메이션 두 개 (파도마다 다른 속도/offset)
    val infiniteTransition = rememberInfiniteTransition()

    // 파도1 timeOffset
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
    // 파도2 timeOffset
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

        // 파도 그리는 함수
        fun Path.drawSineWave(
            amplitude: Float,
            frequency: Float,
            offset: Float,
            invert: Boolean = false  // 아래쪽 기준으로 뒤집을지 여부
        ) {
            // 시작점을 왼쪽 상단 (혹은 하단)으로
            if (!invert) {
                moveTo(0f, canvasHeight / 2f)
            } else {
                moveTo(0f, canvasHeight / 2f)
            }
            val step = 10f
            for (x in 0..canvasWidth.toInt() step step.toInt()) {
                val fx = x.toFloat()
                // sin 함수를 기반으로 y 좌표 생성
                val y = canvasHeight / 2f + amplitude * sin(frequency * fx + offset)
                lineTo(fx, y)
            }
            // 파도 아래쪽(위쪽)을 닫아서 Path 완성
            lineTo(canvasWidth, canvasHeight)
            lineTo(0f, canvasHeight)
            close()
        }

        // (1) Wave #1 Path
        val path1 = Path().apply {
            drawSineWave(
                amplitude = wave1Amplitude,
                frequency = wave1Frequency,
                offset = wave1Offset
            )
        }
        // (2) Wave #2 Path
        val path2 = Path().apply {
            drawSineWave(
                amplitude = wave2Amplitude,
                frequency = wave2Frequency,
                offset = wave2Offset
            )
        }

        // 그라디언트 브러시
        val brush1 = Brush.verticalGradient(wave1Colors, startY = 0f, endY = canvasHeight)
        val brush2 = Brush.verticalGradient(wave2Colors, startY = 0f, endY = canvasHeight)

        // 첫 번째 파도 그림
        drawPath(path1, brush = brush1)

        // 두 번째 파도 그림 (조금 투명도를 주면 겹칠 때 더 부드러운 효과가 남)
        drawPath(
            path2,
            brush = brush2,
            alpha = 0.8f  // 필요시 조절
        )
    }
}


@Preview(showBackground = true)
@Composable
fun WaveBackgroundPreview() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WaveBackground()
    }
}
