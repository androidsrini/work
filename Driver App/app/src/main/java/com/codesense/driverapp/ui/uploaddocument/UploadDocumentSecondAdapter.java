package com.codesense.driverapp.ui.uploaddocument;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;

import java.util.List;

public class UploadDocumentSecondAdapter extends RecyclerView.Adapter<UploadDocumentSecondAdapter.ViewHolder> {


    private List<UploadDocumentModel> uploadDocumentModelList;
    private Activity activity;
    private int width;
    private int height;

    public UploadDocumentSecondAdapter(Activity activity, List<UploadDocumentModel> uploadDocumentModelList,
                                       int w, int h) {
        this.activity = activity;
        this.width = w;
        this.height = h;
        this.uploadDocumentModelList = uploadDocumentModelList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_view_item, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        UploadDocumentModel uploadDocumentModel = uploadDocumentModelList.get(position);
        String status = uploadDocumentModel.getStatus();
        String title = uploadDocumentModel.getTitle();

        viewHolder.tvDriverText.setText(status);
        viewHolder.tvDriverdesc.setText(title);
    }

    @Override
    public int getItemCount() {
        return uploadDocumentModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rlDriveLicense;
        private ImageView imgRightArrow;
        private TextView tvDriverText, tvDriverdesc;
        //int position;

        public ViewHolder(View view) {
            super(view);

            rlDriveLicense = view.findViewById(R.id.rlDriveLicense);
            tvDriverText = view.findViewById(R.id.tvDriverText);
            tvDriverdesc = view.findViewById(R.id.tvDriverdesc);
            imgRightArrow = view.findViewById(R.id.imgRightArrow);


            int topBottomSpace = (int) (height * 0.0089);


            int imgIconWidth = (int) (width * 0.105);
            int imgIconHeight = (int) (width * 0.105);

            RelativeLayout.LayoutParams imgLayParams = (RelativeLayout.LayoutParams) imgRightArrow.getLayoutParams();
            imgLayParams.width = imgIconWidth;
            imgLayParams.height = imgIconHeight;
            imgRightArrow.setLayoutParams(imgLayParams);

            RelativeLayout.LayoutParams rlDriveLicenseLayoutParams = (RelativeLayout.LayoutParams) rlDriveLicense.getLayoutParams();
            rlDriveLicenseLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace * 3, topBottomSpace * 3, 0);
            rlDriveLicense.setLayoutParams(rlDriveLicenseLayoutParams);

            tvDriverText.setPadding(topBottomSpace * 3, topBottomSpace, 0, 0);
            tvDriverdesc.setPadding(topBottomSpace * 3, topBottomSpace * 2, 0, topBottomSpace * 3);
            imgRightArrow.setPadding(0, topBottomSpace * 3, topBottomSpace * 3, 0);

        }
    }
}

