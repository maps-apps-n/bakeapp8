package com.example.android.bakeapp8;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;



public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private ArrayList<RecipesParcelableResp.sObjects> steps;
    private Context context;
    private InstructionClickListener listener;
    private int Selection = -1;

    public StepsAdapter(Context context, ArrayList<RecipesParcelableResp.sObjects> steps){
        this.context = context;
        this.steps = steps;
    }

    @Override
    public StepsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cv_steps,parent,false);
        return new StepsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StepsAdapter.ViewHolder vh, int position) {

        String shortDesc = steps.get(position).getShortDescription();
        String thumbnailUrl = steps.get(position).getThumbnailURL();

        vh.shortDesc.setText(context.getString(R.string.step,position,shortDesc));

        if(!thumbnailUrl.isEmpty()) {
            Picasso.with(context).load(thumbnailUrl).into(vh.image, new Callback() {
                @Override
                public void onSuccess() {
                }
                @Override
                public void onError() {
                    vh.image.setVisibility(View.GONE);
                }
            });
        } else {
            vh.image.setVisibility(View.GONE);
        }

        if(position == Selection) {
            vh.rl.setSelected(true);
        } else {
            vh.rl.setSelected(false);
        }
}

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.steps_desc)TextView shortDesc;
        @BindView(R.id.steps_rl)
        RelativeLayout rl;
        @BindView(R.id.thumbnail)
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void onClick(View view) {
            listener.onInstructionClicked(view,getAdapterPosition());
            Selection = getAdapterPosition();
            notifyDataSetChanged();
        }
    }

    public interface  InstructionClickListener{
        void onInstructionClicked(View view, int position);
    }
    public void setOnInstructionClickListener(InstructionClickListener listener){
        this.listener = listener;
    }
}