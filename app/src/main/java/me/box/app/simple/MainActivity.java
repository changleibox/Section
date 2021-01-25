package me.box.app.simple;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Section section = findViewById(R.id.section);

        final TextView header = new TextView(this);
        header.setText(R.string.label_header);
        section.setHeader(header);
        final TextView footer = new TextView(this);
        footer.setText(R.string.label_footer);
        section.setFooter(footer);

        section.setDividerSize(40);
        section.setDividerColor(Color.BLUE);

        section.setShowDividerModels(Section.DIVIDER_MIDDLE | Section.DIVIDER_START | Section.DIVIDER_END);
        section.setDividerBuilder((index, dividerColor, dividerSize) -> {
            final TextView divider = new TextView(this);
            // divider.setBackgroundColor(dividerColor);
            divider.setText(R.string.app_name);
            divider.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
            return divider;
        });
        section.getChildAt(2).setVisibility(View.GONE);
        section.reloadDecorate();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}