package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun VocabularyIllustration(itemId: String, modifier: Modifier = Modifier, fallbackEmoji: String = "✨", imagePath: String? = null) {
    val defaultIds = remember {
        setOf("mama", "papa", "baby", "oma", "fork", "spoon", "glass", "knife", "beach", "sea", "icecream", "ball")
    }

    Box(modifier = modifier, contentAlignment = androidx.compose.ui.Alignment.Center) {
        val isCustomImage = remember(imagePath) {
            imagePath != null && (imagePath.startsWith("http") || imagePath.startsWith("file://") || imagePath.startsWith("content://"))
        }

        if (isCustomImage) {
            coil.compose.AsyncImage(
                model = imagePath,
                contentDescription = itemId,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
            )
        } else {
            Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val cx = w / 2
            val cy = h / 2
            val minSize = minOf(w, h)

            when (itemId) {
            "mama" -> {
                // Draw a friendly, happy mother face with pretty hair
                val r = minSize * 0.28f
                
                // Pretty dark brown hair behind
                drawCircle(
                    color = Color(0xFF4E342E),
                    radius = r * 1.15f,
                    center = Offset(cx, cy - r * 0.1f)
                )

                // Face circle
                drawCircle(
                    color = Color(0xFFFFD180),
                    radius = r,
                    center = Offset(cx, cy)
                )

                // Cheeks (blush)
                drawCircle(
                    color = Color(0xFFFF8A80),
                    radius = r * 0.2f,
                    center = Offset(cx - r * 0.6f, cy + r * 0.2f)
                )
                drawCircle(
                    color = Color(0xFFFF8A80),
                    radius = r * 0.2f,
                    center = Offset(cx + r * 0.6f, cy + r * 0.2f)
                )

                // Eyes (happy curved/sleeping style)
                drawArc(
                    color = Color(0xFF3E2723),
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(cx - r * 0.5f, cy - r * 0.25f),
                    size = Size(r * 0.22f, r * 0.18f),
                    style = Stroke(width = minSize * 0.02f)
                )
                drawArc(
                    color = Color(0xFF3E2723),
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(cx + r * 0.28f, cy - r * 0.25f),
                    size = Size(r * 0.22f, r * 0.18f),
                    style = Stroke(width = minSize * 0.02f)
                )

                // Sweet smiling red lips
                drawArc(
                    color = Color(0xFFD81B60),
                    startAngle = 0f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(cx - r * 0.25f, cy + r * 0.15f),
                    size = Size(r * 0.5f, r * 0.35f),
                    style = Stroke(width = minSize * 0.035f)
                )
            }
            "papa" -> {
                // Cheerful father face with friendly beard & short hair
                val r = minSize * 0.28f

                // Hair cap
                drawCircle(
                    color = Color(0xFF37474F),
                    radius = r * 1.05f,
                    center = Offset(cx, cy - r * 0.15f)
                )

                // Face
                drawCircle(
                    color = Color(0xFFFFE0B2),
                    radius = r,
                    center = Offset(cx, cy)
                )

                // Hair cut overlay
                val hairPath = Path().apply {
                    moveTo(cx - r * 1.05f, cy - r * 0.2f)
                    quadraticTo(cx - r * 0.5f, cy - r * 1.05f, cx, cy - r * 1.05f)
                    quadraticTo(cx + r * 0.5f, cy - r * 1.05f, cx + r * 1.05f, cy - r * 0.2f)
                    lineTo(cx + r * 1.05f, cy - r * 0.6f)
                    quadraticTo(cx, cy - r * 1.2f, cx - r * 1.05f, cy - r * 0.6f)
                    close()
                }
                drawPath(path = hairPath, color = Color(0xFF37474F))

                // Friendly short beard/mustache outline (arc at chin)
                drawArc(
                    color = Color(0xFF37474F),
                    startAngle = 20f,
                    sweepAngle = 140f,
                    useCenter = false,
                    topLeft = Offset(cx - r * 0.75f, cy + r * 0.1f),
                    size = Size(r * 1.5f, r * 0.8f),
                    style = Stroke(width = minSize * 0.05f)
                )

                // Cute cartoon spectacles/glasses
                val glassRadius = r * 0.25f
                drawCircle(
                    color = Color(0xFF1E88E5),
                    radius = glassRadius,
                    center = Offset(cx - r * 0.35f, cy - r * 0.1f),
                    style = Stroke(width = minSize * 0.02f)
                )
                drawCircle(
                    color = Color(0xFF1E88E5),
                    radius = glassRadius,
                    center = Offset(cx + r * 0.35f, cy - r * 0.1f),
                    style = Stroke(width = minSize * 0.015f)
                )
                // Glasses nose bridge
                drawLine(
                    color = Color(0xFF1E88E5),
                    start = Offset(cx - r * 0.1f, cy - r * 0.1f),
                    end = Offset(cx + r * 0.1f, cy - r * 0.1f),
                    strokeWidth = minSize * 0.024f
                )

                // Two eye dots
                drawCircle(color = Color(0xFF263238), radius = r * 0.06f, center = Offset(cx - r * 0.35f, cy - r * 0.1f))
                drawCircle(color = Color(0xFF263238), radius = r * 0.06f, center = Offset(cx + r * 0.35f, cy - r * 0.1f))

                // Smile
                drawArc(
                    color = Color(0xFFD84315),
                    startAngle = 0f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(cx - r * 0.2f, cy + r * 0.22f),
                    size = Size(r * 0.4f, r * 0.22f),
                    style = Stroke(width = minSize * 0.03f)
                )
            }
            "baby" -> {
                // Baby face with a pacifier and single curl of hair
                val r = minSize * 0.28f

                // Baby face circle
                drawCircle(
                    color = Color(0xFFFFE0B2),
                    radius = r,
                    center = Offset(cx, cy)
                )

                // Cheeks
                drawCircle(
                    color = Color(0xFFFF8A80),
                    radius = r * 0.18f,
                    center = Offset(cx - r * 0.5f, cy + r * 0.2f)
                )
                drawCircle(
                    color = Color(0xFFFF8A80),
                    radius = r * 0.18f,
                    center = Offset(cx + r * 0.5f, cy + r * 0.2f)
                )

                // Big cute baby shiny eyes
                drawCircle(color = Color(0xFF212121), radius = r * 0.14f, center = Offset(cx - r * 0.3f, cy - r * 0.1f))
                drawCircle(color = Color(0xFF212121), radius = r * 0.14f, center = Offset(cx + r * 0.3f, cy - r * 0.1f))
                // Specular glint
                drawCircle(color = Color.White, radius = r * 0.045f, center = Offset(cx - r * 0.33f, cy - r * 0.13f))
                drawCircle(color = Color.White, radius = r * 0.045f, center = Offset(cx + r * 0.27f, cy - r * 0.13f))

                // Sweet pacifier/soother at mouth
                drawCircle(
                    color = Color(0xFF80DEEA), // cyan shield
                    radius = r * 0.24f,
                    center = Offset(cx, cy + r * 0.38f)
                )
                drawCircle(
                    color = Color(0xFFFFEB3B), // yellow holding ring
                    radius = r * 0.12f,
                    center = Offset(cx, cy + r * 0.54f),
                    style = Stroke(width = minSize * 0.03f)
                )

                // Sweet golden baby hair curl on top
                val curlPath = Path().apply {
                    moveTo(cx, cy - r * 0.98f)
                    quadraticTo(cx - r * 0.2f, cy - r * 1.3f, cx + r * 0.1f, cy - r * 1.35f)
                    quadraticTo(cx + r * 0.3f, cy - r * 1.15f, cx + r * 0.06f, cy - r * 1.05f)
                }
                drawPath(path = curlPath, color = Color(0xFFFFB300), style = Stroke(width = minSize * 0.035f))
            }
            "oma" -> {
                // Lovable grandma with glasses and standard hair-bun
                val r = minSize * 0.28f

                // Hair bun
                drawCircle(
                    color = Color(0xFFCFD8DC),
                    radius = r * 0.45f,
                    center = Offset(cx, cy - r * 1.25f)
                )

                // Grey curly hair behind
                drawCircle(
                    color = Color(0xFFECEFF1),
                    radius = r * 1.12f,
                    center = Offset(cx, cy - r * 0.1f)
                )

                // Face circle
                drawCircle(
                    color = Color(0xFFFFD180),
                    radius = r,
                    center = Offset(cx, cy)
                )

                // Eyeglasses (cute square frames in purple)
                val glassW = r * 0.42f
                val glassH = r * 0.32f
                drawRoundRect(
                    color = Color(0xFF8E24AA),
                    topLeft = Offset(cx - r * 0.65f, cy - r * 0.2f),
                    size = Size(glassW, glassH),
                    cornerRadius = CornerRadius(10f, 10f),
                    style = Stroke(width = minSize * 0.022f)
                )
                drawRoundRect(
                    color = Color(0xFF8E24AA),
                    topLeft = Offset(cx + r * 0.15f, cy - r * 0.2f),
                    size = Size(glassW, glassH),
                    cornerRadius = CornerRadius(10f, 10f),
                    style = Stroke(width = minSize * 0.022f)
                )
                // Eyeglasses bridge
                drawLine(
                    color = Color(0xFF8E24AA),
                    start = Offset(cx - r * 0.15f, cy - r * 0.05f),
                    end = Offset(cx + r * 0.15f, cy - r * 0.05f),
                    strokeWidth = minSize * 0.024f
                )

                // Cute sleeping eyes
                drawCircle(color = Color(0xFF3E2723), radius = r * 0.05f, center = Offset(cx - r * 0.44f, cy - r * 0.05f))
                drawCircle(color = Color(0xFF3E2723), radius = r * 0.05f, center = Offset(cx + r * 0.36f, cy - r * 0.05f))

                // Kind smile
                drawArc(
                    color = Color(0xFFE91E63),
                    startAngle = 0f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(cx - r * 0.25f, cy + r * 0.18f),
                    size = Size(r * 0.5f, r * 0.32f),
                    style = Stroke(width = minSize * 0.03f)
                )
            }
            "fork" -> {
                // Steel grey fork with detailed toddler style
                val forkW = minSize * 0.11f
                val forkH = minSize * 0.48f

                // Fork base cup (the head)
                drawRoundRect(
                    color = Color(0xFF90A4AE),
                    topLeft = Offset(cx - forkW * 1.1f, cy - forkH * 0.42f),
                    size = Size(forkW * 2.2f, forkH * 0.32f),
                    cornerRadius = CornerRadius(12f, 12f)
                )

                // Tines cutout (drawn in white/background color to separate 4 tines)
                // 3 slits
                val slitW = forkW * 0.22f
                val slitH = forkH * 0.24f
                drawRect(
                    color = Color.Transparent, // We'll mock the cutout by overlaying the background, or just draw steel tines!
                    topLeft = Offset(cx - forkW * 0.6f, cy - forkH * 0.45f),
                    size = Size(slitW, slitH)
                )
                // Let's draw fork tines positively using drawRect of steel color
                val tineW = forkW * 0.24f
                val tineH = forkH * 0.35f
                for (i in 0..3) {
                    val tintX = cx - forkW * 1.1f + i * (forkW * 0.62f)
                    drawRoundRect(
                        color = Color(0xFF78909C),
                        topLeft = Offset(tintX, cy - forkH * 0.5f),
                        size = Size(tineW, tineH),
                        cornerRadius = CornerRadius(4f, 4f)
                    )
                }

                // Connecting thick bridge
                drawRoundRect(
                    color = Color(0xFF78909C),
                    topLeft = Offset(cx - forkW, cy - forkH * 0.24f),
                    size = Size(forkW * 2f, forkH * 0.15f),
                    cornerRadius = CornerRadius(8f, 8f)
                )

                // Handle (stem)
                drawRoundRect(
                    color = Color(0xFFB0BEC5),
                    topLeft = Offset(cx - forkW * 0.4f, cy - forkH * 0.12f),
                    size = Size(forkW * 0.8f, forkH * 0.62f),
                    cornerRadius = CornerRadius(16f, 16f)
                )

                // Colorful handle tip
                drawRoundRect(
                    color = Color(0xFFFF4081),
                    topLeft = Offset(cx - forkW * 0.4f, cy + forkH * 0.28f),
                    size = Size(forkW * 0.8f, forkH * 0.22f),
                    cornerRadius = CornerRadius(12f, 12f)
                )
            }
            "spoon" -> {
                // Cheerful kitchen spoon
                val spoonW = minSize * 0.13f
                val spoonH = minSize * 0.48f

                // Spoon oval scoop
                drawOval(
                    color = Color(0xFFB0BEC5),
                    topLeft = Offset(cx - spoonW * 1.3f, cy - spoonH * 0.52f),
                    size = Size(spoonW * 2.6f, spoonH * 0.38f)
                )
                // Inside shiny crescent
                drawOval(
                    color = Color(0xFFECEFF1),
                    topLeft = Offset(cx - spoonW * 0.9f, cy - spoonH * 0.48f),
                    size = Size(spoonW * 1.5f, spoonH * 0.22f)
                )

                // Handle shaft
                drawRoundRect(
                    color = Color(0xFF78909C),
                    topLeft = Offset(cx - spoonW * 0.4f, cy - spoonH * 0.16f),
                    size = Size(spoonW * 0.8f, spoonH * 0.66f),
                    cornerRadius = CornerRadius(16f, 16f)
                )

                // Handle grip (colored blue)
                drawRoundRect(
                    color = Color(0xFF29B6F6),
                    topLeft = Offset(cx - spoonW * 0.4f, cy + spoonH * 0.25f),
                    size = Size(spoonW * 0.8f, spoonH * 0.25f),
                    cornerRadius = CornerRadius(12f, 12f)
                )
            }
            "glass" -> {
                // Glass with delicious orange juice
                val wG = minSize * 0.25f
                val hG = minSize * 0.42f

                // Glass cup path (tapered bottom)
                val glassOutline = Path().apply {
                    moveTo(cx - wG * 0.55f, cy - hG * 0.5f)
                    lineTo(cx + wG * 0.55f, cy - hG * 0.5f)
                    lineTo(cx + wG * 0.42f, cy + hG * 0.5f)
                    lineTo(cx - wG * 0.42f, cy + hG * 0.5f)
                    close()
                }

                // Orange juice path (partially filled)
                val juicePath = Path().apply {
                    moveTo(cx - wG * 0.48f, cy - hG * 0.12f)
                    lineTo(cx + wG * 0.48f, cy - hG * 0.12f)
                    lineTo(cx + wG * 0.43f, cy + hG * 0.46f)
                    lineTo(cx - wG * 0.43f, cy + hG * 0.46f)
                    close()
                }

                // Draw orange juice filling
                drawPath(path = juicePath, color = Color(0xFFFF9800))

                // Cute bubble in juice
                drawCircle(color = Color(0xAAFFFFFF), radius = minSize * 0.02f, center = Offset(cx - wG * 0.15f, cy + hG * 0.1f))
                drawCircle(color = Color(0xAAFFFFFF), radius = minSize * 0.012f, center = Offset(cx + wG * 0.18f, cy + hG * 0.22f))

                // Draw solid glass outline border
                drawPath(
                    path = glassOutline,
                    color = Color(0xFFB2EBF2),
                    style = Stroke(width = minSize * 0.032f)
                )

                // Cute drinking straw
                drawLine(
                    color = Color(0xFFE91E63),
                    start = Offset(cx - wG * 0.1f, cy + hG * 0.35f),
                    end = Offset(cx + wG * 0.58f, cy - hG * 0.65f),
                    strokeWidth = minSize * 0.026f,
                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                )
            }
            "knife" -> {
                // Friendly child-safe dinner knife
                val kW = minSize * 0.12f
                val kH = minSize * 0.45f

                // Safe round metal blade
                val bladePath = Path().apply {
                    moveTo(cx - kW * 0.35f, cy + kH * 0.08f)
                    lineTo(cx - kW * 0.35f, cy - kH * 0.52f)
                    quadraticTo(cx + kW * 0.25f, cy - kH * 0.52f, cx + kW * 0.35f, cy - kH * 0.42f)
                    quadraticTo(cx + kW * 0.35f, cy, cx - kW * 0.05f, cy + kH * 0.08f)
                    close()
                }
                drawPath(path = bladePath, color = Color(0xFFCFD8DC))

                // Highlight gloss on blade
                drawLine(
                    color = Color.White,
                    start = Offset(cx - kW * 0.15f, cy - kH * 0.45f),
                    end = Offset(cx - kW * 0.15f, cy - kH * 0.1f),
                    strokeWidth = minSize * 0.015f
                )

                // Wooden brown grip handle
                drawRoundRect(
                    color = Color(0xFF8D6E63),
                    topLeft = Offset(cx - kW * 0.45f, cy + kH * 0.06f),
                    size = Size(kW * 0.9f, kH * 0.46f),
                    cornerRadius = CornerRadius(14f, 14f)
                )

                // Golden rivets on handle for realism
                drawCircle(color = Color(0xFFFFD54F), radius = minSize * 0.015f, center = Offset(cx, cy + kH * 0.2f))
                drawCircle(color = Color(0xFFFFD54F), radius = minSize * 0.015f, center = Offset(cx, cy + kH * 0.38f))
            }
            "beach" -> {
                // Sunny beach with parasol
                val r = minSize * 0.35f

                // Draw yellow golden sand dunes
                val sandPath = Path().apply {
                    moveTo(cx - r, cy + r * 0.8f)
                    quadraticTo(cx - r * 0.2f, cy + r * 0.2f, cx + r, cy + r * 0.6f)
                    lineTo(cx + r, cy + r * 1.1f)
                    lineTo(cx - r, cy + r * 1.1f)
                    close()
                }
                drawPath(path = sandPath, color = Color(0xFFFFE082))

                // Sea water behind beach (tiny sliver on left)
                drawCircle(color = Color(0xFF4FC3F7), radius = r * 0.4f, center = Offset(cx - r * 0.8f, cy + r * 0.3f))

                // Beach parasol pole
                drawLine(
                    color = Color(0xFF795548),
                    start = Offset(cx, cy + r * 0.45f),
                    end = Offset(cx - r * 0.15f, cy - r * 0.65f),
                    strokeWidth = minSize * 0.024f
                )

                // Colorful parasol umbrella dome
                val domePath = Path().apply {
                    moveTo(cx - r * 0.85f, cy - r * 0.15f)
                    quadraticTo(cx - r * 0.25f, cy - r * 0.75f, cx - r * 0.15f, cy - r * 0.65f)
                    quadraticTo(cx + r * 0.1f, cy - r * 0.55f, cx + r * 0.55f, cy - r * 0.15f)
                    quadraticTo(cx - r * 0.15f, cy - r * 0.05f, cx - r * 0.85f, cy - r * 0.15f)
                    close()
                }
                drawPath(path = domePath, color = Color(0xFFE53935)) // Red canopy

                // Yellow stripe highlight on umbrella
                val stripePath = Path().apply {
                    moveTo(cx - r * 0.5f, cy - r * 0.32f)
                    quadraticTo(cx - r * 0.2f, cy - r * 0.68f, cx - r * 0.15f, cy - r * 0.65f)
                    quadraticTo(cx - r * 0.05f, cy - r * 0.52f, cx + r * 0.18f, cy - r * 0.22f)
                    close()
                }
                drawPath(path = stripePath, color = Color(0xFFFFEB3B)) // Yellow accent
            }
            "sea" -> {
                // Ocean waves with a little jumping child-friendly dolphin/fish
                val r = minSize * 0.36f

                // Deep water base
                drawCircle(
                    color = Color(0xFF0288D1),
                    radius = r,
                    center = Offset(cx, cy)
                )

                // Wavy overlay 1 (Cyan light waves)
                val wavePath = Path().apply {
                    moveTo(cx - r, cy)
                    quadraticTo(cx - r * 0.5f, cy - r * 0.4f, cx, cy)
                    quadraticTo(cx + r * 0.5f, cy + r * 0.4f, cx + r, cy)
                    lineTo(cx + r, cy + r)
                    lineTo(cx - r, cy + r)
                    close()
                }
                drawPath(path = wavePath, color = Color(0xFF26C6DA))

                // Bright yellow sun in upper right
                drawCircle(
                    color = Color(0xFFFFEB3B),
                    radius = r * 0.28f,
                    center = Offset(cx + r * 0.6f, cy - r * 0.5f)
                )

                // Jumping cute orange companion fish
                val fishW = r * 0.45f
                val fishH = r * 0.28f
                val fX = cx - r * 0.15f
                val fY = cy - r * 0.15f

                val fPath = Path().apply {
                    moveTo(fX - fishW * 0.5f, fY)
                    quadraticTo(fX, fY - fishH * 0.6f, fX + fishW * 0.5f, fY)
                    quadraticTo(fX, fY + fishH * 0.6f, fX - fishW * 0.5f, fY)
                    close()
                }
                drawPath(path = fPath, color = Color(0xFFFF7043))

                // Tail fin
                val tailP = Path().apply {
                    moveTo(fX - fishW * 0.4f, fY)
                    lineTo(fX - fishW * 0.72f, fY - fishH * 0.42f)
                    lineTo(fX - fishW * 0.65f, fY)
                    lineTo(fX - fishW * 0.72f, fY + fishH * 0.42f)
                    close()
                }
                drawPath(path = tailP, color = Color(0xFFFF5722))
            }
            "icecream" -> {
                // Summer waffle ice cream cone
                val cW = minSize * 0.18f
                val cH = minSize * 0.38f

                // Waffle cone triangle
                val conePath = Path().apply {
                    moveTo(cx - cW, cy)
                    lineTo(cx + cW, cy)
                    lineTo(cx, cy + cH)
                    close()
                }
                drawPath(path = conePath, color = Color(0xFFD7CCC8)) // Waffle brown

                // Grid cross hatches on waffle
                drawLine(Color(0xFF8D6E63), Offset(cx - cW * 0.5f, cy + cH * 0.25f), Offset(cx + cW * 0.5f, cy + cH * 0.25f), strokeWidth = minSize * 0.012f)
                drawLine(Color(0xFF8D6E63), Offset(cx - cW * 0.25f, cy + cH * 0.62f), Offset(cx + cW * 0.25f, cy + cH * 0.62f), strokeWidth = minSize * 0.012f)
                drawLine(Color(0xFF8D6E63), Offset(cx - cW * 0.5f, cy), Offset(cx, cy + cH), strokeWidth = minSize * 0.012f)
                drawLine(Color(0xFF8D6E63), Offset(cx + cW * 0.5f, cy), Offset(cx, cy + cH), strokeWidth = minSize * 0.012f)

                // Large pink strawberry ice-cream scoop
                drawCircle(
                    color = Color(0xFFF48FB1),
                    radius = cW * 1.2f,
                    center = Offset(cx, cy - cW * 0.2f)
                )

                // Smaller green pistachio scoop on top
                drawCircle(
                    color = Color(0xFFA5D6A7),
                    radius = cW * 0.9f,
                    center = Offset(cx, cy - cW * 1.4f)
                )

                // Bright red cherry on absolute peak
                drawCircle(
                    color = Color(0xFFE53935),
                    radius = cW * 0.35f,
                    center = Offset(cx, cy - cW * 2.3f)
                )
                // Cherrys stem
                drawLine(
                    color = Color(0xFF3E2723),
                    start = Offset(cx, cy - cW * 2.5f),
                    end = Offset(cx + cW * 0.3f, cy - cW * 2.9f),
                    strokeWidth = minSize * 0.014f
                )
            }
            "ball" -> {
                // Highly colorful toddler beach ball
                val r = minSize * 0.32f

                // Outer border/base
                drawCircle(
                    color = Color(0xFFF5F5F5),
                    radius = r,
                    center = Offset(cx, cy)
                )

                // Draw slices (Red, Blue, Yellow, Green segments)
                // We'll draw 6 colored segments using simple arc angles
                drawArc(
                    color = Color(0xFFEF5350), // Red
                    startAngle = 0f,
                    sweepAngle = 120f,
                    useCenter = true,
                    topLeft = Offset(cx - r, cy - r),
                    size = Size(r * 2, r * 2)
                )
                drawArc(
                    color = Color(0xFF42A5F5), // Blue
                    startAngle = 120f,
                    sweepAngle = 120f,
                    useCenter = true,
                    topLeft = Offset(cx - r, cy - r),
                    size = Size(r * 2, r * 2)
                )
                drawArc(
                    color = Color(0xFFFFEE58), // Yellow
                    startAngle = 240f,
                    sweepAngle = 120f,
                    useCenter = true,
                    topLeft = Offset(cx - r, cy - r),
                    size = Size(r * 2, r * 2)
                )

                // White glossy inner circles for toddler stylized look
                drawCircle(
                    color = Color.White,
                    radius = r * 0.26f,
                    center = Offset(cx, cy)
                )
                // Yellow core badge
                drawCircle(
                    color = Color(0xFFFFA726),
                    radius = r * 0.14f,
                    center = Offset(cx, cy)
                )
            }
            else -> {
                val r = minSize * 0.32f
                drawCircle(
                    color = Color(0xFFFFD54F).copy(alpha = 0.2f),
                    radius = r * 1.15f,
                    center = Offset(cx, cy)
                )
                drawCircle(
                    color = Color(0xFFFFCA28),
                    radius = r,
                    center = Offset(cx, cy)
                )
                drawCircle(
                    color = Color.White,
                    radius = r * 0.85f,
                    center = Offset(cx, cy)
                )
            }
        }
    }
}

    if (!defaultIds.contains(itemId)) {
        Box(
            modifier = Modifier.fillMaxSize(0.6f),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text(fallbackEmoji, fontSize = 36.sp)
        }
    }
}
}
