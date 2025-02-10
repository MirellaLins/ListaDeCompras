package dev.mlds.listadecompras.view.screens

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import android.graphics.Color
import android.graphics.Typeface
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate

@Composable
fun HistoricoDePrecosGrafico(precos: List<Pair<String, Double>>) {
    val context = LocalContext.current

    // Converte os dados para Entry (usado pelo MPAndroidChart)

    // Converte os dados para Entry (usado pelo MPAndroidChart)
    val entries = precos.mapIndexed { index, (data, valor) ->
        Entry(index.toFloat(), valor.toFloat()) // X = posição no eixo, Y = valor
    }
    AndroidView(
        factory = { ctx ->
            LineChart(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                description.isEnabled = false

                val dataSet = LineDataSet(entries, "Histórico de Preços").apply {
                    color = ColorTemplate.getHoloBlue()
                    setCircleColor(ColorTemplate.getHoloBlue())
                    lineWidth = 2f
                    circleRadius = 4f
                    setDrawCircleHole(false)
                    valueTextSize = 10f
                    setDrawFilled(true)
                    fillColor = ColorTemplate.getHoloBlue()
                }

                val lineData = LineData(dataSet)
                data = lineData

                xAxis.apply {
                    setDrawGridLines(false)
                    setDrawLabels(false) // Oculta os índices numéricos do eixo X
                }

                axisLeft.apply {
                    setDrawGridLines(true)
                    textColor = Color.BLACK
                }

                axisRight.isEnabled = false

                legend.apply {
                    form = Legend.LegendForm.LINE
                    textSize = 12f
                    typeface = Typeface.DEFAULT_BOLD
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}
