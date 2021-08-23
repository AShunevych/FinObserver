package com.ashunevich.finobserver;

import android.content.Context;
import android.util.Log;

import com.ashunevich.finobserver.data.DashboardAccountItem;
import com.ashunevich.finobserver.data.DashboardDAO;
import com.ashunevich.finobserver.database.RoomDashboardDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class DashboardDBSimpleTest {
    public DashboardDAO userDao;
    public RoomDashboardDatabase db;
    Context context;

    @Before
    public void setupOptions() {
        context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, RoomDashboardDatabase.class).
                build();
        userDao = db.dashboard_dao();
    }

   //CRUD test
    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void writeUserAndReadInList() {
        DashboardAccountItem item =
                newItem("NAme",1000.00);

        Log.d("Account ID to insert",String.valueOf(item.getAccountID()));
        userDao.insert(item);

        List<DashboardAccountItem> listItems = userDao.getAllAcountTest();

        Log.d("AccountID at pos 0 in list",String.valueOf(listItems.get(0).getAccountID()));

        assertThat(listItems.get(0).getAccountID(), equalTo(1));
    }

    @Test
    public void deleteAllFromDB(){
        DashboardAccountItem item =
                newItem("NAme",1000.00);

        DashboardAccountItem item2 =
                newItem("NAme2",2500.00);

        userDao.insert(item);
        userDao.insert(item2);

        List<DashboardAccountItem> listItems = userDao.getAllAcountTest();

        Log.d("SIZE_AFTER_INSERT",String.valueOf(listItems.size()));
        assertThat(listItems.size(),equalTo(2));

        userDao.deleteAll();

        List<DashboardAccountItem> listItemsAfterDelete = userDao.getAllAcountTest();

        assertThat(listItemsAfterDelete.size(),equalTo(0));
        Log.d("SIZE_AFTER_DELETE",String.valueOf(listItemsAfterDelete.size()));
    }

    @Test
    public  void deleteItemByPos(){
        DashboardAccountItem item =
                newItem("NAme",1000.00);

        DashboardAccountItem item2 =
                newItem("NAme2",2500.00);

        userDao.insert(item);
        userDao.insert(item2);

        List<DashboardAccountItem> listItems = userDao.getAllAcountTest();

        assertThat(listItems.size(),equalTo(2));

        userDao.delete(listItems.get(0));

        assertThat(item2.getAccountValue(),equalTo(listItems.get(0).getAccountValue()));
    }

    @Test
    public void updateItemValue(){
        DashboardAccountItem item =
                newItem("NAme",1000.0);

        userDao.insert(item);

        userDao.updateAccountAfterTransaction(userDao.getAllAcountTest().get(0).getAccountID(),500);

        DashboardAccountItem item2 = userDao.getAllAcountTest().get(0);

        assertThat(item2.getAccountValue(), equalTo(500.0));
    }

    @Test
    public void updateSingleItem(){
        DashboardAccountItem item =
                newItem("NAme",1000.0);
        userDao.insert(item);

        userDao.update(new DashboardAccountItem(1,"NotName",100.0,"USD","SomeId2"));

        DashboardAccountItem item2 = userDao.getAllAcountTest().get(0);

        assertThat(item2.getAccountID(), equalTo(1));
        assertThat(item2.getAccountName(), equalTo("NotName"));
        assertThat(item2.getAccountValue(), equalTo(100.0));
        assertThat(item2.getAccountCurrency(), equalTo("USD"));
        assertThat(item2.getImageID(), equalTo("SomeId2"));

    }

    public DashboardAccountItem newItem(String name,Double value){
        return new DashboardAccountItem(name,value,"UAH","SomeId");
    }



}
