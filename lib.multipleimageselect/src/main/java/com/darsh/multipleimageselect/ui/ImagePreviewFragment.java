package com.darsh.multipleimageselect.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.darsh.multipleimageselect.R;
import com.darsh.multipleimageselect.models.Image;

/**
 * @author lin.cx 957109587@qq.com
 * @version 3.0
 */

public class ImagePreviewFragment extends Fragment {
  private Image image;
  private View rootView;
  private PinchImageView imageView;

  private static final String MOMENT_IMAGE = "save_moment_image";

  public ImagePreviewFragment() {

  }

  public void setImage(Image image) {
    this.image = image;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    rootView = inflater.inflate(R.layout.fragment_image_preview, container, false);
    return rootView;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    imageView = (PinchImageView) rootView.findViewById(R.id.image_pre);
    imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        getActivity().finish();
      }
    });
    imageView.setEnabled(false);

    if (savedInstanceState != null) {
      image = savedInstanceState.getParcelable(MOMENT_IMAGE);
    }

    showImage();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (outState != null) {
      outState.putParcelable(MOMENT_IMAGE, image);
    }
  }

  private void showImage() {
    if (image == null) {
      return;
    }

//    final Dialog dialog = LoadingDialog.createLoadingDialog(getContext(), null);
//    dialog.show();
    final FrameLayout frameLayout = (FrameLayout) rootView.findViewById(R.id.frame_root);
    final View loadView = rootView.findViewById(R.id.dialog_view);
    // 页面中的Img
    final ImageView imgLoading = (ImageView) loadView.findViewById(R.id.img_loading_progress);
    imgLoading.setVisibility(View.VISIBLE);

    // 页面中显示文本
    TextView tipText = (TextView) loadView.findViewById(R.id.tv_loading_progress_msg);

    // 加载动画
    Animation animation = AnimationUtils.loadAnimation(getContext(),
            R.anim.dialog_load_animation);
    // 显示动画
    imgLoading.startAnimation(animation);
    // 显示文本
//    if(TextUtils.isEmpty(msg)) {
//      tipText.setVisibility(View.GONE);
//    }else {
//      tipText.setVisibility(View.VISIBLE);
//      tipText.setText(msg);
//    }

    if (!TextUtils.isEmpty(image.path)) {
      Glide.with(this)
              .load(image.path)
              .into(new GlideDrawableImageViewTarget(imageView) {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                  super.onResourceReady(resource, animation);
                  //加载完成
                  imageView.setEnabled(true);
                  loadView.setVisibility(View.GONE);
                }
              });
    } else {
      //加载完成
      imageView.setImageResource(android.R.drawable.ic_menu_report_image);
      loadView.setVisibility(View.GONE);
    }
  }
}
