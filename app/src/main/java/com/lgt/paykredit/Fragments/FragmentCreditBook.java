package com.lgt.paykredit.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lgt.paykredit.Adapter.AdapterCreditBookCustomers;
import com.lgt.paykredit.Models.ModelCreditBookCustomers;
import com.lgt.paykredit.R;
import com.lgt.paykredit.extras.Common;
import com.lgt.paykredit.extras.Language;
import com.lgt.paykredit.extras.PayKreditAPI;
import com.lgt.paykredit.extras.SingletonRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class FragmentCreditBook extends Fragment {

    private RecyclerView rvCreditBookCustomers;
    private AdapterCreditBookCustomers adapterCreditBookCustomers;
    private List<ModelCreditBookCustomers> listCreditBook;

    private PopupMenu popup;
    private ImageView ivFilter;
    private FrameLayout frameCreditBook;

    private ProgressBar pbCreditBook;
    private EditText etSearch;

    private Common common;
    private TextView tvAddCustomer, tvCreditFragment, tvReceivedCreditBook, tvNoCustomerFound, tv_credit, tv_recieved;

    private String mMobileNumber, mName, mUserID;

    private SharedPreferences sharedPreferences;

    public static FragmentCreditBook fragmentCreditBook;

    public FragmentCreditBook() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_credit_book, container, false);


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        rvCreditBookCustomers = view.findViewById(R.id.rvCreditBookCustomers);
        tvAddCustomer = view.findViewById(R.id.tvAddCustomer);
        frameCreditBook = view.findViewById(R.id.frameCreditBook);
        tvCreditFragment = view.findViewById(R.id.tvCreditFragment);
        tvReceivedCreditBook = view.findViewById(R.id.tvReceivedCreditBook);
        pbCreditBook = view.findViewById(R.id.pbCreditBook);
        tvNoCustomerFound = view.findViewById(R.id.tvNoCustomerFound);
        tv_credit = view.findViewById(R.id.tv_credit);
        tv_recieved = view.findViewById(R.id.tv_recieved);

        etSearch = view.findViewById(R.id.etSearch);

        ivFilter = view.findViewById(R.id.ivFilter);

        common = new Common(getActivity());

        sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);

        if (sharedPreferences.contains("KEY_USER_ID")) {
            mUserID = sharedPreferences.getString("KEY_USER_ID", "");
        }

        tvAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openContacts
                openPhoneContacts();
            }
        });


        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPopUpMenu();
            }
        });
        loadCustomers();


        fragmentCreditBook = this;


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence != null) {

                    if (adapterCreditBookCustomers != null) {
                        adapterCreditBookCustomers.getFilter().filter(charSequence.toString());
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Common.getLanguage(getActivity()) == "HI") {
                    // translate in hindi
                    tv_credit.setText("शेष उधार");
                    tv_recieved.setText("कुल पेमेंट");
                    tvAddCustomer.setText("ग्राहक जोड़ें");
                    etSearch.setHint("खोजे");
                } else {

                    //If not, display "no connection" warning:
                    tv_credit.setText(tv_credit.getText().toString());
                    tv_recieved.setText(tv_recieved.getText().toString());
                    tvAddCustomer.setText(tvAddCustomer.getText().toString());
                    etSearch.setHint(etSearch.getHint().toString());

                }
            }
        }, 500);

    }


    public static FragmentCreditBook getInstance() {
        return fragmentCreditBook;
    }

    private void openPopUpMenu() {
        if (getActivity() != null) {
            popup = new PopupMenu(getActivity(), ivFilter);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_filter, popup.getMenu());
            popup.show();
        } else {
            Toast.makeText(getActivity(), "Please re-open app", Toast.LENGTH_SHORT).show();
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_name) {
                    filterUsingName();
                }

                if (item.getItemId() == R.id.menu_amount) {
                    filterUsingAmount();
                }

                if (item.getItemId() == R.id.menu_latest) {
                    loadCustomers();
                }

                return true;
            }
        });
    }

    private void filterUsingDue() {

        for (int i = 0; i < listCreditBook.size(); i++) {
            if (listCreditBook.get(i).getAdvanceOrDue().equalsIgnoreCase("Due")) {
                Log.e("gygygyggygy", listCreditBook.get(i).getAdvanceOrDue() + "");

                adapterCreditBookCustomers = new AdapterCreditBookCustomers(listCreditBook, getActivity());
                rvCreditBookCustomers.hasFixedSize();
                rvCreditBookCustomers.setNestedScrollingEnabled(false);
                rvCreditBookCustomers.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                rvCreditBookCustomers.setAdapter(adapterCreditBookCustomers);
                adapterCreditBookCustomers.notifyDataSetChanged();
            }
        }


    }

    private void filterUsingAmount() {
        Collections.sort(listCreditBook, new Comparator<ModelCreditBookCustomers>() {
            @Override
            public int compare(ModelCreditBookCustomers modelTest, ModelCreditBookCustomers t1) {
                return new Float(modelTest.getAmount()).compareTo(Float.valueOf(t1.getAmount()));
            }
        });

        Collections.reverse(listCreditBook);

        adapterCreditBookCustomers = new AdapterCreditBookCustomers(listCreditBook, getActivity());
        rvCreditBookCustomers.hasFixedSize();
        rvCreditBookCustomers.setNestedScrollingEnabled(false);
        rvCreditBookCustomers.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvCreditBookCustomers.setAdapter(adapterCreditBookCustomers);
        adapterCreditBookCustomers.notifyDataSetChanged();

        Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();

    }

    private void filterUsingName() {
        Collections.sort(listCreditBook, new Comparator<ModelCreditBookCustomers>() {
            @Override
            public int compare(ModelCreditBookCustomers modelTest, ModelCreditBookCustomers t1) {
                return new String(modelTest.getName()).compareTo(t1.getName());
            }
        });

        adapterCreditBookCustomers = new AdapterCreditBookCustomers(listCreditBook, getActivity());
        rvCreditBookCustomers.hasFixedSize();
        rvCreditBookCustomers.setNestedScrollingEnabled(false);
        rvCreditBookCustomers.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        rvCreditBookCustomers.setAdapter(adapterCreditBookCustomers);
        adapterCreditBookCustomers.notifyDataSetChanged();

        Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
    }

    private void openPhoneContacts() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, 1);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 1) && (resultCode == RESULT_OK)) {
            Cursor cursor = null;
            try {
                if (data != null) {

                    Uri uri = data.getData();
                    if (uri != null) {
                        cursor = getActivity().getContentResolver().query(uri, new String[]{
                                ContactsContract.CommonDataKinds.Phone.NUMBER,
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}, null, null, null);
                    }
                }
                if (cursor != null && cursor.moveToFirst()) {

                    mMobileNumber = cursor.getString(0);
                    mName = cursor.getString(1);

                    formatMobileNumber(mMobileNumber, mName);

                }

            } catch (Exception e) {

            }
        }

    }

    private void formatMobileNumber(String mobile, String name) {

        Log.e("mobilewithoutcorrection", mobile + "");

        String aa = mobile.replaceAll("\\s", "");
        String original, reverse = "";
        original = aa;
        int length = original.length();
        for (int i = length - 1; i >= 0; i--) {
            reverse = reverse + original.charAt(i);
            if (reverse.length() == 10) {
                break;
            }
        }

        String original2, reverse2 = "";
        original2 = reverse;
        int length2 = original2.length();
        for (int i = length2 - 1; i >= 0; i--)
            reverse2 = reverse2 + original2.charAt(i);

        apiCallAddCustomers(reverse2, name);

    }

    private void apiCallAddCustomers(final String mobileNumberAfterCorrection, final String name) {

        pbCreditBook.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.ADD_CUSTOMER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                pbCreditBook.setVisibility(View.GONE);
                Log.e("jkjlkjkljlkjlk", response + "");

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    String customer_id = jsonObject.getString("customer_id");

                    if (status.equalsIgnoreCase("1")) {
                        Log.e("addcusomterapiresponse", response + "");
                        loadCustomers();
                    } else {
                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbCreditBook.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", mUserID);
                params.put("customer_name", name);
                params.put("customer_mobile", mobileNumberAfterCorrection);
                Log.e("paramscreditbook", params + "");
                return params;
            }
        };

        RequestQueue requestQueue = SingletonRequestQueue.getInstance(getActivity()).getRequestQueue();
        requestQueue.add(stringRequest);


    }


    public void loadCustomers() {

        pbCreditBook.setVisibility(View.VISIBLE);

        listCreditBook = new ArrayList<>();
        listCreditBook.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PayKreditAPI.CUSTOMER_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("creditbookresponse", response + "");

                pbCreditBook.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String message = jsonObject.getString("message");
                    String status = jsonObject.getString("status");
                    String send_value = jsonObject.getString("send_value");
                    String recived_value = jsonObject.getString("recived_value");

                    if (status.equalsIgnoreCase("1")) {

                        tvReceivedCreditBook.setText(recived_value);
                        tvCreditFragment.setText(send_value);

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        if (jsonArray.length() > 0) {
                            tvNoCustomerFound.setVisibility(View.GONE);
                            rvCreditBookCustomers.setVisibility(View.VISIBLE);
                        }

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String tbl_add_customer_id = object.getString("tbl_add_customer_id");
                            String customer_name = object.getString("customer_name");
                            String customer_mobile = object.getString("customer_mobile");
                            String balance = object.getString("balance");
                            String balance_status = object.getString("balance_status");

                            listCreditBook.add(new ModelCreditBookCustomers(tbl_add_customer_id, "", customer_name, balance, balance_status, "", customer_mobile));
                            //listCreditBook.add(new ModelCreditBookCustomers("", "", "Ranjan Kumar Singh", "10", "Advance", "16-03-2020"));

                        }

                        adapterCreditBookCustomers = new AdapterCreditBookCustomers(listCreditBook, getActivity());
                        rvCreditBookCustomers.hasFixedSize();
                        rvCreditBookCustomers.setNestedScrollingEnabled(false);
                        rvCreditBookCustomers.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                        rvCreditBookCustomers.setAdapter(adapterCreditBookCustomers);
                        adapterCreditBookCustomers.notifyDataSetChanged();


                    } else {
                        tvNoCustomerFound.setVisibility(View.VISIBLE);
                        rvCreditBookCustomers.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbCreditBook.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", mUserID);
                Log.e("creditbookparams", params + "");
                return params;
            }
        };

        RequestQueue requestQueue = SingletonRequestQueue.getInstance(getActivity()).getRequestQueue();
        requestQueue.add(stringRequest);


    }

}
