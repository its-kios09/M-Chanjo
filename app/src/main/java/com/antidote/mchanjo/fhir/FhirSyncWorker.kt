package com.antidote.mchanjo.fhir

import android.content.Context
import androidx.work.WorkerParameters
import com.antidote.mchanjo.FhirApplication
import com.google.android.fhir.sync.AcceptLocalConflictResolver
import com.google.android.fhir.sync.FhirSyncWorker

class FhirSyncWorker(appContext: Context, workerParams: WorkerParameters) :
  FhirSyncWorker(appContext, workerParams) {

  override fun getDownloadWorkManager() =DownloadWorkManagerImpl()

  override fun getConflictResolver() = AcceptLocalConflictResolver

  override fun getFhirEngine() = FhirApplication.fhirEngine(applicationContext)
}
