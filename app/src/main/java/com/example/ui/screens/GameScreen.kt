package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Vocabulary
import com.example.data.VocabularyItem
import com.example.ui.components.VocabularyIllustration
import com.example.viewmodel.GameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val currentRoundItems by viewModel.currentRoundItems.collectAsState()
    val shuffledDutch by viewModel.shuffledDutch.collectAsState()
    val shuffledMacedonian by viewModel.shuffledMacedonian.collectAsState()
    val matchedIds by viewModel.matchedIds.collectAsState()
    val selectedDutchId by viewModel.selectedDutchId.collectAsState()
    val selectedMacedonianId by viewModel.selectedMacedonianId.collectAsState()
    val totalStars by viewModel.totalStars.collectAsState()
    val allProgressLogs by viewModel.allProgress.collectAsState()
    val allVocabList by viewModel.allVocabularyItemsFromDb.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val currentScreen by viewModel.currentScreen.collectAsState()
    val categories by viewModel.categories.collectAsState()

    val scope = rememberCoroutineScope()

    // Parenting Gate dialog states
    var showParentGate by remember { mutableStateOf(false) }
    var gateMathProblem by remember { mutableStateOf(Pair(0, 0)) }
    var gateInputAnswer by remember { mutableStateOf("") }
    var gateIsWrong by remember { mutableStateOf(false) }
    var showParentPanel by remember { mutableStateOf(false) }

    // Coordinates mapping for drag-n-drop targets
    val macedonianBounds = remember { mutableStateMapOf<String, Rect>() }

    // Particle updater loop
    LaunchedEffect(viewModel.particles.size) {
        while (viewModel.particles.isNotEmpty()) {
            delay(16)
            viewModel.updateParticles()
        }
    }

    // Is the round completely matched?
    val roundCompleted = currentRoundItems.isNotEmpty() && matchedIds.size == currentRoundItems.size

    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600

    // Gradient background for kids (sky sunrise playful vibe)
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE0F7FA), // Soft cyan sky
                        Color(0xFFFFF9C4), // Soft sunshine yellow
                        Color(0xFFFFECB3)  // Playful melon orange
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: Clouds styling with title and Stars count
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Clouds shaped Title
                Box(
                    modifier = Modifier
                        .shadow(4.dp, shape = RoundedCornerShape(24.dp))
                        .background(Color.White, shape = RoundedCornerShape(24.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🇳🇱 ➔ 🇲🇰",
                            fontSize = 24.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Column {
                            Text(
                                text = "Match & Leer",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFE53935)
                            )
                            Text(
                                text = "Dutch to Macedonian",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray
                            )
                        }
                    }
                }

                // Parent's Gate Button & Stars Counter
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Parents corner button
                    Button(
                        onClick = {
                            // Generate safe parent's verification check math quiz
                            val num1 = Random.nextInt(4, 9)
                            val num2 = Random.nextInt(5, 9)
                            gateMathProblem = Pair(num1, num2)
                            gateInputAnswer = ""
                            gateIsWrong = false
                            showParentGate = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier.shadow(2.dp, RoundedCornerShape(50))
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Filled.Lock, contentDescription = "Parents Screen", modifier = Modifier.size(16.dp))
                            Text("Parents", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Kids Sparkly Gold Star score counter
                    Box(
                        modifier = Modifier
                            .shadow(4.dp, shape = RoundedCornerShape(24.dp))
                            .background(Color(0xFFFFD54F), shape = RoundedCornerShape(24.dp))
                            .border(3.dp, Color.White, shape = RoundedCornerShape(24.dp))
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = "Stars Count",
                                tint = Color(0xFFF57F17),
                                modifier = Modifier.size(22.dp)
                            )
                            AnimatedContent(
                                targetState = totalStars,
                                transitionSpec = {
                                    slideInVertically { height -> height } + fadeIn() togetherWith
                                            slideOutVertically { height -> -height } + fadeOut()
                                },
                                label = "starAnim"
                            ) { stars ->
                                Text(
                                    text = "$stars",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF5D4037)
                                )
                            }
                        }
                    }
                }
            }

            LaunchedEffect(selectedCategory) {
                macedonianBounds.clear()
            }

            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    slideInHorizontally { width -> width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> -width } + fadeOut()
                },
                label = "screenTransition",
                modifier = Modifier.weight(1f).fillMaxWidth()
            ) { screen ->
                when (screen) {
                    is com.example.viewmodel.Screen.CategorySelection -> {
                        CategorySelectionView(
                            categories = categories,
                            progressLogs = allProgressLogs,
                            vocabList = allVocabList,
                            onCategoryClick = { category ->
                                viewModel.navigateToCategory(category)
                            },
                            onStartQuiz = { count, category ->
                                viewModel.startCustomQuiz(count, category)
                            }
                        )
                    }
                    is com.example.viewmodel.Screen.TopicDetail -> {
                        TopicDetailView(
                            category = screen.category,
                            viewModel = viewModel,
                            shuffledDutch = shuffledDutch,
                            shuffledMacedonian = shuffledMacedonian,
                            matchedIds = matchedIds,
                            selectedDutchId = selectedDutchId,
                            selectedMacedonianId = selectedMacedonianId,
                            roundCompleted = roundCompleted,
                            macedonianBounds = macedonianBounds,
                            isTablet = isTablet,
                            onBackClick = {
                                viewModel.navigateBackToHome()
                            }
                        )
                    }
                }
            }
        }

        // Overlay Particles effect render layer
        ParticlesScreen(particles = viewModel.particles)

        // Parent Math Authorization Gate Dialog
        if (showParentGate) {
            AlertDialog(
                onDismissRequest = { showParentGate = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Ouder-Verificatie 🛡️", fontWeight = FontWeight.Black, fontSize = 20.sp)
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Om de ouderlijke statistieken te bekijken, gelieve dit som op te lossen (ter bescherming tegen peuters):",
                            fontSize = 14.sp
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Hoeveel is  ${gateMathProblem.first} + ${gateMathProblem.second} ?",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF3F51B5)
                            )
                        }

                        OutlinedTextField(
                            value = gateInputAnswer,
                            onValueChange = { gateInputAnswer = it },
                            label = { Text("Uw antwoord") },
                            placeholder = { Text("Typ getal") },
                            isError = gateIsWrong,
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF3F51B5),
                                errorBorderColor = Color.Red
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (gateIsWrong) {
                            Text(
                                text = "Fout antwoord. Probeer het opnieuw!",
                                color = Color.Red,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val correct = gateMathProblem.first + gateMathProblem.second
                            if (gateInputAnswer.trim() == correct.toString()) {
                                showParentGate = false
                                showParentPanel = true
                            } else {
                                gateIsWrong = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
                    ) {
                        Text("Verifieer")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showParentGate = false }) {
                        Text("Annuleer", color = Color.Gray)
                    }
                }
            )
        }

        // Parent Stats Panel Overlay Window
        if (showParentPanel) {
            val allVocabList by viewModel.allVocabularyItemsFromDb.collectAsState()
            ParentProgressStatsView(
                progressLogs = allProgressLogs,
                vocabList = allVocabList,
                onClose = { showParentPanel = false },
                onClearHistory = { viewModel.clearHistory() }
            )
        }
    }
}

