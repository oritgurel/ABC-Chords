package com.oritmalki.mymusicapp2;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;

import com.oritmalki.mymusicapp2.model.Beat;
import com.oritmalki.mymusicapp2.database.BeatDao;

import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

public class BeatDao_Impl implements BeatDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfBeat;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfBeat;

  public BeatDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBeat = new EntityInsertionAdapter<Beat>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `beat`(`chord_name`,`measureNum`) VALUES (?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Beat value) {
        if (value.getChordName() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getChordName());
        }
        if (value.measureNum == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindLong(2, value.measureNum);
        }
      }
    };
    this.__deletionAdapterOfBeat = new EntityDeletionOrUpdateAdapter<Beat>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `beat` WHERE `chord_name` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Beat value) {
        if (value.getChordName() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getChordName());
        }
      }
    };
  }

  @Override
  public void insertAll(List<Beat> beats) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfBeat.insert(beats);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertBeat(Beat beat) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfBeat.insert(beat);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(Beat beat) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfBeat.handle(beat);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Beat> getBeatsFromMeasure(int measureNum) {
    final String _sql = "SELECT * FROM beat where measureNum LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, measureNum);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfChordName = _cursor.getColumnIndexOrThrow("chord_name");
      final int _cursorIndexOfMeasureNum = _cursor.getColumnIndexOrThrow("measureNum");
      final List<Beat> _result = new ArrayList<Beat>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Beat _item;
        _item = new Beat();
        final String _tmpChordName;
        _tmpChordName = _cursor.getString(_cursorIndexOfChordName);
        _item.setChordName(_tmpChordName);
        if (_cursor.isNull(_cursorIndexOfMeasureNum)) {
          _item.measureNum = null;
        } else {
          _item.measureNum = _cursor.getInt(_cursorIndexOfMeasureNum);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
