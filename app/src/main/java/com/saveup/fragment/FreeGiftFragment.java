package com.saveup.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.saveup.activity.MainActivity;
import com.saveup.android.R;
import com.saveup.utils.SessionSave;

import static com.saveup.utils.AppConstant.BASEURL;

/**
 * A simple {@link Fragment} subclass.
 */
public class FreeGiftFragment extends Fragment {

    private TextView txtCopy;
    private TextView referralcodeview;
    private Button button_invite;
    String code;


    public FreeGiftFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_free_gift, container, false);

        txtCopy = view.findViewById(R.id.txtCopy);
        referralcodeview = view.findViewById(R.id.referralcodeview);
        button_invite = view.findViewById(R.id.button_invite);
        // System.out.println("refferel_code>>>"+referrel_code);
        code = SessionSave.getSession("referral_code", getContext());
        System.out.println("referral_code>>>" + SessionSave.getSession("referral_code", getContext()));

        if (code != null) {
            referralcodeview.setText(code);
        }
        txtCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClipboard(getActivity(), code);
                Toast.makeText(getActivity(), getString(R.string.referral_code), Toast.LENGTH_SHORT).show();
            }
        });

        button_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("text/plain");
           //     shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.referral_msg)+" " + BASEURL + "/r/" + code);
                   shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "SaveUp - The best coupon marketplace of Bangladesh! Use my referral code and get 100 BDT Free from your next purchase through SaveUp. " + BASEURL + "/r/" + code);
                startActivity(shareIntent);
            }
        });

        initUI();

        return view;
    }

    private void initUI() {

//
//       if (referrel_code != null) {
//            referralcodeview.setText(referrel_code);
//
//            txtCopy.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    setClipboard(getActivity(), referrel_code);
//                    Toast.makeText(getActivity(), "Referral Copied to Clipboard", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            button_invite.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
//                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    shareIntent.setType("text/plain");
//                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Invitation code is " + referrel_code);
//                    startActivity(shareIntent);
//                }
//            });
//
//        }
    }

    private void setClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.search_layout.setVisibility(View.GONE);
        mainActivity.menu_Name.setVisible(false);

        mainActivity.toggle.setDrawerIndicatorEnabled(false);
        mainActivity.toolbar.setVisibility(View.VISIBLE);
        mainActivity.disableDrawer(false);
        mainActivity.toolbar.setTitle(getResources().getString(R.string.invite_friend));
        ((MainActivity) getActivity()).toolbar.setLogo(R.drawable.transpent_logo);

        final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
        mainActivity.toggle.setHomeAsUpIndicator(upArrow);
        mainActivity.toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.onBackPressed();
            }
        });

    }
}