@Composable
fun DutchItemCard(
    item: VocabularyItem,
    isMatched: Boolean,
    isSelected: Boolean,
    onSelect: () -> Unit,
    macedonianBounds: Map<String, Rect>,
    viewModel: GameViewModel
) {
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var isDragging by remember { mutableStateOf(false) }
    var localBounds by remember { mutableStateOf(Rect.Zero) }

    val scaleFactor by animateFloatAsState(
        targetValue = if (isMatched) 0.85f else if (isDragging || isSelected) 1.08f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    val translationX = if (isDragging) dragOffset.x else 0f
    val translationY = if (isDragging) dragOffset.y else 0f

    // Card background brush & borders
    val mainBgColor = if (isMatched) Color(0xFFE8F5E9) else Color(item.themeColorHex)
    val strokeColor = if (isMatched) {
        Color(0xFF4CAF50)
    } else if (isSelected) {
        Color(0xFFF57F17) // Gold outline when tapped/selected
    } else {
        Color.White
    }

    Box(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .height(110.dp)
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scaleFactor
                scaleY = scaleFactor
                this.translationX = translationX
                this.translationY = translationY
            }
            .shadow(
                elevation = if (isDragging) 12.dp else 4.dp,
                shape = RoundedCornerShape(24.dp)
            )
            .background(mainBgColor, RoundedCornerShape(24.dp))
            .border(
                width = if (isSelected) 4.dp else 3.dp,
                color = strokeColor,
                shape = RoundedCornerShape(24.dp)
            )
            .onGloballyPositioned { coords ->
                localBounds = coords.boundsInWindow()
            }
            .pointerInput(item.id) {
                if (isMatched) return@pointerInput
                detectDragGestures(
                    onDragStart = { offset ->
                        isDragging = true
                        dragOffset = Offset.Zero
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragOffset += dragAmount
                    },
                    onDragEnd = {
                        isDragging = false
                        // Calculate hit detection on drops
                        val absoluteDropPoint = Offset(
                            localBounds.left + localBounds.width / 2 + dragOffset.x,
                            localBounds.top + localBounds.height / 2 + dragOffset.y
                        )
                        var hitId: String? = null
                        for ((macId, rect) in macedonianBounds) {
                            if (rect.contains(absoluteDropPoint)) {
                                hitId = macId
                                break
                            }
                        }
                        if (hitId != null) {
                            viewModel.selectDutch(item.id)
                            viewModel.selectMacedonian(hitId)
                        }
                        dragOffset = Offset.Zero
                    },
                    onDragCancel = {
                        isDragging = false
                        dragOffset = Offset.Zero
                    }
                )
            }
            .clickable(enabled = !isMatched) {
                // Clicking is also fully supported for toddlers!
                onSelect()
            },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // High fidelity beautiful canvas shape illustrator
            Box(
                modifier = Modifier
                    .size(65.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .border(2.dp, Color(0xFFF5F5F5), RoundedCornerShape(16.dp))
                    .padding(4.dp)
            ) {
                VocabularyIllustration(itemId = item.id, fallbackEmoji = item.emoji, imagePath = item.imagePath)
            }

            // Word labels & category stamp
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = item.category.uppercase(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.dutch,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF4E342E)
                )
            }

            // Matched checked checkmark representation
            if (isMatched) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = "Matched Done Check",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(28.dp)
                )
            } else {
                // Cute drag indicator dot grid
                Text("☰", fontSize = 16.sp, color = Color.Gray.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
fun MacedonianTargetCard(
    item: VocabularyItem,
    isMatched: Boolean,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onGloballyPositioned: (Rect) -> Unit
) {
    val scaleFactor by animateFloatAsState(
        targetValue = if (isMatched) 0.85f else if (isSelected) 1.05f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )

    // Card background styling
    val mainBgColor = if (isMatched) Color(0xFFE8F5E9) else Color.White
    val strokeColor = if (isMatched) {
        Color(0xFF4CAF50)
    } else if (isSelected) {
        Color(0xFF3F51B5) // Indigo outline when selected
    } else {
        Color(0xFFE0E0E0)
    }

    Box(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .height(110.dp)
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scaleFactor
                scaleY = scaleFactor
            }
            .shadow(4.dp, shape = RoundedCornerShape(24.dp))
            .background(mainBgColor, RoundedCornerShape(24.dp))
            .border(
                width = if (isSelected) 4.dp else 2.dp,
                color = strokeColor,
                shape = RoundedCornerShape(24.dp)
            )
            .onGloballyPositioned { coords ->
                onGloballyPositioned(coords.boundsInWindow())
            }
            .clickable(enabled = !isMatched) {
                onSelect()
            },
        contentAlignment = Alignment.Center
    ) {
        if (isMatched) {
            // Completed matched state
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(12.dp)
            ) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = "Success Double Done",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = item.macedonianCyrillic,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
            }
        } else {
            // Cute Macedonian display with Cyrillic & phonetic sounds
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Macedonian Cyrillic main bold text
                Text(
                    text = item.macedonianCyrillic,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1E88E5),
                    textAlign = TextAlign.Center
                )

                // Standard Roman spelling (Jabolko)
                Text(
                    text = item.macedonian,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5E35B1),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Toddler phonetic pronunciation speaker guide ("JA-bol-ko")
                Box(
                    modifier = Modifier
                        .background(Color(0xFFECEFF1), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "🗣️ ${item.pronunciation}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF37474F)
                    )
                }
            }
        }
    }
}

