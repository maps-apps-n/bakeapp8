package com.example.android.bakeapp8;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

public class BookmarkProviderWidget extends AppWidgetProvider {

    static void updateWidget(Context context, AppWidgetManager wmanager,
                             int appWidgetId) {

        CharSequence appname = context.getString(R.string.app_name);
        String recipename = Utility.getSavedIngr(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

        if(recipename.equals("")) {
            views.setTextViewText(R.id.tv_widget, appname);
        } else {
            views.setTextViewText(R.id.tv_widget, recipename);
        }

        Intent wIntent = new Intent(context, MainActivity.class);
        PendingIntent intentp = PendingIntent.getActivity(context, 0, wIntent, 0);
        views.setOnClickPendingIntent(R.id.appbar_widget, intentp);
        setWidgetAdapter(context, views);

        boolean Info = context.getResources()
                .getBoolean(R.bool.use_detail_activity);
        Intent i = Info
                ? new Intent(context, InstructionsActivity.class)
                : new Intent(context,MainActivity.class);
        PendingIntent pIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            pIntent = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(i)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        views.setPendingIntentTemplate(R.id.widget, pIntent);
        views.setEmptyView(R.id.widget, R.id.empty_widget);
        wmanager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager wmanager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, wmanager, appWidgetId);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager wmanager, int appWidgetId, Bundle newOptions) {
        context.startService(new Intent(context, Service.class));
    }

    @Override
    public void onReceive(Context context, Intent i) {
        super.onReceive(context, i);
        if(InstructionsActivity.UPDATE_DATA.equals(i.getAction())) {
            AppWidgetManager wmanager = AppWidgetManager.getInstance(context);
            int[] appwidgetIds = wmanager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            wmanager.notifyAppWidgetViewDataChanged(appwidgetIds, R.id.widget);
            onUpdate(context,wmanager,appwidgetIds);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void setWidgetAdapter(Context context, @NonNull final RemoteViews views){
        views.setRemoteAdapter(R.id.widget,
                new Intent(context, Service.class));
    }

    public static void setWidgetText(Context context, String name){
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        views.setTextViewText(R.id.tv_widget,name);
    }
}

