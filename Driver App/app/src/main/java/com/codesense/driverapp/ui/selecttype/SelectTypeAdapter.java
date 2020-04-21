package com.codesense.driverapp.ui.selecttype;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.codesense.driverapp.R;
import com.codesense.driverapp.data.OwnerTypesItem;

import java.util.List;

public class SelectTypeAdapter extends RecyclerView.Adapter<SelectTypeAdapter.SelectTypeViewHolder> {

    private List<OwnerTypesItem> ownerTypesItems;
    private OnSelectedTypeClick onSelectedTypeClick;

    public SelectTypeAdapter(List<OwnerTypesItem> ownerTypesItems, OnSelectedTypeClick onSelectedTypeClick) {
        this.ownerTypesItems = ownerTypesItems;
        this.onSelectedTypeClick = onSelectedTypeClick;
    }

    @NonNull
    @Override
    public SelectTypeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SelectTypeViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.select_type_screen, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SelectTypeViewHolder selectTypeViewHolder, int i) {
        OwnerTypesItem ownerTypesItem = ownerTypesItems.get(i);
        selectTypeViewHolder.tvDriverText.setText(ownerTypesItem.getOwnerType());
        selectTypeViewHolder.tvDriverDesc.setText(ownerTypesItem.getDescription());
    }

    @Override
    public int getItemCount() {
        return ownerTypesItems.size();
    }

    class SelectTypeViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDriverDesc, tvDriverText;

        public SelectTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDriverDesc = itemView.findViewById(R.id.tvDriverDesc);
            tvDriverText = itemView.findViewById(R.id.tvDriverText);
            itemView.setOnClickListener((v)->{
                if (null != onSelectedTypeClick)
                    onSelectedTypeClick.onTypeSelected(ownerTypesItems.get(getAdapterPosition()));
            });
        }
    }

    public interface OnSelectedTypeClick {
        void onTypeSelected(OwnerTypesItem ownerTypesItem);
    }
}
