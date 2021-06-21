package com.ashunevich.finobserver.factories;



import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

@SuppressWarnings("unchecked")
public interface FactoryDAO<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(T... t);

    @Delete
    void delete(T t);

    @Update
    void update(T... t);
}
