package com.example.android.bakeapp8;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.NotifyBulkInsert;
import net.simonvt.schematic.annotation.NotifyInsert;
import net.simonvt.schematic.annotation.TableEndpoint;


@ContentProvider(authority = Provider.AUTHORITY, database = Db.class)
public final class Provider {

    private Provider(){ }

    public static final String AUTHORITY = "com.example.android.bakeapp8.Provider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    interface Path {
        String RECIPES = "rList";
        String INGREDIENTS = "ingredients";
        String CONTENT = "fromList";
    }

    private static Uri buildUri(String... paths){
        Uri.Builder bldr = BASE_CONTENT_URI.buildUpon();
        for (String path : paths){
            bldr.appendPath(path);
        }
        return bldr.build();
    }

    @TableEndpoint(table = Db.RECIPES) public static class RecipList {
        @ContentUri(
                path = Path.RECIPES,
                type = "vnd.android.cursor.dir/list",
                defaultSort = ListColumns.RECIPE_ID + " ASC")
        public static final Uri CONTENT_URI = buildUri(Path.RECIPES);
        @InexactContentUri(
                path = Path.RECIPES + "/#",
                name = "ID_RECIPES",
                type ="vnd.android.cursor.item/list",
                whereColumn = ListColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return  buildUri(Path.RECIPES, String.valueOf(id));
        }
        @InexactContentUri(
                path = Path.RECIPES + "/*",
                name = "TITLE",
                type = "vnd.android.cursor.item/list",
                whereColumn = ListColumns.TITLE,
                pathSegment = 1)
        public static Uri withRecipeName(String name){
            return buildUri(Path.RECIPES, name);
        }
    }

    @TableEndpoint(table = Db.INGREDIENTS) public static class Recipes {
        @ContentUri(
                path = Path.INGREDIENTS,
                type = "vnd.android.cursor.dir/ingredients")
        public static final Uri CONTENT_URI = buildUri(Path.INGREDIENTS);
        @InexactContentUri(
                name = "INGREDIENTS_FROM_LIST",
                path = Path.INGREDIENTS + "/" + Path.CONTENT + "/#",
                type = "vnd.android.cursor.dir/list",
                whereColumn = MainColumn.ID_RECIPES,
                pathSegment = 2)
        public static Uri fromList(int recipeId){
            return buildUri(Path.INGREDIENTS, Path.CONTENT, String.valueOf(recipeId));
        }
        @NotifyInsert(paths = Path.INGREDIENTS) public static Uri[] onInsert(ContentValues contentValues){
            final int recipeId = contentValues.getAsInteger(MainColumn.ID_RECIPES);
            return new Uri[]{
                    RecipList.withId(recipeId), fromList(recipeId)
            };
        }
        @NotifyBulkInsert(paths = Path.INGREDIENTS)
        public static Uri[] onBulkInsert(Context context, Uri uri, ContentValues[] values, long[] ids) {
            return new Uri[] {
                    uri,
            };
        }
    }
}
