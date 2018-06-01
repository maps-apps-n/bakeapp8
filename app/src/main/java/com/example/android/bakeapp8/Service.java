package com.example.android.bakeapp8;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;



public class Service extends RemoteViewsService {

    private static final String[] RECIPE_COLUMNS = {
            ListColumns._ID,
            ListColumns.TITLE,
            ListColumns.SERVES
    };

    private static final int INDEX_ID = 0;
    private static final int INDEX_RECIPE_NAME = 1;
    private static final int INDEX_RECIPE_SERVING_SIZE = 2;
    private static final String[] INGREDIENTS_COLUMNS = {
            MainColumn.ID,
            MainColumn.INGREDIENT,
            MainColumn.AMOUNT,
            MainColumn.QUANTITY,
            Db.INGREDIENTS + "." + MainColumn.ID_RECIPES
    };

    private static final int ING_ID_INDEX = 0;
    private static final int ING_INDEX = 1;
    private static final int ING_MEAS_INDEX = 2;
    private static final int ING_QUANTY_INDEX = 3;
    private static final int ID_RECIPES_INDEX = 4;

    @Override
    public void onCreate() {
        super.onCreate();
        BookmarkProviderWidget.setWidgetText(this, Utility.getSavedIngr(this));
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() { }

            @Override
            public void onDataSetChanged() {
                if(data != null){
                    data.close();
                }

                final long identityToken = Binder.clearCallingIdentity();
                data = getContentResolver().query(Provider.Recipes.CONTENT_URI,
                        INGREDIENTS_COLUMNS,
                        null,
                        null,
                        null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null){
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if(position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }

                RemoteViews remote = new RemoteViews(getPackageName(),
                        R.layout.widget_text);

                String title = data.getString(ING_INDEX);
                String measurement = data.getString(ING_MEAS_INDEX);
                double quantity = data.getDouble(ING_QUANTY_INDEX);

                remote.setTextViewText(R.id.title_widget, title);
                remote.setTextViewText(R.id.amount_widget, getString(R.string.ingredient_amount,
                        Double.toString(quantity),measurement));

                return remote;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(),R.layout.widget_text);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int i) {
                if (data.moveToPosition(i))
                    return data.getLong(ING_ID_INDEX);
                return i;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
