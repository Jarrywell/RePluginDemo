package com.test.android.plugin1.provider;


import com.android.test.utils.DLog;
import com.test.android.plugin1.R;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

/**
 * des:
 * author: libingyan
 * Date: 18-9-5 12:29
 */
@Keep
public class FileProviderActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = "FileProviderActivity";

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1023;
    private static final int REQUEST_TAKE_PHOTO = 1024;
    private static final String PROVIDER_AUTHORITIES = "com.android.test.host.demo.plugin1.FILE_PROVIDER";


    private Button mBtnTakePhoto;

    private ImageView mImgPhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_provider);

        mBtnTakePhoto = findViewById(R.id.id_btn_take_photo);
        mBtnTakePhoto.setOnClickListener(this);

        mImgPhoto = findViewById(R.id.id_image_photo);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnTakePhoto) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    takePhoto();
                }
            } else {
                takePhoto();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                Toast.makeText(this, "sd Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO) {
            File photoFile = getPhotoFile();
            if (photoFile != null && photoFile.exists() && photoFile.length() > 0) {
                Bitmap bmp = decodeSampledBitmapFromFile(photoFile.getAbsolutePath(), 500, 500);
                if (bmp != null) {
                    mImgPhoto.setImageBitmap(bmp);
                } else {
                    Toast.makeText(this, "Get Photo Fail.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Get Photo Fail.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this, PROVIDER_AUTHORITIES, getPhotoFile());
        } else {
            uri = Uri.fromFile(getPhotoFile());
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private File getPhotoFile() {
        File result = null;
        final String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            result = new File(Environment.getExternalStorageDirectory(), "plugin1-photo.jpg");
            try {
                result.createNewFile();
            } catch (Exception e) {
                DLog.i(TAG, "getPhotoFile() exception!", e);
                result = null;
            }
        }
        return result;
    }

    /**
     * 从SD卡上加载图片，得到合适的Bitmap
     *
     * @param pathName
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }

    /**
     * 计算图片的压缩比率
     *
     * @param options   BitmapFactory.Options参数
     * @param reqWidth  需要的宽度
     * @param reqHeight 需要的高度
     * @return
     */
    private static int calculateInSampleSize(
        BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
