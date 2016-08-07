package com.mojota.bazinga;


import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.mojota.bazinga.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TwoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TwoFragment extends Fragment implements PicListAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mRvList;
    private PicListAdapter mListAdapter;
    private List mList = new ArrayList();
    private SwipeRefreshLayout mSwipeRefresh;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TwoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TwoFragment newInstance(String param1, String param2) {
        TwoFragment fragment = new TwoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mSwipeRefresh.setColorSchemeResources(R.color.color_red, R.color.color_orange, R.color
                .color_yellow, R.color.color_green);
        mSwipeRefresh.setOnRefreshListener(this);
        mRvList = (RecyclerView) view.findViewById(R.id.rv_list);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRvList.setLayoutManager(llm);
        mRvList.setItemAnimator(new DefaultItemAnimator());
        mList = getList();
        mListAdapter = new PicListAdapter(getActivity(), mList);
        mRvList.setAdapter(mListAdapter);
        mListAdapter.setOnItemClickListener(this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder
                    viewHolder) {

                int dragFlags = 0;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags,swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem(viewHolder.getAdapterPosition());
            }

        });
        touchHelper.attachToRecyclerView(mRvList);
        return view;
    }


    @Override
    public void onItemClick(View view, int position) {
        Intent picIntent = new Intent(getActivity(), PicActivity.class);
        picIntent.putExtra("resId", Constants.PICS[position]);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_pic);
        ActivityCompat.startActivity(getActivity(), picIntent,
                ActivityOptions.makeSceneTransitionAnimation(getActivity(), imageView, "shareIv")
                        .toBundle());
//        addItem(position);  // 增加条目
    }

    private void addItem(int position) {
        mListAdapter.addItem(position);
        if (position == 0){
            mRvList.smoothScrollToPosition(0);
        }
    }

    public void removeItem(int position) {
        mListAdapter.removeItem(position);
    }

    public List getList() {
        List list = new ArrayList();
        for (int i = 0; i < Constants.PICS.length; i++) {
            list.add(Constants.PICS[i]);
        }
        return list;
    }

    @Override
    public void onRefresh() {
        mList = getList();
        mListAdapter.setList(mList);
        mListAdapter.notifyItemRangeChanged(0,mList.size()); // 这个可以有动画效果
//        mListAdapter.notifyDataSetChanged();// 这个没有动画效果
        mSwipeRefresh.setRefreshing(false);
    }
}
