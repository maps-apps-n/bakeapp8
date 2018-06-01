package com.example.android.bakeapp8;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;


public class Utility {

    private static final String SAVED = "saved";

    public static String getSavedIngr(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(SAVED, "");
    }

    public static void setSavedIngr(Context context, String title){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(SAVED,title);
        edit.apply();
    }

    public static boolean isRecipe(Context context, int recipeId){
        SQLiteOpenHelper helper = com.example.android.bakeapp8.generated.Db.getInstance(context);
        SQLiteDatabase liteDatabase = helper.getReadableDatabase();
        long exist = DatabaseUtils.queryNumEntries(liteDatabase, Db.RECIPES, ListColumns.RECIPE_ID + " = " + recipeId, null);
        liteDatabase.close();
        return exist > 0;
    }
}
