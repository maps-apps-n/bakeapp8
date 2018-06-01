package com.example.android.bakeapp8;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.bakeapp8.RecipesParcelableResp.iObjects;

public class InstructionsActivity extends AppCompatActivity implements Instructions.instCb {

    private static final String LOG_TAG = InstructionsActivity.class.getSimpleName();

    @BindView(R.id.tb_instr)Toolbar tb;
    @BindView(R.id.title_instr)TextView title;
    @BindView(R.id.toggle_bookmark)ToggleButton toggle;

    private static final String INS_ACT_TAG = "ACTIVITY_TAG";
    public static final String UPDATE_DATA = "com.example.android.bakeapp8.ACTION_DATA_UPDATE";
    private boolean twopane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions);
        ButterKnife.bind(this);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = this.getIntent().getExtras();

        final ArrayList<iObjects> ingrList = b
                .getParcelableArrayList(getString(R.string.list_ingr));
        final ArrayList<RecipesParcelableResp.sObjects> stepsList = b.
                getParcelableArrayList(getString(R.string.list_steps));

        final String title = b.getString(getString(R.string.title_recipes));
        final int idRecep = b.getInt(getString(R.string.recep_id));
        final int serving = b.getInt(getString(R.string.serving));
        final int pos = b.getInt(getString(R.string.position));
        this.title.setText(title);

        if(Utility.isRecipe(this,idRecep)){
            toggle.setChecked(true);
        } else {
            toggle.setChecked(false);
        }

        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Intent rdataupdate = new Intent(UPDATE_DATA);
                if (toggle.isChecked()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ContentValues content = new ContentValues();
                            content.put(ListColumns.TITLE, title);
                            content.put(ListColumns.RECIPE_ID, idRecep);
                            content.put(ListColumns.SERVES, serving);

                            ArrayList<ContentProviderOperation> provider = new ArrayList<>(ingrList.size());
                            for (iObjects ingr : ingrList){
                                ContentProviderOperation.Builder bld = ContentProviderOperation.newInsert(
                                        Provider.Recipes.CONTENT_URI);
                                bld.withValue(MainColumn.ID_RECIPES, idRecep);
                                bld.withValue(MainColumn.INGREDIENT, ingr.getIngredient());
                                bld.withValue(MainColumn.AMOUNT, ingr.getMeasure());
                                bld.withValue(MainColumn.QUANTITY, ingr.getQuantity());
                                provider.add(bld.build());
                            }

                            try{
                                InstructionsActivity.this.getContentResolver().delete(Provider.Recipes.CONTENT_URI,null,null);
                                InstructionsActivity.this.getContentResolver().delete(Provider.RecipList.CONTENT_URI,null,null);
                                Utility.setSavedIngr(InstructionsActivity.this,title);
                                getContentResolver().insert(Provider.RecipList.CONTENT_URI, content);
                                InstructionsActivity.this.getContentResolver().applyBatch(Provider.AUTHORITY, provider);
                                InstructionsActivity.this.sendBroadcast(rdataupdate);
                            } catch(RemoteException | OperationApplicationException e){
                                Log.e(LOG_TAG, "Could not save. Try Again", e);
                            }
                        }
                    }).start(); }
                    else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Utility.setSavedIngr(InstructionsActivity.this,"");
                            InstructionsActivity.this.getContentResolver().delete(Provider.Recipes.CONTENT_URI,null,null);
                            InstructionsActivity.this.getContentResolver().delete(Provider.RecipList.CONTENT_URI,null,null);
                            InstructionsActivity.this.sendBroadcast(rdataupdate);
                        }
                    }).start();
                }
            }
        });

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.instcontainer, Instructions.newInstance(ingrList, stepsList))
                    .commit();
        }
        else {

        }
        if(findViewById(R.id.inst_direction) != null){
            twopane = true;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.inst_direction,
                            StepsFragment.newInstance(stepsList, -1), INS_ACT_TAG)
                    .commit();
        }
        else {
            twopane = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDirectionSelected(ArrayList<RecipesParcelableResp.sObjects> sList, int position) {
         if(twopane){
             getSupportFragmentManager().beginTransaction()
                     .replace(R.id.inst_direction,
                             StepsFragment.newInstance(sList, position), INS_ACT_TAG)
                     .commit();
         }
         else {
             Intent i = new Intent(this, Steps.class)
                     .putParcelableArrayListExtra(getString(R.string.list_steps), sList)
                     .putExtra(getString(R.string.spos), position);
             startActivity(i);
         }
    }
}
