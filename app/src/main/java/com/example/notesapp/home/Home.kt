package com.example.notesapp.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.notesapp.R
import com.example.notesapp.Utils
import com.example.notesapp.models.Notes
import com.example.notesapp.repository.Resources
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale


@Composable
fun Home(
    homeViewModel: HomeViewModel?,
    onNoteClick: (id: String) -> Unit,
    navToDetailPage: () -> Unit,
    navToLoginPage: () -> Unit
) {
    val homeUiState = homeViewModel?.homeUiState ?: HomeUiState()

    var openDialog by remember {
        mutableStateOf(false)
    }
    var selectedNote: Notes? by remember {
        mutableStateOf(null)
    }

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = Unit) {
        homeViewModel?.loadNotes()
    }


    ////////////////////////////////////////////////////////////////////////
    /*    //Si deshabilito este segmento tengo la pervisualización
        // Compruebo si el usuario está registrado
        val isUserSignedIn = homeViewModel?.hasUser ?: false

        if (!isUserSignedIn) {
            //Si no lo está muestro un mensaje
            navToLoginPage.invoke()
            return
        }*/
    ////////////////////////////////////////////////////////////////////////

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = Color.Gray,
        floatingActionButton = {
            FloatingActionButton(onClick = { navToDetailPage.invoke() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                )
            }
        },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.notes_logo_app_blanco),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.small)
                    )
                },
                actions = {
                    IconButton(onClick = {
                        homeViewModel?.signOut()
                        navToLoginPage.invoke()
                    }) {
                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null)
                    }
                },
                title = {
                    Text(text = "Inicio")
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding),

        ) {
            when (homeUiState.notesList) {
                is Resources.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(align = Alignment.Center)
                    )
                }

                is Resources.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                    ) {
                        items(homeUiState.notesList.data ?: emptyList()) { note ->
                            NoteItem(
                                notes = note,
                                onLongClick = {
                                    openDialog = true
                                    selectedNote = note
                                },
                            ) {
                                onNoteClick.invoke(note.documentId)
                            }
                            //Genero una ventana emergente para borrar la nota
                            AnimatedVisibility(visible = openDialog) {
                                AlertDialog(
                                    onDismissRequest = {
                                        openDialog = false
                                    },
                                    title = { Text(text = "¿Borrar nota?") },
                                    confirmButton = {
                                        Button(
                                            onClick = {
                                                selectedNote?.documentId?.let {
                                                    homeViewModel?.deleteNote(it)
                                                }
                                                openDialog = false
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                backgroundColor = Color.Red
                                            ),
                                        ) {
                                            Text(text = "Borrar")
                                        }
                                    },
                                    dismissButton = {
                                        Button(onClick = { openDialog = false }) {
                                            Text(text = "Cancelar")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                else -> {
                    Text(
                        text = homeUiState.notesList.throwable?.localizedMessage
                            ?: "Error desconocido",
                        color = Color.Red
                    )
                }
            }
            BannerAd(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(16.dp),
                adId = "Anuncio"
            )
            
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    notes: Notes,
    onLongClick: () -> Unit,
    onClick: () -> Unit
) {
    Card( //Genero eventos sobre notas para hacer diferentes clicks
        modifier = Modifier
            .combinedClickable(
                onLongClick = { onLongClick.invoke() },
                onClick = { onClick.invoke() })
            .padding(8.dp)
            .fillMaxWidth(),
        backgroundColor = Utils.colors[notes.colorIndex]
    ) {
        Column {
            Text(
                text = notes.title,
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Clip,
            )
            Spacer(modifier = Modifier.size(4.dp))
            //Diminuyo la opacidad del texto. Proporciona un aspecto distintivo
            CompositionLocalProvider(
                LocalContentAlpha provides ContentAlpha.disabled
            ) {
                Text(
                    text = notes.description,
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 4
                )
            }
            Spacer(modifier = Modifier.size(4.dp))
            CompositionLocalProvider(
                LocalContentAlpha provides ContentAlpha.disabled
            ) {
                Text(
                    text = formatDate(notes.timestamp),
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.End),
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 4
                )
            }
        }
    }
}

private fun formatDate(timestamp: Timestamp): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return sdf.format(timestamp.toDate())
}


@Composable
fun BannerAd(modifier: Modifier = Modifier, adId: String) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.size(24.dp))
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = adId
                    loadAd(AdRequest.Builder().build())
                }
            }

        )
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    Home(homeViewModel = null, onNoteClick = {}, navToDetailPage = { /* TODO */ }) {}
}