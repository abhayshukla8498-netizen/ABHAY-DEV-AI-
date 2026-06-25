package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.net.Uri
import android.content.ClipboardManager
import android.content.ClipData
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ProjectViewModel
import com.example.ui.WorkspaceUiState
import com.example.ui.theme.StudioBlue
import com.example.ui.theme.StudioPurple
import com.example.ui.theme.StudioTeal
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatorScreen(
    viewModel: ProjectViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToWorkspace: () -> Unit,
    modifier: Modifier = Modifier
) {
    val promptInput by viewModel.promptInput.collectAsState()
    val creationType by viewModel.creationType.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Trigger navigation to workspace when generation completes successfully
    LaunchedEffect(uiState) {
        if (uiState is WorkspaceUiState.Success) {
            onNavigateToWorkspace()
        }
    }

    // List of templates to help the user
    val websiteTemplates = listOf(
        TemplateItem("Modern dark portfolio with glow buttons", "Create a modern dark-themed developer portfolio website with pulsing blue glow buttons, an interactive floating navigation bar, and a functional styled contact form."),
        TemplateItem("Responsive recipe blog card layout", "Create a responsive culinary recipe blog card grid with tag filters, dynamic card hover visual expansion, and clean warm orange tones."),
        TemplateItem("Sleek digital fitness dashboard", "Create a clean dashboard page for tracking weekly sports workouts with styled linear stats charts, progress rings, and a fitness plan checklist.")
    )

    val androidTemplates = listOf(
        TemplateItem("Interactive daily chore list", "Create a daily home chore checklist app. Include dynamic list rows with checkboxes, a custom header showing tasks completed progress, and an add item button."),
        TemplateItem("Glow alarm clock scheduler", "Create a beautiful glow-themed sleep helper alarm scheduler with hours slider, toggle switches for repeat settings, and an active alarm status card."),
        TemplateItem("Modern user profile page layout", "Create a high-fidelity user profile layout. Include a large rounded avatar, user statistics row, edit profile button, and standard feature switches.")
    )

    val templates = if (creationType == "WEBSITE") websiteTemplates else androidTemplates

    Scaffold(
        topBar = {
            var showSettingsDialog by remember { mutableStateOf(false) }

            TopAppBar(
                title = { Text("AI Creator", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back Arrow")
                    }
                },
                actions = {
                    IconButton(onClick = { showSettingsDialog = true }) {
                        Icon(Icons.Default.Settings, "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )

            if (showSettingsDialog) {
                val context = LocalContext.current
                AlertDialog(
                    onDismissRequest = { showSettingsDialog = false },
                    confirmButton = {
                        TextButton(onClick = { showSettingsDialog = false }) {
                            Text("Close", fontWeight = FontWeight.Bold)
                        }
                    },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Settings & Customer Care", fontWeight = FontWeight.Bold)
                        }
                    },
                    text = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Status / Optimization
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Optimized",
                                        tint = StudioTeal,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "System Status: Optimized",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Text(
                                            text = "AI models, compiler, and simulator are optimized for rapid generation.",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                                            lineHeight = 14.sp
                                        )
                                    }
                                }
                            }

                            // Customer Care section
                            Column {
                                Text(
                                    text = "Customer Care & Support",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "If you face any issues, need customization, or have questions, you can contact the developer directly. Tap below to send an email.",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    lineHeight = 15.sp
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                Button(
                                    onClick = {
                                        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                            data = Uri.parse("mailto:")
                                            putExtra(Intent.EXTRA_EMAIL, arrayOf("abhayshukla8498@gmail.com"))
                                            putExtra(Intent.EXTRA_SUBJECT, "Customer Care Support Request")
                                            putExtra(Intent.EXTRA_TEXT, "Hello Abhay,\n\nI am using the AI Creator App, and I would like to seek help/provide feedback regarding:\n\n[Write your details here]\n\nBest regards,\n[Your Name]")
                                        }
                                        try {
                                            context.startActivity(Intent.createChooser(emailIntent, "Send Email via..."))
                                        } catch (e: Exception) {
                                            // Handled fallback if no email app
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = StudioBlue
                                    ),
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Email,
                                            contentDescription = "Email Developer",
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Mail to Developer",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                                thickness = 1.dp
                            )

                            // Follow Developer on Instagram Section
                            Column {
                                Text(
                                    text = "Follow Developer on Instagram",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Join my Instagram community to get direct support, design templates, and exclusive feature previews.",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    lineHeight = 15.sp
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                val instaInteractionSource = remember { MutableInteractionSource() }
                                val isInstaPressed by instaInteractionSource.collectIsPressedAsState()
                                val instaScale by animateFloatAsState(
                                    targetValue = if (isInstaPressed) 0.94f else 1.0f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    ),
                                    label = "InstaFollowScale"
                                )

                                Box(
                                    modifier = Modifier
                                        .scale(instaScale)
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            Brush.horizontalGradient(
                                                colors = listOf(
                                                    Color(0xFF833AB4), // Instagram Purple
                                                    Color(0xFFFD1D1D), // Instagram Red
                                                    Color(0xFFFCAF45)  // Instagram Orange
                                                )
                                            )
                                        )
                                        .clickable(
                                            interactionSource = instaInteractionSource,
                                            indication = null,
                                            onClick = {
                                                val instaUrl = "https://www.instagram.com/just_abhay302?igsh=c2YyeHdyczNpaXRq"
                                                try {
                                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instaUrl))
                                                    context.startActivity(intent)
                                                } catch (e: Exception) {
                                                    Toast.makeText(context, "Could not open Instagram", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        // Custom vector-like styled Instagram Logo
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .border(2.dp, Color.White, RoundedCornerShape(6.dp))
                                                .padding(3.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(8.dp)
                                                    .border(1.5.dp, Color.White, CircleShape)
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.TopEnd)
                                                    .padding(top = 0.5.dp, end = 0.5.dp)
                                                    .size(2.dp)
                                                    .background(Color.White, CircleShape)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Follow @just_abhay302",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                                thickness = 1.dp
                            )

                            // Animated Share App Section
                            Column {
                                Text(
                                    text = "Share this App",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Share with your network on Instagram, Facebook, YouTube, X, or copy the link directly with animations.",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    lineHeight = 15.sp
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                var showIcons by remember { mutableStateOf(false) }
                                LaunchedEffect(Unit) {
                                    showIcons = true
                                }
                                
                                val entryScale by animateFloatAsState(
                                    targetValue = if (showIcons) 1f else 0.2f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    ),
                                    label = "ShareSectionEntrance"
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .scale(entryScale)
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Instagram
                                    ShareItem(
                                        name = "Instagram",
                                        bgBrush = Brush.verticalGradient(
                                            colors = listOf(Color(0xFFC13584), Color(0xFFE1306C), Color(0xFFFD9E3A))
                                        ),
                                        onClick = {
                                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                                type = "text/plain"
                                                putExtra(Intent.EXTRA_TEXT, "Check out this amazing AI Creator App by Abhay Dev AI! 🚀 Build beautiful websites and Android apps instantly!\n\nLink: https://ais-pre-734deyojbztg6faxhdnmr4-983443813885.asia-southeast1.run.app")
                                            }
                                            try {
                                                context.startActivity(Intent.createChooser(shareIntent, "Share to Instagram..."))
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "Could not open sharing", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .border(2.dp, Color.White, RoundedCornerShape(6.dp))
                                                .padding(3.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(8.dp)
                                                    .border(1.5.dp, Color.White, CircleShape)
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.TopEnd)
                                                    .padding(top = 0.5.dp, end = 0.5.dp)
                                                    .size(2.dp)
                                                    .background(Color.White, CircleShape)
                                            )
                                        }
                                    }

                                    // Facebook
                                    ShareItem(
                                        name = "Facebook",
                                        bgBrush = Brush.linearGradient(
                                            colors = listOf(Color(0xFF1877F2), Color(0xFF0F56B3))
                                        ),
                                        onClick = {
                                            val facebookUrl = "https://www.facebook.com/sharer/sharer.php?u=https://ais-pre-734deyojbztg6faxhdnmr4-983443813885.asia-southeast1.run.app"
                                            try {
                                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl))
                                                context.startActivity(intent)
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "Could not open Facebook", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    ) {
                                        Text(
                                            text = "f",
                                            color = Color.White,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif,
                                            modifier = Modifier.offset(y = (-1).dp)
                                        )
                                    }

                                    // YouTube
                                    ShareItem(
                                        name = "YouTube",
                                        bgBrush = Brush.linearGradient(
                                            colors = listOf(Color(0xFFFF0000), Color(0xFFCC0000))
                                        ),
                                        onClick = {
                                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                                type = "text/plain"
                                                putExtra(Intent.EXTRA_TEXT, "Look at this spectacular AI Creator App built by Abhay Dev AI. Check out real-time previews here!\n\nLink: https://ais-pre-734deyojbztg6faxhdnmr4-983443813885.asia-southeast1.run.app")
                                            }
                                            try {
                                                context.startActivity(Intent.createChooser(shareIntent, "Share YouTube Link..."))
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "Could not open share", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = "YouTube Play",
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    // X
                                    ShareItem(
                                        name = "X",
                                        bgBrush = Brush.linearGradient(
                                            colors = listOf(Color(0xFF1E293B), Color(0xFF0F172A))
                                        ),
                                        onClick = {
                                            val tweetUrl = "https://twitter.com/intent/tweet?text=" + Uri.encode("I am building futuristic websites and Android apps instantly using AI Creator App by Abhay Dev AI! 🚀 Check it out here: https://ais-pre-734deyojbztg6faxhdnmr4-983443813885.asia-southeast1.run.app #AICreator")
                                            try {
                                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl))
                                                context.startActivity(intent)
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "Could not open X / Twitter", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    ) {
                                        Text(
                                            text = "X",
                                            color = Color.White,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Black,
                                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                                        )
                                    }

                                    // Link
                                    ShareItem(
                                        name = "Link",
                                        bgBrush = Brush.linearGradient(
                                            colors = listOf(Color(0xFF00E5FF), Color(0xFF00B0FF))
                                        ),
                                        onClick = {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val clip = ClipData.newPlainText("AI Creator App URL", "https://ais-pre-734deyojbztg6faxhdnmr4-983443813885.asia-southeast1.run.app")
                                            clipboard.setPrimaryClip(clip)
                                            Toast.makeText(context, "Link Copied to Clipboard! 🚀", Toast.LENGTH_SHORT).show()
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Share,
                                            contentDescription = "Copy Link",
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                )
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        // Handle generation loading state with an overlay screen
        if (uiState is WorkspaceUiState.Generating) {
            LoadingOverlay(type = creationType)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Intro banner
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(StudioPurple.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AutoAwesome, "AI Icon", tint = StudioPurple)
                    }
                    Column {
                        Text(
                            text = "Let's build something.",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Explain your vision in natural language below.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                }

                // Type Toggle (Website vs Android App)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        // Website Mode Tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (creationType == "WEBSITE") MaterialTheme.colorScheme.surface else Color.Transparent)
                                .clickable { viewModel.setType("WEBSITE") }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.Language,
                                    "Web",
                                    tint = if (creationType == "WEBSITE") StudioTeal else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    "Website",
                                    fontWeight = if (creationType == "WEBSITE") FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp,
                                    color = if (creationType == "WEBSITE") MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }

                        // Android App Mode Tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (creationType == "ANDROID_APP") MaterialTheme.colorScheme.surface else Color.Transparent)
                                .clickable { viewModel.setType("ANDROID_APP") }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.Android,
                                    "App",
                                    tint = if (creationType == "ANDROID_APP") StudioBlue else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    "Android App",
                                    fontWeight = if (creationType == "ANDROID_APP") FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp,
                                    color = if (creationType == "ANDROID_APP") MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }

                // Prompt Text Input area
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Describe your project",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )
                    OutlinedTextField(
                        value = promptInput,
                        onValueChange = { viewModel.setPrompt(it) },
                        placeholder = {
                            Text(
                                text = if (creationType == "WEBSITE") {
                                    "e.g., A developer portfolio website with contact details, project grids, and dark neon style..."
                                } else {
                                    "e.g., A task list app with task checkboxes, category tabs, and an add items card..."
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (creationType == "WEBSITE") StudioTeal else StudioBlue,
                            focusedLabelColor = if (creationType == "WEBSITE") StudioTeal else StudioBlue
                        )
                    )
                }

                // Quick Prompt Templates Carousel
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Need Inspiration? Tap a starter idea:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(end = 16.dp)
                    ) {
                        items(templates) { item ->
                            Card(
                                modifier = Modifier
                                    .width(200.dp)
                                    .clickable { viewModel.setPrompt(item.fullPrompt) },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = CardDefaults.outlinedCardBorder()
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.TipsAndUpdates,
                                        contentDescription = "Tips",
                                        tint = if (creationType == "WEBSITE") StudioTeal else StudioBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = item.shortTitle,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = item.fullPrompt,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        maxLines = 3,
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Build Button
                Button(
                    onClick = {
                        keyboardController?.hide()
                        viewModel.generateProject(promptInput, creationType)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (creationType == "WEBSITE") StudioTeal else StudioBlue
                    )
                ) {
                    Icon(Icons.Default.AutoAwesome, "Auto")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Assemble Code & Live Preview",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }

                // Error representation if any
                if (uiState is WorkspaceUiState.Error) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                        ),
                        border = borderCardBorder(MaterialTheme.colorScheme.error.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                Icons.Default.ErrorOutline,
                                "Error",
                                tint = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = (uiState as WorkspaceUiState.Error).message,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Data class representing template cards
data class TemplateItem(
    val shortTitle: String,
    val fullPrompt: String
)

// Helper to draw a thin border on cards
@Composable
fun borderCardBorder(color: Color) = BorderStroke(1.dp, color)

// Glowing Neon Loading Screen Overlay
@Composable
fun LoadingOverlay(type: String) {
    val loadingTips = listOf(
        "Contacting Google Gemini servers...",
        "Analyzing intent & prompt structure...",
        "Drafting complete Kotlin package architecture...",
        "Compiling elegant Material 3 styling components...",
        "Configuring interactive simulation tree properties...",
        "Structuring responsive Tailwind classes...",
        "Injecting dynamic interactive JavaScript actions...",
        "Sanitizing clean source code text...",
        "Assembling live real-time preview canvas..."
    )

    var currentTipIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            currentTipIndex = (currentTipIndex + 1) % loadingTips.size
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutCirc),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.98f))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Spinning holographic developer circle
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .scale(scale),
                contentAlignment = Alignment.Center
            ) {
                // Glow circles
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.sweepGradient(
                                listOf(StudioBlue, StudioPurple, StudioTeal, StudioBlue)
                            ),
                            CircleShape
                        )
                        .rotate(rotation)
                )

                // White inner mask
                Box(
                    modifier = Modifier
                        .fillMaxSize(0.9f)
                        .background(MaterialTheme.colorScheme.background, CircleShape)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (type == "WEBSITE") Icons.Default.Language else Icons.Default.Android,
                        contentDescription = "Hologram Icon",
                        tint = if (type == "WEBSITE") StudioTeal else StudioBlue,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "DESIGNING",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                        letterSpacing = 2.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(44.dp))

            Text(
                text = "AI Studio is assembling your project",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Text Tip Rotator
            AnimatedContent(
                targetState = loadingTips[currentTipIndex],
                transitionSpec = {
                    fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
                },
                label = "tipRotator"
            ) { tip ->
                Text(
                    text = tip,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Modern linear accent progress bar
            LinearProgressIndicator(
                modifier = Modifier
                    .width(180.dp)
                    .height(4.dp)
                    .clip(CircleShape),
                color = if (type == "WEBSITE") StudioTeal else StudioPurple,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

@Composable
fun ShareItem(
    name: String,
    bgBrush: Brush,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Smooth, snappy spring animation for tapping
    val scaleFactor by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "ShareButtonScale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .scale(scaleFactor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(bgBrush)
                .border(1.dp, Color.White.copy(alpha = 0.25f), CircleShape)
        ) {
            icon()
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
    }
}
