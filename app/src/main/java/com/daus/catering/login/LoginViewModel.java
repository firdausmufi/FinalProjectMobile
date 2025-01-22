package com.daus.catering.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.daus.catering.database.DatabaseClient;
import com.daus.catering.database.DatabaseModel;
import com.daus.catering.database.dao.DatabaseDao;

import java.util.List;

public class LoginViewModel extends AndroidViewModel {

    LiveData<List<DatabaseModel>> modelDatabase;
    DatabaseDao databaseDao;

    public LoginViewModel(@NonNull Application application) {
        super(application);

        databaseDao = DatabaseClient.getInstance(application).getAppDatabase().databaseDao();
    }

    public LiveData<List<DatabaseModel>> getDataUser(String username, String password) {
        modelDatabase = databaseDao.getUserByName(username, password);
        return modelDatabase;
    }

}
