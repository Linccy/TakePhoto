package com.darsh.multipleimageselect.ui;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.darsh.multipleimageselect.R;
import com.darsh.multipleimageselect.adapters.CustomImagesPreviewAdapter;
import com.darsh.multipleimageselect.models.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 图片浏览，点击保存
 *
 * @author lin.cx 957109587@qq.com
 * @version 3.0
 */

public class ImageBrowsActivity extends HelperActivity implements ViewPager.OnPageChangeListener {
  private ActionBar actionBar;
  private List<Image> images;
  private ViewPager viewPager;
  private TextView tvPosition;
  private boolean hasPermission = true;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //取消状态栏
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);

    setContentView(R.layout.activity_image_brows);
    setView(findViewById(R.id.big_images_bg));
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
    viewPager = (ViewPager) findViewById(R.id.image_browser);
    viewPager.setAdapter(new CustomImagesPreviewAdapter(getSupportFragmentManager(), images));
    viewPager.setCurrentItem(position);
    viewPager.addOnPageChangeListener(this);

    tvPosition = (TextView) findViewById(R.id.tv_image_index);
    tvPosition.setText(position + 1 + "/" + images.size());


    findViewById(R.id.btn_save_images).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        int position = viewPager.getCurrentItem();
        Image image = images.get(position);
        Glide.with(view.getContext())
                .load(image.path)
                .asBitmap()
                .toBytes()
                .into(new SimpleTarget<byte[]>() {
                  @Override
                  public void onResourceReady(byte[] bytes, GlideAnimation<? super byte[]> glideAnimation) {
                    try {
                      if (checkWritePermission()) {
                        saveImage(bytes);
                      }
                    } catch (Exception e) {
                      e.printStackTrace();
                    }
                  }
                });

      }
    });
  }

  /**
   * 检查写入权限
   */
  private boolean checkWritePermission() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
      return false;
    } else {
      return true;
    }
  }

  public void saveImage(byte[] bytes) {
    try {
      File file = createFile();
      OutputStream out = new FileOutputStream(file);
      out.write(bytes);
      out.flush();
      out.close();
      saveImageSuccess(file);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private void saveImageSuccess(final File file) {
    Snackbar.make(viewPager, getString(R.string.save_image_success) + ":" + file.getPath(), Snackbar.LENGTH_LONG)
            .setAction(getString(R.string.image_show), new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(Uri.fromFile(file), "*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivity(intent);
              }
            }).show();
    //发送更新相册广播
    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
  }

  private File createFile() {
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    File file = new File(Environment.getExternalStorageDirectory(), "/images/" + timeStamp + ".jpg");
    if (!file.getParentFile().exists()) {
      file.getParentFile().mkdirs();
    }
    return file;
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    setResult(RESULT_CANCELED);
    finish();
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

  }

  @Override
  public void onPageSelected(int position) {
    String content = (position + 1) + "/" + images.size();
    tvPosition.setText(content);
  }

  @Override
  public void onPageScrollStateChanged(int state) {

  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == 100 && permissions.length > 0 && permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

    }
  }
}
