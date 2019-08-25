package com.codesense.driverapp.ui.imagepicker;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.GridView;

import com.codesense.driverapp.base.BaseActivity;
import com.codesense.driverapp.ui.imagepicker.adapter.ImageAdapter;
import com.google.android.gms.cast.framework.media.ImagePicker;
import com.product.process.annotation.Initialize;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager;
import com.codesense.driverapp.R;
import com.product.annotationbuilder.ProductBindView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class ImagePickerActivity extends BaseActivity {

    private static final String TAG = "Driver";
    @Initialize(R.id.imagePickerRecyclerView)
    RecyclerView imagePickerRecyclerView;

    private ImageAdapter imageAdapter;
    private List<ImagePickerInfo> imagePickerInfos;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private List<ImagePickerInfo> loadAllLocalImage() {
        List<ImagePickerInfo> filePath = new ArrayList<>();
        final String[] columns = { MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media._ID;
        Cursor imagecursor = this.managedQuery(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
        null, orderBy);
        int image_column_index = imagecursor
                .getColumnIndex(MediaStore.Images.Media._ID);
        for (int i = 0; i < imagecursor.getCount(); i++) {
            imagecursor.moveToPosition(i);
            int id = imagecursor.getInt(image_column_index);
            int dataColumnIndex = imagecursor
                    .getColumnIndex(MediaStore.Images.Media.DATA);
            ImagePickerInfo imagePickerInfo = new ImagePickerInfo(imagecursor.getString(dataColumnIndex),
                    MediaStore.Images.Thumbnails.getThumbnail(
                            getApplicationContext().getContentResolver(), id,
                            MediaStore.Images.Thumbnails.MINI_KIND, null));
            filePath.add(imagePickerInfo);
        }
        Log.d(TAG, " Collected image size: " + filePath.size());
        return filePath;
    }

    /*private void updateImagePickerGalleryUI() {
        compositeDisposable.add()
    }*/

    @Override
    protected int layoutRes() {
        return R.layout.activity_image_picker;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProductBindView.bind(this);
        imagePickerInfos = new ArrayList<>();
        imagePickerInfos.addAll(loadAllLocalImage());
        imageAdapter = new ImageAdapter(imagePickerInfos);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        imagePickerRecyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        imagePickerRecyclerView.setAdapter(imageAdapter);
    }

    public class ImagePickerInfo {
        String filePath;
        Bitmap thumbnails;

        public ImagePickerInfo(String filePath, Bitmap thumbnails) {
            this.filePath = filePath;
            this.thumbnails = thumbnails;
        }

        public String getFilePath() {
            return filePath;
        }

        public Bitmap getThumbnails() {
            return thumbnails;
        }
    }
}
