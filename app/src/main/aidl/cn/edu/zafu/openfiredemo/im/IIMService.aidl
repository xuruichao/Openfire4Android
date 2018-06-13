// IIMService.aidl
package cn.edu.zafu.openfiredemo.im;
// Declare any non-default types here with import statements
import cn.edu.zafu.openfiredemo.im.bean.Contacts;

interface IIMService {

    void autoReceipt();

    void autoFriend(boolean autoFriend);

    void createConnection();

    void login(String account, String password);

    void loginAnonymously();

    void register(String account, String password);

    void startRecord();

    void stopRecord();

    void cancelRecord();

    void listenRecord(String filePath);

    void sendMessage(String targetAddress, String content);

    void addFriend(String name);

    void deleteFriend(String name);

    List<Contacts> getContactsList();

    void addConnectionListener();

    void addChatListener();

    void disconnect();

    boolean isConnected();

}
