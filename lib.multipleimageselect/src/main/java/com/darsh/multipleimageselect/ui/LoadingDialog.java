package com.darsh.multipleimageselect.ui;

/**
 * @author lin.cx 957109587@qq.com
 * @version 3.0
 */

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.darsh.multipleimageselect.R;

/**
 * 公用的弹出框
 *
 * @author lining
 */
public class LoadingDialog {

  /**
   * 得到自定义的progressDialog
   *
   * @param context
   * @param msg
   * @return
   */
  public static Dialog createLoadingDialog(Context context, String msg){
    return createLoadingDialog(context, R.drawable.widget_loading_frame_bg, msg);
  }

  public static Dialog createLoadingDialog(Context context, @DrawableRes int imgId, String msg) {

    // 首先得到整个View
    View view = LayoutInflater.from(context).inflate(
            R.layout.loading_dialog_view, null);
    // 获取整个布局
    LinearLayout layout = (LinearLayout) view
            .findViewById(R.id.dialog_view);
    // 页面中的Img
    ImageView imgLoading = (ImageView) view.findViewById(R.id.img_loading_progress);
    imgLoading.setImageResource(imgId);

    // 页面中显示文本
    TextView tipText = (TextView) view.findViewById(R.id.tv_loading_progress_msg);

    // 加载动画，动画用户使img图片不停的旋转
    Animation animation = AnimationUtils.loadAnimation(context,
            R.anim.dialog_load_animation);
    // 显示动画
    imgLoading.startAnimation(animation);
    // 显示文本
    if(TextUtils.isEmpty(msg)) {
      tipText.setVisibility(View.GONE);
    }else {
      tipText.setVisibility(View.VISIBLE);
      tipText.setText(msg);
    }

    // 创建自定义样式的Dialog
    Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);
    // 设置返回键返回
    loadingDialog.setCancelable(true);
    //点击空白取消
    loadingDialog.setCanceledOnTouchOutside(true);
    loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT));

    return loadingDialog;
  }
}