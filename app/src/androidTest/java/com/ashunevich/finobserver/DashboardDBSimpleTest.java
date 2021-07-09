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
        context = ApplicationProvider.getApplicationContext ();
        db = Room.inMemoryDatabaseBuilder (context, RoomDashboardDatabase.class).
                build ();
        userDao = db.dashboard_dao ();
    }


    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void writeUserAndReadInList() {
        DashboardAccountItem item =
                new DashboardAccountItem
        ("name",25.00,"UAH","Someid");

        Log.d("Account ID to insert",String.valueOf (item.getAccountID()));
        userDao.insert(item);

        List<DashboardAccountItem> listItems = userDao.getAllAcountTest ();

        Log.d("AccountID at pos 0 in list",String.valueOf (listItems.get (0).getAccountID ()));

        assertThat(listItems.get (0).getAccountID (), equalTo(1));
    }

    @Test
    public void deleteAllFromDB(){
        DashboardAccountItem item =
                newItem ("NAme",1000.00);

        DashboardAccountItem item2 =
                newItem ("NAme2",2500.00);

        userDao.insert(item);
        userDao.insert(item2);

        List<DashboardAccountItem> listItems = userDao.getAllAcountTest ();

        Log.d ("SIZE_AFTER_INSERt",String.valueOf (listItems.size ()));
        assertThat (listItems.size (),equalTo (2));

        userDao.deleteAll ();

        List<DashboardAccountItem> listItemsAfterDelete = userDao.getAllAcountTest ();

        assertThat (listItemsAfterDelete.size (),equalTo (0));
        Log.d ("SIZE_AFTER_DELETE",String.valueOf (listItemsAfterDelete.size ()));
    }

    @Test
    public  void deleteItemByPos(){
        DashboardAccountItem item =
                newItem ("NAme",1000.00);

        DashboardAccountItem item2 =
                newItem ("NAme2",2500.00);

        userDao.insert(item);
        userDao.insert(item2);

        List<DashboardAccountItem> listItems = userDao.getAllAcountTest ();

        assertThat (listItems.size (),equalTo (2));

        userDao.delete (listItems.get (0));

        assertThat (item2.getAccountValue (),equalTo (listItems.get (0).getAccountValue ()));
    }


    public DashboardAccountItem newItem(String name,Double value){
        return new DashboardAccountItem (name,value,"UAH","SomeId");
    }



}
