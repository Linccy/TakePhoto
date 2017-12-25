package com.darsh.multipleimageselect.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.darsh.multipleimageselect.R;
import com.darsh.multipleimageselect.adapters.CustomImagesPreviewAdapter;
import com.darsh.multipleimageselect.models.Image;

import java.util.List;


/**
 * @author lin.cx 957109587@qq.com
 * @version 3.0
 */

public class ImagesPreviewActivity extends HelperActivity {
  private ActionBar actionBar;
  private List<Image> images;
  private ViewPager viewPager;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_images_preview);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);

      actionBar.setDisplayShowTitleEnabled(true);
      actionBar.setTitle(R.string.image_preview);
    }

    Intent intent = getIntent();
    if (intent == null) {
      finish();
      return;
    }

    images = intent.getParcelableArrayListExtra("images");
    int position = intent.getIntExtra("position", 0);
    if (images.size() <= position) {
      position = images.size() - 1;
    }
    viewPager = (ViewPager) findViewById(R.id.view_pager);
    viewPager.setAdapter(new CustomImagesPreviewAdapter(getSupportFragmentManager(), images));
    viewPager.setCurrentItem(position);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_preview_action_bar, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (R.id.menu_item_do == item.getItemId()) {
      Intent intent = new Intent();
      intent.putExtra("position", viewPager.getCurrentItem());
      setResult(RESULT_OK, intent);
      finish();
      return true;
    } else if (item.getItemId() == android.R.id.home) {
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    setResult(RESULT_CANCELED);
    finish();
  }
}
