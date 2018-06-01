package com.example.android.bakeapp8;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class Ingr extends Fragment {

    public static final String INGREDIENTS = "ingredients";
    @BindView(R.id.rv_ingredients)RecyclerView ingRv;
    private ArrayList<RecipesParcelableResp.iObjects> ingredients;
    private Unbinder unbind;

    public static Ingr newInstance(ArrayList<RecipesParcelableResp.iObjects> list){
        Ingr ingredient = new Ingr();
        Bundle b = new Bundle();
        b.putParcelableArrayList(INGREDIENTS,list);
        ingredient.setArguments(b);
        return ingredient;
    }

    public Ingr() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ingredients = getArguments().getParcelableArrayList(INGREDIENTS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ingredients_rv, container, false);
        unbind = ButterKnife.bind(this,view);
        IngrAdapter adapterI = new IngrAdapter(getContext(), ingredients);
        ingRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        ingRv.setAdapter(adapterI);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }
}
