package com.codesense.driverapp.ui.selecttype;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.base.BaseActivity;
import com.codesense.driverapp.data.OwnerTypeResponse;
import com.codesense.driverapp.data.OwnerTypesItem;
import com.codesense.driverapp.di.utils.ApiUtility;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.net.ApiResponse;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.net.ServiceType;
import com.codesense.driverapp.ui.legalagreement.LegalAgreementActivity;
import com.google.gson.Gson;
import com.product.annotationbuilder.ProductBindView;
import com.product.process.annotation.Initialize;
import com.product.process.annotation.Onclick;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SelectTypeActivity extends BaseActivity {

    /**
     * To created RequestHandler object.
     */
    @Inject
    protected RequestHandler requestHandler;
    /**
     * To created Utility object.
     */
    @Inject
    protected Utility utility;
    @Initialize(R.id.tvTitle)
    TextView tvTitle;
    @Initialize(R.id.tvSelectTypeDesc)
    TextView tvSelectTypeDesc;
    @Initialize(R.id.toolbarClose)
    ImageView toolbarClose;
    @Initialize(R.id.driverTypeRecyclerView)
    RecyclerView driverTypeRecyclerView;
    private List<OwnerTypesItem> ownerTypesItems;
    private SelectTypeAdapter selectTypeAdapter;
    /**
     * To created compositeDisposable object
     */
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    /**
     * This method to create instance based on context.
     *
     * @param context - To show UI.
     */
    public static void start(Context context) {
        context.startActivity(new Intent(context, SelectTypeActivity.class));
    }


    @Override
    protected int layoutRes() {
        return R.layout.activity_select_type;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProductBindView.bind(this);
        ownerTypesItems = new ArrayList<>();
        tvTitle.setText(getResources().getText(R.string.select_type_title));
        selectTypeAdapter = new SelectTypeAdapter(ownerTypesItems, this::handleOnSelectedTypeSelection);
        driverTypeRecyclerView.setAdapter(selectTypeAdapter);
        fetchOwnerTypeRequest();
        setDynamicValue();
    }


    private void setDynamicValue() {

        int imgIconWidth = (int) (screenWidth * 0.075);
        int imgIconHeight = (int) (screenWidth * 0.075);

        RelativeLayout.LayoutParams imgLayParams = (RelativeLayout.LayoutParams) toolbarClose.getLayoutParams();
        imgLayParams.width = imgIconWidth;
        imgLayParams.height = imgIconHeight;
        toolbarClose.setLayoutParams(imgLayParams);

        int topBottomSpace = (int) (screenHeight * 0.0089);

        LinearLayout.LayoutParams tvRegisterDesLayoutParams = (LinearLayout.LayoutParams) tvSelectTypeDesc.getLayoutParams();
        tvRegisterDesLayoutParams.setMargins(topBottomSpace * 2, topBottomSpace * 3, topBottomSpace * 2, 0);
        tvSelectTypeDesc.setLayoutParams(tvRegisterDesLayoutParams);

        LinearLayout.LayoutParams driverTypeRecyclerViewLayoutParams = (LinearLayout.LayoutParams) driverTypeRecyclerView.getLayoutParams();
        driverTypeRecyclerViewLayoutParams.setMargins(topBottomSpace * 3, topBottomSpace * 5, topBottomSpace * 3, 0);
        driverTypeRecyclerView.setLayoutParams(driverTypeRecyclerViewLayoutParams);

        //toolbarClose.setImageResource(null);
        toolbarClose.setImageResource(R.drawable.ic_close);
    }

        /**
         * This method to update SelectType recyclerview UI.
         * @param ownerTypesItems list.
         */
    private void updateSelectTypeAdapter(List<OwnerTypesItem> ownerTypesItems) {
        this.runOnUiThread(()->{
            this.ownerTypesItems.clear();
            this.ownerTypesItems.addAll(ownerTypesItems);
            selectTypeAdapter.notifyDataSetChanged();
        });
    }

    private void handleOnSelectedTypeSelection(OwnerTypesItem ownerTypesItem) {
        LegalAgreementActivity.start(this, ownerTypesItem);
        /*compositeDisposable.add(requestHandler.getOwnerAgreementRequest(ApiUtility.getInstance().getApiKeyMetaData())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSubscribe(d->apiResponseHandle(ApiResponse.loading()))
        .subscribe(result->apiResponseHandle(ApiResponse.success(result)),
                error->{apiResponseHandle(ApiResponse.error(error));}));*/
    }

    /**
     * This method to fetch OwnerType from server.
     */
    private void fetchOwnerTypeRequest() {
        compositeDisposable.add(requestHandler.fetchOwnerTypeRequest(ApiUtility.getInstance().getApiKeyMetaData())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(d -> apiResponseHandle(ApiResponse.loading(ServiceType.OWNER_TYPES)))
                .subscribe(result -> apiResponseHandle(ApiResponse.success(ServiceType.OWNER_TYPES, result)),
                        error -> apiResponseHandle(ApiResponse.error(ServiceType.OWNER_TYPES, error))));
    }

    /**
     * This method to handle api response.
     *
     * @param apiResponse (loding, success, error)
     */
    private void apiResponseHandle(ApiResponse apiResponse) {
        ServiceType serviceType = apiResponse.getServiceType();
        switch (apiResponse.status) {
            case LOADING:
                utility.showProgressDialog(this);
                break;
            case SUCCESS:
                utility.dismissDialog();
                if (ServiceType.OWNER_TYPES == serviceType) {
                    if (apiResponse.isValidResponse()) {
                        OwnerTypeResponse ownerTypeResponse = new Gson().fromJson(apiResponse.data, OwnerTypeResponse.class);
                        if (null != ownerTypeResponse && null != ownerTypeResponse.getOwnerTypes()) {
                            updateSelectTypeAdapter(ownerTypeResponse.getOwnerTypes());
                        }
                    }
                }
                break;
            case ERROR:
                utility.dismissDialog();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Onclick(R.id.toolbarClose)
    public void toolbarClose(View v) {
        finish();
    }
}
