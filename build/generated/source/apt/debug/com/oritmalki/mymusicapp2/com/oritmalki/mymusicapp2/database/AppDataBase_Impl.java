package com.oritmalki.mymusicapp2.com.oritmalki.mymusicapp2.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.util.TableInfo;

import com.oritmalki.mymusicapp2.database.AppDataBase;
import com.oritmalki.mymusicapp2.database.BeatDao;
import com.oritmalki.mymusicapp2.database.MeasureDao;

import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class AppDataBase_Impl extends AppDataBase {
  private volatile MeasureDao _measureDao;

  private volatile BeatDao _beatDao;

  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Sheet` (`id` INTEGER, `name` TEXT, `title` TEXT, `author` TEXT, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `measure` (`sheetId` INTEGER, `measure_number` INTEGER, `time_signature` TEXT, `showTimeSig` INTEGER NOT NULL, `beats` TEXT, `numerator` INTEGER, `denominator` INTEGER, PRIMARY KEY(`measure_number`), FOREIGN KEY(`sheetId`) REFERENCES `Sheet`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `beat` (`chord_name` TEXT NOT NULL, `measureNum` INTEGER, PRIMARY KEY(`chord_name`), FOREIGN KEY(`measureNum`) REFERENCES `measure`(`measure_number`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"c0eb47ffbfe4cb39136b5ea83fc01f11\")");
      }

      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Sheet`");
        _db.execSQL("DROP TABLE IF EXISTS `measure`");
        _db.execSQL("DROP TABLE IF EXISTS `beat`");
      }

      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        _db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsSheet = new HashMap<String, TableInfo.Column>(4);
        _columnsSheet.put("id", new TableInfo.Column("id", "INTEGER", false, 1));
        _columnsSheet.put("name", new TableInfo.Column("name", "TEXT", false, 0));
        _columnsSheet.put("title", new TableInfo.Column("title", "TEXT", false, 0));
        _columnsSheet.put("author", new TableInfo.Column("author", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSheet = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSheet = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSheet = new TableInfo("Sheet", _columnsSheet, _foreignKeysSheet, _indicesSheet);
        final TableInfo _existingSheet = TableInfo.read(_db, "Sheet");
        if (! _infoSheet.equals(_existingSheet)) {
          throw new IllegalStateException("Migration didn't properly handle Sheet(com.oritmalki.com.oritmalki.mymusicapp2.com.oritmalki.com.oritmalki.mymusicapp2.database.com.oritmalki.com.oritmalki.mymusicapp2.database.database.model.Sheet).\n"
                  + " Expected:\n" + _infoSheet + "\n"
                  + " Found:\n" + _existingSheet);
        }
        final HashMap<String, TableInfo.Column> _columnsMeasure = new HashMap<String, TableInfo.Column>(7);
        _columnsMeasure.put("sheetId", new TableInfo.Column("sheetId", "INTEGER", false, 0));
        _columnsMeasure.put("measure_number", new TableInfo.Column("measure_number", "INTEGER", false, 1));
        _columnsMeasure.put("time_signature", new TableInfo.Column("time_signature", "TEXT", false, 0));
        _columnsMeasure.put("showTimeSig", new TableInfo.Column("showTimeSig", "INTEGER", true, 0));
        _columnsMeasure.put("beats", new TableInfo.Column("beats", "TEXT", false, 0));
        _columnsMeasure.put("numerator", new TableInfo.Column("numerator", "INTEGER", false, 0));
        _columnsMeasure.put("denominator", new TableInfo.Column("denominator", "INTEGER", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMeasure = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysMeasure.add(new TableInfo.ForeignKey("Sheet", "CASCADE", "NO ACTION",Arrays.asList("sheetId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesMeasure = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMeasure = new TableInfo("measure", _columnsMeasure, _foreignKeysMeasure, _indicesMeasure);
        final TableInfo _existingMeasure = TableInfo.read(_db, "measure");
        if (! _infoMeasure.equals(_existingMeasure)) {
          throw new IllegalStateException("Migration didn't properly handle measure(com.oritmalki.com.oritmalki.mymusicapp2.com.oritmalki.com.oritmalki.mymusicapp2.database.com.oritmalki.com.oritmalki.mymusicapp2.database.database.model.Measure).\n"
                  + " Expected:\n" + _infoMeasure + "\n"
                  + " Found:\n" + _existingMeasure);
        }
        final HashMap<String, TableInfo.Column> _columnsBeat = new HashMap<String, TableInfo.Column>(2);
        _columnsBeat.put("chord_name", new TableInfo.Column("chord_name", "TEXT", true, 1));
        _columnsBeat.put("measureNum", new TableInfo.Column("measureNum", "INTEGER", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBeat = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysBeat.add(new TableInfo.ForeignKey("measure", "CASCADE", "NO ACTION",Arrays.asList("measureNum"), Arrays.asList("measure_number")));
        final HashSet<TableInfo.Index> _indicesBeat = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBeat = new TableInfo("beat", _columnsBeat, _foreignKeysBeat, _indicesBeat);
        final TableInfo _existingBeat = TableInfo.read(_db, "beat");
        if (! _infoBeat.equals(_existingBeat)) {
          throw new IllegalStateException("Migration didn't properly handle beat(com.oritmalki.com.oritmalki.mymusicapp2.com.oritmalki.com.oritmalki.mymusicapp2.database.com.oritmalki.com.oritmalki.mymusicapp2.database.database.model.Beat).\n"
                  + " Expected:\n" + _infoBeat + "\n"
                  + " Found:\n" + _existingBeat);
        }
      }
    }, "c0eb47ffbfe4cb39136b5ea83fc01f11");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "Sheet","measure","beat");
  }

  @Override
  public MeasureDao measureDao() {
    if (_measureDao != null) {
      return _measureDao;
    } else {
      synchronized(this) {
        if(_measureDao == null) {
          _measureDao = new MeasureDao_Impl(this);
        }
        return _measureDao;
      }
    }
  }

  @Override
  public BeatDao beatDao() {
    if (_beatDao != null) {
      return _beatDao;
    } else {
      synchronized(this) {
        if(_beatDao == null) {
          _beatDao = new BeatDao_Impl(this);
        }
        return _beatDao;
      }
    }
  }
}
