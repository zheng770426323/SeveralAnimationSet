package com.example.auser.severalanimationset.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.auser.severalanimationset.R;
import com.example.auser.severalanimationset.adpter.CardAdapter;
import com.example.auser.severalanimationset.util.BlurBitmapUtils;
import com.example.auser.severalanimationset.util.ViewSwitchUtils;
import com.view.jameson.library.CardScaleHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private List<Integer> mList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private CardScaleHelper mCardScaleHelper;
    private ImageView mBlurView;
    private Runnable mBlurRunnable;
    private int mLastPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上系统
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  //全屏的意思，也就是会将状态栏隐藏
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;  //保留系统状态栏
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();//隐藏actionbar*/
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        for (int i = 0;i<5;i++){
            mList.add(R.mipmap.banner_v2);
            mList.add(R.mipmap.banner_v3);
            mList.add(R.mipmap.banner_v4);
            mList.add(R.mipmap.banner_v5);
            mList.add(R.mipmap.banner_v6);
            mList.add(R.mipmap.banner_v7);
            mList.add(R.mipmap.banner_v8);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(new CardAdapter(mList));
        // mRecyclerView绑定scale效果
        mCardScaleHelper = new CardScaleHelper();
        mCardScaleHelper.setCurrentItemPos(0);
        mCardScaleHelper.attachToRecyclerView(mRecyclerView);

        initBlurBackground();
    }

    private void initBlurBackground() {
        mBlurView = (ImageView) findViewById(R.id.blurView);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    notifyBackgroundChange();
                }
            }
        });
        notifyBackgroundChange();
    }

    private void notifyBackgroundChange() {
        if (mLastPos == mCardScaleHelper.getCurrentItemPos())
            return;
        mLastPos = mCardScaleHelper.getCurrentItemPos();
        final int resId = mList.get(mLastPos);
        mBlurView.removeCallbacks(mBlurRunnable);
        mBlurRunnable = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
                ViewSwitchUtils.startSwitchBackgroundAnim(mBlurView, BlurBitmapUtils.getBlurBitmap(mBlurView.getContext(), bitmap, 15));
            }
        };
        mBlurView.postDelayed(mBlurRunnable, 500);
    }
}
