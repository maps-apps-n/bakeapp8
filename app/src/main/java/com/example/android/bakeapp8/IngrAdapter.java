package com.example.android.bakeapp8;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



public class IngrAdapter extends RecyclerView.Adapter<IngrAdapter.ViewHolder> {

    private ArrayList<RecipesParcelableResp.iObjects> ingredients;
    private Context context;

    public IngrAdapter(Context context, ArrayList<RecipesParcelableResp.iObjects> ing){
        this.context = context;
        this.ingredients = ing;
    }

    @Override
    public IngrAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_ingredients,parent,false);
        return new IngrAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngrAdapter.ViewHolder vh, int position) {

        String ingredientname = ingredients.get(position).getIngredient();
        String ingredientmeasurement = ingredients.get(position).getMeasure();
        double ingredientquantity = ingredients.get(position).getQuantity();
        String ingredientamount = context.getString(R.string.ingredient_amount, Double.toString(ingredientquantity),ingredientmeasurement);
        vh.name.setText(ingredientname);
        vh.amount.setText(ingredientamount);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredient_name)TextView name;
        @BindView(R.id.ingredient_amount)TextView amount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}