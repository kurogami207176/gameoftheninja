package com.alaindroid.gameoftheninja.animation;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class SpinRotationAnimation extends RotationAnimation {
    private float spinSpeed;
    private boolean directionToLeft;
    @Setter
    @Getter
    private boolean wobble;
    public SpinRotationAnimation(Rotatable rotatable, Type type,
                                 float spinSpeed, boolean directionToLeft) {
        super(rotatable, type);
        this.directionToLeft = directionToLeft;
        this.wobble = true;
        this.spinSpeed = spinSpeed;
    }

    @Override
    float nextAngle(float deltaTime) {
        float newWobbleAngle;
        if (spin() ) {
            if (directionToLeft) {
                newWobbleAngle = currentAngle() - deltaTime*spinSpeed;
            }
            else {
                newWobbleAngle = currentAngle() + deltaTime*spinSpeed;
            }
        }
        else {
            newWobbleAngle = 0;
        }
        return newWobbleAngle;
    }
}
