package ru.annikonenkov.ejb;

import jakarta.ejb.Remote;

@Remote
public interface FirstRemoteInterface {

    public int plus(int i, int y);

    public MyOwnClass getMyOwnClass();

}
