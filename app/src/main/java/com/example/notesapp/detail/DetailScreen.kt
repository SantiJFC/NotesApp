package com.example.notesapp.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notesapp.Utils
import com.example.notesapp.ui.theme.NotesAppTheme
import kotlinx.coroutines.launch


@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel?, noteId: String, onNavigate: () -> Unit
) {
    //Se obtiene el detalle del viewModel. Si es nulo se devuelve un detalle vacío
    val detailUiState = detailViewModel?.detailUiState ?: DetailUiState()

    //Para saber el detalle de si está vacío o no  creo una variable adicional
    val isFormsNotBlank = detailUiState.note.isNotBlank() && detailUiState.title.isNotBlank()

    val selectecColor by animateColorAsState(
        targetValue = Utils.colors[detailUiState.colorIndex]
    )

    val isNoteIdNotBlank = noteId.isNotBlank()

    //Condición del icono para checkear/actualizar la selección del detailViewModel
    val icon = if (isNoteIdNotBlank) Icons.Default.Refresh else Icons.Default.Check
    LaunchedEffect(key1 = Unit) {
        if (isNoteIdNotBlank) {
            detailViewModel?.getNote(noteId)
        } else {
            detailViewModel?.resetState()
        }
    }

    val scope = rememberCoroutineScope()
    //Es necesario para el test y/o el snackbar de la aplicación
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        //Botón del icono para cheeckear/actualizar la selección del detailViewModel
        floatingActionButton = {
            AnimatedVisibility(visible = isFormsNotBlank) {
                FloatingActionButton(onClick = {
                    if (isNoteIdNotBlank) {
                        detailViewModel?.updateNote(noteId)
                    } else {
                        detailViewModel?.addNote()
                    }
                }) {
                    Icon(imageVector = icon, contentDescription = null)
                }
            }
        },

        ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = selectecColor)
                .padding(padding)
        ) {
            if (detailUiState.noteAddedStatus) {
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar("Nota añadida correctamente")
                    detailViewModel?.resetNoteAddedStatus()
                    onNavigate.invoke()
                }
            }
            if (detailUiState.updateNoteStatus) {
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar("Nota actualizada correctamente")
                    detailViewModel?.resetNoteAddedStatus()
                    onNavigate.invoke()
                }
            }
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                contentPadding = PaddingValues(
                    vertical = 16.dp, horizontal = 8.dp
                )
            ) {
                itemsIndexed(Utils.colors) { colorIndex, color ->
                    ColorItem(color = color) {
                        detailViewModel?.onColorChange(colorIndex)
                    }
                }
            }
            OutlinedTextField(value = detailUiState.title, onValueChange = {
                detailViewModel?.onTitleChange(it)
            }, label = {
                Text(text = "Título")
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            )
            OutlinedTextField(
                value = detailUiState.note, onValueChange = {
                    detailViewModel?.onNoteChange(it)
                },
                label = {
                    Text(text = "Notas")
                }, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun ColorItem(color: Color, onClick: () -> Unit?) {
    Surface(
        color = color,
        shape = CircleShape,
        modifier = Modifier
            .padding(8.dp)
            .size(36.dp)
            .clickable {
                onClick.invoke()
            },
        border = BorderStroke(2.dp, Color.Black)
    ) {

    }

}

@Preview(showSystemUi = true)
@Composable
fun PreviewDetailScreen() {
    NotesAppTheme {
        DetailScreen(detailViewModel = null, noteId = "") {

        }

    }
}