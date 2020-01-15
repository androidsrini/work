package com.codesense.driverapp.di.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.data.AvailableVehiclesItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AppSpinnerViewGroup<T> extends FrameLayout {

    RelativeLayout selectVehicleRelativeLayout;
    TextView selectVehicleTextView;
    ImageView selectVehicleArrowImageView;
    View selectVehicleDivider;
    private int screenWidth;
    private int screenHeight;
    private int mSelectedVehicle;
    private boolean vehicleSelectionFirstTime = true;
    private ArrayList<T> arrayList;

    public AppSpinnerViewGroup(@NonNull @android.support.annotation.NonNull Context context) {
        super(context);
        init();
    }

    public AppSpinnerViewGroup(@NonNull @android.support.annotation.NonNull Context context, @Nullable @android.support.annotation.Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AppSpinnerViewGroup(@NonNull @android.support.annotation.NonNull Context context, @Nullable @android.support.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        arrayList = new ArrayList<>();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.app_spinner_viewgroup, this, true);
        selectVehicleRelativeLayout = view.findViewById(R.id.selectVehicleRelativeLayout);
        selectVehicleTextView = view.findViewById(R.id.selectVehicleTextView);
        selectVehicleArrowImageView = view.findViewById(R.id.selectVehicleArrowImageView);
        selectVehicleDivider = view.findViewById(R.id.selectVehicleDivider);
        resizeViews();
        selectVehicleRelativeLayout.setOnClickListener(this::showAvailableVehiclePopupScreen);
    }

    private void resizeViews() {
        calculateScreenSize();

        int topBottomSpace = (int) (screenHeight * 0.0089);
        int imgIconArrowWidth = (int) (screenWidth * 0.055);
        int imgIconArrowHeight = (int) (screenWidth * 0.055);
        //Resize view
        /*RelativeLayout.LayoutParams selectVehicleRelativeLayoutParams = (RelativeLayout.LayoutParams) selectVehicleRelativeLayout.getLayoutParams();
        selectVehicleRelativeLayoutParams.setMargins(topBottomSpace * 5, topBottomSpace * 3, topBottomSpace * 5, 0);
        selectVehicleRelativeLayout.setLayoutParams(selectVehicleRelativeLayoutParams);*/

        RelativeLayout.LayoutParams selectVehicleArrowImageViewParams = (RelativeLayout.LayoutParams) selectVehicleArrowImageView.getLayoutParams();
        selectVehicleArrowImageViewParams.width = imgIconArrowWidth;
        selectVehicleArrowImageViewParams.height = imgIconArrowHeight;
        selectVehicleArrowImageView.setLayoutParams(selectVehicleArrowImageViewParams);

        /*RelativeLayout.LayoutParams selectVehicleDividerParams = (RelativeLayout.LayoutParams) selectVehicleDivider.getLayoutParams();
        selectVehicleDividerParams.setMargins(topBottomSpace * 5, 0, topBottomSpace * 5, 0);
        selectVehicleDivider.setLayoutParams(selectVehicleDividerParams);*/
    }

    private void calculateScreenSize() {
        if (0 == screenWidth || 0 == screenHeight) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowmanager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
            screenWidth = displayMetrics.widthPixels;
            screenHeight = displayMetrics.heightPixels;
        }
    }

    /**
     * This method to show available vehicle list popup
     * @param view
     */
    private void showAvailableVehiclePopupScreen(View view) {
        View customView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_pop_up_screen, null);
        int leftRightSpace = (int) (screenWidth * 0.0153);
        final PopupWindow popupWindow = new PopupWindow(customView, leftRightSpace * 58, ViewGroup.LayoutParams.WRAP_CONTENT);
        final ListView listView = customView.findViewById(R.id.listItemListView);
        ArrayAdapter<T> dataAdapter = new ArrayAdapter<T>(getContext(), R.layout.spinner_dropdown_textview, arrayList) {
            @android.support.annotation.NonNull
            @Override
            public View getView(int position, View convertView,@android.support.annotation.NonNull ViewGroup parent) {
                View v = null;
                v = super.getView(position, null, parent);
                TextView tv = (TextView) v;
                if (position == mSelectedVehicle) {
                    v.setBackgroundColor(getResources().getColor(R.color.primary_color));
                    tv.setTextColor(getResources().getColor(R.color.secondary_color));
                } else {
                    v.setBackgroundColor(getResources().getColor(R.color.background_color));
                    tv.setTextColor(getResources().getColor(R.color.secondary_color));
                }
                if (vehicleSelectionFirstTime) {
                    v.setBackgroundColor(getResources().getColor(R.color.background_color));
                    tv.setTextColor(getResources().getColor(R.color.secondary_color));
                }
                if (getItem(position) instanceof AvailableVehiclesItem) {
                    AvailableVehiclesItem availableVehiclesItem = (AvailableVehiclesItem) getItem(position);
                    tv.setText(availableVehiclesItem.getVehicleName());
                }
                return v;
            }
        };
        listView.setAdapter(dataAdapter);
        listView.setSelection(mSelectedVehicle);
        listView.setOnItemClickListener((parent, view3, position, id) -> {
            popupWindow.dismiss();
            Object o = arrayList.get(position);
            if (o instanceof AvailableVehiclesItem) {
                AvailableVehiclesItem selectedState = (AvailableVehiclesItem) o;
                selectVehicleTextView.setText(selectedState.getVehicleName());
                selectVehicleTextView.setTextColor(getResources().getColor(R.color.secondary_color));
                mSelectedVehicle = position;
                vehicleSelectionFirstTime = false;
            }
            //updateSelectedVehicleUI(selectedState);
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(view);
    }

    public void updateItem(ArrayList<T> objectArrayList) {
        arrayList.clear();
        arrayList.addAll(objectArrayList);
    }

    public interface OnItemSelectListener {
        void onItemSelected(int position);
    }
}
