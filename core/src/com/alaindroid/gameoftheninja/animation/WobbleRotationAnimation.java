package com.alaindroid.gameoftheninja.animation;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
public class WobbleRotationAnimation extends RotationAnimation {
    private float wobbleSpeed;
    private float maxWobbleAngle;
    @Getter
    @Setter
    private boolean currentWobbleDirectionLeft;
    public WobbleRotationAnimation(Rotatable rotatable, RotationAnimation.Type type,
                                   float wobbleSpeed, float maxWobbleAngle) {
        super(rotatable, type);
        currentWobbleDirectionLeft = false;
        this.wobbleSpeed = wobbleSpeed;
        this.maxWobbleAngle = maxWobbleAngle;
    }

    @Override
    float nextAngle(float deltaTime) {
        float newWobbleAngle;
        if (wobble() && Math.abs(currentAngle()) < maxWobbleAngle) {
            if (currentWobbleDirectionLeft()) {
                newWobbleAngle = Math.max(-maxWobbleAngle, currentAngle() - deltaTime*wobbleSpeed);
            }
            else {
                newWobbleAngle = Math.min(maxWobbleAngle, currentAngle() + deltaTime*wobbleSpeed);
            }
        }
        else {
            newWobbleAngle = 0;
        }
        if (Math.abs(newWobbleAngle) == maxWobbleAngle) {
            currentWobbleDirectionLeft(!currentWobbleDirectionLeft());
        }
        return newWobbleAngle;
    }
}
