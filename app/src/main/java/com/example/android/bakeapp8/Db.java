package com.example.android.bakeapp8;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = Db.VERSION)
public final class Db {
    public Db(){ }
    public static final int VERSION = 4;
        @Table(ListColumns.class) public static final String RECIPES = "list";
        @Table(MainColumn.class) public static final String INGREDIENTS = "recipe_ingredients";
}
