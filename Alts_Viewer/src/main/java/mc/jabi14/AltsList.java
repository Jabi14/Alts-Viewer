package mc.jabi14;

import java.util.ArrayList;
import java.util.List;

public class AltsList {

    private final String ip;
    private List<String> accountList = new ArrayList<>();

    public AltsList(String ip) {
        this.ip = ip;
    }

    public AltsList(String ip, String nick) {
        this.ip = ip;
        this.accountList.add(nick);
    }

    public void addPlayer(String nickName){
        accountList.add(nickName);
    }

    public List<String> getAccountList() {
        return accountList;
    }

    public String getIp() {
        return ip;
    }

    public int getSize(){
        return accountList.size();
    }
}