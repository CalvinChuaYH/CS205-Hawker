package com.example.androidapp.App_Objects;

import com.example.androidapp.Joystick;

public interface Roadblock {
    double[] handleCollide(Player player, Joystick joystick);
}
