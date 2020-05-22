package com.auxiliary.myapplication.ui.activitys;


import android.os.Bundle;
import android.view.View;

import com.auxiliary.myapplication.adapter.CaseDetailAdapter;
import com.auxiliary.myapplication.model.AuxiliryCaseDetail;
import com.auxiliary.myapplication.model.Case;
import com.example.myapplication.R;


import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class IllegalCaseDetailectivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<AuxiliryCaseDetail> mCases = new ArrayList<>();
    private CaseDetailAdapter adapter;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_illegal_case_detailectivity);
        //pushMessage = getIntent().getStringExtra("pushMessage");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void init() {
        recyclerView = findViewById(R.id.case_detail_recyclerView);
        initData();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CaseDetailAdapter(mCases);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        Case aCase = (Case) getIntent().getSerializableExtra("case");

        for (int i = 0; i < 12; i++) {
            switch (i) {
                case 0:
                    AuxiliryCaseDetail caseDetail = new AuxiliryCaseDetail(R.drawable.xingming, "姓名:", aCase.getOffName());
                    mCases.add(caseDetail);
                    break;
                case 1:
                    AuxiliryCaseDetail caseDetail1 = new AuxiliryCaseDetail(R.drawable.jiguan, "籍贯:", aCase.getOffBirthPlace());
                    mCases.add(caseDetail1);
                    break;
                case 2:
                    AuxiliryCaseDetail caseDetail2 = new AuxiliryCaseDetail(R.drawable.zhengjianleix, "证件类型:", aCase.getOffCertificateType());
                    mCases.add(caseDetail2);
                    break;
                case 3:
                    AuxiliryCaseDetail caseDetail3 = new AuxiliryCaseDetail(R.drawable.zhengjianhaoma, "证件号码:", aCase.getOffCertificateNumber());
                    mCases.add(caseDetail3);
                    AuxiliryCaseDetail caseDetail12 = new AuxiliryCaseDetail(R.drawable.chepai, "车辆号码:", aCase.getOffPlateNumber());
                    mCases.add(caseDetail12);
                    break;
                case 4:
                    caseDetail3 = new AuxiliryCaseDetail(R.drawable.zhengjianhaoma, "证件号码:", aCase.getOffCertificateNumber());
                    mCases.add(caseDetail3);
                    break;

                case 5:
                    AuxiliryCaseDetail caseDetail4 = new AuxiliryCaseDetail(R.drawable.weifashij, "违法时间:", aCase.getOffTime());
                    mCases.add(caseDetail4);
                    break;

                case 6:
                    AuxiliryCaseDetail caseDetail5 = new AuxiliryCaseDetail(R.drawable.weifadidian, "违法地点:", aCase.getOffPlace());
                    mCases.add(caseDetail5);
                    break;
                case 7:
                    AuxiliryCaseDetail caseDetail6 = new AuxiliryCaseDetail(R.drawable.zhonglei, "违法类型:", aCase.getOffType());
                    mCases.add(caseDetail6);
                    break;

                case 8:
                    AuxiliryCaseDetail caseDetail7 = new AuxiliryCaseDetail(R.drawable.chufa, "处罚方式:", aCase.getOffPunishmentType());
                    mCases.add(caseDetail7);
                    break;

                case 9:
                    AuxiliryCaseDetail caseDetail8 = new AuxiliryCaseDetail(R.drawable.chufajine, "处罚金额:", String.valueOf(aCase.getOffMoney()));
                    mCases.add(caseDetail8);
                    break;

                case 10:
                    AuxiliryCaseDetail caseDetail9 = new AuxiliryCaseDetail(R.drawable.jingyuan, "处罚人:", String.valueOf(aCase.getPunishmentId()));
                    mCases.add(caseDetail9);
                    break;

                case 11:
                    AuxiliryCaseDetail caseDetail10 = new AuxiliryCaseDetail(R.drawable.jingyuan, "处罚人:", String.valueOf(aCase.getPunishmentName()));
                    mCases.add(caseDetail10);
                    break;


            }
        }
    }
}

