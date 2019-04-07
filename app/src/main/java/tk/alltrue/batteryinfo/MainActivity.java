package tk.alltrue.batteryinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.LevelListDrawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mBatteryLevelTextView, mBatteryVoltageTextView, mBatteryTemperatureTextView,
            mBatteryTechnologyTextView, mBatteryStatusTextView, mBatteryHealthTextView,
            mBatteryCurrentTextView, mBatteryChargeTextView;
    private ImageView mBatteryIconImageView;
    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                mBatteryLevelTextView.setText("Battery level: "
                        + String.valueOf(intent.getIntExtra(
                        BatteryManager.EXTRA_LEVEL, 0)) + "%");
                mBatteryVoltageTextView.setText("Voltage: "
                        + String.valueOf((float) intent.getIntExtra(
                        BatteryManager.EXTRA_VOLTAGE, 0) / 1000) + "V");
                mBatteryTemperatureTextView.setText("Temperature: "
                        + String.valueOf((float) intent.getIntExtra(
                        BatteryManager.EXTRA_TEMPERATURE, 0) / 10)
                        + "°C");
                mBatteryTechnologyTextView
                        .setText("Battery type: "
                                + intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY));

                int batteryIconId = intent.getIntExtra(
                        BatteryManager.EXTRA_ICON_SMALL, 0);
                LevelListDrawable batteryLevel = (LevelListDrawable) getResources()
                        .getDrawable(batteryIconId);
                mBatteryIconImageView.setBackground(batteryLevel);


                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                float batteryPct = level / (float) scale;
                mBatteryCurrentTextView.setText("Charge level: " + batteryPct);

                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
                        BatteryManager.BATTERY_STATUS_UNKNOWN);
                String strStatus;
                if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
                    strStatus = "Charging";
                } else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
                    strStatus = "Discharging";
                } else if (status == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
                    strStatus = "Not charging";
                } else if (status == BatteryManager.BATTERY_STATUS_FULL) {
                    strStatus = "Full";
                } else {
                    strStatus = "Undefined";
                }
                mBatteryStatusTextView.setText("Status: " + strStatus);

                int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH,
                        BatteryManager.BATTERY_HEALTH_UNKNOWN);
                String strHealth;
                if (health == BatteryManager.BATTERY_HEALTH_GOOD) {
                    strHealth = "Good";
                } else if (health == BatteryManager.BATTERY_HEALTH_OVERHEAT) {
                    strHealth = "Overheat";
                } else if (health == BatteryManager.BATTERY_HEALTH_DEAD) {
                    strHealth = "Dead";
                } else if (health == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) {
                    strHealth = "Over Voltage";
                } else if (health == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE) {
                    strHealth = "Unspecified Failure";
                } else {
                    strHealth = "Undefined";
                }
                mBatteryHealthTextView.setText("Battery state: " + strHealth);


                int chargePlug = intent.getIntExtra(
                        BatteryManager.EXTRA_PLUGGED,
                        BatteryManager.BATTERY_STATUS_UNKNOWN);
                String charge;
                if (chargePlug == BatteryManager.BATTERY_PLUGGED_USB) {
                    charge = "plugged USB";
                } else if (chargePlug == BatteryManager.BATTERY_PLUGGED_AC) {
                    charge = "plugged AC";
                } else if (chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS) {
                    charge = "plugged wireless";
                } else {
                    charge = "Undefined";
                }
                mBatteryChargeTextView.setText("Charge type: " + charge);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBatteryLevelTextView = findViewById(R.id.textViewLevel);
        mBatteryVoltageTextView = findViewById(R.id.textViewVoltage);
        mBatteryTemperatureTextView = findViewById(R.id.textViewTemperature);
        mBatteryTechnologyTextView = findViewById(R.id.textViewTechnology);
        mBatteryStatusTextView = findViewById(R.id.textViewStatus);
        mBatteryHealthTextView = findViewById(R.id.textViewHealth);
        mBatteryCurrentTextView = findViewById(R.id.textViewCurrent);
        mBatteryChargeTextView = findViewById(R.id.textViewCharge);
        mBatteryIconImageView = findViewById(R.id.imageViewIcon);
        this.registerReceiver(this.batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onPause() {
        if (batteryReceiver != null) {
            unregisterReceiver(batteryReceiver);
            batteryReceiver = null;
        }
        super.onPause();
    }
}
