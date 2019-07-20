package com.codesense.driverapp.ui.drawer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;

import java.io.File;
import java.util.ArrayList;

public class NavDrawerListAdapter extends BaseAdapter {

    private static final String TAG = "paytronix";
    private static final String DRAWABLE = "drawable";
    public Context context;
    public ArrayList<NavDrawerItem> navDrawerItems;
    public String navDrawerLabel;

    TypedArray typedArray = null;
    Boolean file = false;
    String MenuItem;
    int details, drawerItemPosition;
    ArrayList<String> selectedMenuList;
    private boolean doesOrderTakOutMenuEnable = false;
    private boolean isDesignOneEnabled = false;
    private int menuWidth, menuHeight, slideMenuLeftRightSpace;
    private static final int OLO_HEADER_TEXT_SIZE = 16;
    Boolean isSignedIn = false;
    boolean isSideDrawerSectionFontChanged = false;

    private String retrieveIconNameFromPath(String path) {
        String[] filePath = path.split(File.separator);
        int length = filePath.length;
        String fullName = (length > 1 ? filePath[length - 1] : null);
        String[] nameArray = !TextUtils.isEmpty(fullName) ? fullName.split("\\.") : new String[]{};
        int nameLength = nameArray.length;
        return (nameLength > 1 ? nameArray[0] : "");
    }

    public NavDrawerListAdapter(Context context, int width, ArrayList<NavDrawerItem> navDrawerItems, ArrayList<String> selectedMenuList) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
        this.selectedMenuList = selectedMenuList;
        slideMenuLeftRightSpace = (int) (width * 0.037); //0.3
        menuWidth = (int) (width * 0.0741); //0.5
        menuHeight = menuWidth;

    }


    @Override
    public int getCount() {
        return selectedMenuList.size();
    }

    @Override
    public Object getItem(int position) {
        return selectedMenuList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
            viewHolder.txtTitle = convertView.findViewById(android.R.id.text1);
            viewHolder.drawerMenuIconImageView = (ImageView) convertView.findViewById(R.id.drawerMenuIconImageView);
            viewHolder.drawerMenuItemContainerRelativeLayout = convertView.findViewById(R.id.drawerMenuItemContainerRelativeLayout);


            viewHolder.drawerMenuItemContainerRelativeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.background_color));
            viewHolder.txtTitle.setTextColor(ContextCompat.getColor(context, R.color.secondary_color));


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RelativeLayout.LayoutParams menuIconParam = (RelativeLayout.LayoutParams) viewHolder.drawerMenuIconImageView.getLayoutParams();
        menuIconParam.width = menuWidth;
        menuIconParam.height = menuHeight;
        menuIconParam.setMargins(slideMenuLeftRightSpace, 0, 0, 0);
        viewHolder.drawerMenuIconImageView.setLayoutParams(menuIconParam);

        viewHolder.txtTitle.setText("");
        Resources res = convertView.getResources();
        drawerItemPosition = position + 1;
        try {
            //String str = "drawermenuitem_" + drawerItemPosition;
            String str = selectedMenuList.get(position);
            details = res.getIdentifier(str, "array", convertView.getContext().getPackageName());
            file = true;

        } catch (Resources.NotFoundException e) {
            file = false;
        }
        try {
            if (file) {
                typedArray = res.obtainTypedArray(details);
                MenuItem = "";
                updateTitleAndIconUI(position, viewHolder, typedArray);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e("Array is out of Bounds", "ArrayIndexOutOfBound" + e);
        }
        typedArray.recycle();
        return convertView;
    }

    @SuppressLint("ResourceType")
    private void updateTitleAndIconUI(int position, ViewHolder viewHolder, TypedArray typedArray) {
        if (typedArray != null && typedArray.length() > 0) {

            NavDrawerItem navDrawerItem = navDrawerItems.get(position);
            viewHolder.txtTitle.setText(navDrawerItem.getTitle());

            if (!TextUtils.isEmpty(navDrawerItem.getIconName())) {
                int drawableID = context.getResources().getIdentifier(retrieveIconNameFromPath(navDrawerItem.getIconName()), DRAWABLE, context.getPackageName());
                //Log.d(TAG, "Menu icon name: " + retrieveIconNameFromPath(navDrawerItem.getIconName()) + " ,uri not null: " + drawableID);
                Drawable drawable = viewHolder.drawerMenuIconImageView.getDrawable();
                if (drawable instanceof BitmapDrawable) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    if (bitmap != null && !bitmap.isRecycled())
                        bitmap.recycle();
                }

                viewHolder.drawerMenuIconImageView.setBackgroundResource(drawableID);

            }

        }


        /*for (int k = 0; k < typedArray.length(); k++) {
            MenuItem = typedArray.getString(0);
            if (k == 1) {
                if (MenuItem.equals("section")) {
                    viewHolder.txtTitle1.setText(typedArray.getString(k));
                } else {
                    NavDrawerItem navDrawerItem = navDrawerItems.get(position);
                    if (doesOrderTakOutMenuEnable && isSignedIn) {
                        if (!TextUtils.isEmpty(navDrawerItem.getIconName())) {
                            int drawableID = context.getResources().getIdentifier(retrieveIconNameFromPath(navDrawerItem.getIconName()), DRAWABLE, context.getPackageName());
                            //Log.d(TAG, "Menu icon name: " + retrieveIconNameFromPath(navDrawerItem.getIconName()) + " ,uri not null: " + drawableID);
                            Drawable drawable = viewHolder.drawerMenuIconImageView.getDrawable();
                            if (drawable instanceof BitmapDrawable) {
                                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                                Bitmap bitmap = bitmapDrawable.getBitmap();
                                if (bitmap != null && !bitmap.isRecycled())
                                    bitmap.recycle();
                            }
                            viewHolder.drawerMenuIconImageView.setBackgroundResource(drawableID);
                        } else {
                            viewHolder.drawerMenuIconImageView.setBackground(null);
                        }
                    }
                    viewHolder.txtTitle.setText(navDrawerItem.getTitle());
                }
                break;
            }
        }*/


    }


    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return super.hasStableIds();
    }

    @Override
    public boolean areAllItemsEnabled() {
        // TODO Auto-generated method stub
        return super.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        // TODO Auto-generated method stub
        return super.isEnabled(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView txtTitle;
        ImageView drawerMenuIconImageView;
        RelativeLayout drawerMenuItemContainerRelativeLayout;
    }
}
