package in.edu.siesgst.eventpanel;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import in.edu.siesgst.eventpanel.fragment.participant.ParticipantContent;
import in.edu.siesgst.eventpanel.util.LocalDBHandler;
import in.edu.siesgst.eventpanel.util.QRInterface;

public class BarcodeScannerActivity extends AppCompatActivity {

    private String tml_id_value;

    private TextView mTmlId;
    private TextView mParticipantName;
    private TextView mParticipantPhone;
    private TextView mParticipantPaymentStatus;
    private ImageView barcodeImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new IntentIntegrator(this).
                setBeepEnabled(true).
                initiateScan();
    }

    private void setupUI(){
        //ParticipantContent.Participant participant=new LocalDBHandler(getApplicationContext()).getParticipant(tml_id_value);
        setContentView(R.layout.activity_barcode_scanner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        barcodeImage=(ImageView)findViewById(R.id.barcode_detail_activity_barcode_image);
        mTmlId=(TextView)findViewById(R.id.barcode_detail_activity_participant_tml_id);
        mParticipantName=(TextView)findViewById(R.id.barcode_detail_activity_participant_name);
        mParticipantPhone=(TextView)findViewById(R.id.barcode_detail_activity_participant_phone);
        mParticipantPaymentStatus=(TextView)findViewById(R.id.barcode_detail_activity_participant_paymentstatus);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        barcodeImage.setImageBitmap(QRInterface.encodeQRcode(tml_id_value, 400, 400));
        mTmlId.setText(tml_id_value);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            tml_id_value=scanResult.getContents();
            setupUI();
        }
        else
            finish();
    }
}
