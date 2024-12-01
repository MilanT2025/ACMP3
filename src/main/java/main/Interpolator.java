package main;

import com.formdev.flatlaf.util.Animator;

public interface Interpolator extends Animator.Interpolator {
    float getMaxValue();
}
