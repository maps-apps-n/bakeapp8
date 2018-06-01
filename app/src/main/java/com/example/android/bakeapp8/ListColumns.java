package com.example.android.bakeapp8;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;



public interface ListColumns {

    @DataType(INTEGER) @PrimaryKey @AutoIncrement
    String _ID = "_id";
    @DataType(INTEGER) @NotNull
    String RECIPE_ID = "recipe_id";
    @DataType(TEXT) @NotNull
    String TITLE = "recipe_name";
    @DataType(INTEGER) @NotNull
    String SERVES = "serving_size";
}
