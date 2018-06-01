package com.example.android.bakeapp8;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Steps extends AppCompatActivity {

    @Nullable
    @BindView(R.id.step_no)TextView number;
    @Nullable
    @BindView(R.id.step_back)ImageButton back;
    @Nullable
    @BindView(R.id.step_nxt)ImageButton nxt;

    private static final String TAG_RETAINED_FRAGMENT = "RetainedFragment";
    private static final String NUMB_STEP = "no_of_step";
    private StepsFragment fragment;
    private int spos;
    private ArrayList<RecipesParcelableResp.sObjects> steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.steps);
        ButterKnife.bind(this);

        final FragmentManager manager = getSupportFragmentManager();
        fragment = (StepsFragment) manager.findFragmentByTag(TAG_RETAINED_FRAGMENT);
        Bundle b = getIntent().getExtras();
        steps = b.getParcelableArrayList(getString(R.string.list_steps));
        spos = b.getInt(getString(R.string.spos));

        if(savedInstanceState == null) {
            spos = b.getInt(getString(R.string.spos));
        } else {
            spos = savedInstanceState.getInt(NUMB_STEP);
        }

        updateStepNumberText(spos);

        if (back != null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    spos--;
                    fragment = StepsFragment.newInstance(steps, spos);
                    updateStepNumberText(spos);
                    manager.beginTransaction().replace(R.id.steps_fl, fragment,TAG_RETAINED_FRAGMENT).commit();
                }
            });
        }

        if (nxt != null) {
            nxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    spos++;
                    fragment = StepsFragment.newInstance(steps, spos);
                    updateStepNumberText(spos);
                    manager.beginTransaction().replace(R.id.steps_fl, fragment,TAG_RETAINED_FRAGMENT).commit();
                }
            });
        }

        if(fragment == null){
            fragment = StepsFragment.newInstance(steps, spos);
            manager.beginTransaction().replace(R.id.steps_fl, fragment,TAG_RETAINED_FRAGMENT).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NUMB_STEP, spos);
    }

    private void updateStepNumberText(int stepPosition){
        if(number != null) {
            number.setText(Integer.toString(stepPosition));

            if (stepPosition == 0) {
                back.setVisibility(View.GONE);
            } else if (stepPosition == steps.size() - 1) {
                nxt.setVisibility(View.GONE);
            } else {
                back.setVisibility(View.VISIBLE);
                nxt.setVisibility(View.VISIBLE);
            }
        }
    }
}
