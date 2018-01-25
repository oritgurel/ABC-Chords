package com.oritmalki.mymusicapp2;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.database.Cursor;

import com.oritmalki.mymusicapp2.model.Beat;
import com.oritmalki.mymusicapp2.database.Converters;
import com.oritmalki.mymusicapp2.model.Measure;
import com.oritmalki.mymusicapp2.database.MeasureDao;
import com.oritmalki.mymusicapp2.model.TimeSignature;

import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

public class MeasureDao_Impl implements MeasureDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfMeasure;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfMeasure;

  public MeasureDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMeasure = new EntityInsertionAdapter<Measure>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `measure`(`sheetId`,`measure_number`,`time_signature`,`showTimeSig`,`beats`,`numerator`,`denominator`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Measure value) {
        if (value.sheetId == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.sheetId);
        }
        if (value.measureNumber == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindLong(2, value.measureNumber);
        }
        if (value.getTimeSig() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getTimeSig());
        }
        final int _tmp;
        _tmp = value.isShowTimeSig() ? 1 : 0;
        stmt.bindLong(4, _tmp);
        final String _tmp_1;
        _tmp_1 = Converters.beatsToString(value.getBeats());
        if (_tmp_1 == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, _tmp_1);
        }
        final TimeSignature _tmpTimeSignature = value.getTimeSignature();
        if(_tmpTimeSignature != null) {
          stmt.bindLong(6, _tmpTimeSignature.getNumerator());
          stmt.bindLong(7, _tmpTimeSignature.getDenominator());
        } else {
          stmt.bindNull(6);
          stmt.bindNull(7);
        }
      }
    };
    this.__deletionAdapterOfMeasure = new EntityDeletionOrUpdateAdapter<Measure>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `measure` WHERE `measure_number` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Measure value) {
        if (value.measureNumber == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindLong(1, value.measureNumber);
        }
      }
    };
  }

  @Override
  public void insertAll(List<Measure> measures) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfMeasure.insert(measures);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(Measure measure) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfMeasure.handle(measure);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Measure> getAll() {
    final String _sql = "SELECT * FROM measure";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfSheetId = _cursor.getColumnIndexOrThrow("sheetId");
      final int _cursorIndexOfMeasureNumber = _cursor.getColumnIndexOrThrow("measure_number");
      final int _cursorIndexOfTimeSig = _cursor.getColumnIndexOrThrow("time_signature");
      final int _cursorIndexOfShowTimeSig = _cursor.getColumnIndexOrThrow("showTimeSig");
      final int _cursorIndexOfBeats = _cursor.getColumnIndexOrThrow("beats");
      final int _cursorIndexOfNumerator = _cursor.getColumnIndexOrThrow("numerator");
      final int _cursorIndexOfDenominator = _cursor.getColumnIndexOrThrow("denominator");
      final List<Measure> _result = new ArrayList<Measure>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Measure _item;
        final TimeSignature _tmpTimeSignature;
        if (! (_cursor.isNull(_cursorIndexOfNumerator) && _cursor.isNull(_cursorIndexOfDenominator))) {
          _tmpTimeSignature = new TimeSignature();
          final int _tmpNumerator;
          _tmpNumerator = _cursor.getInt(_cursorIndexOfNumerator);
          _tmpTimeSignature.setNumerator(_tmpNumerator);
          final int _tmpDenominator;
          _tmpDenominator = _cursor.getInt(_cursorIndexOfDenominator);
          _tmpTimeSignature.setDenominator(_tmpDenominator);
        }  else  {
          _tmpTimeSignature = null;
        }
        _item = new Measure();
        if (_cursor.isNull(_cursorIndexOfSheetId)) {
          _item.sheetId = null;
        } else {
          _item.sheetId = _cursor.getInt(_cursorIndexOfSheetId);
        }
        if (_cursor.isNull(_cursorIndexOfMeasureNumber)) {
          _item.measureNumber = null;
        } else {
          _item.measureNumber = _cursor.getInt(_cursorIndexOfMeasureNumber);
        }
        final String _tmpTimeSig;
        _tmpTimeSig = _cursor.getString(_cursorIndexOfTimeSig);
        _item.setTimeSig(_tmpTimeSig);
        final boolean _tmpShowTimeSig;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfShowTimeSig);
        _tmpShowTimeSig = _tmp != 0;
        _item.setShowTimeSig(_tmpShowTimeSig);
        final List<Beat> _tmpBeats;
        final String _tmp_1;
        _tmp_1 = _cursor.getString(_cursorIndexOfBeats);
        _tmpBeats = Converters.stringToBeats(_tmp_1);
        _item.setBeats(_tmpBeats);
        _item.setTimeSignature(_tmpTimeSignature);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Measure findByNumber(int measureNumber) {
    final String _sql = "SELECT * FROM measure where measure_number LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, measureNumber);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfSheetId = _cursor.getColumnIndexOrThrow("sheetId");
      final int _cursorIndexOfMeasureNumber = _cursor.getColumnIndexOrThrow("measure_number");
      final int _cursorIndexOfTimeSig = _cursor.getColumnIndexOrThrow("time_signature");
      final int _cursorIndexOfShowTimeSig = _cursor.getColumnIndexOrThrow("showTimeSig");
      final int _cursorIndexOfBeats = _cursor.getColumnIndexOrThrow("beats");
      final int _cursorIndexOfNumerator = _cursor.getColumnIndexOrThrow("numerator");
      final int _cursorIndexOfDenominator = _cursor.getColumnIndexOrThrow("denominator");
      final Measure _result;
      if(_cursor.moveToFirst()) {
        final TimeSignature _tmpTimeSignature;
        if (! (_cursor.isNull(_cursorIndexOfNumerator) && _cursor.isNull(_cursorIndexOfDenominator))) {
          _tmpTimeSignature = new TimeSignature();
          final int _tmpNumerator;
          _tmpNumerator = _cursor.getInt(_cursorIndexOfNumerator);
          _tmpTimeSignature.setNumerator(_tmpNumerator);
          final int _tmpDenominator;
          _tmpDenominator = _cursor.getInt(_cursorIndexOfDenominator);
          _tmpTimeSignature.setDenominator(_tmpDenominator);
        }  else  {
          _tmpTimeSignature = null;
        }
        _result = new Measure();
        if (_cursor.isNull(_cursorIndexOfSheetId)) {
          _result.sheetId = null;
        } else {
          _result.sheetId = _cursor.getInt(_cursorIndexOfSheetId);
        }
        if (_cursor.isNull(_cursorIndexOfMeasureNumber)) {
          _result.measureNumber = null;
        } else {
          _result.measureNumber = _cursor.getInt(_cursorIndexOfMeasureNumber);
        }
        final String _tmpTimeSig;
        _tmpTimeSig = _cursor.getString(_cursorIndexOfTimeSig);
        _result.setTimeSig(_tmpTimeSig);
        final boolean _tmpShowTimeSig;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfShowTimeSig);
        _tmpShowTimeSig = _tmp != 0;
        _result.setShowTimeSig(_tmpShowTimeSig);
        final List<Beat> _tmpBeats;
        final String _tmp_1;
        _tmp_1 = _cursor.getString(_cursorIndexOfBeats);
        _tmpBeats = Converters.stringToBeats(_tmp_1);
        _result.setBeats(_tmpBeats);
        _result.setTimeSignature(_tmpTimeSignature);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Beat> getBeats() {
    final String _sql = "SELECT * FROM beat";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
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
