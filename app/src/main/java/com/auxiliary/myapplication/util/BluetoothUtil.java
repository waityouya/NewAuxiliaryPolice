package com.auxiliary.myapplication.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothUtil {
    /*
     * 检测蓝牙是否打开
     */
    public static boolean isBluetoothOn(){
        BluetoothAdapter bluetoothAdapter =  BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter != null){
            if(bluetoothAdapter.isEnabled()){
                return true;
            }
        }
        return false;
    }

    /*
     *获取所有已匹配的设备
     */
    public static List<BluetoothDevice> getPairedDevices(){
        List deviceList = new ArrayList<>();
        Set<BluetoothDevice> pairedDevice = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        if(pairedDevice.size() >0){
            for (BluetoothDevice device:pairedDevice
                 ) {
                deviceList.add(device);
            }
        }
        return  deviceList;

    }

    /*
     * 获取所有已配对的打印类设备
     */
    public static List<BluetoothDevice> getPairedPrinterDevice(){
        return getSpecificDevice(BluetoothClass.Device.Major.IMAGING);
    }

    /*
     * 从已配对设配中，删选出某一特定类型的设备展示
     * @param deviceClass
     * @return
     */
    public static List<BluetoothDevice> getSpecificDevice(int deviceClass){
        List<BluetoothDevice> devices = BluetoothUtil.getPairedDevices();
        List<BluetoothDevice> printerDevices = new ArrayList<>();
        for (BluetoothDevice device : devices) {
            BluetoothClass klass = device.getBluetoothClass();
            // 关于蓝牙设备分类参考 http://stackoverflow.com/q/23273355/4242112
            if (klass.getMajorDeviceClass() == deviceClass)
                printerDevices.add(device);
        }

        return printerDevices;
    }
    /**
     * 弹出系统对话框，请求打开蓝牙
     */
    public static void openBluetooth(Activity activity) {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, 666);
    }

    public static BluetoothSocket connectDevice(BluetoothDevice device) {
        BluetoothSocket socket = null;
        try {
            socket = device.createRfcommSocketToServiceRecord(
                    UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            socket.connect();
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException closeException) {
                return null;
            }
            return null;
        }
        return socket;
    }
}
