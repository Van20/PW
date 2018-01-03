package smktelkom_mlg.sch.id.mywallet.Database_sql.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import smktelkom_mlg.sch.id.mywallet.Beranda_screen.MainActivity;
import smktelkom_mlg.sch.id.mywallet.Promo_screen.Blanja_screen.BlanjaActivity;
import smktelkom_mlg.sch.id.mywallet.Promo_screen.Blibli_screen.BlibliActivity;
import smktelkom_mlg.sch.id.mywallet.Promo_screen.Bukalapak_screen.BukalapakActivity;
import smktelkom_mlg.sch.id.mywallet.Promo_screen.Elevenia_screen.EleveniaActivity;
import smktelkom_mlg.sch.id.mywallet.Promo_screen.JD_screen.JdActivity;
import smktelkom_mlg.sch.id.mywallet.Promo_screen.Lazada_screen.LazadaActivity;
import smktelkom_mlg.sch.id.mywallet.Promo_screen.Shopee_screen.ShopeeActivity;
import smktelkom_mlg.sch.id.mywallet.Promo_screen.Tokopedia_screen.TokopediaActivity;
import smktelkom_mlg.sch.id.mywallet.R;

public class FragmentPromo extends Fragment {

    public FragmentPromo() {
    }

    RelativeLayout view;
    CardView Lazada,Tokopedia,Bukalapak,Elevenia,Shopee,Jd,Blanja,Blibli;
    private Context context;
    private ProgressDialog a;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = (RelativeLayout) inflater.inflate(R.layout.fragment_promo, container, false);
        getActivity().setTitle("View Online Shop");
        ((MainActivity) getActivity()).hideFloatingActionButton();

        Lazada = (CardView)view.findViewById(R.id.lazada);
        Lazada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LazadaActivity.class));
            }
        });

        Tokopedia = (CardView)view.findViewById(R.id.tokopedia);
        Tokopedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TokopediaActivity.class));
            }
        });

        Bukalapak = (CardView)view.findViewById(R.id.bukalapak);
        Bukalapak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BukalapakActivity.class));
            }
        });

        Elevenia = (CardView)view.findViewById(R.id.elevenia);
        Elevenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EleveniaActivity.class));
            }
        });

        Shopee = (CardView)view.findViewById(R.id.shopee);
        Shopee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ShopeeActivity.class));
            }
        });

        Jd = (CardView)view.findViewById(R.id.jd);
        Jd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), JdActivity.class));
            }
        });

        Blanja = (CardView)view.findViewById(R.id.blanja);
        Blanja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BlanjaActivity.class));
            }
        });

        Blibli = (CardView)view.findViewById(R.id.blibli);
        Blibli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BlibliActivity.class));
            }
        });

        return view;

    }

}