package text.qiao.com.coordinatorlayoutstudy;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    // 控制ToolBar的变量
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;

    private static final int ALPHA_ANIMATIONS_DURATION = 200;
    @BindView(R.id.main_tv_toolbar_title)
    TextView mainTvToolbarTitle;
    @BindView(R.id.main_cl_heard)
    CircleImageView mainClHeard;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    @BindView(R.id.main_iv_placeholder)
    ImageView mainIvPlaceholder;
    @BindView(R.id.main_ll_title_container)
    LinearLayout mainLlTitleContainer;
    @BindView(R.id.main_fl_title)
    FrameLayout mainFlTitle;
    @BindView(R.id.main_abl_bar)
    AppBarLayout mainAblBar;
    @BindView(R.id.main_tb_toolbar)
    Toolbar mainTbToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mainTbToolbar.setTitle("");
        mainClHeard.bringToFront();
        mainAblBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs((float) i / (float) maxScroll);
                handleAlphaOnTitle(percentage);
                handleToolbarTitleVisibility(percentage);
            }
        });
        initParallaxValues();
    }

    // 设置自动滑动（视差）效果
    private void initParallaxValues() {
        CollapsingToolbarLayout.LayoutParams petDetailsLp =
                (CollapsingToolbarLayout.LayoutParams) mainIvPlaceholder.getLayoutParams();

        CollapsingToolbarLayout.LayoutParams petBackgroundLp =
                (CollapsingToolbarLayout.LayoutParams) mainFlTitle.getLayoutParams();

        petDetailsLp.setParallaxMultiplier(0.9f);
        petBackgroundLp.setParallaxMultiplier(0.3f);

        mainIvPlaceholder.setLayoutParams(petDetailsLp);
        mainFlTitle.setLayoutParams(petBackgroundLp);
    }

//    private void handleToolbarTitlevisibility(float percentage) {
//        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
//            if (!mIsTheTitleVisible) {
//                mIsTheTitleVisible = true;
//                startAlphaAnimation(mainTvToolbarTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
//            }
//        } else {
//            if (mIsTheTitleVisible) {
//                mIsTheTitleVisible = false;
//                startAlphaAnimation(mainTvToolbarTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
//            }
//        }
//    }
//
//    private void handleAlphaOnTitle(float percentage) {
//        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
//            if (mIsTheTitleContainerVisible) {
//                mIsTheTitleContainerVisible = true;
//                startAlphaAnimation(mainLlTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
//            }
//        } else {
//            if (!mIsTheTitleContainerVisible) {
//                mIsTheTitleContainerVisible = false;
//                startAlphaAnimation(mainLlTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
//            }
//        }
//    }
    // 处理ToolBar的显示
    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mainTvToolbarTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }
        } else {
            if (mIsTheTitleVisible) {
                startAlphaAnimation(mainTvToolbarTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    // 控制Title的显示
    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mainLlTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }
        } else {
            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mainLlTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }
    // 设置渐变的动画
    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }
}
