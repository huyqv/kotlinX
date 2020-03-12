package com.huy.kotlin.extension

/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2019/04/08
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */

const val FAK_REQUEST_CODE = 23425
/*

fun Fragment.navigateFacebookAccountKit(phone: String? = null) {

    val intent = android.content.Intent(context, AccountKitActivity::class.java)
    val configurationBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(
            LoginType.PHONE, AccountKitActivity.ResponseType.TOKEN)

    if (null != phone) {
        val phoneNumber = PhoneNumber("VN", phone, "VN")
        configurationBuilder.setInitialPhoneNumber(phoneNumber)
    } else {
        configurationBuilder.setDefaultCountryCode("VN")
    }

    val configuration = configurationBuilder.build()
    intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configuration)
    when {
        configuration.isReceiveSMSEnabled && !isGooglePlayServicesAvailable() -> {
            requestPermissions(kotlin.arrayOf(android.Manifest.permission.RECEIVE_SMS), FAK_REQUEST_CODE)
            return
        }
        configuration.isReadPhoneStateEnabled && !isGooglePlayServicesAvailable() -> {
            requestPermissions(kotlin.arrayOf(android.Manifest.permission.READ_PHONE_STATE), FAK_REQUEST_CODE)
            return
        }
    }
    hideProgress()
    startActivityForResult(intent, FAK_REQUEST_CODE)
}

fun Fragment.isGooglePlayServicesAvailable(): Boolean {
    val apiAvailability = GoogleApiAvailability.getInstance()
    val googlePlayServicesAvailable = apiAvailability.isGooglePlayServicesAvailable(context)
    if (AppUtil.currentOsVersionCode() < Build.VERSION_CODES.O) {
        return googlePlayServicesAvailable == ConnectionResult.SUCCESS
    }
    // ...
    return googlePlayServicesAvailable == ConnectionResult.SUCCESS
}

fun Intent.onFacebookAuthenticated(requestCode: Int, block: (String*/
/*phoneNumber*//*
) -> Unit) {
    if (requestCode != FAK_REQUEST_CODE) return
    AccountKit.loginResultWithIntent(this).also {
        if (it?.accessToken != null) {
            AccountKit.getCurrentAccount(object : AccountKitCallback<Account> {
                override fun onData(account: Account) {
                    block(account.phoneNumber.toString())
                }

                override fun onError(error: AccountKitError) {
                }
            })

        }
    }
}

*/
