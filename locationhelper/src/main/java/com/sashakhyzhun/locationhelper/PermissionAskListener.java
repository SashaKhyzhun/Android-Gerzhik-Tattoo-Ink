package com.sashakhyzhun.locationhelper;

/**
 * @autor SashaKhyzhun
 * Created on 3/29/17.
 */

/**** Callback on various cases on checking permission
    *
    * 1.  Below M, runtime permission not needed. In that case onPermissionGranted() would be called.
    *     If permission is already granted, onPermissionGranted() would be called.
    *
    * 2.  Above M, if the permission is being asked first time onPermissionAsk() would be called.
    *
    * 3.  Above M, if the permission is previously asked but not granted, onPermissionPreviouslyDenied()
    *     would be called.
    *
    * 4.  Above M, if the permission is disabled by device policy or the user checked "Never ask again"
    *     check box on previous request permission, onPermissionDisabled() would be called.
    ****/


public interface PermissionAskListener {
    /**
     * Callback to ask permission
     */
    void onPermissionAsk();
    /**
     * Callback on permission denied
     **/
    void onPermissionPreviouslyDenied();
    /**
     * Callback on permission "Never show again" checked and denied
     **/
    void onPermissionDisabled();
    /**
     * Callback on permission granted
     ***/
    void onPermissionGranted();

}
