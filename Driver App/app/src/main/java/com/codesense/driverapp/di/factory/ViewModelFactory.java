package com.codesense.driverapp.di.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.ui.addvehicle.AddVehicleViewModel;
import com.codesense.driverapp.ui.launchscreen.LaunchScreenViewModel;
import com.codesense.driverapp.ui.uploaddocument.UploadDocumentViewModel;
import com.codesense.driverapp.ui.vehicle.VehicleListViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ViewModelFactory implements ViewModelProvider.Factory {

    private RequestHandler requestHandler;

    @Inject
    public ViewModelFactory(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LaunchScreenViewModel.class)) {
            return (T) new LaunchScreenViewModel(requestHandler);
        } else if (modelClass.isAssignableFrom(UploadDocumentViewModel.class)) {
            return (T) new UploadDocumentViewModel(requestHandler);
        } else if (modelClass.isAssignableFrom(AddVehicleViewModel.class)) {
            return (T) new AddVehicleViewModel(requestHandler);
        } else if (modelClass.isAssignableFrom(VehicleListViewModel.class)) {
            return (T) new VehicleListViewModel(requestHandler);
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}
