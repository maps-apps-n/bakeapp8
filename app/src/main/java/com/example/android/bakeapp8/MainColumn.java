package com.example.android.bakeapp8;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;
import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;


public interface MainColumn {

    @DataType(INTEGER) @PrimaryKey @AutoIncrement
    String ID = "_id";
    @DataType(INTEGER) @References(
            table = Db.RECIPES, column = ListColumns.RECIPE_ID)
    String ID_RECIPES = "recipes_id";
    @DataType(REAL)
    String QUANTITY = "quantity";
    @DataType(TEXT)
    String AMOUNT = "measurement";
    @DataType(TEXT)
    String INGREDIENT = "ingredient";
}
