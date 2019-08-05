package com.codesense.driverapp.ui.documentstatus;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.data.DocumentsListItem;
import com.codesense.driverapp.ui.uploaddocument.UploadDocumentModel;

import java.util.List;

public class DocumentStatusAdapter extends RecyclerView.Adapter<DocumentStatusAdapter.ViewHolder> {


    private List<DocumentsListItem> uploadDocumentModelList;
    private Activity activity;
    private int width;
    private int height;

    public DocumentStatusAdapter(Activity activity, List<DocumentsListItem> uploadDocumentModelList,
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
                .inflate(R.layout.document_status_item, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        DocumentsListItem uploadDocumentModel = uploadDocumentModelList.get(position);
        String status = uploadDocumentModel.getDocumentStatus().getStatusMessage();
        String title = uploadDocumentModel.getDisplayName();

        if (status.equalsIgnoreCase("completed")) {
            setCompleted(viewHolder);
        } else {
            setPending(viewHolder);
        }

        viewHolder.tvVehicleStatusText.setText(status);
        viewHolder.tvVehicledesc.setText(title);
    }

    @Override
    public int getItemCount() {
        return uploadDocumentModelList.size();
    }

    private void setCompleted(ViewHolder viewHolder) {
        int topBottomSpace = (int) (height * 0.0089);

        int imgIconWidth = (int) (width * 0.085);
        int imgIconHeight = (int) (width * 0.085);
        RelativeLayout.LayoutParams imgLayParams = (RelativeLayout.LayoutParams) viewHolder.imgRightArrow.getLayoutParams();
        imgLayParams.width = imgIconWidth;
        imgLayParams.height = imgIconHeight;
        imgLayParams.setMargins(0, topBottomSpace * 3, topBottomSpace * 3, 0);
        viewHolder.imgRightArrow.setLayoutParams(imgLayParams);
        viewHolder.imgRightArrow.setPadding(0, topBottomSpace * 3, topBottomSpace * 3, 0);


        viewHolder.rlDriverMain.setBackgroundColor(activity.getResources().getColor(R.color.background_color));
        viewHolder.imgRightArrow.setBackgroundResource(R.drawable.tick_bg_icon);

    }

    private void setPending(ViewHolder viewHolder) {
        int topBottomSpace = (int) (height * 0.0089);
        int imgIconWidth = (int) (width * 0.085);
        int imgIconHeight = (int) (width * 0.085);

        RelativeLayout.LayoutParams imgLayParams = (RelativeLayout.LayoutParams) viewHolder.imgRightArrow.getLayoutParams();
        imgLayParams.width = imgIconWidth;
        imgLayParams.height = imgIconHeight;
        imgLayParams.setMargins(0, topBottomSpace * 3, topBottomSpace * 3, 0);
        viewHolder.imgRightArrow.setLayoutParams(imgLayParams);
        viewHolder.imgRightArrow.setPadding(0, topBottomSpace * 3, topBottomSpace * 3, 0);


        viewHolder.rlDriverMain.setBackgroundColor(activity.getResources().getColor(R.color.background_document_status));
        viewHolder.imgRightArrow.setBackgroundResource(R.drawable.right_only_bg);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rlDriverVehicle, rlDriverMain;
        private ImageView imgRightArrow;
        private TextView tvVehicleStatusText, tvVehicledesc;

        public ViewHolder(View view) {
            super(view);

            rlDriverVehicle = view.findViewById(R.id.rlDriverVehicle);
            rlDriverMain = view.findViewById(R.id.rlDriverMain);
            tvVehicleStatusText = view.findViewById(R.id.tvVehicleStatusText);
            tvVehicledesc = view.findViewById(R.id.tvVehicledesc);
            imgRightArrow = view.findViewById(R.id.imgRightArrow);


            int topBottomSpace = (int) (height * 0.0089);


            int imgIconWidth = (int) (width * 0.085);
            int imgIconHeight = (int) (width * 0.085);

            RelativeLayout.LayoutParams imgLayParams = (RelativeLayout.LayoutParams) imgRightArrow.getLayoutParams();
            imgLayParams.width = imgIconWidth;
            imgLayParams.height = imgIconHeight;
            imgLayParams.setMargins(0, topBottomSpace * 3, topBottomSpace * 3, 0);
            imgRightArrow.setLayoutParams(imgLayParams);

            ConstraintLayout.LayoutParams rlDriverLicenseVehicleComLayoutParams = (ConstraintLayout.LayoutParams) rlDriverMain.getLayoutParams();
            rlDriverLicenseVehicleComLayoutParams.setMargins(0, topBottomSpace * 2, 0, 0);
            rlDriverMain.setLayoutParams(rlDriverLicenseVehicleComLayoutParams);

            RelativeLayout.LayoutParams rlLegalLayoutParams = (RelativeLayout.LayoutParams) rlDriverVehicle.getLayoutParams();
            rlLegalLayoutParams.setMargins(topBottomSpace * 2, topBottomSpace, topBottomSpace * 2, topBottomSpace);
            rlDriverVehicle.setLayoutParams(rlLegalLayoutParams);

//            RelativeLayout.LayoutParams rlDriveLicenseLayoutParams = (RelativeLayout.LayoutParams) rlDriveLicense.getLayoutParams();
//            rlDriveLicenseLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace * 3, topBottomSpace * 3, 0);
//            rlDriveLicense.setLayoutParams(rlDriveLicenseLayoutParams);

            tvVehicleStatusText.setPadding(topBottomSpace * 3, topBottomSpace, 0, 0);
            tvVehicledesc.setPadding(topBottomSpace * 3, topBottomSpace * 2, 0, topBottomSpace * 3);
            imgRightArrow.setPadding(0, topBottomSpace * 3, topBottomSpace * 3, 0);

        }
    }
}

