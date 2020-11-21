package me.box.app.simple;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Section section = findViewById(R.id.section);

        section.setShowDividerModels(Section.DIVIDER_MIDDLE | Section.DIVIDER_START | Section.DIVIDER_END);
        section.setDividerBuilder((index, dividerColor, dividerSize) -> {
            final TextView divider = new TextView(this);
            divider.setBackgroundColor(dividerColor);
            divider.setText(R.string.app_name);
            divider.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
            return divider;
        });
    }
}