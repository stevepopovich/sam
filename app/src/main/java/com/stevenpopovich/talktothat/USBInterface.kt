package com.stevenpopovich.talktothat

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager

interface USBInterface {
    val PRODUCT_ID: Int
    val VENDOR_ID: Int

    fun getDevice(usbManager: UsbManager): UsbDevice? {
        return usbManager.deviceList.entries.firstOrNull {
            it.value.productId == this.PRODUCT_ID && it.value.vendorId == this.VENDOR_ID
        }?.value
    }
}
