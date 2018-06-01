package com.example.android.bakeapp8;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Singleton {

    private static Singleton access;
    private RequestQueue queue;
    private static Context context;

    private Singleton(Context context) {
        Singleton.context = context.getApplicationContext();
        queue = getRequestQueue();
    }

    public static synchronized Singleton getInstance(Context context) {
        if (access == null) {
            access = new Singleton(context);
        }
        return access;
    }

    public RequestQueue getRequestQueue() {
        if (queue == null) {
            queue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return queue;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}