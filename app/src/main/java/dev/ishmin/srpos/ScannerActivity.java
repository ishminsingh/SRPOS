package dev.ishmin.srpos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import dev.ishmin.srpos.Fragments.billing.BillingFragment;
import dev.ishmin.srpos.Fragments.purchase.PurchaseFragment;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    ZXingScannerView scannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        Bundle bundle = getIntent().getExtras();
    }
    @Override
    public void handleResult(Result rawResult) {
        //BillingFragment.textView.setText(rawResult.getText());
      try{ if(BillingFragment.flag1==1)
       {
           BillingFragment.sku=rawResult.getText().toString();
       // BillingFragment x= new BillingFragment();
        BillingFragment.entry();
       }
       else
       {//hello
          PurchaseFragment.scannerresult=rawResult.getText().toString();
           PurchaseFragment.scanner();
       }
      }
      catch (Exception e)
      {
          e.printStackTrace();
      }
        onBackPressed();
    }
    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }
}
