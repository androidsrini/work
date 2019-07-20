package com.codesense.driverapp.ui.vehicle;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;

import java.util.List;

public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.ViewHolder> {


    private List<VehicleListModel> vehicleListModelList;
    private Activity activity;
    private int width;
    private int height;

    public VehicleListAdapter(Activity activity, List<VehicleListModel> vehicleListModelList,
                              int w, int h) {
        this.activity = activity;
        this.width = w;
        this.height = h;
        this.vehicleListModelList = vehicleListModelList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.vehicle_list_item, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
//        VehicleListModel vehicleListModel = vehicleListModelList.get(position);
//        String status = vehicleListModel.getVehicleStatus();
//        String name = vehicleListModel.getVehicleName();
//        String vehicleNum = vehicleListModel.getVehicleNo();
//
//
//        viewHolder.tvStatusText.setText(status);
//        viewHolder.tvVehicleName.setText(name);
//        viewHolder.tvVehicleNum.setText(vehicleNum);
    }

    @Override
    public int getItemCount() {
        return 1;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rlUserVehicle, rlUserVehcleMain;
        private ImageView imgRightArrow, imgVehicle, imgPerson;
        private TextView tvStatusText, tvVehicleNum, tvVehicleName;

        public ViewHolder(View view) {
            super(view);

            rlUserVehicle = view.findViewById(R.id.rlUserVehicle);
            rlUserVehcleMain = view.findViewById(R.id.rlUserVehcleMain);
            tvStatusText = view.findViewById(R.id.tvStatusText);
            tvVehicleNum = view.findViewById(R.id.tvVehicleNum);
            tvVehicleName = view.findViewById(R.id.tvVehicleName);
            imgRightArrow = view.findViewById(R.id.imgRightArrow);
            imgVehicle = view.findViewById(R.id.imgVehicle);
            imgPerson = view.findViewById(R.id.imgPerson);


            int topBottomSpace = (int) (height * 0.0089);


            int imgIconWidth = (int) (width * 0.085);
            int imgIconHeight = (int) (width * 0.085);

            int vehicleimgIconWidth = (int) (width * 0.085);
            int vehicleimgIconHeight = (int) (width * 0.085);
            RelativeLayout.LayoutParams imgLayParams = (RelativeLayout.LayoutParams) imgRightArrow.getLayoutParams();
            imgLayParams.width = imgIconWidth;
            imgLayParams.height = imgIconHeight;
            imgRightArrow.setLayoutParams(imgLayParams);

            RelativeLayout.LayoutParams imgVehicleimgLayParams = (RelativeLayout.LayoutParams) imgVehicle.getLayoutParams();
            imgVehicleimgLayParams.width = vehicleimgIconWidth;
            imgVehicleimgLayParams.height = vehicleimgIconHeight;
            imgVehicleimgLayParams.setMargins(topBottomSpace, topBottomSpace * 2, 0, 0);
            imgVehicle.setLayoutParams(imgVehicleimgLayParams);

            RelativeLayout.LayoutParams imgPersonimgLayParams = (RelativeLayout.LayoutParams) imgPerson.getLayoutParams();
            imgPersonimgLayParams.width = vehicleimgIconWidth;
            imgPersonimgLayParams.height = vehicleimgIconHeight;
            imgPersonimgLayParams.setMargins(topBottomSpace, topBottomSpace * 2, 0, topBottomSpace * 2);
            imgPerson.setLayoutParams(imgPersonimgLayParams);

            RelativeLayout.LayoutParams rlDriverLicenseVehicleComLayoutParams = (RelativeLayout.LayoutParams) rlUserVehcleMain.getLayoutParams();
            rlDriverLicenseVehicleComLayoutParams.setMargins(0, topBottomSpace * 2, 0, 0);
            rlUserVehcleMain.setLayoutParams(rlDriverLicenseVehicleComLayoutParams);

            RelativeLayout.LayoutParams rlLegalLayoutParams = (RelativeLayout.LayoutParams) rlUserVehicle.getLayoutParams();
            rlLegalLayoutParams.setMargins(topBottomSpace * 2, topBottomSpace, topBottomSpace * 2, topBottomSpace);
            rlUserVehicle.setLayoutParams(rlLegalLayoutParams);

            tvStatusText.setPadding(topBottomSpace * 3, topBottomSpace, 0, 0);
            tvVehicleNum.setPadding(topBottomSpace * 2, topBottomSpace * 2, 0, 0);
            tvVehicleName.setPadding(topBottomSpace * 2, topBottomSpace, 0, topBottomSpace * 3);
            imgRightArrow.setPadding(0, topBottomSpace * 2, topBottomSpace * 2, 0);


        }
    }
}

