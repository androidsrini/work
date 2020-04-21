package com.codesense.driverapp.ui.imagepicker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.codesense.driverapp.R;

import com.codesense.driverapp.ui.imagepicker.ImagePickerActivity;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<ImagePickerActivity.ImagePickerInfo> imagePickerInfos;

    public ImageAdapter(List<ImagePickerActivity.ImagePickerInfo> imagePickerInfos) {
        this.imagePickerInfos = imagePickerInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImagePickerActivity.ImagePickerInfo imagePickerInfo = imagePickerInfos.get(position);
        holder.imageView.setImageBitmap(imagePickerInfo.getThumbnails());
    }

    @Override
    public int getItemCount() {
        return imagePickerInfos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.thumbImage);
        }
    }


/*
    public int getCount() {
        return count;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.gallery_item, null);
            holder.imageview = (ImageView) convertView
                    .findViewById(R.id.thumbImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.imageview.setId(position);
        *//*holder.imageview.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                int id = v.getId();
                Intent intent = new Intent(MainActivity.this,
                        lastscreen.class);
                intent.setDataAndType(Uri.parse("file://" + arrPath[id]),
                        "image/*");
                intent.putExtra("path", arrPath[id]);
                startActivity(intent);
            }
        });*//*
        holder.imageview.setImageBitmap(thumbnails[position]);
        return convertView;
    }

    class ViewHolder {
        ImageView imageview;
    }*/
}