@Composable
fun ParticlesScreen(particles: List<com.example.viewmodel.Particle>) {
    Box(modifier = Modifier.fillMaxSize()) {
        particles.forEach { part ->
            Box(
                modifier = Modifier
                    .offset(x = 180.dp + part.currentX.dp, y = 300.dp + part.currentY.dp)
                    .scale(part.scale)
                    .graphicsLayer { alpha = part.alpha }
            ) {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFD54F),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

// Parent Progress Monitoring Screen
@Composable
fun ParentProgressStatsView(
    progressLogs: List<com.example.data.ProgressEntity>,
    vocabList: List<com.example.data.VocabularyItem>,
    onClose: () -> Unit,
    onClearHistory: () -> Unit
) {
    // Collect stats from history
    val totalAttempts = progressLogs.size
    val successes = progressLogs.count { it.isCorrect }
    val errors = totalAttempts - successes
    val successRate = if (totalAttempts > 0) (successes * 100 / totalAttempts) else 0

    // Compute metrics per word
    val wordSuccessCount = remember(progressLogs, vocabList) {
        mutableMapOf<String, Int>().apply {
            vocabList.forEach { v -> this[v.dutch] = 0 }
            progressLogs.filter { it.isCorrect }.forEach { log ->
                this[log.dutchWord] = (this[log.dutchWord] ?: 0) + 1
            }
        }
    }
    val wordFailureCount = remember(progressLogs, vocabList) {
        mutableMapOf<String, Int>().apply {
            vocabList.forEach { v -> this[v.dutch] = 0 }
            progressLogs.filter { !it.isCorrect }.forEach { log ->
                this[log.dutchWord] = (this[log.dutchWord] ?: 0) + 1
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x99000000))
            .clickable { onClose() },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .clickable(enabled = false) {}, // prevent click-out side dismissal, force close button click
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Filled.Info,
                            contentDescription = "Parents Corner Icon",
                            tint = Color(0xFF3F51B5),
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = "Parents' Corner 📊",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF1E88E5)
                            )
                            Text(
                                text = "SQLite Database View & Progress",
                                fontSize = 9.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Button(
                        onClick = onClose,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text("Close", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Stats Summary Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFE3F2FD), RoundedCornerShape(12.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Total Played", fontSize = 10.sp, color = Color.Gray)
                            Text("$totalAttempts", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFF1565C0))
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFE8F5E9), RoundedCornerShape(12.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Matched OK", fontSize = 10.sp, color = Color.Gray)
                            Text("$successes 🌟", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFF2E7D32))
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1.1f)
                            .background(Color(0xFFFFF8E1), RoundedCornerShape(12.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Correct Rate", fontSize = 10.sp, color = Color.Gray)
                            Text("$successRate%", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFFF57F17))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = Color(0xFFEEEEEE), thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))

                // TAB 0: Mastery Logs List
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Vocabulary Mastery Tracker",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(vocabList) { vocab ->
                            val matchedCount = wordSuccessCount[vocab.dutch] ?: 0
                            val missedCount = wordFailureCount[vocab.dutch] ?: 0

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFFAFAFA), RoundedCornerShape(12.dp))
                                    .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(12.dp))
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(Color(vocab.themeColorHex).copy(alpha = 0.3f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(vocab.emoji, fontSize = 16.sp)
                                    }
                                    Column {
                                        Text(
                                            text = "${vocab.dutch} ➔ ${vocab.macedonianCyrillic}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                        Text(
                                            text = "${vocab.category} (${vocab.macedonian} / ${vocab.pronunciation})",
                                            fontSize = 10.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFFE8F5E9), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 6.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = "OK: $matchedCount",
                                            color = Color(0xFF2E7D32),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    if (missedCount > 0) {
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0xFFFFEBEE), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = "X: $missedCount",
                                                color = Color(0xFFC62828),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Footer Actions: Clear history
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔒 No ads, fully offline database viewer.",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )

                    Button(
                        onClick = onClearHistory,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Reset stats", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reset Logs", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun CategorySelectionView(
    categories: List<String>,
    progressLogs: List<com.example.data.ProgressEntity>,
    vocabList: List<com.example.data.VocabularyItem>,
    onCategoryClick: (String) -> Unit,
    onStartQuiz: (Int, String) -> Unit
) {
    val learnedWords = remember(progressLogs) {
        progressLogs.filter { it.isCorrect }.map { it.dutchWord }.toSet()
    }

    var quizSize by remember { mutableStateOf(5) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF6))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "🏆 Super Slimme Quiz 🎯",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF1A237E)
                )
                Text(
                    text = "Kies hoeveel woorden je wilt matchen uit alle categorieën:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3F51B5),
                    textAlign = TextAlign.Center
                )

                // Row of Pill Buttons
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val countPills = listOf(3, 5, 8, 10, 12, 15)
                    countPills.forEach { count ->
                        val isSelected = quizSize == count
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) Color(0xFF3F51B5) else Color.White)
                                .border(2.dp, Color(0xFF3F51B5).copy(alpha = 0.3f), CircleShape)
                                .clickable { quizSize = count },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = count.toString(),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isSelected) Color.White else Color(0xFF3F51B5)
                            )
                        }
                    }
                }

                Button(
                    onClick = { onStartQuiz(quizSize, "Alle") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp)),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Text(
                        text = "Start Quiz! 🚀 ($quizSize woorden)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }
        }

        Text(
            text = "Of kies een losse categorie: 👇",
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF4E342E),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                val emoji = when (category) {
                    "Gezin" -> "👨‍👩‍👦"
                    "Keuken" -> "🍳"
                    "Vakantie" -> "🏖️"
                    else -> "🌈"
                }

                val bgGradient = when (category) {
                    "Gezin" -> Brush.linearGradient(listOf(Color(0xFFFFD180), Color(0xFFFFB74D)))
                    "Keuken" -> Brush.linearGradient(listOf(Color(0xFFFFF59D), Color(0xFFFFEE58)))
                    "Vakantie" -> Brush.linearGradient(listOf(Color(0xFF80DEEA), Color(0xFF26C6DA)))
                    else -> Brush.linearGradient(listOf(Color(0xFFE1BEE7), Color(0xFFBA68C8)))
                }

                val contentColor = when (category) {
                    "Gezin" -> Color(0xFFE65100)
                    "Keuken" -> Color(0xFFF57F17)
                    "Vakantie" -> Color(0xFF006064)
                    else -> Color(0xFF4A148C)
                }

                val description = when (category) {
                    "Gezin" -> "Mama, Papa, Oma..."
                    "Keuken" -> "Vork, Lepel, Mes..."
                    "Vakantie" -> "Strand, Zee, Bal..."
                    else -> "Alle woorden!"
                }

                // Compute progress count
                val categoryWords = if (category == "Alle") vocabList else vocabList.filter { it.category.equals(category, ignoreCase = true) }
                val (correctCount, totalCount) = Pair(categoryWords.count { it.dutch in learnedWords }, categoryWords.size)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .shadow(4.dp, RoundedCornerShape(24.dp))
                        .clickable { onCategoryClick(category) },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(bgGradient)
                            .padding(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = emoji,
                                fontSize = 48.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = category,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = contentColor
                                )
                                Text(
                                    text = description,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = contentColor.copy(alpha = 0.8f),
                                    textAlign = TextAlign.Center
                                )
                            }

                            // Progress bubble outline
                            Box(
                                modifier = Modifier
                                    .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(50))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "$correctCount / $totalCount gekoppeld ✔️",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = contentColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopicDetailView(
    category: String,
    viewModel: GameViewModel,
    shuffledDutch: List<com.example.data.VocabularyItem>,
    shuffledMacedonian: List<com.example.data.VocabularyItem>,
    matchedIds: Set<String>,
    selectedDutchId: String?,
    selectedMacedonianId: String?,
    roundCompleted: Boolean,
    macedonianBounds: MutableMap<String, Rect>,
    isTablet: Boolean,
    onBackClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Word Dictionary, 1 = Puzzle match game

    val categoryColor = when (category) {
        "Gezin" -> Color(0xFFE65100)
        "Keuken" -> Color(0xFFF57F17)
        "Vakantie" -> Color(0xFF006064)
        else -> Color(0xFF4A148C)
    }

    val categoryBgColor = when (category) {
        "Gezin" -> Color(0xFFFFE0B2)
        "Keuken" -> Color(0xFFFFF9C4)
        "Vakantie" -> Color(0xFFB2EBF2)
        else -> Color(0xFFE1BEE7)
    }

    val allVocabList by viewModel.allVocabularyItemsFromDb.collectAsState()
    val wordsOfCategory = remember(category, allVocabList) {
        if (category == "Alle") {
            allVocabList
        } else {
            allVocabList.filter { it.category.equals(category, ignoreCase = true) }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Sub-Header Navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onBackClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                border = BorderStroke(2.dp, categoryColor.copy(alpha = 0.6f)),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                modifier = Modifier.shadow(2.dp, RoundedCornerShape(16.dp))
            ) {
                Text(text = "⬅️ Start", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = categoryColor)
            }

            Box(
                modifier = Modifier
                    .shadow(2.dp, RoundedCornerShape(20.dp))
                    .background(categoryBgColor, RoundedCornerShape(20.dp))
                    .border(2.dp, categoryColor.copy(alpha = 0.8f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 5.dp)
            ) {
                Text(
                    text = when (category) {
                        "Gezin" -> "👨‍👩‍👦 Gezin"
                        "Keuken" -> "🍳 Keuken"
                        "Vakantie" -> "🏖️ Vakantie"
                        else -> "🌈 Alle Woorden"
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = categoryColor
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Kid-friendly dynamic tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(18.dp))
                .border(2.dp, Color.LightGray.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (selectedTab == 0) categoryColor else Color.Transparent)
                    .clickable { selectedTab = 0 }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📖 Woorden (${wordsOfCategory.size})",
                    color = if (selectedTab == 0) Color.White else Color.Gray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (selectedTab == 1) categoryColor else Color.Transparent)
                    .clickable { selectedTab = 1 }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🎯 Match Game",
                    color = if (selectedTab == 1) Color.White else Color.Gray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Content
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (selectedTab == 0) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(wordsOfCategory) { word ->
                        WordDictionaryCard(word = word, categoryColor = categoryColor)
                    }
                }
            } else {
                MatchGamePanel(
                    viewModel = viewModel,
                    shuffledDutch = shuffledDutch,
                    shuffledMacedonian = shuffledMacedonian,
                    matchedIds = matchedIds,
                    selectedDutchId = selectedDutchId,
                    selectedMacedonianId = selectedMacedonianId,
                    roundCompleted = roundCompleted,
                    macedonianBounds = macedonianBounds,
                    isTablet = isTablet
                )
            }
        }
    }
}

