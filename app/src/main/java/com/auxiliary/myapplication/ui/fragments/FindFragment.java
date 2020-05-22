package com.auxiliary.myapplication.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;


import com.auxiliary.myapplication.adapter.CaseAdapter;
import com.auxiliary.myapplication.model.Case;
import com.auxiliary.myapplication.model.Page;
import com.auxiliary.myapplication.ui.activitys.FindActivity;
import com.auxiliary.myapplication.ui.activitys.MainActivity;
import com.auxiliary.myapplication.util.Contract;
import com.auxiliary.myapplication.util.GlobalHandler;
import com.auxiliary.myapplication.util.JsonUtils;
import com.auxiliary.myapplication.util.LoadingDailogUtil;
import com.auxiliary.myapplication.util.OkHttpUtils;
import com.auxiliary.myapplication.util.RSAUtils;
import com.auxiliary.myapplication.util.ToastUtil;
import com.example.myapplication.R;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FindFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FindFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindFragment extends Fragment implements GlobalHandler.HandleMsgListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView textViewTitle;
    private XRecyclerView mRecyclerView;
    private ArrayList<Case> mCases = new ArrayList<>();
    private SharedPreferences sp ;
    private SearchView searchView;
    //private View emptyView;
    private GlobalHandler mHandler;
    CaseAdapter adapter;
    private boolean isSearch = false;
    private boolean isCount = false;

    String token;
    String userId;
    int page = 0;

    private OnFragmentInteractionListener mListener;

    public FindFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FindFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FindFragment newInstance(String param1, String param2) {
        FindFragment fragment = new FindFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_find, container, false);
        initView(view);
        getData(page,userId,token);
        setLister();
        return view;
    }

    private void initView(View view){
        searchView = view.findViewById(R.id.searchView);
        textViewTitle = view.findViewById(R.id.tv_title);
        textViewTitle.setText("查找");
        mRecyclerView = view.findViewById(R.id.recyclerView);
        sp = getActivity().getSharedPreferences("login", 0);
        token = sp.getString("token",null);
        userId = sp.getString("userId",null);
        mHandler = GlobalHandler.getInstance();
        mHandler.setHandleMsgListener(this);
        searchView.setSubmitButtonEnabled(true);
        // emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view,null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        //mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);
        mRecyclerView.setLayoutManager(layoutManager);
        //  mRecyclerView.setEmptyView(emptyView);
        mRecyclerView.getDefaultFootView().setNoMoreHint("————我是有底线的————");

        adapter = new CaseAdapter(mCases);
        mRecyclerView.setAdapter(adapter);




    }

    private void  setLister(){
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                // LogUtil.d("下拉");
                if(isCount){

                    mRecyclerView.loadMoreComplete();
                    mRecyclerView.setNoMore(true);

                }else {

                    page++;
                    getData(page,userId,token);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                isSearch = true;
                LoadingDailogUtil.showLoadingDialog(getActivity(),"搜索中...");
                getDataBySearch(query,userId,token);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!"".equals(searchView.getQuery().toString().trim())) {

                    mRecyclerView.setLoadingMoreEnabled(false);
                } else {

                    mRecyclerView.setLoadingMoreEnabled(true);
                }
                final ArrayList<Case> filteredModelList = filter(mCases, newText);
                adapter.setFilter(filteredModelList);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                searchView.setQuery("", false);
                searchView.clearFocus();
                return true;
            }
        });

    }

    /**
     *
     * 逻辑4：过滤方法，目的是过滤符合当前数据中符合条件的数据源
     * @param models
     * @param query
     * @return
     */
    private ArrayList<Case> filter(ArrayList<Case> models, String query) {

        query = query.toLowerCase();

        final ArrayList<Case> filteredModelList = new ArrayList<>();

        for (Case model : models) {

            final String nameText = model.getOffName();
            final String idText = model.getOffCertificateNumber();

            if (nameText.contains(query) || idText.contains(query)) {

                filteredModelList.add(model);

            }

        }

        return filteredModelList;

    }
    private void getData(int pageNumber,String userId,String token){
        OkHttpUtils okHttpUtils = new OkHttpUtils();
        Page page = new Page(pageNumber,userId,token);
        String getDataJson = JsonUtils.conversionJsonString(page);
        try {
            String publicEncryptJson = RSAUtils.publicEncrypt(getDataJson,RSAUtils.getPublicKey(RSAUtils.SERVER_PUBLIC_KEY));
            okHttpUtils.postInfo(Contract.SERVER_ADDRESS+"AulixiryGetCasePage",publicEncryptJson);
        }catch (Exception e){
            e.printStackTrace();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showShortToast("未知错误");
                    LoadingDailogUtil.cancelLoadingDailog();

                }

            });
        }


    }

    private void getDataBySearch(String query,String userId,String token){
        OkHttpUtils okHttpUtils = new OkHttpUtils();
        Case case1 = new Case();
        case1.setOffName(query);
        case1.setPunishmentId(Integer.valueOf(userId));
        case1.setAppToken(token);

        String getDataJson = JsonUtils.conversionJsonString(case1);
        try {
            String publicEncryptJson = RSAUtils.publicEncrypt(getDataJson,RSAUtils.getPublicKey(RSAUtils.SERVER_PUBLIC_KEY));
            okHttpUtils.postInfo(Contract.SERVER_ADDRESS+"GetSearchCaseServlet",publicEncryptJson);
        }catch (Exception e){
            e.printStackTrace();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showShortToast("未知错误");
                    LoadingDailogUtil.cancelLoadingDailog();

                }

            });
        }


    }

    @Override
    public void handleMsg(Message msg) {
        switch (msg.what){
            case 0:
                ToastUtil.showShortToast("网络出错，请检查网络");
                LoadingDailogUtil.cancelLoadingDailog();
                break;
            case 1:
                //也可以用这个接收
                try {
                    JSONObject jsonObject = new JSONObject(msg.getData().getString("backInfo"));
                    switch (jsonObject.getString("code")){
                        case "ok":
                            if(!isSearch){
                                JSONArray jsonArray = jsonObject.getJSONArray("items");
                                isCount = jsonObject.getBoolean("isCount");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    Case case1 = new Case();
                                    case1.setOffName(object.getString("offName"));
                                    case1.setOffCertificateNumber(object.getString("offCertificateNumber"));
                                    case1.setCaseId(object.getInt("caseId"));
                                    case1.setOffType(object.getString("offType"));
                                    case1.setOffTime(object.getString("offTime"));
                                    case1.setOffBirthPlace(object.getString("offBirthPlace"));
                                    case1.setOffPlace(object.getString("offPlace"));
                                    case1.setOffCertificateType(object.getString("offCertificateType"));
                                    case1.setOffPlateNumber(object.getString("offPlateNumber"));
                                    case1.setOffPunishmentType(object.getString("offPunishmentType"));
                                    case1.setPunishmentName(object.getString("punishmentName"));
                                    case1.setOffMoney(object.getInt("offMoney"));
                                    mCases.add(case1);


                                }
                                if(page == 0){
                                    //adapter = new CaseAdapter(mCases);
                                    // mRecyclerView.setAdapter(adapter);
                                    adapter.update(mCases);
                                }else {

                                    adapter.update(mCases);
                                    mRecyclerView.loadMoreComplete();
                                    ToastUtil.showShortToast("加载成功");
                                }
                            }else {
                                JSONArray jsonArray = jsonObject.getJSONArray("items");
                                ArrayList<Case> serachCases =new ArrayList<>();
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    Case case1 = new Case();
                                    case1.setOffName(object.getString("offName"));
                                    case1.setOffCertificateNumber(object.getString("offCertificateNumber"));
                                    case1.setCaseId(object.getInt("caseId"));
                                    case1.setOffType(object.getString("offType"));
                                    case1.setOffTime(object.getString("offTime"));
                                    case1.setOffBirthPlace(object.getString("offBirthPlace"));
                                    case1.setOffPlace(object.getString("offPlace"));
                                    case1.setOffCertificateType(object.getString("offCertificateType"));
                                    case1.setOffPlateNumber(object.getString("offPlateNumber"));
                                    case1.setOffPunishmentType(object.getString("offPunishmentType"));
                                    case1.setPunishmentName(object.getString("punishmentName"));
                                    case1.setOffMoney(object.getInt("offMoney"));
                                    serachCases.add(case1);

                                }
                                isSearch = false;
                                adapter.update(serachCases);
                                LoadingDailogUtil.cancelLoadingDailog();
                            }


                            break;
                        case "003":

                            checkInvalidDialog();
                            if(isSearch){
                                isSearch = false;
                                LoadingDailogUtil.cancelLoadingDailog();
                            }
                            break;
                        default:
                            if(isSearch){
                                isSearch = false;
                                LoadingDailogUtil.cancelLoadingDailog();
                            }
                            ToastUtil.showShortToast("服务器错误");



                    }
                    break;
                }catch (Exception e){
                    e.printStackTrace();
                    if(isSearch){
                        isSearch = false;
                        LoadingDailogUtil.cancelLoadingDailog();
                    }
                    ToastUtil.showShortToast("未知错误");

                }

        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁移除所有消息，避免内存泄露
        mHandler.removeCallbacks(null);
    }

    private void checkInvalidDialog(){
        final NormalDialog dialog = new NormalDialog(getActivity());
        dialog.content("身份失效，请重新登录")
                .btnNum(1)
                .btnText("确定")
                .showAnim(new BounceTopEnter())
                .dismissAnim(new SlideBottomExit())
                .show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
                Intent intent = new Intent( getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if ( !hidden) {
            mHandler.setHandleMsgListener(this);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
