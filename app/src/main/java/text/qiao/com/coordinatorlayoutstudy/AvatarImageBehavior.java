package text.qiao.com.coordinatorlayoutstudy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @project：CoordinatorLayoutStudy
 * @fileName AvatarImageBehavior
 * @author：乔少聪
 * @date：2018/11/23 16:27
 * @describe：
 */
@SuppressWarnings("unused")
public class AvatarImageBehavior extends CoordinatorLayout.Behavior<CircleImageView> {
    private final static float MIN_AVATAR_PERCENTAGE_SIZE = 0.3f;
    private final static int EXTRA_FINAL_AVATAR_PADDING = 80;

    private int mStartYPosition; // 起始的Y轴位置
    private int mFinalYPosition; // 结束的Y轴位置
    private int mStartHeight; // 开始的图片高度
    private int mFinalHeight; // 结束的图片高度
    private int mStartXPosition; // 起始的X轴高度
    private int mFinalXPosition; // 结束的X轴高度
    private float mStartToolbarPosition; // Toolbar的起始位置

    private final Context mContext;
    private float mAvatarMaxSize;


    public AvatarImageBehavior(Context context, AttributeSet attrs) {
        this.mContext = context;
        init();
    }


    private void init() {
        bindDimensions();
    }

    private void bindDimensions() {
        mAvatarMaxSize = mContext.getResources().getDimension(R.dimen.image_width);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull CircleImageView child, @NonNull View dependency) {
        return dependency instanceof Toolbar;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull CircleImageView child, @NonNull View dependency) {
        shouldInitProperties(child,dependency);
        final int maxScrollDistance= (int) (mStartToolbarPosition-getStatusBarHeight());
        float expandedPercentageFactor = dependency.getY() / maxScrollDistance;  // 滑动的百分比

        float distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
                * (1f - expandedPercentageFactor)) + (child.getHeight() / 2);  // Y轴距离
        float distanceXToSubtract = ((mStartXPosition - mFinalXPosition)
                * (1f - expandedPercentageFactor)) + (child.getWidth() / 2);  // X轴距离
        float heightToSubtract = ((mStartHeight - mFinalHeight) * (1f - expandedPercentageFactor)); // 高度减小

        // 设置图片位置
        child.setY(mStartYPosition - distanceYToSubtract);
        child.setX(mStartXPosition - distanceXToSubtract);

        // 设置图片大小
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.width = (int) (mStartHeight - heightToSubtract);
        lp.height = (int) (mStartHeight - heightToSubtract);
        child.setLayoutParams(lp);
        return true;
    }

    private void shouldInitProperties(CircleImageView child, View dependency) {

        if (mStartXPosition == 0) {
            mStartXPosition = (int) (child.getX() + (child.getWidth() / 2));
        }
        if (mFinalXPosition == 0) {
            mFinalXPosition = (int) mContext.getResources().getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material) + (mFinalHeight / 2);
        }
        if (mStartYPosition == 0) {
            mStartYPosition = (int) (child.getY() + (child.getHeight() / 2));
        }
        if (mFinalYPosition == 0) {
            mFinalYPosition = (int) (dependency.getHeight() / 2);
        }
        if (mStartHeight == 0)  // 图片高度
        {
            mStartHeight = child.getHeight();
        }

        if (mFinalHeight == 0)  // Toolbar缩略图高度
        {
            mFinalHeight = mContext.getResources().getDimensionPixelOffset(R.dimen.image_final_width);
        }

        if (mStartToolbarPosition == 0)  // Toolbar的起始位置{
        {
            mStartToolbarPosition = dependency.getY() + (dependency.getHeight() / 2);
        }

    }

    // 获取状态栏高度
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
