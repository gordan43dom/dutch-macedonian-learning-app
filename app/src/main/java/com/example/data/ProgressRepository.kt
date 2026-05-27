package com.example.data

import kotlinx.coroutines.flow.Flow

class ProgressRepository(private val progressDao: ProgressDao) {
    val allProgress: Flow<List<ProgressEntity>> = progressDao.getAllProgress()

    suspend fun saveProgress(record: ProgressEntity) {
        progressDao.insertProgress(record)
    }

    suspend fun clearProgress() {
        progressDao.clearAllProgress()
    }
}
