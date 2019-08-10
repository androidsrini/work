package com.codesense.driverapp.ui.uploaddocument;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.data.DocumentsListItem;

import java.io.File;
import java.util.List;

public class UploadDocumentAdapter extends RecyclerView.Adapter<UploadDocumentAdapter.ViewHolder> {


    private List<DocumentsListItem> uploadDocumentModelList;
    private Activity activity;
    private int width;
    private int height;

    public UploadDocumentAdapter(Activity activity, List<DocumentsListItem> uploadDocumentModelList,
                                 int w, int h) {
        this.activity = activity;
        this.width = w;
        this.height = h;
        this.uploadDocumentModelList = uploadDocumentModelList;

    }

    private String findFileName(@NonNull String filePath) {
        return new File(filePath).getName();
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

        DocumentsListItem uploadDocumentModel = uploadDocumentModelList.get(position);
        boolean isFileSelected = !TextUtils.isEmpty(uploadDocumentModel.getFilePath());
        String status = isFileSelected ? activity.getString(R.string.document_status_completed)
                :  (null != uploadDocumentModel.getDocumentStatus()) ?
                uploadDocumentModel.getDocumentStatus().getStatusMessage() : activity.getString(R.string.recommended_next_step);
        String title = uploadDocumentModel.getDisplayName();
        viewHolder.tvDriverText.setText(status);
        viewHolder.tvDriverdesc.setText(title);
        if (!isFileSelected) {
            //To show selected image UI
            viewHolder.documentFileNameTextView.setVisibility(View.GONE);
            viewHolder.imgRightArrow.setBackgroundResource(R.drawable.right_only_bg);
        } else {
            viewHolder.documentFileNameTextView.setVisibility(View.VISIBLE);
            viewHolder.documentFileNameTextView.setText(findFileName(uploadDocumentModel.getFilePath()));
            viewHolder.imgRightArrow.setBackgroundResource(R.drawable.tick_bg_icon);
            // To show unselected image and content
        }
    }

    @Override
    public int getItemCount() {
        return uploadDocumentModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rlDriveLicense;
        private ImageView imgRightArrow;
        private TextView tvDriverText, tvDriverdesc ,documentFileNameTextView;
        //int position;

        public ViewHolder(View view) {
            super(view);

            rlDriveLicense = view.findViewById(R.id.rlDriveLicense);
            tvDriverText = view.findViewById(R.id.tvDriverText);
            tvDriverdesc = view.findViewById(R.id.tvDriverdesc);
            imgRightArrow = view.findViewById(R.id.imgRightArrow);
            documentFileNameTextView = view.findViewById(R.id.documentFileNameTextView);

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
            documentFileNameTextView.setPadding(topBottomSpace * 3, 0, 0, 0);
            imgRightArrow.setPadding(0, topBottomSpace * 3, topBottomSpace * 3, 0);

        }
    }
}
