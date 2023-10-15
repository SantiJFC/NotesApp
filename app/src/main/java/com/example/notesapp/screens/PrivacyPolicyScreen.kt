package com.example.notesapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.notesapp.NestedRoutes

@Composable
fun PrivacyPolicyScreen(navController: NavController) {
    var accepted by remember { mutableStateOf(false) }

    val greenColor = Color(0xFF00FF00)
    val redColor = Color(0xFFFF0000)

    // Texto fijo en la parte superior
    val headerText = "Política de Privacidad"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 2.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(8.dp),
                )

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = headerText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    color = Color.White,
                    textAlign = TextAlign.Center,

                    )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White),

                    ) {
                    item {
                        Text(
                            text = policyText,
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    }
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    // Marcar como aceptado y navegar a la pantalla de inicio de sesión
                                    accepted = true
                                    navController.navigate(NestedRoutes.Login.name)
                                },
                                enabled = !accepted,
                                colors = ButtonDefaults.buttonColors(backgroundColor = greenColor) // Cambia el color del botón "Aceptar"
                            ) {
                                Text(text = "Aceptar", color = Color.White)
                            }

                            Button(
                                onClick = {
                                    // Salir de la aplicación
                                    // Asegúrate de manejar esto adecuadamente en tu aplicación real
                                    // Esto es solo un ejemplo
                                    System.exit(0)
                                },
                                enabled = !accepted,
                                colors = ButtonDefaults.buttonColors(backgroundColor = redColor) // Cambia el color del botón "Rechazar"
                            ) {
                                Text(text = "Rechazar", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}

// Texto de la política de privacidad
private val policyText = """ 
      POLÍTICA DE PRIVACIDAD PARA APLICACIONES MÓVILES (APPS)Fecha última actualización: 21/09/2023
De conformidad con el Reglamento (UE) 2016/679, del Parlamento Europeo y del Consejo, de 27 de abril de 2016, relativo a la protección de las personas físicas en lo que respecta al tratamiento de datos personales y a la libre circulación de estos datos (Reglamento General de Protección de Datos – RGPD), TEIS S.A. , informa a los usuarios de la aplicación Notes App (en adelante, la Aplicación), acerca del tratamiento de los datos personales, que ellos voluntariamente hayan facilitado durante el proceso de registro, acceso y utilización del servicio.
1.	 FINALIDAD DEL TRATAMIENTO DE DATOS

Para proceder al registro, acceso y posterior uso de la Aplicación, el Usuario deberá facilitar -de forma voluntaria-, datos de carácter personal (esencialmente, identificativos y de contacto), los cuales serán incorporados a soportes automatizados titularidad de TEIS S.A..

La recogida, almacenamiento, modificación, estructuración y en su caso, eliminación, de los datos proporcionados por los Usuarios, constituirán operaciones de tratamiento llevadas a cabo por el Responsable, con la finalidad de garantizar el correcto funcionamiento de la Aplicación, mantener la relación de prestación de servicios y/o comercial con el Usuario, y para la gestión, administración, información, prestación y mejora del servicio.

Los datos personales facilitados por el Usuario -especialmente, el correo electrónico o e-mail- podrán emplearse también para remitir boletines (newsletters), así como comunicaciones comerciales de promociones y/o publicidad de la Aplicación, siempre y cuando, el Usuario haya prestado previamente su consentimiento expreso para la recepción de estas comunicaciones vía electrónica.

2.	LEGITIMACIÓN
3.	El tratamiento de los datos del Usuario, se realiza con las siguientes bases jurídicas que legitiman el mismo:
a.	La solicitud de información y/o la contratación de los servicios de la Aplicación, cuyos términos y condiciones se pondrán a disposición del Usuario en todo caso, con carácter previo, para su expresa aceptación.
b.	El consentimiento libre, específico, informado e inequívoco del Usuario, poniendo a su disposición la presente política de privacidad, que deberá aceptar mediante una declaración o una clara acción afirmativa, como el marcado de una casilla dispuesta al efecto.

En caso de que el Usuario no facilite a TEIS S.A. sus datos, o lo haga de forma errónea o incompleta, no será posible proceder al uso de la Aplicación.


4.	4. CONSERVACIÓN DE LOS DATOS PERSONALES
Los datos personales proporcionados por el Usuario, se conservarán en los sistemas y bases de datos del Responsable del Tratamiento, mientras aquel continúe haciendo uso de la Aplicación, y siempre que no solicite su supresión.

Con el objetivo de depurar las posibles responsabilidades derivadas del tratamiento, los datos se conservarán por un período mínimo de cinco años.

5.	DESTINATARIOS
Los datos no se comunicarán a ningún tercero ajeno a TEIS S.A., salvo obligación legal o en cualquier caso, previa solicitud del consentimiento del Usuario.

De otra parte, TEIS S.A. podrá dar acceso o transmitir los datos personales facilitados por el Usuario, a terceros proveedores de servicios, con los que haya suscrito acuerdos de encargo de tratamiento de datos, y que únicamente accedan a dicha información para prestar un servicio en favor y por cuenta del Responsable.

6.	RETENCIÓN DE DATOS
TEIS S.A., informa al Usuario de que, como prestador de servicio de alojamiento de datos y en virtud de lo establecido en la Ley 34/2002, de 11 de julio, de Servicios de la Sociedad de la Información y de Comercio Electrónico (LSSI), retiene por un período máximo de 12 meses la información imprescindible para identificar el origen de los datos alojados y el momento en que se inició la prestación del servicio.

La retención de estos datos no afecta al secreto de las comunicaciones y solo podrán ser utilizados en el marco de una investigación criminal o para la salvaguardia de la seguridad pública, poniéndose a disposición de los jueces y/o tribunales o del Ministerio que así los requiera.

La comunicación de datos a las Fuerzas y Cuerpos de Seguridad del Estado, se hará en virtud de lo dispuesto por la normativa sobre protección de datos personales, y bajo el máximo respeto a la misma.

7.	PROTECCIÓN DE LA INFORMACIÓN ALOJADA
El Responsable del Tratamiento, adopta las medidas necesarias para garantizar la seguridad, integridad y confidencialidad de los datos conforme a lo dispuesto en el Reglamento (UE) 2016/679 del Parlamento Europeo y del Consejo, de 27 de abril de 2016, relativo a la protección de las personas físicas en lo que respecta al tratamiento de datos personales y a la libre circulación de los mismos.

Si bien el Responsable, realiza copias de seguridad de los contenidos alojados en sus servidores, sin embargo, no se responsabiliza de la pérdida o el borrado accidental de los datos por parte de los Usuarios. De igual manera, no garantiza la reposición total de los datos borrados por los Usuarios, ya que los citados datos podrían haber sido suprimidos y/o modificados durante el período de tiempo transcurrido desde la última copia de seguridad.

Los servicios facilitados o prestados a través de la Aplicación, excepto los servicios específicos de backup, no incluyen la reposición de los contenidos conservados en las copias de seguridad realizadas por el Responsable del Tratamiento, cuando esta pérdida sea imputable al usuario; en este caso, se determinará una tarifa acorde a la complejidad y volumen de la recuperación, siempre previa aceptación del usuario. La reposición de datos borrados solo está incluida en el precio del servicio cuando la pérdida del contenido sea debida a causas atribuibles al Responsable.

8.	EJERCICIO DE DERECHOS
TEIS S.A., informa al Usuario de que le asisten los derechos de acceso, rectificación, limitación, supresión, oposición y portabilidad, los cuales podrá ejercitar mediante petición dirigida al correo electrónico: santijfc@gmail.com

Asimismo, el Usuario tiene derecho a revocar el consentimiento inicialmente prestado, y a interponer reclamaciones de derechos frente a la Agencia Española de Protección de Datos (AEPD).

9.	COMUNICACIONES COMERCIALES POR VÍA ELECTRÓNICA
En aplicación de la LSSI (Ley de Servicios de la Sociedad de la Información), TEIS S.A., no enviará comunicaciones publicitarias o promocionales por correo electrónico u otro medio de comunicación electrónica equivalente que previamente no hubieran sido solicitadas o expresamente autorizadas por los destinatarios de las mismas.

En el caso de usuarios con los que exista una relación contractual, jurídica o de servicios previa, el Responsable del Tratamiento, sí está autorizado al envío de comunicaciones comerciales referentes a productos o servicios del Responsable que sean similares a los que inicialmente fueron objeto de contratación con el cliente.

En caso de que el Usuario quiera darse de baja a la hora de recibir las citadas comunicaciones, podrá hacerlo remitiendo su voluntad por e-mail al correo electrónico: santijfc@gmail.com

    """.trimIndent()

@Preview
@Composable
fun PrivacyPolicyScreenPreview() {
    PrivacyPolicyScreen(navController = rememberNavController())
}
