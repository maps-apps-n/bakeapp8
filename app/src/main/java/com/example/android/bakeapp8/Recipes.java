package com.example.android.bakeapp8;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Recipes extends Fragment {

    @BindView(R.id.rv_recipes)RecyclerView rvmain;
    @BindView(R.id.loading_recipes)ProgressBar pb;
    @BindView(R.id.loading_txt)TextView empty;
    @BindView(R.id.main_list_layout)CoordinatorLayout cl;
    private List<RecipesParcelableResp> recipList;
    private RecipesAdapter recipesAdapter;
    private Unbinder unbind;

    public Recipes() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recipes_list, container, false);
        unbind = ButterKnife.bind(this,v);
        setData();
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }

    private void setData(){

        String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
        if(checkConnection(getContext())) {
            StringRequest recipestring = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Gson gson = new GsonBuilder().create();
                Type type = new TypeToken<ArrayList<RecipesParcelableResp>>() {
                }.getType();
                recipList = gson.fromJson(response, type);

                if (getResources().getBoolean(R.bool.tablet_mode)) {
                    recipesAdapter = new RecipesAdapter(getContext(), recipList);
                    rvmain.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    rvmain.setHasFixedSize(true);
                    rvmain.addItemDecoration(new Items(2,
                            Items.PxConverter(getContext(), 0), true));
                    rvmain.setAdapter(recipesAdapter);
                    getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                } else {
                    recipesAdapter = new RecipesAdapter(getContext(), recipList);
                    rvmain.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    rvmain.setAdapter(recipesAdapter);
                }

                recipesAdapter.setOnItemClickListener(new RecipesAdapter.CardListener() {
                    @Override
                    public void onCardClicked(View view, int position) {
                        Intent i = new Intent(getActivity(), InstructionsActivity.class);
                        Bundle b = new Bundle();
                        b.putInt(getString(R.string.position), position);
                        b.putString(getString(R.string.recipeName), recipList.get(position).getName());
                        b.putInt(getString(R.string.recep_id), recipList.get(position).getId());
                        b.putInt(getString(R.string.serving), recipList.get(position).getServings());
                        b.putParcelableArrayList(getString(R.string.iList), recipList.get(position).getIngredients());
                        b.putParcelableArrayList(getString(R.string.steps), recipList.get(position).getSteps());
                        i.putExtras(b);
                        startActivityForResult(i, 1);
                    }
                });

                pb.setVisibility(View.INVISIBLE);
                empty.setVisibility(View.INVISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Singleton.getInstance(getContext().getApplicationContext()).addToRequestQueue(recipestring);
        } else {
            empty.setVisibility(View.VISIBLE);
            pb.setVisibility(View.INVISIBLE);

            Snackbar snack = Snackbar.make(cl, R.string.emptyrecipe, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setData();
                        }});
            snack.show();
        }
    }

    private boolean checkConnection(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ntwk = connectivity.getActiveNetworkInfo();
        return ntwk != null &&
                ntwk.isConnectedOrConnecting();
    }

    public void refreshList(){
        if(recipesAdapter != null) {
            recipesAdapter.notifyDataSetChanged();
        }
    }
}
