package com.example.android.bakeapp8;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;



public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private List<RecipesParcelableResp> recipes;
    private CardListener cardlistener;
    private Context context;

    public RecipesAdapter(Context context, List<RecipesParcelableResp> recipes){
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cv_main,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder vh, int position) {

        String name = recipes.get(position).getName();
        String image = recipes.get(position).getImage();
        int serving = recipes.get(position).getServings() ;
        int idRecipe = recipes.get(position).getId();

        vh.name.setText(name);
        vh.servingsize.setText(context.getString(R.string.servings, serving));

        if(!image.isEmpty()) {
            Picasso.with(context).load(image).into(vh.img);
        } else {
            vh.img.setVisibility(View.GONE);
        }

        if(Utility.isRecipe(context,idRecipe)){
            vh.highlight.setVisibility(View.VISIBLE);
        } else {
            vh.highlight.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_title)TextView name;
        @BindView(R.id.serving_size)TextView servingsize;
        @BindView(R.id.bookmark_highlight)FrameLayout highlight;
        @BindView(R.id.recipImage)ImageView img;

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            ButterKnife.bind(this,v);
        }

        @Override
        public void onClick(View view) {
            cardlistener.onCardClicked(view, getAdapterPosition());
        }
    }

    public interface CardListener {
        void onCardClicked(View view, int position);
    }

    public void setOnItemClickListener(CardListener listener){this.cardlistener = listener;}
}
