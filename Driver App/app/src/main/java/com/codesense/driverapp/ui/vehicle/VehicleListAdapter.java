package com.codesense.driverapp.ui.vehicle;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.data.VehiclesListItem;

import java.util.List;

public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.ViewHolder> {


    private List<VehiclesListItem> vehicleListModelList;
    private Activity activity;
    private int width;
    private int height;
    private OnItemActionListener onItemActionListener;


    public VehicleListAdapter(Activity activity, List<VehiclesListItem> vehicleListModelList,
                              int w, int h,OnItemActionListener listener) {
        this.activity = activity;
        this.width = w;
        this.height = h;
        this.vehicleListModelList = vehicleListModelList;
        this.onItemActionListener = listener;

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
        VehiclesListItem vehiclesListItem = vehicleListModelList.get(position);
        viewHolder.tvVehicleNum.setText(vehiclesListItem.getVehicleNumber());
        if (TextUtils.isEmpty(vehiclesListItem.getDriverFirstName()) && TextUtils.isEmpty(vehiclesListItem.getDriverLastName())) {
            viewHolder.tvVehicleName.setText(activity.getString(R.string.not_mapped_status));
        } else {
            //Need to update owner name.
            viewHolder.tvVehicleName.setText(vehiclesListItem.getDriverFirstName().concat(vehiclesListItem.getDriverLastName()));
        }

        if (vehiclesListItem.getVerificationStatus().equals("0")){
           viewHolder.rlUserVehcleMain.setBackgroundResource(R.color.background_document_status);
        }else{
            viewHolder.rlUserVehcleMain.setBackgroundResource(R.color.background_document_status_enable);
        }
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
        return vehicleListModelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rlUserVehicle, rlUserVehcleMain;
        private ImageView imgRightArrow, imgVehicle, imgPerson;
        private TextView tvStatusText, tvVehicleNum, tvVehicleName;
        private SwitchCompat activeSwitchCompat;

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
            activeSwitchCompat = view.findViewById(R.id.active_switchCompat);

            int topBottomSpace = (int) (height * 0.0089);


            int imgIconWidth = (int) (width * 0.115);
            int imgIconHeight = (int) (width * 0.115);

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

            activeSwitchCompat.setVisibility(View.GONE);

            imgRightArrow.setOnClickListener((v)->{
                if (null != onItemActionListener && -1 != getAdapterPosition()) {
                    onItemActionListener.onEditActionClick(getAdapterPosition());
                }
            });

            itemView.setOnClickListener((v)->{
                if (null != onItemActionListener && -1 != getAdapterPosition()) {
                    onItemActionListener.onViewClick(getAdapterPosition());
                }
            });

            activeSwitchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (-1 != getAdapterPosition()) {
                    onItemActionListener.onButtonClick(getAdapterPosition(), isChecked);
                }
            });

        }
    }

    public interface OnItemActionListener {
        void onViewClick(int position);
        void onEditActionClick(int position);
        void onButtonClick(int position, boolean isChecked);
    }
}

