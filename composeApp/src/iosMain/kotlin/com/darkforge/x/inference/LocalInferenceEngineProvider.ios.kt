package com.darkforge.x.inference

actual fun createLocalInferenceEngine(): LocalInferenceEngine? = IosLiteRTInferenceEngine()
