package com.example.android.bakeapp8;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class Instructions extends Fragment {

    private static final String INGREDIENTS = "iList";
    private static final String STEPS = "steps";
    private static final String SCROLL_POS = "scroll_position";

    @BindView(R.id.rv_ingredients)
    RecyclerView rvIngredients;
    @BindView(R.id.rv_inst)
    RecyclerView rvInst;
    @BindView(R.id.layout_inst)
    LinearLayout layoutInstructions;
    @BindView(R.id.sv_inst)
    NestedScrollView svInstructions;

    private ArrayList<RecipesParcelableResp.iObjects> iList;
    private ArrayList<RecipesParcelableResp.sObjects> sList;
    private IngrAdapter ingredientsadapter;
    private StepsAdapter stepsAdapter;
    private LinearLayoutManager manager;
    private Unbinder unbinder;
    private int scrollPosition;


    public static Instructions newInstance(
            ArrayList<RecipesParcelableResp.iObjects> iList, ArrayList<RecipesParcelableResp.sObjects> steps) {
        Instructions instructions = new Instructions();
        Bundle args = new Bundle();
        args.putParcelableArrayList(INGREDIENTS, iList);
        args.putParcelableArrayList(STEPS, steps);
        instructions.setArguments(args);
        return instructions;
    }

    public interface instCb {
        void onDirectionSelected(ArrayList<RecipesParcelableResp.sObjects> sList,
                                 int position);
    }
    public Instructions() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iList = getArguments().getParcelableArrayList(INGREDIENTS);
        sList = getArguments().getParcelableArrayList(STEPS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.instructions_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        ingredientsadapter = new IngrAdapter(getContext(), iList);
        rvIngredients.setLayoutManager(new LinearLayoutManager(getContext()));
        rvIngredients.setAdapter(ingredientsadapter);
        rvIngredients.setNestedScrollingEnabled(false);
        stepsAdapter = new StepsAdapter(getContext(), sList);
        manager = new LinearLayoutManager(getContext());
        rvInst.setLayoutManager(manager);
        rvInst.setAdapter(stepsAdapter);
        rvInst.setNestedScrollingEnabled(false);
        stepsAdapter.setOnInstructionClickListener(new StepsAdapter.InstructionClickListener() {
            @Override
            public void onInstructionClicked(View view, int position) {
                ((instCb)getActivity()).onDirectionSelected(sList,position);
            }
        });
        if(savedInstanceState != null){
            scrollPosition = savedInstanceState.getInt(SCROLL_POS);
            svInstructions.post(new Runnable() {
                @Override
                public void run() {
                    svInstructions.scrollTo(0, scrollPosition);
                }
            });
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCROLL_POS, svInstructions.getScrollY());
    }

    @Override
    public void onPause() {
        super.onPause();
        scrollPosition = svInstructions.getScrollY();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
