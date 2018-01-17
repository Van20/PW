package smktelkom_mlg.sch.id.mywallet.Database_sql.Fragment;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;

import smktelkom_mlg.sch.id.mywallet.Beranda_screen.MainActivity;
import smktelkom_mlg.sch.id.mywallet.Database_sql.Controller.CategoryController;
import smktelkom_mlg.sch.id.mywallet.Database_sql.Controller.ExpenseController;
import smktelkom_mlg.sch.id.mywallet.Database_sql.Controller.SaldoController;
import smktelkom_mlg.sch.id.mywallet.Database_sql.Model.Category;
import smktelkom_mlg.sch.id.mywallet.Database_sql.Model.Expense;
import smktelkom_mlg.sch.id.mywallet.Database_sql.Model.Saldo;
import smktelkom_mlg.sch.id.mywallet.Database_sql.Utils.DatePicker;
import smktelkom_mlg.sch.id.mywallet.Database_sql.Utils.Utils;
import smktelkom_mlg.sch.id.mywallet.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragmentHome extends Fragment {

    public FragmentHome() {
    }

    RelativeLayout view;
    View mView;


    /**
     * Initial Variable
     */
    int SELECTED_ID = 0;
    int SELECTED_KATID = 0;

    Saldo mySaldo;
    Expense selectedExpense;

    List<Expense> Expenses;
    ArrayAdapter<Expense> adapter;

    EditText inputAmount, inputDesk;
    TextView currentSaldo, todayTotal;
    Spinner spinCategory;
    ListView expenseList;
    Button btnTgl,btnNext,btnPrev;

    private CategoryController category;
    private SaldoController saldo;
    private ExpenseController expense;

    private FloatingActionMenu Fab;
    private List<FloatingActionMenu> menus = new ArrayList<>();
    private Handler mUiHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = (RelativeLayout) inflater.inflate(R.layout.fragment_home, container, false);
        getActivity().setTitle("Manage Money");


        category = new CategoryController(getActivity());
        expense = new ExpenseController(getActivity());
        saldo = new SaldoController(getActivity());

        currentSaldo = (TextView) view.findViewById(R.id.currentSaldo);
        todayTotal = (TextView) view.findViewById(R.id.todayOut);
        expenseList = (ListView) view.findViewById(R.id.outList);

        btnTgl = (Button) view.findViewById(R.id.tgl);
        btnTgl.setText(Utils.getDateNow());
        btnTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        btnNext = (Button) view.findViewById(R.id.next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDay("+");
            }
        });

        btnPrev = (Button) view.findViewById(R.id.prev);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDay("-");
            }
        });

        new MyAsynch().execute(btnTgl.getText().toString());
        expenseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedExpense = (Expense) parent.getItemAtPosition(position);
                SELECTED_ID = selectedExpense.getId();
                menuItemDialog();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Fab = (FloatingActionMenu) view.findViewById(R.id.fab);

        final com.github.clans.fab.FloatingActionButton programFab1 = new com.github.clans.fab.FloatingActionButton(getActivity());
        programFab1.setButtonSize(com.github.clans.fab.FloatingActionButton.SIZE_MINI);
        programFab1.setLabelText(getString(R.string.pemasukan));
        programFab1.setImageResource(R.drawable.ic_fab);
        Fab.addMenuButton(programFab1);

        final com.github.clans.fab.FloatingActionButton programFab2 = new com.github.clans.fab.FloatingActionButton(getActivity());
        programFab2.setButtonSize(com.github.clans.fab.FloatingActionButton.SIZE_MINI);
        programFab2.setLabelText(getString(R.string.pengeluaran));
        programFab2.setImageResource(R.drawable.ic_fab);
        Fab.addMenuButton(programFab2);

        programFab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                programFab1.setLabelColors(ContextCompat.getColor(getActivity(), R.color.grey),
                        ContextCompat.getColor(getActivity(), R.color.light_grey),
                        ContextCompat.getColor(getActivity(), R.color.white_transparent));

                showFormDialog2("Pemasukan");
                programFab1.setLabelTextColor(ContextCompat.getColor(getActivity(), R.color.black));

                programFab2.setLabelColors(ContextCompat.getColor(getActivity(), R.color.black),
                        ContextCompat.getColor(getActivity(), R.color.light_grey),
                        ContextCompat.getColor(getActivity(), R.color.white_transparent));
                programFab2.setLabelTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            }
        });


        programFab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                programFab2.setLabelColors(ContextCompat.getColor(getActivity(), R.color.grey),
                        ContextCompat.getColor(getActivity(), R.color.light_grey),
                        ContextCompat.getColor(getActivity(), R.color.white_transparent));
                showFormDialog("Pengeluaran");
                programFab2.setLabelTextColor(ContextCompat.getColor(getActivity(), R.color.black));

                programFab1.setLabelColors(ContextCompat.getColor(getActivity(), R.color.black),
                        ContextCompat.getColor(getActivity(), R.color.light_grey),
                        ContextCompat.getColor(getActivity(), R.color.white_transparent));
                programFab1.setLabelTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            }
        });

        Fab.setClosedOnTouchOutside(true);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        menus.add(Fab);

        int delay = 400;
        for (final FloatingActionMenu menu : menus) {
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    menu.showMenuButton(true);
                }
            }, delay);
            delay += 150;
        }

        Fab.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fab.toggle(true);
            }
        });

    }

    private void changeDay(String s){
        String date = btnTgl.getText().toString();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(df.parse(date));
            if (s.equalsIgnoreCase("+")){
                c.add(Calendar.DATE,1);
            }else{
                c.add(Calendar.DATE,-1);
            }
            date = df.format(c.getTime());
            btnTgl.setText(date);
            new MyAsynch().execute(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    /**
     * Inflate Item To List
     */

    private class MyAsynch extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... Strings) { // run time intensive task in separate thread
            Expenses = expense.getExpenseByDate(Strings[0]);
            return null;
        }

        protected void onPostExecute(String result) {
            // Give the data to you adapter from here,instead of the place where you gave it earlier
            setmysaldo();
            setListAdapter(Expenses);
            expenseList.setAdapter(adapter);
            if (expense.getCountByDate(btnTgl.getText().toString()) != 0) {
                todayTotal.setText(" " + Utils.convertCur(expense.getTotal(btnTgl.getText().toString())));
            } else {
                todayTotal.setText(" - ");
            }

            if (adapter.getCount() == 0) {
                expenseList.setEmptyView(view.findViewById(R.id.emptyview));
            }
        }
    }

    /**
     * Method To Change Available Money
     */
    public void setmysaldo() {
        mySaldo = new Saldo(1, Integer.parseInt(saldo.getSaldo()));
        String current = Utils.convertCur(String.valueOf(mySaldo.getSaldo()));
        currentSaldo.setText(current);
    }

    /**
     * Custom Adapater For List
     */
    public void setListAdapter(List<Expense> Pengeluarans) {
        adapter = new ArrayAdapter<Expense>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, Pengeluarans) {
            @Override
            public View getView(int position,
                                View convertView,
                                ViewGroup parent) {
                Expense current = getItem(position);
                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater()
                            .inflate(R.layout.item_expense, null, false);
                }
                TextView txtDes = (TextView) convertView.findViewById(R.id.deskrip);
                TextView txtKat = (TextView) convertView.findViewById(R.id.namakat);
                TextView txtJml = (TextView) convertView.findViewById(R.id.jumlah);

                txtDes.setText(current.getDeskripsi());
                txtKat.setText(category.getName(current.getIdkat()));
                txtJml.setText(Utils.convertCur(String.valueOf(current.getJumlah())));

                return convertView;
            }
        };
    }


    /**
     * Custom Adapater For
     * Inflate Kategori To Spinner
     */
    public void loadKategori() {
        List<Category> Kategoris = category.getAllCategory();
        ArrayAdapter<Category> dataAdapter = new ArrayAdapter<Category>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, Kategoris);
        spinCategory.setAdapter(dataAdapter);
        spinCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category kat = (Category) parent.getItemAtPosition(position);
                SELECTED_KATID = kat.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    /**
     * Dialog To Select Action Update Or Delete
     */
    public void menuItemDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Edit / Hapus Pengeluaran");
        builder.setItems(R.array.dialog_expense, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        showFormDialog("Update");
                        break;
                    case 1:
                        expense.deleteExpenseById(SELECTED_ID);
                        saldo.updateSaldo(mySaldo.getSaldo() + selectedExpense.getJumlah());
                        new MyAsynch().execute(btnTgl.getText().toString());
                        break;
                }
            }
        });
        builder.show();
    }

    /**
     * Form Dialog To Add Or Update
     */
    public void showFormDialog(String to) {
        final String todo = to;
        final LayoutInflater mainLayout = LayoutInflater.from(getActivity());
        mView = mainLayout.inflate(R.layout.dialog_expense, null);
        inputAmount = (EditText) mView.findViewById(R.id.inputJumlah);
        inputDesk = (EditText) mView.findViewById(R.id.inputDesk);
        spinCategory = (Spinner) mView.findViewById(R.id.spinner);
        loadKategori();

        if (todo.equals("Update")) {
            inputAmount.setText(String.valueOf(selectedExpense.getJumlah()));
            inputDesk.setText(selectedExpense.getDeskripsi());
            setSelectedSpinner(spinCategory, category.getName(selectedExpense.getIdkat()));
        }

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        Expense peng = new Expense();
                        peng.setId(SELECTED_ID);
                        peng.setIdkat(SELECTED_KATID);

                        String description = inputDesk.getText().toString();
                        String expenditure = inputAmount.getText().toString();
                        if (TextUtils.isEmpty(description)) {
                            Toast.makeText(getActivity(), "Enter description of Expenditure!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (TextUtils.isEmpty(expenditure)) {
                            Toast.makeText(getActivity(), "Enter amount of Expenditure!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        peng.setDeskripsi(inputDesk.getText().toString());
                        peng.setJumlah(Integer.parseInt(inputAmount.getText().toString()));
                        peng.setTanggal(btnTgl.getText().toString());
                        if (todo.equals("Pengeluaran")) {
                            int a = 0;
                            int b = 10000;


                            if ((mySaldo.getSaldo() - peng.getJumlah()) < a) {
                                Toast.makeText(getActivity(), "Money is not enough", Toast.LENGTH_LONG).show();
                                return;
                            } else {
                                expense.addExpense(peng);
                                saldo.updateSaldo(mySaldo.getSaldo() - peng.getJumlah());
                                if ((mySaldo.getSaldo() - peng.getJumlah()) < b) {
                                    Toast.makeText(getActivity(), "Money stayed a little", Toast.LENGTH_LONG).show();
                                    currentSaldo.setTextColor(getResources().getColor(R.color.red_notice));
                                }
                            }
                        }
                         else {
                            expense.updateExpense(peng);
                            saldo.updateSaldo(chageBalance(peng.getJumlah()));
                        }

                        new MyAsynch().execute(btnTgl.getText().toString());
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });
        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    public void showFormDialog2(String to) {
        final String todo = to;
        final LayoutInflater mainLayout = LayoutInflater.from(getActivity());
        mView = mainLayout.inflate(R.layout.dialog_income, null);
        inputAmount = (EditText) mView.findViewById(R.id.inputJumlah);

        if (todo.equals("Update")) {
            inputAmount.setText(String.valueOf(selectedExpense.getJumlah()));
        }

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
        alertDialogBuilderUserInput.setView(mView);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        Expense inc = new Expense();
                        inc.setId(SELECTED_ID);

                        String expenditure = inputAmount.getText().toString();
                        if (TextUtils.isEmpty(expenditure)) {
                            Toast.makeText(getActivity(), "Enter amount of Expenditure!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        inc.setJumlah(Integer.parseInt(inputAmount.getText().toString()));
                        if (todo.equals("Pemasukan")) {
                            int a = 10000;
                                expense.addExpense(inc);
                                saldo.updateSaldo(mySaldo.getSaldo() + inc.getJumlah());
                            Toast.makeText(getActivity(), "Add Balance Successfully", Toast.LENGTH_LONG).show();
                            if ((mySaldo.getSaldo() + inc.getJumlah()) > a){
                                currentSaldo.setTextColor(getResources().getColor(R.color.white));
                            }
                        } else {
                            expense.updateExpense(inc);
                            saldo.updateSaldo(chageBalance(inc.getJumlah()));
                        }

                        new MyAsynch().execute(btnTgl.getText().toString());
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });
        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }
    /**
     * Show Dialog To Open Date Picker
     */
    private void showDatePicker() {
        DatePicker date = new DatePicker();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(onGetDate);
        date.show(getFragmentManager(), "Date Picker");
    }

    /**
     * Datepicker SetOn Listener
     */
    DatePickerDialog.OnDateSetListener onGetDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = sdf.format(c.getTime());
            btnTgl.setText(formattedDate);
            new MyAsynch().execute(formattedDate);
        }
    };

    /**
     * Return balance to change current saldo
     */
    private int chageBalance(int newjml) {
        int oldjml = selectedExpense.getJumlah();
        if (newjml > oldjml) {
            return mySaldo.getSaldo() - (newjml - oldjml);
        } else if (newjml < oldjml) {
            return mySaldo.getSaldo() + (oldjml - newjml);
        } else {
            return mySaldo.getSaldo();
        }
    }

    /**
     * Checked Item Spinner on Update
     */
    private void setSelectedSpinner(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(myString)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}