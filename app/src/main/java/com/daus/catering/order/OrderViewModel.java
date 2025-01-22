package com.daus.catering.order;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.daus.catering.database.DatabaseClient;
import com.daus.catering.database.DatabaseModel;
import com.daus.catering.database.dao.DatabaseDao;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class OrderViewModel extends AndroidViewModel {

    LiveData<List<DatabaseModel>> modelDatabase;
    DatabaseDao databaseDao;

    public OrderViewModel(@NonNull Application application) {
        super(application);

        databaseDao = DatabaseClient.getInstance(application).getAppDatabase().databaseDao();
    }

    public LiveData<List<DatabaseModel>> getDataIdUser() {
        modelDatabase = databaseDao.getAllOrder();
        return modelDatabase;
    }

    public void addDataOrder(final String strMenu, final int strJmlItems, final int strHarga) {
        Completable.fromAction(() -> {
                    DatabaseModel databaseModel = new DatabaseModel();
                    databaseModel.nama_menu = strMenu;
                    databaseModel.items = strJmlItems;
                    databaseModel.harga = strHarga;
                    databaseDao.insertData(databaseModel);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

}
