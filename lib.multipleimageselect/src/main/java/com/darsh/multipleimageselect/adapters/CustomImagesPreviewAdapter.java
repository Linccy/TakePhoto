package com.darsh.multipleimageselect.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.darsh.multipleimageselect.ui.ImagePreviewFragment;
import com.darsh.multipleimageselect.models.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lin.cx 957109587@qq.com
 * @version 3.0
 */

public class CustomImagesPreviewAdapter extends FragmentPagerAdapter {

  private List<Image> images = new ArrayList<>();

  public CustomImagesPreviewAdapter(FragmentManager fm, List<Image> images) {
    super(fm);
    this.images = images;
  }

  @Override
  public int getCount() {
    return images == null ? 0 : images.size();
  }

  @Override
  public Fragment getItem(int position) {
    ImagePreviewFragment fragment=new ImagePreviewFragment();
    fragment.setImage(images.get(position));
    return fragment;
  }

  public List<Image> getImages() {
    return images;
  }

  public void setImages(List<Image> images) {
    this.images.clear();
    this.images.addAll(images);
    notifyDataSetChanged();
  }
}
