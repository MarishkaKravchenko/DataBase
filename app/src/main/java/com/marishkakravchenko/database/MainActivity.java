package com.marishkakravchenko.database;

import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.marishkakravchenko.database.db.DbOpenHelper;
import com.marishkakravchenko.database.db.tables.DataTable;
import com.marishkakravchenko.database.model.DataModel;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.marishkakravchenko.database.model.DataModelSQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private DefaultStorIOSQLite mDefaultStorIOSQLite;

    EditText userValue;
    Button saveBtn, loadBtn;
    TextView response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteOpenHelper sqLiteOpenHelper = new DbOpenHelper(this);

        userValue = (EditText) findViewById(R.id.user_value);
        saveBtn = (Button) findViewById(R.id.button1);
        loadBtn = (Button) findViewById(R.id.button2);
        response = (TextView) findViewById(R.id.response);

        saveBtn.setOnClickListener(this);
        loadBtn.setOnClickListener(this);

        //Получаем с помощью builder'а экземпляр StorIO
        mDefaultStorIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(sqLiteOpenHelper)
                .addTypeMapping(DataModel.class, new DataModelSQLiteTypeMapping())
                .build();

/*
        for (DataModel dataModel : getMockData(5)) {
            Log.d(TAG, "MAKTAG put to DB: " + dataModel.toString());
            putDataToStorIO(dataModel);
        }

        List<DataModel> storedDataList = loadDataFromDb();

        for (DataModel dataModel : storedDataList) {
            Log.d(TAG, "MAKTAG load from DB: " + dataModel.toString());

        }*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1 :
                String s = userValue.getText().toString();
                putDataToStorIO(new DataModel(s, System.currentTimeMillis()));
                Toast.makeText(MainActivity.this, "Ok", Toast.LENGTH_SHORT).show();
                break;

            case R.id.button2 :
                List<DataModel> storedDataList = loadDataFromDb();
                for (DataModel dataModel : storedDataList){
                    response.setText(dataModel.getValue());
                }
                break;
        }

    }

    /**
         * Метод возвращает коллекцию всех данных из БД
         *
         * @return List объектов DataModel
         */
        private List<DataModel> loadDataFromDb() {
            return mDefaultStorIOSQLite
                    .get()
                    .listOfObjects(DataModel.class)
                    .withQuery(
                            Query.builder()
                                    .table(DataTable.TABLE_DATA)
                                    .build())
                    .prepare()
                    .executeAsBlocking();
        }

        /**
         * Метод сохраянет в БД заданный объект типа DataModel
         *
         * @param dataModel сохраняемый объект
         */
        private void putDataToStorIO(DataModel dataModel) {
            mDefaultStorIOSQLite
                    .put()
                    .object(dataModel)
                    .prepare()
                    .executeAsBlocking();
        }

        /**
         * Метод для тестирования, генерирует список DataModel
         *
         * @return список объектов DataModel
         */
        private List<DataModel> getMockData(int count) {

            List<DataModel> mockList = new ArrayList<>();

            for (int i = 0; i <= count; i++) {
                try {
                    mockList.add(new DataModel("Строка " + i, System.currentTimeMillis()));

                    //Ждём 1 мс, что бы не было одинакового времени
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return mockList;
        }
}
