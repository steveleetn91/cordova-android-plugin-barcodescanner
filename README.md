# Cordova Android Plugin BarCode Scanner
Cordova Android Plugin BarCode Scanner. I used `ML Kit Android` library so a some BarCode API it's not working.

Barcode scanning is performed completely on the device, and doesn't require a network connection.

Note that this API does not recognize barcodes in these forms:

- 1D Barcodes with only one character
- Barcodes in ITF format with fewer than six characters, and this format is known to be flaky due to absence of checksum
- Barcodes encoded with FNC2, FNC3 or FNC4
- QR codes generated in the ECI mode
- This API recognizes no more than 10 barcodes per API call.

## How to install? 

    cordova plugin add https://github.com/steveleetn91/cordova-android-plugin-barcodescanner.git

## How to use? 
    
    document.addEventListener('deviceready', onDeviceReady, false);

    function onDeviceReady() {
        // Cordova is now initialized. Have fun!

        console.log('Running cordova-' + cordova.platformId + '@' + cordova.version);
        document.getElementById('deviceready').classList.add('ready');
        startScan();

    }

    const startScan = () => {
        window.barcodescanner.scanner().then(() => {
            
        }).catch(() => {

        })
    }

    document.addEventListener("BARCODE_RECEIVE",(e) => {
        console.log("BARCODE_RECEIVE",e.code);
    })
    document.addEventListener("BARCODE_FAIL",() => {
        console.log("BARCODE_FAIL");
        startScan();
    })

## Issue 

If you need anything please create new issue `https://github.com/steveleetn91/cordova-android-plugin-barcodescanner/issues`

## Freelancer Service (Cordova/Ionic)

If you need a freelancer for cordova project, so let's me know. I can work 16 hours / 1 day and rate is 10$/1 hour. I can speak english and IELTS scope is `6.0~7.0`.

 - Write plugin 
 - Coding App 
 - Maintain cordova/ionic app 

Contact email : hoang.le.tn91@gmail.com

Facebook: https://www.facebook.com/profile.php?id=100015561036994