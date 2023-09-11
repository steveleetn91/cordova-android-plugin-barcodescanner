module.exports = {
    scanner : () => {
        return new Promise((resolve,reject) => {
            cordova.exec(() => {
                
                return resolve();
              }, () => {
                return reject();
              }, "CordovaBarcode", 'scanner', []);
        })
    }
}