package com.alaindroid.gameoftheninja.animation;

public abstract class RotationAnimation implements CustomAnimation {

    private Rotatable rotatable;
    private RotationAnimation.Type type;

    abstract float nextAngle(float deltaTime);

    public RotationAnimation(Rotatable rotatable, RotationAnimation.Type type) {
        this.rotatable = rotatable;
        this.type = type;
    }

    @Override
    public void tick(float deltaTime) {
        rotatable.currentAngle(nextAngle(deltaTime));
    }

    float currentAngle() {
        return rotatable.currentAngle();
    }

    protected boolean wobble() {
        return type == Type.WOBBLE;
    }

    protected  boolean spin() {
        return type == Type.SPIN;
    }

    enum Type {
        NONE, WOBBLE, SPIN
    }
}
