package smktelkom_mlg.sch.id.mywallet.Uang_screen;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import smktelkom_mlg.sch.id.mywallet.Beranda_screen.MainActivity;
import smktelkom_mlg.sch.id.mywallet.Database_sql.Controller.SaldoController;
import smktelkom_mlg.sch.id.mywallet.Database_sql.Model.Saldo;
import smktelkom_mlg.sch.id.mywallet.Database_sql.Utils.SPManager;
import smktelkom_mlg.sch.id.mywallet.Database_sql.Utils.Utils;
import smktelkom_mlg.sch.id.mywallet.R;


public class SettingUp extends Activity {

    private SaldoController saldo;
    EditText saldotxt;
    SPManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        saldo = new SaldoController(this);
        prefManager = new SPManager(this);

        if(prefManager.isFirstTimeLaunch()){
            saldo.addSaldo(new Saldo(1, 10000));
        }
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        saldotxt = (EditText) this.findViewById(R.id.setMoney);
        setContentView(R.layout.activity_setting_up);

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        Intent i = new Intent(SettingUp.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void restoreData(View view) {
        Toast.makeText(this, Utils.doRestore(), Toast.LENGTH_LONG).show();
        launchHomeScreen();
    }

    public void firsttimeSetting(View view) {
        saldotxt = (EditText) this.findViewById(R.id.setMoney);
        try {
            saldo.updateSaldo(Integer.parseInt(saldotxt.getText().toString()));
            Log.d("New Balance", saldo.getSaldo());
            AlertDialog alertDialog = new AlertDialog.Builder(SettingUp.this).create();
            alertDialog.setTitle("Succesfully...");
            alertDialog.setMessage(getResources().getString(R.string.settingupmessagesucces));
            alertDialog.setIcon(R.drawable.ic_info_black_24dp);
            alertDialog.setButton("Continue", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String isiSaldo = saldotxt.getText().toString();

                    if (TextUtils.isEmpty(isiSaldo)) {
                        Toast.makeText(getApplicationContext(), "Enter Saldo!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        launchHomeScreen();
                    }
                }
            });
            alertDialog.show();

        } catch (SQLException e) {
            Log.d("Error", "SqlExceptionError");
        }
    }

}
