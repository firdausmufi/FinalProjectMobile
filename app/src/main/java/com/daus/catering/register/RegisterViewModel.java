package com.daus.catering.register;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.daus.catering.database.DatabaseClient;
import com.daus.catering.database.DatabaseModel;
import com.daus.catering.database.dao.DatabaseDao;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class RegisterViewModel extends AndroidViewModel {

    DatabaseDao databaseDao;

    public RegisterViewModel(@NonNull Application application) {
        super(application);

        databaseDao = DatabaseClient.getInstance(application).getAppDatabase().databaseDao();
    }

    public void addDataRegister(final String strEmail, final String strUsername,
                                final String strPassword) {
        Completable.fromAction(() -> {
                    DatabaseModel databaseModel = new DatabaseModel();
                    databaseModel.email = strEmail;
                    databaseModel.username = strUsername;
                    databaseModel.password = strPassword;
                    databaseDao.insertData(databaseModel);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

}
