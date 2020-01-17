package com.codesense.driverapp.ui.adddriver;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UploadDocumentArrayAdapter<DocumentsItem> extends ArrayAdapter {

    List<DocumentsItem> documentsItemList;
    private int width;
    private int height;
    private Context context;

    public UploadDocumentArrayAdapter(@NonNull @android.support.annotation.NonNull Context context, int w, int h, List<DocumentsItem> documentsItems) {
        super(context, 0);
        this.context = context;
        this.documentsItemList = documentsItems;
        this.width = w;
        this.height = h;
    }

    @Nullable
    @android.support.annotation.Nullable
    @Override
    public DocumentsItem getItem(int position) {
        return documentsItemList.get(position);
    }

    @Override
    public int getCount() {
        return documentsItemList.size();
    }

    @NonNull
    @android.support.annotation.NonNull
    @Override
    public View getView(int position, @Nullable @android.support.annotation.Nullable View convertView, @NonNull @android.support.annotation.NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    /**
     * This method to return selected file list count
     * @return int
     */
    /*public int getSelectedFilesCount() {
        int  count = 0;
        for (DocumentsItem documentsListItem: documentsItemList) {
            if (!TextUtils.isEmpty(documentsListItem.getFilePath())) {
                count ++;
            }
        }
        return count;
    }*/

    public class ViewHolder {

        private RelativeLayout rlDriveLicense;
        private ImageView imgRightArrow;
        private TextView tvDriverText, tvDriverdesc ,documentFileNameTextView;
        //int position;

        public ViewHolder(View view) {

            rlDriveLicense = view.findViewById(R.id.rlDriveLicense);
            tvDriverText = view.findViewById(R.id.tvDriverText);
            tvDriverdesc = view.findViewById(R.id.tvDriverdesc);
            imgRightArrow = view.findViewById(R.id.imgRightArrow);
            documentFileNameTextView = view.findViewById(R.id.documentFileNameTextView);

            int topBottomSpace = (int) (height * 0.0089);
            int imgIconWidth = (int) (width * 0.085);
            int imgIconHeight = (int) (width * 0.085);

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
