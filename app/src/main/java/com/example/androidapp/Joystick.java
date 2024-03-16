package com.example.androidapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Joystick {
    private int outerCircleRadius;
    private  int innerCircleRadius;
    private int outerCircleCenterPosX;
    private int outerCircleCenterPosY;
    private int innerCircleCenterPosX;
    private int innerCircleCenterPosY;
    private Paint outerCirclePaint;
    private Paint innerCirclePaint;
    private boolean isPressed;
    private double joystickCenterToTouchDistance;
    private double actuatorX;
    private double actuatorY;

    public Joystick(int centerPositionX, int centerPositionY, int outerCircleRadius, int innerCircleRadius){
        outerCircleCenterPosX = centerPositionX;
        outerCircleCenterPosY = centerPositionY;
        innerCircleCenterPosX = centerPositionX;
        innerCircleCenterPosY = centerPositionY;

        this.outerCircleRadius = outerCircleRadius;
        this.innerCircleRadius = innerCircleRadius;

        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(Color.GRAY);
        outerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(Color.BLUE);
        innerCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    public void update() {
        updateInnerCirclePosition();
    }

    private void updateInnerCirclePosition() {
        innerCircleCenterPosX = (int) (outerCircleCenterPosX + actuatorX*outerCircleRadius);
        innerCircleCenterPosY = (int) (outerCircleCenterPosY + actuatorY*outerCircleRadius);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(outerCircleCenterPosX, outerCircleCenterPosY,
            outerCircleRadius, outerCirclePaint);

        canvas.drawCircle(innerCircleCenterPosX, innerCircleCenterPosY,
                innerCircleRadius, innerCirclePaint);
    }

    public boolean isPressed(double x, double y) {
        joystickCenterToTouchDistance = Math.sqrt(
            Math.pow(outerCircleCenterPosX - x, 2) +
            Math.pow(outerCircleCenterPosY - y, 2)
        );
        return joystickCenterToTouchDistance < outerCircleRadius;
    }

    public void setIsPressed(boolean isPressed) {
        this.isPressed = isPressed;
    }

    public boolean getIsPressed() {
        return isPressed;
    }

    public void setActuator(double x, double y) {
        double deltaX = x - outerCircleCenterPosX;
        double deltaY = y - outerCircleCenterPosY;
        double deltaDistance = Math.sqrt(
                Math.pow(deltaX, 2) + Math.pow(deltaY, 2)
        );
        if(deltaDistance < outerCircleRadius){
            actuatorX = deltaX/outerCircleRadius;
            actuatorY = deltaY/outerCircleRadius;
        }else{
            actuatorX = deltaX/deltaDistance;
            actuatorY = deltaY/deltaDistance;
        }
    }

    public void resetActuator() {
        actuatorX = 0.0;
        actuatorY = 0.0;
    }

    public double getActuatorX() {
        return actuatorX;
    }

    public double getActuatorY() {
        return actuatorY;
    }
}
