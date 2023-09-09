package com.handy.fetchbook.activity.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.handy.fetchbook.R;

import java.util.Random;

public class CircleTurntableActivity extends AppCompatActivity implements View.OnClickListener {

    private Animation mStartAnimation;
    private Animation mEndAnimation;
    private ImageView mLuckyTurntable;
    private boolean isRunning;
    private int mItemCount = 7;
    //转到转盘的位置，1:代表左边1。2：代表左2 3：代表左3
    private int[] mPrizePosition = {1, 2, 3, 4, 5, 6, 7};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_turntable);

        mLuckyTurntable = findViewById(R.id.id_lucky_turntable);
        ImageView mStartBtn = findViewById(R.id.id_start_btn);
        mStartBtn.setOnClickListener(this);

        mStartAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        mStartAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                endAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }

    @Override
    public void onClick(View v) {
        // 未抽过奖并有抽奖的机会
        if (!isRunning) {
            isRunning = true;
            mStartAnimation.reset();
            mLuckyTurntable.startAnimation(mStartAnimation);
            if (mEndAnimation != null) {
                mEndAnimation.cancel();
            }
            //new Handler().postDelayed(this::endAnimation, 720);
        }

    }

    // 结束动画，慢慢停止转动，抽中的奖品定格在指针指向的位置
    private void endAnimation() {
        int position = mPrizePosition[0];
        int oneDegree = (360 / mItemCount);
        float toDegreeMin = oneDegree * (position - 0.5f);
        Log.d("Charles", "value = $" + toDegreeMin);
        float toDegree = toDegreeMin + 360 * 3; //3周 + 偏移量
        Log.d("Charles", "value = $" + toDegree);

        // 按中心点旋转 toDegree度
        // 参数：旋转的开始角度、旋转的结束角度、X轴的伸缩模式、X坐标的伸缩值、Y轴的伸缩模式、Y坐标的伸缩值
        mEndAnimation = new RotateAnimation(0, toDegree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mEndAnimation.setDuration(3000); // 设置旋转时间
        mEndAnimation.setRepeatCount(0); // 设置重复次数
        mEndAnimation.setFillAfter(true);// 动画执行完后是否停留在执行完的状态
        mEndAnimation.setInterpolator(new DecelerateInterpolator()); // 动画播放的速度
        mEndAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isRunning = false;
                Toast.makeText(CircleTurntableActivity.this, "富光350ml水杯", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mLuckyTurntable.startAnimation(mEndAnimation);
        mStartAnimation.cancel();
    }

    //停止动画（异常情况，没有奖品）
    private void stopAnimation() {

        //转盘停止回到初始状态
        if (isRunning) {

            mStartAnimation.cancel();
            mLuckyTurntable.clearAnimation();
            isRunning = false;
        }
    }

}


