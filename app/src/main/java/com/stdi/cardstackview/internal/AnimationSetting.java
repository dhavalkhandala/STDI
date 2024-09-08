package com.stdi.cardstackview.internal;

import android.view.animation.Interpolator;
import com.stdi.cardstackview.Direction;

public interface AnimationSetting {
    Direction getDirection();
    int getDuration();
    Interpolator getInterpolator();
}