@Composable
fun WordDictionaryCard(word: com.example.data.VocabularyItem, categoryColor: Color) {
    var isClicked by remember { mutableStateOf(false) }
    val bounceScale by animateFloatAsState(
        targetValue = if (isClicked) 1.08f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        finishedListener = { isClicked = false },
        label = "clickDictionary"
    )

    val scope = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(bounceScale)
            .shadow(2.dp, RoundedCornerShape(20.dp))
            .clickable {
                isClicked = true
                scope.launch {
                    com.example.sound.TtsManager.playAudio("", word.dutch, "NL")
                }
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color(word.themeColorHex).copy(alpha = 0.15f), RoundedCornerShape(14.dp))
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                VocabularyIllustration(itemId = word.id, modifier = Modifier.fillMaxSize(), fallbackEmoji = word.emoji, imagePath = word.imagePath)
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = word.dutch,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF4E342E)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = word.macedonianCyrillic,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = categoryColor
                    )
                    Text(
                        text = "(${word.macedonian})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                }

                Box(
                    modifier = Modifier
                        .background(Color(0xFFECEFF1), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "🗣️ ${word.pronunciation}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF37474F)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(categoryColor.copy(alpha = 0.1f), CircleShape)
                    .clickable {
                        scope.launch {
                            com.example.sound.TtsManager.playAudio(word.audioPath, word.macedonianCyrillic, "MK_CYR")
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("🔊", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun MatchGamePanel(
    viewModel: GameViewModel,
    shuffledDutch: List<com.example.data.VocabularyItem>,
    shuffledMacedonian: List<com.example.data.VocabularyItem>,
    matchedIds: Set<String>,
    selectedDutchId: String?,
    selectedMacedonianId: String?,
    roundCompleted: Boolean,
    macedonianBounds: MutableMap<String, Rect>,
    isTablet: Boolean
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Sleep of tik om kaarten te matchen! 🌟",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4E342E),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (isTablet) {
                    // Side-by-side tablet
                    val tableScrollState = rememberScrollState()
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(tableScrollState),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            shuffledDutch.forEach { item ->
                                DutchItemCard(
                                    item = item,
                                    isMatched = matchedIds.contains(item.id),
                                    isSelected = selectedDutchId == item.id,
                                    onSelect = { viewModel.selectDutch(item.id) },
                                    macedonianBounds = macedonianBounds,
                                    viewModel = viewModel
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .width(40.dp)
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            shuffledMacedonian.forEach { item ->
                                MacedonianTargetCard(
                                    item = item,
                                    isMatched = matchedIds.contains(item.id),
                                    isSelected = selectedMacedonianId == item.id,
                                    onSelect = { viewModel.selectMacedonian(item.id) },
                                    onGloballyPositioned = { rect ->
                                        macedonianBounds[item.id] = rect
                                    }
                                )
                            }
                        }
                    }
                } else {
                    // Stacked columns on mobile
                    val mobScrollState = rememberScrollState()
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(mobScrollState),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            shuffledDutch.forEach { item ->
                                DutchItemCard(
                                    item = item,
                                    isMatched = matchedIds.contains(item.id),
                                    isSelected = selectedDutchId == item.id,
                                    onSelect = { viewModel.selectDutch(item.id) },
                                    macedonianBounds = macedonianBounds,
                                    viewModel = viewModel
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            shuffledMacedonian.forEach { item ->
                                MacedonianTargetCard(
                                    item = item,
                                    isMatched = matchedIds.contains(item.id),
                                    isSelected = selectedMacedonianId == item.id,
                                    onSelect = { viewModel.selectMacedonian(item.id) },
                                    onGloballyPositioned = { rect ->
                                        macedonianBounds[item.id] = rect
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Info,
                    contentDescription = null,
                    tint = Color(0xFF5D4037).copy(alpha = 0.5f),
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = " Sleep de prent naar de juiste Macedonische naam!",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5D4037).copy(alpha = 0.7f)
                )
            }
        }

        // Completion Trophy
        androidx.compose.animation.AnimatedVisibility(
            visible = roundCompleted,
            enter = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)) + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xB31A1A1A))
                    .clickable(enabled = false) {},
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .padding(20.dp)
                        .shadow(12.dp, RoundedCornerShape(32.dp))
                        .background(Color.White, RoundedCornerShape(32.dp))
                        .border(6.dp, Color(0xFFFFD54F), RoundedCornerShape(32.dp))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "GEWELDIG! 🎉",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF4CAF50),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Браво! Многу добро!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3F51B5),
                            textAlign = TextAlign.Center
                        )

                        val infiniteTransition = rememberInfiniteTransition(label = "trophyStar")
                        val starAngle by infiniteTransition.animateFloat(
                            initialValue = -10f,
                            targetValue = 10f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1200, easing = LinearEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "trophyAngle"
                        )
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .rotate(starAngle),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD54F),
                                modifier = Modifier.fillMaxSize()
                            )
                            Text("😊", fontSize = 34.sp)
                        }

                        val roundSize = shuffledDutch.size
                        Text(
                            text = "+$roundSize Sterren verdiend! " + "🌟".repeat(minOf(5, roundSize)),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFF57C00)
                        )

                        Button(
                            onClick = { viewModel.generateNewRound() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            shape = RoundedCornerShape(32.dp),
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .shadow(3.dp, RoundedCornerShape(32.dp)),
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "Volgende ➔",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
