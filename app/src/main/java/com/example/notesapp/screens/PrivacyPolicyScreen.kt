
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notesapp.NestedRoutes
import com.example.notesapp.home.Home
import com.example.notesapp.ui.theme.NotesAppTheme

@Composable
fun PrivacyPolicyScreen(
    onPrivacyAccepted: () -> Unit,
    onPrivacyRejected: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Política de Privacidad",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Agrega el texto de la política de privacidad aquí en un LazyColumn o ScrollableColumn.
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
        ) {
            items(policyText) { policySection ->
                Text(
                    text = policySection,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    onPrivacyRejected.invoke()
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Rechazar")
            }

            Button(
                onClick = {
                    onPrivacyAccepted.invoke()
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Aceptar")
            }
        }
    }
}

private val policyText = listOf(
    "Este es un texto de ejemplo para la política de privacidad. Debes reemplazarlo con tu propia política de privacidad.",
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
    "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
    "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
    "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.",
    "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
    "Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
)

@Composable
fun NavigatePrivacyPolicyScreen(navController: NavController) {
    var isPrivacyAccepted by remember { mutableStateOf(false) }
    // Si el usuario ya ha aceptado la política de privacidad, muestra la pantalla Home aquí.
    if (isPrivacyAccepted) {
        Home(
            homeViewModel = null,
            onNoteClick = { },
            navToDetailPage = { },
            navToLoginPage = { }
        )
    } else {
        PrivacyPolicyScreen(
            onPrivacyAccepted = {
                isPrivacyAccepted = true // Actualiza el estado a true cuando se acepta la política
                navController.navigate(NestedRoutes.Main.name)
            },
            onPrivacyRejected = {
                navController.navigate(NestedRoutes.Login.name)
            }
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun PrivacyPolicyScreenPreview() {
    NotesAppTheme {
        PrivacyPolicyScreen(
            onPrivacyAccepted = {
                // Acción cuando se acepta la política de privacidad
            },
            onPrivacyRejected = {
                // Acción cuando se rechaza la política de privacidad
            }
        )
    }
}