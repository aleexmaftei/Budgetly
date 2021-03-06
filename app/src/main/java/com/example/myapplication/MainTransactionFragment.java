package com.example.myapplication;

import android.os.Bundle;
import android.text.BoringLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

/**
 * This fragments shows the monthly transactions
 * the tabs are automatically generated from the start of the year to the current month
 */
public class MainTransactionFragment extends Fragment {

    private static final String ARG_PAGE = "ARG_PAGE";
    ViewPager viewPager;
    TabLayout tabLayout;
    View view;
    TextView totalAmount;
    private int tabPosition;
    private Boolean isChart;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private CollectionReference db = FirebaseFirestore.getInstance().collection("users");


    public MainTransactionFragment() {
    }

    // use to save and retrieve some data when
    public static MainTransactionFragment newInstance(Boolean isChart, int lastTab) {

        Bundle args = new Bundle();
        MainTransactionFragment fragment = null;
        args.putBoolean("isChart", isChart);
        args.putInt("lastTab", lastTab);
        fragment = new MainTransactionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // get references to the viewPager and tabs
        view = inflater.inflate(R.layout.main_transaction_layout, container, false);
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.sliding_tabs);
        totalAmount = view.findViewById(R.id.totalAmount_id);

        // query the database and save the transactions of the current month
        db.document(firebaseAuth.getCurrentUser().getUid()).collection("Wallet").document("MainWallet").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private static final String TAG = "MAIN_TRANSACTION";

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        totalAmount.setText(Objects.requireNonNull(document.getData().get("amount")).toString() + " lei");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initTabs();

    }

    // init and generate the tabs automatically
    public void initTabs() {


        // list of tab titles passed to the TransactionFragmentAdapter
        List<String> tabTitles = new ArrayList<>();

        // get the current date month and year
        DateFormat df = new SimpleDateFormat("MM");
        DateFormat dfy = new SimpleDateFormat("YYYY");

        String currMonth = df.format(Calendar.getInstance().getTime());
        String currYear = dfy.format(Calendar.getInstance().getTime());
        int maxDate = Integer.parseInt(currMonth);

        // add all the months of the year up to the current month in the format 'MM/YYYY'
        for (int i = 1; i <= maxDate; i++) {
            String month;

            if (i < 10)
                month = "0" + i;
            else
                month = String.valueOf(i);

            tabTitles.add(month + "/" + currYear);
        }
        Bundle bundle = getArguments();
        Boolean isChart = bundle.getBoolean("isChart");
        int page = bundle.getInt(ARG_PAGE);

        // set the adapter for the viewPager
        viewPager.setAdapter(new TransactionFragmentAdapter(getChildFragmentManager(), getContext(), tabTitles, isChart));


        // get the tabLayout and bind it with the viewPager
        tabLayout.setupWithViewPager(viewPager);
//        tabPosition = tabLayout.getSelectedTabPosition();
        tabLayout.getTabAt(getArguments().getInt("tabPosition")).select();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                getArguments().putInt("tabPosition", tabPosition);
                MainActivity m = (MainActivity) getActivity();
                m.passData(tabPosition);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager.setCurrentItem(getArguments().getInt("lastTab"));


    }

}
