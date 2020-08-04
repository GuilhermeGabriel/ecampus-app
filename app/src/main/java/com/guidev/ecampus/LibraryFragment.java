package com.guidev.ecampus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import im.delight.android.webview.AdvancedWebView;

public class LibraryFragment extends Fragment implements AdvancedWebView.Listener {

    private AdvancedWebView mWebView;
    private ProgressBar progressBar;
    private ImageView wifiincon;
    private Button btnTry;
    private Context context;


    public LibraryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        progressBar = view.findViewById(R.id.progressBar_lib);
        wifiincon = view.findViewById(R.id.no_internet_lib);

        mWebView = view.findViewById(R.id.webview_library);
        mWebView.setListener(getActivity(), this);
        mWebView.loadUrl("http://biblioteca.ufvjm.edu.br/pergamum/mobile/index.php");

        btnTry = view.findViewById(R.id.btn_try);

        checkNetwork();

        return view;
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        checkNetwork();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished(String url) {
        checkNetwork();
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        checkNetwork();
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(getContext(), "Algum erro ocorreu, tente novamente!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public void onExternalPageRequest(String url) {
    }

    private void checkNetwork() {
        if (!isNetworkAvailable()) {
            mWebView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            wifiincon.setVisibility(View.VISIBLE);
            btnTry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkNetwork();
                    mWebView.reload();
                }
            });
            btnTry.setVisibility(View.VISIBLE);
        } else {
            wifiincon.setVisibility(View.INVISIBLE);
            mWebView.setVisibility(View.VISIBLE);
            btnTry.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }
}
