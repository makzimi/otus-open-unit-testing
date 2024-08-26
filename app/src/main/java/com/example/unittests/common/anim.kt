package com.example.unittests.common

import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.OvershootInterpolator

internal fun View.bumpDown() = animate()
  .scale(0.95f)
  .setInterpolator(OvershootInterpolator(2f))
  .setDuration(300L)

internal fun View.bump() {
  this.clearAnimation()
  bumpDown()
    .withEndAction { bumpUp().start() }
    .start()
}

internal fun View.bumpUp(): ViewPropertyAnimator =
  animate()
    .scale(1f)
    .setDuration(300L)
    .setInterpolator(OvershootInterpolator(2f))

internal fun ViewPropertyAnimator.scale(scale: Float): ViewPropertyAnimator =
  this
    .scaleX(scale)
    .scaleY(scale)