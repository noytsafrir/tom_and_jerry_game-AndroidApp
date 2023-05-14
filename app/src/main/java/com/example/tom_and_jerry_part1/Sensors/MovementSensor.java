package com.example.tom_and_jerry_part1.Sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.tom_and_jerry_part1.Interfaces.MovementCallback;

public class MovementSensor {

    // sensor vars
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;

    private MovementCallback movementCallback;

    // vars
    private long timestemp = 0;

    public MovementSensor(Context context, MovementCallback movementCallback) {
        this.movementCallback = movementCallback;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        initEventListener();
    }

    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float x = sensorEvent.values[0];
                calculateStep(x);
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
    }

    private void calculateStep(float x) {
        if(System.currentTimeMillis() - timestemp > 200){
            timestemp = System.currentTimeMillis();
            if(x > 3.0){
                if(movementCallback != null)
                    movementCallback.playerMoveLeft();
            }
            if(x < -3.0){
                if(movementCallback != null)
                    movementCallback.playerMoveRight();
            }
        }
    }

    public void start(){
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    public void stop() {
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }
}
