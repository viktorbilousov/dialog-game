package minigames.tea.service

import minigames.tea.models.Tea

data class TeaQualityRecord(val sourceTea : Tea, val nearestTea : Tea, val weight: Double, val quality: TeaQuality.Quality)