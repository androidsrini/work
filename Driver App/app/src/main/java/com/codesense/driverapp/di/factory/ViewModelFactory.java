package com.codesense.driverapp.di.factory;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.ui.adddriver.DriverViewModel;
import com.codesense.driverapp.ui.addvehicle.AddVehicleViewModel;
import com.codesense.driverapp.ui.launchscreen.LaunchScreenViewModel;
import com.codesense.driverapp.ui.online.OnlineViewModel;
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
        } else if (modelClass.isAssignableFrom(OnlineViewModel.class)) {
            return (T) new OnlineViewModel(requestHandler);
        } else if (modelClass.isAssignableFrom(DriverViewModel.class)) {
            return (T) new DriverViewModel(requestHandler);
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}
