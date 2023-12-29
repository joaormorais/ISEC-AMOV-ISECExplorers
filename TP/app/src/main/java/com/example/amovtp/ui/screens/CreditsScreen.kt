import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.amovtp.R

@Composable
fun CreditsScreen(modifier: Modifier = Modifier) {

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFF2196F3), Color(0xFFB39DDB)),
        startY = 0.0f,
        endY = 1000.0f
    )

    val infoTextStyle = MaterialTheme.typography.bodyLarge.copy(
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )

    var title = stringResource(R.string.title_credits)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(gradient)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text("ISEC", style = infoTextStyle)
            Text("Arquiteturas Móveis", style = infoTextStyle)
            Text("Engenharia Informática", style = MaterialTheme.typography.bodyLarge)
            Text("Ano Letivo: 2023/2024", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Text("João Morais - a2019134282", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("José Carvalho - a2019111914", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Sandra Perdigão - a2019102697", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

